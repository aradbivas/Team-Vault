package com.bivas.teamvault.service;

import com.bivas.teamvault.entity.User;
import com.bivas.teamvault.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private  final UserRepository userRepository;

    public User CreateUser(String name, String email)
    {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if(existingUser.isPresent())
        {
            throw new RuntimeException("User with email " + email + " already exists");
        }

        User user = User.builder()
                        .email(email)
                        .name(name)
                        .build();

        userRepository.save(user);

        return  user;
    }
}
