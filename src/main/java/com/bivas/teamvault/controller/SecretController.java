package com.bivas.teamvault.controller;


import com.bivas.teamvault.dto.SecretDto;
import com.bivas.teamvault.dto.TeamDto;
import com.bivas.teamvault.entity.Secret;
import com.bivas.teamvault.entity.Team;
import com.bivas.teamvault.repository.SecretRepository;
import com.bivas.teamvault.repository.TeamRepository;
import com.bivas.teamvault.service.SecretService;
import com.bivas.teamvault.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("teams/{teamId}/secrets")
@RequiredArgsConstructor
public class SecretController
{
    private final SecretRepository secretRepository;
    private final SecretService secretService;

    @GetMapping
    public ResponseEntity<List<Secret>> getAllSecrets(@PathVariable Long teamId)
    {
        List<Secret> secrets = secretRepository.findByTeamId(teamId);

        return ResponseEntity.ok(secrets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Secret> getSecret(@PathVariable Long teamId, @PathVariable Long id)
    {
        Optional<Secret> secret = secretRepository.findById(id);

        return secret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<?> createSecret(@PathVariable Long teamId, @RequestBody SecretDto secretDto)
    {
        try
        {
            Secret secret = secretService.CrateSecret(secretDto, teamId);

            return ResponseEntity.ok(secret);
        }
        catch (Exception ex)
        {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSecret(@PathVariable long id)
    {
        secretRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
