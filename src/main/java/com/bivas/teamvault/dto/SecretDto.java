package com.bivas.teamvault.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class SecretDto
{
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
    @JsonProperty("userId")
    @NotBlank(message = "user id can not be empty")
    public Long UserId;

    @Setter
    @Getter
    @JsonProperty("value")
    @NotBlank(message = "secret value can not be empty")
    public String Value;
}
