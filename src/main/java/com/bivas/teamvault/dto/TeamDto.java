package com.bivas.teamvault.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class TeamDto {

    @Setter
    @Getter
    @JsonProperty("name")
    public String Name;

    @Setter
    @Getter
    @JsonProperty("userId")
    public Long UserId;
}
