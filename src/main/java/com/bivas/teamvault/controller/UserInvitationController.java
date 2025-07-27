package com.bivas.teamvault.controller;

import com.bivas.teamvault.dto.ErrorResponseDto;
import com.bivas.teamvault.dto.ResponseDto;
import com.bivas.teamvault.dto.TeamMembershipDto;
import com.bivas.teamvault.dto.UserInviteDto;
import com.bivas.teamvault.exception.KeyNotFoundException;
import com.bivas.teamvault.service.UserInviteService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/team-vault")
@RequiredArgsConstructor
public class UserInvitationController {

    private final UserInviteService userInviteService;

    @PostMapping("/teams/{teamId}/invite")
    public ResponseEntity<ResponseDto<UserInviteDto>> InviteUserToTeam(@RequestBody @Valid UserInviteDto userInviteDto, @PathVariable Long teamId) {

        ResponseDto<UserInviteDto> responseDto = new ResponseDto<>();
        try {

            userInviteDto = userInviteService.InviteUser(userInviteDto.Email, teamId);

            responseDto.setData(userInviteDto);

            return ResponseEntity.ok(responseDto);
        } catch (EntityNotFoundException entityNotFoundException) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.NOT_FOUND.name(), "Team Not Found", entityNotFoundException.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        } catch (Exception ex) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(), "General Error", ex.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @GetMapping("/teams/accept-invite")
    public ResponseEntity<ResponseDto<TeamMembershipDto>> acceptInvite(@RequestParam String token,
                                                                       @RequestParam Long teamId) {

        ResponseDto<TeamMembershipDto> responseDto = new ResponseDto<>();

        try {
            TeamMembershipDto teamMembershipDto = userInviteService.AcceptUserInvite(token, teamId);

            responseDto.setData(teamMembershipDto);
            
            return ResponseEntity.ok(responseDto);
        } catch (KeyNotFoundException ex) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(), "General Error", ex.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        } catch (Exception ex) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(), "General Error", ex.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }
}
