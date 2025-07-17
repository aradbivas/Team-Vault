package com.bivas.teamvault.service;

import com.bivas.teamvault.entity.Team;
import com.bivas.teamvault.entity.TeamMembership;
import com.bivas.teamvault.entity.User;
import com.bivas.teamvault.repository.TeamMembershipRepository;
import com.bivas.teamvault.repository.TeamRepository;
import com.bivas.teamvault.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.lang.System.in;

@Service
@RequiredArgsConstructor
public class TeamService
{
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamMembershipRepository teamMembershipRepository;
    private final TeamMembershipService  teamMembershipService;

    public Team CreateTeam(String name, Long userId)
    {
        Optional<User> existingUser = userRepository.findById(userId);

        if(existingUser.isEmpty())
        {
            throw  new RuntimeException("User not found");
        }

        Team team = Team.builder()
                        .name(name)
                        .owner(existingUser.get())
                        .build();

        teamRepository.save(team);

        teamMembershipService.AddUserToTeam(existingUser.get().getId(), team.getId(), TeamMembership.Role.OWNER);

        return team;
    }

    public void DeleteTeam(Long teamId)
    {
        Optional<Team> team = teamRepository.findById(teamId);

        if(team.isEmpty())
        {
            throw  new RuntimeException("Team not found");
        }

       List<TeamMembership> teamMembershipList =  teamMembershipRepository.findByTeamId(teamId);

        teamMembershipRepository.deleteAll(teamMembershipList);

        teamRepository.deleteById(teamId);
    }
}
