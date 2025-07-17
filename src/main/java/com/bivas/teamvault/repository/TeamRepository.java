package com.bivas.teamvault.repository;

import com.bivas.teamvault.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, Long> {
    // Add custom queries if needed later
}
