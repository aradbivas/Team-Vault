package com.bivas.teamvault.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> {
    @JsonProperty("error")
    public ErrorResponseDto errorResponseDto;

    @JsonProperty("data")
    public T data;
}
