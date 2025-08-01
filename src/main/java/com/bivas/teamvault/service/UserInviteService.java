package com.bivas.teamvault.service;

import com.bivas.teamvault.dto.TeamMembershipDto;
import com.bivas.teamvault.dto.UserInviteDto;
import com.bivas.teamvault.entity.Team;
import com.bivas.teamvault.entity.TeamMembership;
import com.bivas.teamvault.entity.User;
import com.bivas.teamvault.exception.KeyNotFoundException;
import com.bivas.teamvault.properties.InviteLinkProperties;
import com.bivas.teamvault.provider.email.EmailProvider;
import com.bivas.teamvault.provider.email.EmailProviderFactory;
import com.bivas.teamvault.repository.TeamRepository;
import com.bivas.teamvault.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserInviteService {

    private final TeamRepository teamRepository;

    private final EmailProviderFactory emailProviderFactory;

    private final StringRedisTemplate redisTemplate;

    private final TeamMembershipService teamMembershipService;

    private final UserRepository userRepository;

    private final InviteLinkProperties inviteLinkProperties;

    public UserInviteDto InviteUser(String recipientEmail, Long teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        String inviteToken = UUID.randomUUID().toString();

        String inviteLink = inviteLinkProperties.getUrl() + "/teams/invite/accept?teamId=" + teamId + "&token=" + inviteToken;

        EmailProvider emailProvider = emailProviderFactory.getProvider();

        redisTemplate.opsForValue().set(inviteToken, teamId.toString(), Duration.ofHours(1));

        emailProvider.SendInviteEmail(inviteLink, recipientEmail, team.getName());

        return new UserInviteDto(recipientEmail, "Inviation has been sent");
    }

    public TeamMembershipDto AcceptUserInvite(String token, Long teamId) throws KeyNotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String auth0Sub = authentication.getName();

        User user = userRepository.findBySub(auth0Sub).orElseThrow(() -> new EntityNotFoundException("User not found"));

        String teamIdStr = redisTemplate.opsForValue().get(token);

        if (teamIdStr == null) {

            throw new KeyNotFoundException("Invalid or expired invite link.");
        }

        TeamMembershipDto teamMembershipDto = teamMembershipService.AddUserToTeam(teamId, user.getId(), TeamMembership.Role.MEMBER);

        redisTemplate.delete(token);

        return teamMembershipDto;
    }
}
