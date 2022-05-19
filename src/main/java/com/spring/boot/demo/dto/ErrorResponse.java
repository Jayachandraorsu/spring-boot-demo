package com.spring.boot.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {
    private String errorMessage;
    private String errorCode;
}
