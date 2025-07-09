package com.uca.parcialfinalncapas.service;

import com.uca.parcialfinalncapas.dto.request.TicketCreateRequest;
import com.uca.parcialfinalncapas.dto.request.TicketUpdateRequest;
import com.uca.parcialfinalncapas.dto.response.TicketResponse;
import com.uca.parcialfinalncapas.dto.response.TicketResponseList;

import java.util.List;

public interface TicketService {
    /**
     * Crea un nuevo ticket.
     *
     * @param ticket el ticket a crear
     * @return el ticket creado
     */
    TicketResponse createTicket(TicketCreateRequest ticket);

    /**
     * Actualiza un ticket existente.
     *
     * @param ticket los nuevos datos del ticket
     * @return el ticket actualizado
     */
    TicketResponse updateTicket(TicketUpdateRequest ticket);

    /**
     * Elimina un ticket por su ID.
     *
     * @param id el ID del ticket a eliminar
     */
    void deleteTicket(Long id);

    /**
     * Obtiene un ticket por su ID.
     *
     * @param id el ID del ticket a obtener
     * @return el ticket encontrado o null si no existe
     */
    TicketResponse getTicketById(Long id);

    /**
     * Obtiene todos los tickets.
     *
     * @return una lista de todos los tickets
     */
    List<TicketResponseList> getAllTickets();
}
