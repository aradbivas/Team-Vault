package com.bivas.teamvault.controller;

import com.bivas.teamvault.dto.ErrorResponseDto;
import com.bivas.teamvault.dto.ResponseDto;
import com.bivas.teamvault.dto.TeamDto;
import com.bivas.teamvault.repository.TeamRepository;
import com.bivas.teamvault.service.TeamService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/team-vault/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    private final TeamRepository teamRepository;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<TeamDto>> getTeam(@PathVariable Long id) {

        ResponseDto<TeamDto> responseDto = new ResponseDto<>();

        try {
            TeamDto teamDto = teamService.GetTeam(id);

            responseDto.setData(teamDto);

            return ResponseEntity.ok().body(responseDto);
        } catch (EntityNotFoundException entityNotFoundException) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.NOT_FOUND.name(), "Team Not found", entityNotFoundException.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);

        } catch (Exception exception) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(), "General Error", exception.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseDto<TeamDto>> createTeam(@RequestBody @Valid TeamDto teamDto) {

        ResponseDto<TeamDto> responseDto = new ResponseDto<>();

        try {

            teamDto = teamService.CreateTeam(teamDto.getName(), teamDto.getUserId());

            responseDto.setData(teamDto);

            return ResponseEntity.ok().body(responseDto);

        } catch (EntityNotFoundException entityNotFoundException) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.BAD_REQUEST.name(), "User Not found", entityNotFoundException.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);

        } catch (EntityExistsException ex) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.BAD_REQUEST.name(), "Team Already Exists", ex.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        } catch (Exception exception) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(), "General Error", exception.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable long id) {
        ResponseDto<?> responseDto = new ResponseDto<>();

        try {

            teamService.DeleteTeam(id);

            return ResponseEntity.ok().build();

        } catch (EntityNotFoundException entityNotFoundException) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.NOT_FOUND.name(), "Team Not found", entityNotFoundException.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);

        } catch (Exception ex) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(), "General Error", ex.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }
}