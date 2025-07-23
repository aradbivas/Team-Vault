package com.bivas.teamvault.service;

import com.bivas.teamvault.dto.UserDto;
import com.bivas.teamvault.entity.TeamMembership;
import com.bivas.teamvault.entity.User;
import com.bivas.teamvault.repository.TeamMembershipRepository;
import com.bivas.teamvault.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TeamMembershipRepository teamMembershipRepository;


    public UserDto GetUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("User with id " + id + "does not exists");
        }

        User user = optionalUser.get();

        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }

    public UserDto CreateUser(String name, String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            throw new EntityExistsException("User with email " + email + " already exists");
        }

        User user = User.builder()
                .email(email)
                .name(name)
                .build();

        userRepository.save(user);

        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }

    public void DeleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("User with id " + id + "does not exists");
        }

        List<TeamMembership> teamMembershipList = teamMembershipRepository.findByUserId(id);

        if (!teamMembershipList.isEmpty()) {

            teamMembershipRepository.deleteAll(teamMembershipList);
        }

        userRepository.deleteById(id);
    }
}
