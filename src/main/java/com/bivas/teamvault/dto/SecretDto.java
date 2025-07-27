package com.bivas.teamvault.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class SecretDto {
    @Setter
    @Getter
    @JsonProperty("id")
    public Long Id;

    @Setter
    @Getter
    @JsonProperty("name")
    @NotBlank(message = "Secret name can not be empty")
    public String Name;

    @Setter
    @Getter
    @JsonProperty("description")
    public String Description;

    @Setter
    @Getter
    @JsonProperty("createdBy")
    public UserDto CreatedBy;

    @Setter
    @Getter
    @JsonProperty("createdAt")
    public String CreatedAt;

    @Setter
    @Getter
    @JsonProperty("content")
    @NotBlank(message = "secret content can not be empty")
    public String Content;
}
