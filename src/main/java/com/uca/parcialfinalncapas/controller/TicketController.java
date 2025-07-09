package com.uca.parcialfinalncapas.controller;

import com.uca.parcialfinalncapas.dto.request.TicketCreateRequest;
import com.uca.parcialfinalncapas.dto.request.TicketUpdateRequest;
import com.uca.parcialfinalncapas.dto.response.GeneralResponse;
import com.uca.parcialfinalncapas.dto.response.TicketResponse;
import com.uca.parcialfinalncapas.dto.response.TicketResponseList;
import com.uca.parcialfinalncapas.exceptions.BadTicketRequestException;
import com.uca.parcialfinalncapas.service.TicketService;
import com.uca.parcialfinalncapas.utils.ResponseBuilderUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@AllArgsConstructor
public class TicketController {
    private TicketService ticketService;

    @GetMapping
    public ResponseEntity<GeneralResponse> getAllTickets() {
        return ResponseBuilderUtil.buildResponse("Tickets obtenidos correctamente",
                ticketService.getAllTickets().isEmpty() ? HttpStatus.BAD_REQUEST : HttpStatus.OK,
                ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getTicketById(@PathVariable Long id) {
        TicketResponse ticket = ticketService.getTicketById(id);
        if (ticket == null) {
            throw new BadTicketRequestException("Ticket no encontrado");
        }
        return ResponseBuilderUtil.buildResponse("Ticket found", HttpStatus.OK, ticket);
    }

    @PostMapping
    public ResponseEntity<GeneralResponse> createTicket(@Valid @RequestBody TicketCreateRequest ticket) {
        TicketResponse createdTicket = ticketService.createTicket(ticket);
        return ResponseBuilderUtil.buildResponse("Ticket creado correctamente", HttpStatus.CREATED, createdTicket);
    }

    @PutMapping
    public ResponseEntity<GeneralResponse> updateTicket(@Valid @RequestBody TicketUpdateRequest ticket) {
        TicketResponse updatedTicket = ticketService.updateTicket(ticket);
        return ResponseBuilderUtil.buildResponse("Ticket actualizado correctamente", HttpStatus.OK, updatedTicket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseBuilderUtil.buildResponse("Ticket eliminado correctamente", HttpStatus.OK, null);
    }
}
