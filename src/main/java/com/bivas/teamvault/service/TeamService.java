package com.bivas.teamvault.service;

import com.bivas.teamvault.dto.TeamDto;
import com.bivas.teamvault.dto.UserDto;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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


    public List<TeamDto> getTeams() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String auth0Sub = authentication.getName();

        Optional<User> user = userRepository.findBySub(auth0Sub);

        user.orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<TeamMembership> teamMemberships = teamMembershipRepository.findByUserId(user.get().getId());

        List<TeamDto> teamDtos = new ArrayList<>();

        teamMemberships.forEach(teamMembership -> {

            UserDto userDto = new UserDto(user.get().getId(), user.get().getName(), user.get().getEmail());

            TeamDto teamDto = new TeamDto(teamMembership.getTeam().getId(), teamMembership.getTeam().getName(), teamMembership.getTeam().getDescription(), userDto);

            teamDtos.add(teamDto);
        });

        return teamDtos;
    }

    public TeamDto GetTeam(long id) {

        Optional<Team> team = teamRepository.findById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String auth0Sub = authentication.getName();

        Optional<User> user = userRepository.findBySub(auth0Sub);

        team.orElseThrow(() -> new EntityNotFoundException("Team not found"));

        UserDto userDto = new UserDto(user.get().getId(), user.get().getName(), user.get().getEmail());

        TeamDto teamDto = new TeamDto(team.get().getId(), team.get().getName(), team.get().getDescription(), userDto);

        return teamDto;
    }

    public TeamDto CreateTeam(String name, String description) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String auth0Sub = authentication.getName();

        Optional<User> user = userRepository.findBySub(auth0Sub);

        user.orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Team> existingTeams = teamRepository.findByOwner(user.get());

        if (!existingTeams.isEmpty() && existingTeams.stream().anyMatch(team -> team.getName().equals(name))) {

            throw new EntityExistsException("Team with name " + name + " already exists");
        }
        Team team = Team.builder()
                .name(name)
                .description(description)
                .owner(user.get())
                .build();

        teamRepository.save(team);

        teamMembershipService.AddUserToTeam(team.getId(), user.get().getId(), TeamMembership.Role.OWNER);

        UserDto userDto = new UserDto(user.get().getId(), user.get().getName(), user.get().getEmail());

        TeamDto teamDto = new TeamDto(team.getId(),
                team.getName(),
                team.getDescription(),
                userDto);

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
