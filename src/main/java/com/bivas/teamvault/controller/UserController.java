package com.bivas.teamvault.controller;

import com.bivas.teamvault.dto.ErrorResponseDto;
import com.bivas.teamvault.dto.ResponseDto;
import com.bivas.teamvault.dto.UserDto;
import com.bivas.teamvault.repository.UserRepository;
import com.bivas.teamvault.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/team-vault/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<ResponseDto<UserDto>> getMeUser() {

        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        try {

            UserDto userDto = userService.GetMeUser();

            responseDto.setData(userDto);

            return ResponseEntity.ok(responseDto);

        } catch (EntityNotFoundException entityNotFoundException) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.NOT_FOUND.name(), "User Not found", entityNotFoundException.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        } catch (Exception exception) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(), "General Error", exception.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<UserDto>> getUser(@PathVariable Long id) {

        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        try {

            UserDto userDto = userService.GetUser(id);

            responseDto.setData(userDto);

            return ResponseEntity.ok(responseDto);

        } catch (EntityNotFoundException entityNotFoundException) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.NOT_FOUND.name(), "User Not found", entityNotFoundException.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        } catch (Exception exception) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(), "General Error", exception.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @PostMapping
    public ResponseEntity<ResponseDto<UserDto>> createUser(Authentication authentication, @RequestBody @Valid UserDto userDto) {

        ResponseDto<UserDto> responseDto = new ResponseDto<>();

        try {

            String authSub = authentication.getName();

            userDto = userService.CreateUser(authSub, userDto.Name, userDto.Email);

            responseDto.setData(userDto);

            return ResponseEntity.ok(responseDto);

        } catch (EntityExistsException ex) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.BAD_REQUEST.name(), "User Already Exists", ex.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        } catch (Exception ex) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(), "General Error", ex.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<?>> deleteUser(@PathVariable long id) {

        ResponseDto<?> responseDto = new ResponseDto<>();

        try {

            userService.DeleteUser(id);

            return ResponseEntity.noContent().build();

        } catch (EntityNotFoundException entityNotFoundException) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.NOT_FOUND.name(), "User Not Found", entityNotFoundException.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        } catch (Exception ex) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(), "General Error", ex.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }
}
