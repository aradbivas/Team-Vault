package com.bivas.teamvault.controller;

import com.bivas.teamvault.dto.TeamDto;
import com.bivas.teamvault.entity.Team;
import com.bivas.teamvault.repository.TeamRepository;
import com.bivas.teamvault.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
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

    @GetMapping("/{id}")
    public ResponseEntity<TeamDto> getTeam(@PathVariable Long id)
    {
        try
        {
            TeamDto teamDto = teamService.GetTeam(id);

            return ResponseEntity.ok().body(teamDto);
        }
        catch (EntityNotFoundException entityNotFound)
        {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity<?> createTeam(@RequestBody @Valid TeamDto teamDto)
    {
        try
        {
            TeamDto createTeamDto = teamService.CreateTeam(teamDto.Name, teamDto.UserId);

            return ResponseEntity.ok(createTeamDto);
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