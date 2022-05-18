package com.spring.boot.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {
    private String errorMessage;
    private String errorCode;
}
