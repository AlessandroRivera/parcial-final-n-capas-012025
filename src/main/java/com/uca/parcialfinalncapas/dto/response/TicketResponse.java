package com.uca.parcialfinalncapas.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TicketResponse {
    private Long idTicket;
    private String titulo;
    private String descripcion;
    private String estado;
    private LocalDate fecha;
    private String correoSolicitante;
    private String correoSoporte;
}
