package com.uca.parcialfinalncapas.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TicketResponseList {
    private Long idTicket;
    private String titulo;
    private String descripcion;
    private String estado;
    private String fecha;
    private Long solicitanteId;
    private Long soporteId;
}
