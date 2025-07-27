package com.bivas.teamvault.permission;

import org.springframework.security.access.PermissionEvaluator;

public interface DomainPermissionEvaluator extends PermissionEvaluator {
    boolean supports(String targetType);
}