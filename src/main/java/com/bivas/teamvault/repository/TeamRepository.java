package com.bivas.teamvault.repository;

import com.bivas.teamvault.entity.Team;
import com.bivas.teamvault.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByOwner(User owner);
}
