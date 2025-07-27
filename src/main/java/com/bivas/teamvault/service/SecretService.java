package com.bivas.teamvault.service;

import com.bivas.teamvault.dto.SecretDto;
import com.bivas.teamvault.dto.UserDto;
import com.bivas.teamvault.entity.Secret;
import com.bivas.teamvault.entity.Team;
import com.bivas.teamvault.entity.User;
import com.bivas.teamvault.permission.SecretPermissionEvaluator;
import com.bivas.teamvault.repository.SecretRepository;
import com.bivas.teamvault.repository.TeamRepository;
import com.bivas.teamvault.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final SecretPermissionEvaluator secretPermissionEvaluator;

    public SecretDto CrateSecret(SecretDto secretDto, Long teamId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String auth0Sub = authentication.getName();

        User user = userRepository.findBySub(auth0Sub).orElseThrow(() -> new EntityNotFoundException("User not found"));

        Team team = teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException("Team not found"));

        Secret secret = Secret.builder()
                .team(team)
                .createdBy(user)
                .name(secretDto.getName())
                .description(secretDto.getDescription())
                .encryptedValue(secretDto.getContent())
                .build();

        secretRepository.save(secret);

        secretDto.setId(secret.getId());

        secretDto.setCreatedAt(secret.getCreatedAt().toString());

        UserDto userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
        
        secretDto.setCreatedBy(userDto);

        return secretDto;
    }

    public List<SecretDto> getAllSecrets(Long teamId) {

        List<SecretDto> secretDtos = new ArrayList<>();

        List<Secret> secrets = secretRepository.findByTeamId(teamId);

        secrets.forEach(secret -> {

            UserDto userDto = new UserDto(secret.getCreatedBy().getId(), secret.getCreatedBy().getName(), secret.getCreatedBy().getEmail());

            SecretDto secretDto = new SecretDto(secret.getId(), secret.getName(), secret.getDescription(), userDto, secret.getCreatedAt().toString(), secret.getEncryptedValue());

            secretDtos.add(secretDto);
        });

        return secretDtos;
    }

    public SecretDto getSecret(Long id) {

        Optional<Secret> secretOptional = secretRepository.findById(id);

        secretOptional.orElseThrow(() -> new EntityNotFoundException("Secret not found"));

        Secret secret = secretOptional.get();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!secretPermissionEvaluator.hasPermission(auth, secret, "read")) {
            throw new AccessDeniedException("Forbidden");
        }

        UserDto userDto = new UserDto(secret.getCreatedBy().getId(), secret.getCreatedBy().getName(), secret.getCreatedBy().getEmail());

        return new SecretDto(secret.getId(), secret.getName(), secret.getDescription(), userDto, secret.getCreatedAt().toString(), secret.getEncryptedValue());
    }
}
