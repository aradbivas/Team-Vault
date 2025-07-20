package com.bivas.teamvault.controller;

import com.bivas.teamvault.dto.UserDto;
import com.bivas.teamvault.entity.User;
import com.bivas.teamvault.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bivas.teamvault.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/team-vault/users")
@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;

    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers()
    {
        List<User> users = userRepository.findAll();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id)
    {
        Optional<User> user = userRepository.findById(id);

        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDto userDto)
    {
        try
        {
           User user = userService.CreateUser(userDto.Name, userDto.Email);

            return ResponseEntity.ok(user);
        }
        catch (Exception ex)
        {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id)
    {
        userRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
