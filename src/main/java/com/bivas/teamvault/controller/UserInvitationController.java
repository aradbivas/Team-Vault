package com.bivas.teamvault.controller;

import com.bivas.teamvault.dto.ErrorResponseDto;
import com.bivas.teamvault.dto.ResponseDto;
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

    @PostMapping("/team/{teamId}/invite")
    public ResponseEntity<ResponseDto<?>> InviteUserToTeam(@RequestBody @Valid UserInviteDto userInviteDto, @PathVariable Long teamId) {

        ResponseDto<?> responseDto = new ResponseDto<>();
        try {

            userInviteService.InviteUser(userInviteDto.Email, teamId);

            return ResponseEntity.ok().build();
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

    @GetMapping("/team/accept-invite")
    public ResponseEntity<ResponseDto<?>> acceptInvite(@RequestParam String token,
                                                       @RequestParam Long userId) {

        ResponseDto<?> responseDto = new ResponseDto<>();

        try {
            userInviteService.AcceptUserInvite(token, userId);

            return ResponseEntity.ok().build();
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
