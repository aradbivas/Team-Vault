package com.bivas.teamvault.dto;

import com.bivas.teamvault.entity.TeamMembership;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TeamMembershipDto {

    @JsonProperty("id")
    public Long id;

    @JsonProperty("user")
    @NotNull(message = "user id can not be empty")
    public UserDto user;


    @JsonProperty("team")
    @NotNull(message = "team id can not be empty")
    public TeamDto team;

    @JsonProperty("role")
    @NotNull(message = "role can not be empty")
    public TeamMembership.Role Role;
}
