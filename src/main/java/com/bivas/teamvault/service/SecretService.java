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
public class SecretService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final SecretRepository secretRepository;

    public SecretDto CrateSecret(SecretDto secretDto, Long teamId) {

        User user = userRepository.findById(secretDto.UserId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        Team team = teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException("Team not found"));

        Secret secret = Secret.builder()
                .team(team)
                .createdBy(user)
                .name(secretDto.getName())
                .description(secretDto.getDescription())
                .encryptedValue(secretDto.getValue())
                .build();

        secretRepository.save(secret);

        secretDto.setId(secret.getId());

        return secretDto;
    }

    public List<SecretDto> getAllSecrets(Long teamId) {
        List<SecretDto> secretDtos = new ArrayList<>();

        List<Secret> secrets = secretRepository.findByTeamId(teamId);

        secrets.forEach(secret -> {
            SecretDto secretDto = new SecretDto(secret.getId(), secret.getName(), secret.getDescription(), secret.getCreatedBy().getId(), secret.getEncryptedValue());

            secretDtos.add(secretDto);
        });

        return secretDtos;
    }

    public SecretDto getSecret(Long id) {
        Optional<Secret> secretOptional = secretRepository.findById(id);

        secretOptional.orElseThrow(() -> new EntityNotFoundException("Secret not found"));

        Secret secret = secretOptional.get();

        return new SecretDto(secret.getId(), secret.getName(), secret.getDescription(), secret.getCreatedBy().getId(), secret.getEncryptedValue());
    }
}
