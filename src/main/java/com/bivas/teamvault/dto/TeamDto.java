package com.bivas.teamvault.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class TeamDto {

    @Setter
    @Getter
    @JsonProperty("name")
    @NotBlank(message = "name can not be empty")
    public String Name;

    @Setter
    @Getter
    @JsonProperty("owner")
    @NotBlank(message = "user id can not be empty")
    public Long UserId;
}
