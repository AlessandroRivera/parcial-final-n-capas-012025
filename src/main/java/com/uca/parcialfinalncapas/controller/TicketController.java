package com.uca.parcialfinalncapas.controller;

import com.uca.parcialfinalncapas.dto.request.TicketCreateRequest;
import com.uca.parcialfinalncapas.dto.request.TicketStatusUpdateRequest;
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

    // USER puede crear tickets para si mismo, TECH puede crear tickets para cualquier usuario
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('TECH')")
    public ResponseEntity<GeneralResponse> createTicket(@Valid @RequestBody TicketCreateRequest ticket) {
        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuario = authentication.getName();
        
        // Si es USER, validar que solo pueda crear tickets para si mismo
        if (authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
            if (!correoUsuario.equals(ticket.getCorreoUsuario())) {
                throw new BadTicketRequestException("USER solo puede crear tickets para su propio usuario");
            }
        }
        
        // TECH puede crear tickets para cualquier usuario (sin validacion adicional)
        
        TicketResponse createdTicket = ticketService.createTicket(ticket);
        return ResponseBuilderUtil.buildResponse("Ticket creado correctamente", HttpStatus.CREATED, createdTicket);
    }

    // Solo TECH puede actualizar tickets (cambiar estado: OPEN, IN_PROGRESS, CLOSED)
    @PutMapping
    @PreAuthorize("hasRole('TECH')")
    public ResponseEntity<GeneralResponse> updateTicket(@Valid @RequestBody TicketUpdateRequest ticket) {
        TicketResponse updatedTicket = ticketService.updateTicket(ticket);
        return ResponseBuilderUtil.buildResponse("Ticket actualizado correctamente", HttpStatus.OK, updatedTicket);
    }

    // Solo TECH puede cambiar el estado de un ticket (funcionalidad principal segun Parte 4)
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('TECH')")
    public ResponseEntity<GeneralResponse> updateTicketStatus(@PathVariable Long id, 
                                                            @Valid @RequestBody TicketStatusUpdateRequest statusRequest) {
        // TECH puede cambiar estado del ticket: OPEN -> IN_PROGRESS -> CLOSED
        TicketResponse updatedTicket = ticketService.updateTicketStatus(id, statusRequest.getEstado());
        return ResponseBuilderUtil.buildResponse("Estado del ticket actualizado correctamente", HttpStatus.OK, updatedTicket);
    }

    // Solo TECH puede eliminar tickets
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TECH')")
    public ResponseEntity<GeneralResponse> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseBuilderUtil.buildResponse("Ticket eliminado correctamente", HttpStatus.OK, null);
    }
}
