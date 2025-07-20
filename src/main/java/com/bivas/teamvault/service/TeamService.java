package com.bivas.teamvault.service;

import com.bivas.teamvault.dto.TeamDto;
import com.bivas.teamvault.dto.TeamMembershipDto;
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


    public TeamDto GetTeam(long id)
    {
        Optional<Team> team = teamRepository.findById(id);

        team.orElseThrow(()->new EntityNotFoundException("Team not found"));

        TeamDto teamDto = new TeamDto();

        teamDto.setName(team.get().getName());

        teamDto.setUserId(team.get().getId());

        return teamDto;
    }

    public TeamDto CreateTeam(String name, Long userId)
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

        TeamMembershipDto teamMembershipDto = new TeamMembershipDto();

        teamMembershipDto.setTeamId(team.getId());
        teamMembershipDto.setUserId(userId);
        teamMembershipDto.setRole(TeamMembership.Role.OWNER);

        teamMembershipService.AddUserToTeam(teamMembershipDto);

        TeamDto teamDto = new  TeamDto();

        teamDto.setName(name);
        teamDto.setUserId(userId);

        return teamDto;
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
