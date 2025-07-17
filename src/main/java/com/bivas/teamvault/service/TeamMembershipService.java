package com.bivas.teamvault.service;

import com.bivas.teamvault.entity.Team;
import com.bivas.teamvault.entity.TeamMembership;
import com.bivas.teamvault.entity.User;
import com.bivas.teamvault.repository.TeamMembershipRepository;
import com.bivas.teamvault.repository.TeamRepository;
import com.bivas.teamvault.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamMembershipService
{
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamMembershipRepository teamMembershipRepository;

    public void AddUserToTeam(Long userId, Long teamId, TeamMembership.Role role)
    {
      User user = userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("User not found"));

      Team team = teamRepository.findById(teamId).orElseThrow(()->new EntityNotFoundException("Team not found"));

      TeamMembership teamMembership = TeamMembership.builder()
                                                    .user(user)
                                                    .team(team)
                                                    .role(role)
                                                    .build();

      teamMembershipRepository.save(teamMembership);
    }

    public void RemoveUserFromTeam(Long teamMembershipId)
    {
        TeamMembership teamMembership = teamMembershipRepository.findById(teamMembershipId).orElseThrow(()->new EntityNotFoundException("Team member not found"));

        teamMembershipRepository.delete(teamMembership);
    }
}
