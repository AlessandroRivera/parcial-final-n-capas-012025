package com.uca.parcialfinalncapas.service.impl;

import com.uca.parcialfinalncapas.dto.request.TicketCreateRequest;
import com.uca.parcialfinalncapas.dto.request.TicketUpdateRequest;
import com.uca.parcialfinalncapas.dto.response.TicketResponse;
import com.uca.parcialfinalncapas.dto.response.TicketResponseList;
import com.uca.parcialfinalncapas.entities.Ticket;
import com.uca.parcialfinalncapas.entities.User;
import com.uca.parcialfinalncapas.exceptions.BadTicketRequestException;
import com.uca.parcialfinalncapas.exceptions.TicketNotFoundException;
import com.uca.parcialfinalncapas.exceptions.UserNotFoundException;
import com.uca.parcialfinalncapas.repository.TicketRepository;
import com.uca.parcialfinalncapas.repository.UserRepository;
import com.uca.parcialfinalncapas.service.TicketService;
import com.uca.parcialfinalncapas.utils.enums.Rol;
import com.uca.parcialfinalncapas.utils.mappers.TicketMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Esta clase implementa los servicios relacionados con los tickets.
 * Utiliza el TicketRepository para realizar operaciones de acceso a datos.
 */
@Service
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public TicketResponse createTicket(TicketCreateRequest ticket) {
        var usuarioSolicitante = userRepository.findByCorreo(ticket.getCorreoUsuario())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con correo: " + ticket.getCorreoUsuario()));

        var usuarioSoporte = userRepository.findByCorreo(ticket.getCorreoSoporte())
                .orElseThrow(() -> new UserNotFoundException("Usuario asignado no encontrado con correo: " + ticket.getCorreoSoporte()));

        if (!usuarioSoporte.getNombreRol().equals(Rol.TECH.getValue())) {
            throw new BadTicketRequestException("El usuario asignado no es un técnico de soporte");
        }

        var ticketGuardado = ticketRepository.save(TicketMapper.toEntityCreate(ticket, usuarioSolicitante.getId(), usuarioSoporte.getId()));

        return TicketMapper.toDTO(ticketGuardado, usuarioSolicitante.getCorreo(), usuarioSoporte.getCorreo());
    }

    @Override
    @Transactional
    public TicketResponse updateTicket(TicketUpdateRequest ticket) {
        Ticket ticketExistente = ticketRepository.findById(ticket.getId())
                .orElseThrow(() -> new TicketNotFoundException("Ticket no encontrado con ID: " + ticket.getId()));

        var usuarioSolicitante = userRepository.findById(ticketExistente.getUsuarioId())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        var usuarioSoporte = userRepository.findByCorreo(ticket.getCorreoSoporte())
                .orElseThrow(() -> new UserNotFoundException("Usuario asignado no encontrado con correo: " + ticket.getCorreoSoporte()));

        if (!usuarioSoporte.getNombreRol().equals(Rol.TECH.getValue())) {
            throw new BadTicketRequestException("El usuario asignado no es un técnico de soporte");
        }

        var ticketGuardado = ticketRepository.save(TicketMapper.toEntityUpdate(ticket, usuarioSoporte.getId(), ticketExistente));

        return TicketMapper.toDTO(ticketGuardado, usuarioSolicitante.getCorreo(), usuarioSoporte.getCorreo());
    }

    @Override
    public void deleteTicket(Long id) {
        var ticketExistente = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket no encontrado con ID: " + id));

        ticketRepository.delete(ticketExistente);
    }

    @Override
    public TicketResponse getTicketById(Long id) {
    var ticketExistente = ticketRepository.findById(id)
            .orElseThrow(() -> new TicketNotFoundException("Ticket no encontrado con ID: " + id));

    var usuarioSolicitante = userRepository.findById(ticketExistente.getUsuarioId())
            .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

    var usuarioSoporte = userRepository.findById(ticketExistente.getTecnicoAsignadoId())
            .orElseThrow(() -> new UserNotFoundException("Usuario asignado no encontrado"));

        return TicketMapper.toDTO(ticketExistente, usuarioSolicitante.getCorreo(), usuarioSoporte.getCorreo());
    }

    @Override
    public List<TicketResponseList> getAllTickets() {
        return TicketMapper.toDTOList(ticketRepository.findAll());
    }
}
