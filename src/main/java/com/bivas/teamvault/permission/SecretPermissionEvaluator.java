package com.bivas.teamvault.permission;

import com.bivas.teamvault.entity.Secret;
import com.bivas.teamvault.entity.TeamMembership;
import com.bivas.teamvault.repository.SecretRepository;
import com.bivas.teamvault.repository.TeamMembershipRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Component
public class SecretPermissionEvaluator implements DomainPermissionEvaluator {

    private final SecretRepository secretRepository;
    private final TeamMembershipRepository teamMembershipRepository;

    public SecretPermissionEvaluator(SecretRepository secretRepository, TeamMembershipRepository teamMembershipRepository) {
        this.secretRepository = secretRepository;
        this.teamMembershipRepository = teamMembershipRepository;
    }

    @Override
    public boolean supports(String targetType) {
        return "secret".equalsIgnoreCase(targetType);
    }

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        // Optional, implement if needed
        return false;
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if (!supports(targetType)) return false;

        Long secretId = (Long) targetId;

        String auth0Sub = auth.getName();

        Optional<Secret> secretOpt = secretRepository.findById(secretId);
        if (secretOpt.isEmpty()) {
            return false;
        }

        Secret secret = secretOpt.get();

        List<TeamMembership> teamMembershipList = teamMembershipRepository.findByTeamId(secret.getTeam().getId());

        return teamMembershipList.stream().anyMatch(teamMembership -> teamMembership.getUser().getSub().equals(auth0Sub));
    }
}