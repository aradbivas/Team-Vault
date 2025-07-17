package com.bivas.teamvault.dto;

import com.bivas.teamvault.entity.TeamMembership;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class TeamMembershipDto {

    @Getter
    @Setter
    @JsonProperty("userId")
    public Long UserId;

    @Getter
    @Setter
    @JsonProperty("role")
    public TeamMembership.Role Role;
}
