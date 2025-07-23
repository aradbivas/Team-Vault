package com.bivas.teamvault.controller;

import com.bivas.teamvault.dto.ErrorResponseDto;
import com.bivas.teamvault.dto.ResponseDto;
import com.bivas.teamvault.dto.TeamMembershipDto;
import com.bivas.teamvault.repository.TeamMembershipRepository;
import com.bivas.teamvault.service.TeamMembershipService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/team-vault/team-membership")
@RequiredArgsConstructor
public class TeamMembershipController {

    private final TeamMembershipRepository teamMembershipRepository;

    private final TeamMembershipService teamMembershipService;

    @GetMapping("teams/{teamId}")
    public ResponseEntity<ResponseDto<List<TeamMembershipDto>>> getAllTeamMembers(@PathVariable Long teamId) {

        ResponseDto<List<TeamMembershipDto>> responseDto = new ResponseDto<>();

        try {

            List<TeamMembershipDto> teamMembershipDto = teamMembershipService.getAllTeamMembers(teamId);

            responseDto.setData(teamMembershipDto);

            return ResponseEntity.ok(responseDto);

        } catch (Exception exception) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(), "General Error", exception.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<ResponseDto<List<TeamMembershipDto>>> getAllUsersTeams(@PathVariable Long userId) {

        ResponseDto<List<TeamMembershipDto>> responseDto = new ResponseDto<>();

        try {

            List<TeamMembershipDto> teamMembershipDto = teamMembershipService.getAllUsersTeams(userId);

            responseDto.setData(teamMembershipDto);

            return ResponseEntity.ok(responseDto);

        } catch (Exception exception) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(), "General Error", exception.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @PostMapping
    public ResponseEntity<ResponseDto<TeamMembershipDto>> AddTeamMember(@RequestBody TeamMembershipDto teamMembershipDto) {

        ResponseDto<TeamMembershipDto> responseDto = new ResponseDto<>();

        try {
            teamMembershipDto = teamMembershipService.AddUserToTeam(teamMembershipDto.TeamId, teamMembershipDto.UserId, teamMembershipDto.Role);

            responseDto.setData(teamMembershipDto);

            return ResponseEntity.status(201).body(responseDto);

        } catch (EntityNotFoundException entityNotFoundException) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.NOT_FOUND.name(), "Not found", entityNotFoundException.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);

        } catch (Exception exception) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(), "General Error", exception.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @DeleteMapping("/teams/{teamId}/users/{userId}")
    public ResponseEntity<ResponseDto<?>> RemoveTeamMember(@PathVariable Long teamId, @PathVariable long userId) {

        ResponseDto<?> responseDto = new ResponseDto<>();

        try {
            teamMembershipService.RemoveUserFromTeam(teamId, userId);

            return ResponseEntity.ok().build();

        } catch (EntityNotFoundException entityNotFoundException) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.NOT_FOUND.name(), "User Not found", entityNotFoundException.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        } catch (Exception exception) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(), "General Error", exception.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }
}