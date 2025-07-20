package com.bivas.teamvault.dto;

import com.bivas.teamvault.entity.TeamMembership;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamMembershipDto {

    @JsonProperty("id")
    public Long id;

    @JsonProperty("userId")
    @NotNull(message = "user id can not be empty")
    public Long UserId;


    @JsonProperty("teamId")
    @NotNull(message = "team id can not be empty")
    public Long TeamId;

    @JsonProperty("role")
    @NotNull(message = "role can not be empty")
    public TeamMembership.Role Role;
}
