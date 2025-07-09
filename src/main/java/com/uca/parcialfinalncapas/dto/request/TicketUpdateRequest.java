package com.uca.parcialfinalncapas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketUpdateRequest {
    @NotNull(message = "El ID del ticket no puede ser nulo")
    private Long id;
    private String descripcion;
    @NotBlank(message = "El estado del ticket no puede estar vacío")
    private String estado;
    @NotNull(message = "Debe asignar un técnico al ticket")
    private String correoSoporte;
}
