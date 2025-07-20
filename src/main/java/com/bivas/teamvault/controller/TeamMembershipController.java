package com.bivas.teamvault.controller;

import com.bivas.teamvault.dto.TeamMembershipDto;
import com.bivas.teamvault.repository.TeamMembershipRepository;
import com.bivas.teamvault.service.TeamMembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/team-vault/team-membership")
@RequiredArgsConstructor
public class TeamMembershipController {

    private final TeamMembershipRepository teamMembershipRepository;

    private final TeamMembershipService teamMembershipService;

    @GetMapping("teams/{teamId}")
    public ResponseEntity<List<TeamMembershipDto>> getAllTeamMembers(@PathVariable Long teamId)
    {
        List<TeamMembershipDto> teamMembershipDto = teamMembershipService.getAllTeamMembers(teamId);

        return ResponseEntity.ok(teamMembershipDto);
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<List<TeamMembershipDto>> getAllUsersTeams(@PathVariable Long userId)
    {
        List<TeamMembershipDto> teamMemberships = teamMembershipService.getAllUsersTeams(userId);

        return ResponseEntity.ok(teamMemberships);
    }

    @PostMapping
    public ResponseEntity<?> AddTeamMember(@RequestBody TeamMembershipDto teamMembershipDto)
    {
        try
        {
           teamMembershipDto = teamMembershipService.AddUserToTeam(teamMembershipDto);

            return ResponseEntity.ok(teamMembershipDto);
        }
        catch (Exception ex)
        {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/teams/{teamId}/users/{userId}")
    public ResponseEntity<?> RemoveTeamMember(@PathVariable Long teamId, @PathVariable long userId)
    {
        teamMembershipService.RemoveUserFromTeam(teamId, userId);

        return ResponseEntity.ok().build();
    }
}