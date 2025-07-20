package com.bivas.teamvault.service;

import com.bivas.teamvault.dto.SecretDto;
import com.bivas.teamvault.entity.Secret;
import com.bivas.teamvault.entity.Team;
import com.bivas.teamvault.entity.User;
import com.bivas.teamvault.repository.SecretRepository;
import com.bivas.teamvault.repository.TeamRepository;
import com.bivas.teamvault.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecretService
{

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final SecretRepository secretRepository;

    public Secret CrateSecret(SecretDto secretDto, Long teamId)
    {
        User user = userRepository.findById(secretDto.UserId).orElseThrow(()->new EntityNotFoundException("User not found"));

        Team team = teamRepository.findById(teamId).orElseThrow(()->new EntityNotFoundException("Team not found"));

        Secret secret = Secret.builder()
                              .team(team)
                              .createdBy(user)
                              .name(secretDto.getName())
                              .description(secretDto.getDescription())
                              .encryptedValue(secretDto.getValue())
                              .build();

        secretRepository.save(secret);

        return secret;
    }

    public List<SecretDto> getAllSecrets(Long teamId)
    {
        List<SecretDto> secretDtos = new ArrayList<>();

        List<Secret> secrets = secretRepository.findByTeamId(teamId);

        secrets.forEach(secret -> {

            SecretDto secretDto  = new SecretDto();
            secretDto.setName(secret.getName());
            secretDto.setDescription(secret.getDescription());
            secretDto.setValue(secret.getEncryptedValue());
            secretDtos.add(secretDto);});

        return secretDtos;
    }

    public SecretDto getSecret(Long id)
    {
        Optional<Secret> secret = secretRepository.findById(id);

        secret.orElseThrow(()->new EntityNotFoundException("Secret not found"));

        SecretDto secretDto = new SecretDto();

        secretDto.setName(secret.get().getName());

        secretDto.setDescription(secret.get().getDescription());

        secretDto.setValue(secret.get().getEncryptedValue());

        return secretDto;
    }
}
