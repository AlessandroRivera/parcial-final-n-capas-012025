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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@AllArgsConstructor
public class TicketController {
    private TicketService ticketService;

    // TECH ve todos los tickets, USER ve solo sus tickets
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('TECH')")
    public ResponseEntity<GeneralResponse> getAllTickets() {
        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuario = authentication.getName();
        
        // Obtener tickets segun el rol del usuario
        var tickets = ticketService.getTicketsByUserRole(correoUsuario);
        
        return ResponseBuilderUtil.buildResponse("Tickets obtenidos correctamente",
                tickets.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK,
                tickets);
    }

    // USER puede ver sus tickets, TECH puede ver cualquier ticket
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('TECH')")
    public ResponseEntity<GeneralResponse> getTicketById(@PathVariable Long id) {
        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuario = authentication.getName();
        
        // Obtener ticket con validacion de permisos
        TicketResponse ticket = ticketService.getTicketByIdWithUserValidation(id, correoUsuario);
        
        return ResponseBuilderUtil.buildResponse("Ticket encontrado", HttpStatus.OK, ticket);
    }

    // USER puede crear tickets, validar que el correo solicitante coincida con el usuario autenticado
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GeneralResponse> createTicket(@Valid @RequestBody TicketCreateRequest ticket) {
        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuario = authentication.getName();
        
        // Validar que el usuario solo pueda crear tickets para si mismo
        if (!correoUsuario.equals(ticket.getCorreoUsuario())) {
            throw new BadTicketRequestException("Solo puedes crear tickets para tu propio usuario");
        }
        
        TicketResponse createdTicket = ticketService.createTicket(ticket);
        return ResponseBuilderUtil.buildResponse("Ticket creado correctamente", HttpStatus.CREATED, createdTicket);
    }

    // Solo TECH puede actualizar tickets
    @PutMapping
    @PreAuthorize("hasRole('TECH')")
    public ResponseEntity<GeneralResponse> updateTicket(@Valid @RequestBody TicketUpdateRequest ticket) {
        TicketResponse updatedTicket = ticketService.updateTicket(ticket);
        return ResponseBuilderUtil.buildResponse("Ticket actualizado correctamente", HttpStatus.OK, updatedTicket);
    }

    // Solo TECH puede eliminar tickets
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TECH')")
    public ResponseEntity<GeneralResponse> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseBuilderUtil.buildResponse("Ticket eliminado correctamente", HttpStatus.OK, null);
    }
}
