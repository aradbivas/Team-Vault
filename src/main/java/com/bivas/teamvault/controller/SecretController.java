package com.bivas.teamvault.controller;


import com.bivas.teamvault.dto.ErrorResponseDto;
import com.bivas.teamvault.dto.ResponseDto;
import com.bivas.teamvault.dto.SecretDto;
import com.bivas.teamvault.repository.SecretRepository;
import com.bivas.teamvault.service.SecretService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/team-vault/teams/{teamId}/secrets")
@RequiredArgsConstructor
public class SecretController {
    private final SecretRepository secretRepository;
    private final SecretService secretService;

    @GetMapping
    public ResponseEntity<ResponseDto<List<SecretDto>>> getAllSecrets(@PathVariable Long teamId) {
        ResponseDto<List<SecretDto>> responseDto = new ResponseDto<>();

        try {

            List<SecretDto> secretDtos = secretService.getAllSecrets(teamId);

            responseDto.setData(secretDtos);

            return ResponseEntity.ok(responseDto);

        } catch (Exception exception) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(), "General Error", exception.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<SecretDto>> getSecret(@PathVariable Long teamId, @PathVariable Long id) {

        ResponseDto<SecretDto> responseDto = new ResponseDto<>();

        try {
            SecretDto secretDtos = secretService.getSecret(id);

            responseDto.setData(secretDtos);

            return ResponseEntity.ok(responseDto);

        } catch (EntityNotFoundException entityNotFoundException) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.NOT_FOUND.name(), "Secret Not found", entityNotFoundException.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        } catch (Exception exception) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(), "General Error", exception.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseDto<SecretDto>> createSecret(@PathVariable Long teamId, @RequestBody @Valid SecretDto secretDto) {

        ResponseDto<SecretDto> responseDto = new ResponseDto<>();

        try {

            secretDto = secretService.CrateSecret(secretDto, teamId);

            responseDto.setData(secretDto);

            return ResponseEntity.ok(responseDto);

        } catch (EntityNotFoundException entityNotFoundException) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.NOT_FOUND.name(), "Not found", entityNotFoundException.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        } catch (Exception exception) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(), "General Error", exception.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<?>> deleteSecret(@PathVariable long id) {
        ResponseDto<SecretDto> responseDto = new ResponseDto<>();

        try {

            secretRepository.deleteById(id);

            return ResponseEntity.ok().build();

        } catch (Exception exception) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(), "General Error", exception.getMessage());

            responseDto.setErrorResponseDto(errorResponseDto);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }
}
