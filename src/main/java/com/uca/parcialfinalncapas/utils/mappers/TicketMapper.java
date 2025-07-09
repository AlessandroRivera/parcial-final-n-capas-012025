package com.uca.parcialfinalncapas.utils.mappers;

import com.uca.parcialfinalncapas.dto.request.TicketCreateRequest;
import com.uca.parcialfinalncapas.dto.request.TicketUpdateRequest;
import com.uca.parcialfinalncapas.dto.response.TicketResponse;
import com.uca.parcialfinalncapas.dto.response.TicketResponseList;
import com.uca.parcialfinalncapas.entities.Ticket;
import com.uca.parcialfinalncapas.utils.enums.State;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TicketMapper {

    public static Ticket toEntityCreate(TicketCreateRequest ticketRequest, Long usuarioId, Long tecnicoAsignadoId) {
        return Ticket.builder()
                .titulo(ticketRequest.getTitulo())
                .descripcion(ticketRequest.getDescripcion())
                .estado(State.OPEN.getDescription())
                .usuarioId(usuarioId)
                .tecnicoAsignadoId(tecnicoAsignadoId)
                .fecha(LocalDateTime.now())
                .build();
    }

    public static Ticket toEntityUpdate(TicketUpdateRequest ticketRequest, Long tecnicoAsignadoId, Ticket ticketOriginal) {
        return Ticket.builder()
                .id(ticketOriginal.getId())
                .titulo(ticketOriginal.getTitulo())
                .descripcion(ticketRequest.getDescripcion() != null ? ticketRequest.getDescripcion() : ticketOriginal.getDescripcion())
                .estado(ticketRequest.getEstado() != null
                        ? (State.IN_PROGRESS.name().equals(ticketRequest.getEstado())
                        ? State.IN_PROGRESS.getDescription()
                        : State.CLOSED.name().equals(ticketRequest.getEstado())
                        ? State.CLOSED.getDescription()
                        : State.OPEN.getDescription())
                        : ticketOriginal.getEstado())
                .tecnicoAsignadoId(tecnicoAsignadoId != null ? tecnicoAsignadoId : ticketOriginal.getTecnicoAsignadoId())
                .usuarioId(ticketOriginal.getUsuarioId())
                .fecha(LocalDateTime.now())
                .build();
    }

    public static TicketResponse toDTO(Ticket ticket, String correoUsuario, String correoSoporte) {
        return TicketResponse.builder()
                .idTicket(ticket.getId())
                .titulo(ticket.getTitulo())
                .descripcion(ticket.getDescripcion())
                .estado(ticket.getEstado())
                .correoSoporte(correoSoporte)
                .correoSolicitante(correoUsuario)
                .build();
    }

    public static List<TicketResponseList> toDTOList(List<Ticket> tickets) {
        return tickets.stream()
                .map(ticket -> TicketResponseList.builder()
                        .idTicket(ticket.getId())
                        .titulo(ticket.getTitulo())
                        .descripcion(ticket.getDescripcion())
                        .estado(ticket.getEstado())
                        .solicitanteId(ticket.getUsuarioId())
                        .soporteId(ticket.getTecnicoAsignadoId())
                        .build())
                .toList();
    }
}
