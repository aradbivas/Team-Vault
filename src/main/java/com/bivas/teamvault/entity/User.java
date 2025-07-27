package com.bivas.teamvault.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String sub;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }
}
