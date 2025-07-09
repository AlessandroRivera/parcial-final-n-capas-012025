package com.uca.parcialfinalncapas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketStatusUpdateRequest {
    @NotNull(message = "El ID del ticket no puede ser nulo")
    private Long id;
    
    @NotBlank(message = "El estado no puede estar vacio")
    private String estado;
} 