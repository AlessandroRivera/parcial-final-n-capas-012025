package com.uca.parcialfinalncapas.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TicketCreateRequest {
    @NotBlank(message = "El título no puede estar vacío")
    private String titulo;
    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;
    @NotBlank(message = "Debe indicar el correo del usuario")
    private String correoUsuario;
    @NotBlank(message = "Debe indicar el correo del técnico asignado")
    private String correoSoporte;
}
