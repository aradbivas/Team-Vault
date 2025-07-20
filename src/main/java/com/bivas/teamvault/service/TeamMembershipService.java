package com.bivas.teamvault.service;

import com.bivas.teamvault.dto.TeamMembershipDto;
import com.bivas.teamvault.entity.Team;
import com.bivas.teamvault.entity.TeamMembership;
import com.bivas.teamvault.entity.User;
import com.bivas.teamvault.repository.TeamMembershipRepository;
import com.bivas.teamvault.repository.TeamRepository;
import com.bivas.teamvault.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamMembershipService
{
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamMembershipRepository teamMembershipRepository;

    public List<TeamMembershipDto> getAllUsersTeams(Long userId)
    {
        List<TeamMembershipDto> teamMembershipDtos = new ArrayList<>();

        List<TeamMembership> teamMemberships = teamMembershipRepository.findByUserId(userId);

        teamMembershipDtos = MapToTeamMembershipDto(teamMemberships);

        return teamMembershipDtos;
    }

    public List<TeamMembershipDto> getAllTeamMembers(long TeamId)
    {
        List<TeamMembershipDto> teamMembershipDtos = new ArrayList<>();

        List<TeamMembership> teamMemberships = teamMembershipRepository.findByTeamId(TeamId);

        teamMembershipDtos = MapToTeamMembershipDto(teamMemberships);

        return teamMembershipDtos;
    }

    public TeamMembershipDto AddUserToTeam(TeamMembershipDto teamMembershipDto)
    {
      User user = userRepository.findById(teamMembershipDto.UserId).orElseThrow(()->new EntityNotFoundException("User not found"));

      Team team = teamRepository.findById(teamMembershipDto.TeamId).orElseThrow(()->new EntityNotFoundException("Team not found"));

      TeamMembership teamMembership = TeamMembership.builder()
                                                    .user(user)
                                                    .team(team)
                                                    .role(teamMembershipDto.Role)
                                                    .build();

      teamMembershipRepository.save(teamMembership);

      teamMembershipDto.setId(teamMembership.getId());

      return teamMembershipDto;
    }

    public void RemoveUserFromTeam(Long teamId, Long userId)
    {
        List<TeamMembership> teamMemberships = teamMembershipRepository.findByTeamId(teamId);

        Optional<TeamMembership> teamMembership = teamMemberships.stream().filter(x -> x.getUser().getId().equals(userId)).findFirst();

        teamMembership.ifPresent(teamMembershipRepository::delete);
    }

    private List<TeamMembershipDto> MapToTeamMembershipDto(List<TeamMembership> teamMemberships)
    {
        List<TeamMembershipDto> teamMembershipDtos = new ArrayList<>();

        teamMemberships.forEach(teamMembership -> {

            TeamMembershipDto teamMembershipDto = new TeamMembershipDto();

            teamMembership.setId(teamMembership.getId());
            teamMembershipDto.setTeamId(teamMembership.getTeam().getId());
            teamMembershipDto.setUserId(teamMembership.getUser().getId());
            teamMembershipDto.setRole(teamMembership.getRole());

            teamMembershipDtos.add(teamMembershipDto);
        });

        return teamMembershipDtos;
    }
}
