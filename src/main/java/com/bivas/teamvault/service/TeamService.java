package com.bivas.teamvault.service;

import com.bivas.teamvault.dto.TeamDto;
import com.bivas.teamvault.entity.Secret;
import com.bivas.teamvault.entity.Team;
import com.bivas.teamvault.entity.TeamMembership;
import com.bivas.teamvault.entity.User;
import com.bivas.teamvault.repository.SecretRepository;
import com.bivas.teamvault.repository.TeamMembershipRepository;
import com.bivas.teamvault.repository.TeamRepository;
import com.bivas.teamvault.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamMembershipRepository teamMembershipRepository;
    private final TeamMembershipService teamMembershipService;
    private final SecretRepository secretRepository;


    public TeamDto GetTeam(long id) {
        Optional<Team> team = teamRepository.findById(id);

        team.orElseThrow(() -> new EntityNotFoundException("Team not found"));

        TeamDto teamDto = new TeamDto(team.get().getId(), team.get().getName(), team.get().getId());

        return teamDto;
    }

    public TeamDto CreateTeam(String name, Long userId) {

        Optional<User> existingUser = userRepository.findById(userId);

        if (existingUser.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }

        List<Team> existingTeams = teamRepository.findByOwner(existingUser.get());

        if (!existingTeams.isEmpty() && existingTeams.stream().anyMatch(team -> team.getName().equals(name))) {

            throw new EntityExistsException("Team with name " + name + " already exists");
        }
        Team team = Team.builder()
                .name(name)
                .owner(existingUser.get())
                .build();

        teamRepository.save(team);

        teamMembershipService.AddUserToTeam(team.getId(), userId, TeamMembership.Role.OWNER);

        TeamDto teamDto = new TeamDto(team.getId(), team.getName(), team.getOwner().getId());

        return teamDto;
    }

    public void DeleteTeam(Long teamId) {
        Optional<Team> team = teamRepository.findById(teamId);

        if (team.isEmpty()) {
            throw new EntityNotFoundException("Team not found");
        }

        List<TeamMembership> teamMembershipList = teamMembershipRepository.findByTeamId(teamId);

        if (!teamMembershipList.isEmpty()) {

            teamMembershipRepository.deleteAll(teamMembershipList);
        }

        List<Secret> secrets = secretRepository.findByTeamId(teamId);

        if (!secrets.isEmpty()) {
            secretRepository.deleteAll(secrets);
        }

        teamRepository.deleteById(teamId);
    }
}
