package com.uca.parcialfinalncapas.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class GeneralResponse {
    private String uri;
    private String message;
    private int status;
    private LocalDate time;
    private Object data;
}
