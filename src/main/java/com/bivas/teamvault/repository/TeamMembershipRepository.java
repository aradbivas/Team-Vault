package com.bivas.teamvault.repository;

import com.bivas.teamvault.entity.TeamMembership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMembershipRepository extends JpaRepository<TeamMembership, Long> {
    List<TeamMembership> findByUserId(Long userId);

    List<TeamMembership> findByTeamId(Long teamId);
}
