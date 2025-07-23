package com.bivas.teamvault.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponseDto {

    @JsonProperty("status")
    public String status;

    @JsonProperty("title")
    public String title;

    @JsonProperty("error")
    public String error;
}
