package com.spring.boot.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NaceDetailsDto {
    private Long orderId;

    private Long level;

    private String code;

    private String parent;

    private String description;

    private String itemsInclude;

    private String itemsAlsoInclude;

    private String ruling;

    private String itemsExclude;

    private String referenceToIsic;

    private String errorMessage;

}
