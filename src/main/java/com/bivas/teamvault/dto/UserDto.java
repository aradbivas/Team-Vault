package com.bivas.teamvault.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class UserDto {

    @Setter
    @Getter
    @JsonProperty("id")
    public Long Id;

    @Setter
    @Getter
    @JsonProperty("name")
    @NotBlank(message = "name can not be empty")
    public String Name;

    @Setter
    @Getter
    @JsonProperty("email")
    @NotBlank(message = "email can not be empty")
    public String Email;


}
