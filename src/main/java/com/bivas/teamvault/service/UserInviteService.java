package com.bivas.teamvault.service;

import com.bivas.teamvault.dto.TeamMembershipDto;
import com.bivas.teamvault.email.EmailProvider;
import com.bivas.teamvault.email.EmailProviderFactory;
import com.bivas.teamvault.entity.Team;
import com.bivas.teamvault.entity.TeamMembership;
import com.bivas.teamvault.entity.User;
import com.bivas.teamvault.exception.KeyNotFoundException;
import com.bivas.teamvault.repository.TeamRepository;
import com.bivas.teamvault.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserInviteService {

    private final TeamRepository teamRepository;

    private final EmailProviderFactory emailProviderFactory;

    private final StringRedisTemplate redisTemplate;

    private final TeamMembershipService teamMembershipService;

    private  final UserRepository userRepository;

    public void InviteUser(String recipientEmail, Long teamId)
    {
        Team team = teamRepository.findById(teamId)
                                  .orElseThrow(() -> new RuntimeException("Team not found"));

        String inviteToken = UUID.randomUUID().toString();

        String inviteLink = "https://localhost:8080/invite/accept?teamId" + teamId + "&token=" + inviteToken;

        EmailProvider emailProvider = emailProviderFactory.getProvider();

        redisTemplate.opsForValue().set(inviteToken, teamId.toString(), Duration.ofHours(1));

        emailProvider.SendInviteEmail(inviteLink, recipientEmail, team.getName());
    }

    public void AcceptUserInvite(String token, Long userId) throws KeyNotFoundException
    {
        String teamIdStr = redisTemplate.opsForValue().get(token);

        if (teamIdStr == null) {

            throw new KeyNotFoundException("Invalid or expired invite link.");
        }

        TeamMembershipDto teamMembershipDto = new TeamMembershipDto();

        teamMembershipDto.setUserId(userId);

        teamMembershipDto.setRole(TeamMembership.Role.MEMBER);

        teamMembershipDto.setTeamId(Long.parseLong(teamIdStr));

        teamMembershipService.AddUserToTeam(teamMembershipDto);

        redisTemplate.delete(token);
    }
}
