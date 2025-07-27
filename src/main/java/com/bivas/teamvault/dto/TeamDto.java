package com.bivas.teamvault.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@JsonRootName(value = "Team")
public class TeamDto {

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
    @JsonProperty("description")
    public String Description;

    @Setter
    @Getter
    @JsonProperty("owner")
    public UserDto Owner;
}
