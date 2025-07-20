package com.bivas.teamvault.controller;


import com.bivas.teamvault.dto.SecretDto;
import com.bivas.teamvault.entity.Secret;
import com.bivas.teamvault.repository.SecretRepository;
import com.bivas.teamvault.service.SecretService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/team-vault/teams/{teamId}/secrets")
@RequiredArgsConstructor
public class SecretController
{
    private final SecretRepository secretRepository;
    private final SecretService secretService;

    @GetMapping
    public ResponseEntity<List<SecretDto>> getAllSecrets(@PathVariable Long teamId)
    {
        List<SecretDto> secretDtos = secretService.getAllSecrets(teamId);

        return ResponseEntity.ok(secretDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecretDto> getSecret(@PathVariable Long teamId, @PathVariable Long id)
    {
        try
        {
            SecretDto secret = secretService.getSecret(id);

            return ResponseEntity.ok(secret);
        }
        catch (EntityNotFoundException entityNotFoundException){

            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity<?> createSecret(@PathVariable Long teamId, @RequestBody @Valid SecretDto secretDto)
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
