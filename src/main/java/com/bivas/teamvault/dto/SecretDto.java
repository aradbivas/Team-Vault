package com.bivas.teamvault.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class SecretDto
{
    @Setter
    @Getter
    @JsonProperty("name")
    public String Name;

    @Setter
    @Getter
    @JsonProperty("description")
    public String Description;

    @Setter
    @Getter
    @JsonProperty("userId")
    public Long UserId;

    @Setter
    @Getter
    @JsonProperty("value")
    public String Value;
}
