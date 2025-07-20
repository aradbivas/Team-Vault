package com.bivas.teamvault.repository;

import com.bivas.teamvault.entity.Secret;
import com.bivas.teamvault.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SecretRepository extends JpaRepository<Secret, Long> {
    List<Secret> findByTeamId(Long teamId);

    List<Secret> team(Team team);

    Long id(Long id);
}
