package com.bivas.teamvault.permission;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
public class DelegatingPermissionEvaluator implements PermissionEvaluator {

    private final List<DomainPermissionEvaluator> evaluators;

    public DelegatingPermissionEvaluator(List<DomainPermissionEvaluator> evaluators) {
        this.evaluators = evaluators;
    }

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if (targetDomainObject == null) return false;
        String targetType = targetDomainObject.getClass().getSimpleName();
        for (DomainPermissionEvaluator evaluator : evaluators) {
            if (evaluator.supports(targetType)) {
                return evaluator.hasPermission(auth, targetDomainObject, permission);
            }
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        for (DomainPermissionEvaluator evaluator : evaluators) {
            if (evaluator.supports(targetType)) {
                return evaluator.hasPermission(auth, targetId, targetType, permission);
            }
        }
        return false;
    }
}

