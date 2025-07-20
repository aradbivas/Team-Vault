package com.bivas.teamvault.controller;

import com.bivas.teamvault.dto.UserInviteDto;
import com.bivas.teamvault.entity.User;
import com.bivas.teamvault.exception.KeyNotFoundException;
import com.bivas.teamvault.service.UserInviteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/team-vault")
@RequiredArgsConstructor
public class UserInvitationController {

    private final UserInviteService userInviteService;

    @PostMapping("/team/{teamId}/invite")
    public ResponseEntity<User> InviteUserToTeam(@RequestBody @Valid UserInviteDto userInviteDto, @PathVariable Long teamId)
    {
        try
        {
            userInviteService.InviteUser(userInviteDto.Email, teamId);

            return ResponseEntity.ok().build();
        }
        catch (Exception ex)
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/team/accept-invite")
    public ResponseEntity<?> acceptInvite(@RequestParam String token,
                                          @RequestParam Long userId)
    {
        try
        {
            userInviteService.AcceptUserInvite(token, userId);

            return ResponseEntity.ok().build();
        }
        catch (KeyNotFoundException ex)
        {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        catch (Exception ex)
        {
            return ResponseEntity.badRequest().build();
        }
    }
}
