package com.bivas.teamvault.service;

import com.bivas.teamvault.dto.SecretDto;
import com.bivas.teamvault.dto.UserDto;
import com.bivas.teamvault.entity.Secret;
import com.bivas.teamvault.entity.Team;
import com.bivas.teamvault.entity.User;
import com.bivas.teamvault.permission.SecretPermissionEvaluator;
import com.bivas.teamvault.provider.secret.SecretServiceProvider;
import com.bivas.teamvault.provider.secret.SecretServiceProviderFactory;
import com.bivas.teamvault.repository.SecretRepository;
import com.bivas.teamvault.repository.TeamRepository;
import com.bivas.teamvault.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SecretService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final SecretRepository secretRepository;
    private final SecretPermissionEvaluator secretPermissionEvaluator;
    private final SecretServiceProvider secretServiceProvider;

    public SecretService(SecretServiceProviderFactory secretServiceProviderFactory, SecretPermissionEvaluator secretPermissionEvaluator, SecretRepository secretRepository, UserRepository userRepository, TeamRepository teamRepository) {
        this.secretServiceProvider = secretServiceProviderFactory.getProvider();
        this.secretPermissionEvaluator = secretPermissionEvaluator;
        this.secretRepository = secretRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    public SecretDto CrateSecret(SecretDto secretDto, Long teamId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String auth0Sub = authentication.getName();

        User user = userRepository.findBySub(auth0Sub).orElseThrow(() -> new EntityNotFoundException("User not found"));

        Team team = teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException("Team not found"));

        UUID uuid = UUID.randomUUID();

        String key = uuid + secretDto.getName();

        String secretReference = secretServiceProvider.SaveSecret(key, secretDto.getContent());

        Secret secret = Secret.builder()
                .team(team)
                .createdBy(user)
                .name(secretDto.getName())
                .description(secretDto.getDescription())
                .SecretReferenceId(secretReference)
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

            String secretValue = secretServiceProvider.GetSecret(secret.getSecretReferenceId());

            UserDto userDto = new UserDto(secret.getCreatedBy().getId(), secret.getCreatedBy().getName(), secret.getCreatedBy().getEmail());

            SecretDto secretDto = new SecretDto(secret.getId(), secret.getName(), secret.getDescription(), userDto, secret.getCreatedAt().toString(), secretValue);

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

        String secretValue = secretServiceProvider.GetSecret(secret.getSecretReferenceId());

        UserDto userDto = new UserDto(secret.getCreatedBy().getId(), secret.getCreatedBy().getName(), secret.getCreatedBy().getEmail());

        return new SecretDto(secret.getId(), secret.getName(), secret.getDescription(), userDto, secret.getCreatedAt().toString(), secretValue);
    }

    public void DeleteSecret(Long id) {

        Optional<Secret> secretOptional = secretRepository.findById(id);

        secretOptional.orElseThrow(() -> new EntityNotFoundException("Secret not found"));

        secretServiceProvider.DeleteSecret(secretOptional.get().getSecretReferenceId());

        secretRepository.deleteById(id);
    }
}
