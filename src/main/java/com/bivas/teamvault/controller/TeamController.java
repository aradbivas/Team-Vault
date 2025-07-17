package com.bivas.teamvault.controller;

import com.bivas.teamvault.dto.TeamDto;
import com.bivas.teamvault.entity.Team;
import com.bivas.teamvault.repository.TeamRepository;
import com.bivas.teamvault.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController
{
    private final TeamService teamService;

    private final TeamRepository teamRepository;

    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams()
    {
        List<Team> teams = teamRepository.findAll();

        return ResponseEntity.ok(teams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeam(@PathVariable Long id)
    {
        Optional<Team> team = teamRepository.findById(id);

        return team.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<?> createTeam(@RequestBody TeamDto createTeamDto)
    {
        try
        {
            Team team = teamService.CreateTeam(createTeamDto.Name, createTeamDto.UserId);

            return ResponseEntity.ok(team);
        }
        catch (Exception ex)
        {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable long id)
    {
        teamRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }
}