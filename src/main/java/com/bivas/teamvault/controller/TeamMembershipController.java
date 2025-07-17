package com.bivas.teamvault.controller;

import com.bivas.teamvault.dto.TeamDto;
import com.bivas.teamvault.dto.TeamMembershipDto;
import com.bivas.teamvault.entity.Team;
import com.bivas.teamvault.entity.TeamMembership;
import com.bivas.teamvault.repository.TeamMembershipRepository;
import com.bivas.teamvault.service.TeamMembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teams/{teamId}/team-membership")
@RequiredArgsConstructor
public class TeamMembershipController {

    private final TeamMembershipRepository teamMembershipRepository;

    private final TeamMembershipService teamMembershipService;


    @GetMapping
    public ResponseEntity<List<TeamMembership>> getAllTeams()
    {
        List<TeamMembership> teamMemberships = teamMembershipRepository.findAll();

        return ResponseEntity.ok(teamMemberships);
    }

    @PostMapping
    public ResponseEntity<?> AddTeamMember(@PathVariable Long teamId, TeamMembershipDto teamMembershipDto)
    {
        try
        {
            teamMembershipService.AddUserToTeam(teamMembershipDto.UserId, teamId, teamMembershipDto.Role);

            return ResponseEntity.ok().build();
        }
        catch (Exception ex)
        {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> RemoveTeamMember(@PathVariable Long teamId, @PathVariable long userId)
    {
       List<TeamMembership> teamMemberships = teamMembershipRepository.findByTeamId(teamId);

        Optional<TeamMembership> teamMembership = teamMemberships.stream().filter(x -> x.getUser().getId().equals(userId)).findFirst();

        teamMembership.ifPresent(teamMembershipRepository::delete);

        return ResponseEntity.ok().build();
    }
}