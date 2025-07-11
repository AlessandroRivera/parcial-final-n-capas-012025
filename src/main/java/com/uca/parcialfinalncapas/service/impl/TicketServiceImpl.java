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
import com.uca.parcialfinalncapas.utils.enums.State;
import com.uca.parcialfinalncapas.utils.mappers.TicketMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


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

    @Override
    public List<TicketResponseList> getTicketsByUserRole(String correoUsuario) {
        // Buscar usuario por correo
        User usuario = userRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado: " + correoUsuario));

        // Si es TECH, retornar todos los tickets
        if (Rol.TECH.getValue().equals(usuario.getNombreRol())) {
            return TicketMapper.toDTOList(ticketRepository.findAll());
        }
        
        // Si es USER, retornar solo sus tickets
        if (Rol.USER.getValue().equals(usuario.getNombreRol())) {
            List<Ticket> userTickets = ticketRepository.findByUsuarioId(usuario.getId());
            return TicketMapper.toDTOList(userTickets);
        }

        // Si no tiene rol valido, retornar lista vacia
        return List.of();
    }

    @Override
    public TicketResponse getTicketByIdWithUserValidation(Long id, String correoUsuario) {
        // Buscar ticket
        Ticket ticketExistente = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket no encontrado con ID: " + id));

        // Buscar usuario autenticado
        User usuarioAutenticado = userRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado: " + correoUsuario));

        // Si es TECH, puede ver cualquier ticket
        if (Rol.TECH.getValue().equals(usuarioAutenticado.getNombreRol())) {
            return getTicketById(id);
        }

        // Si es USER, solo puede ver sus propios tickets
        if (Rol.USER.getValue().equals(usuarioAutenticado.getNombreRol())) {
            if (!ticketExistente.getUsuarioId().equals(usuarioAutenticado.getId())) {
                throw new BadTicketRequestException("No tienes permisos para ver este ticket");
            }
            return getTicketById(id);
        }

        throw new BadTicketRequestException("Rol de usuario no valido");
    }

    @Override
    @Transactional
    public TicketResponse updateTicketStatus(Long id, String nuevoEstado) {
        // Buscar ticket existente
        Ticket ticketExistente = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket no encontrado con ID: " + id));

        // Validar que el estado sea valido
        String estadoFinal;
        switch (nuevoEstado.toUpperCase()) {
            case "OPEN":
                estadoFinal = State.OPEN.getDescription();
                break;
            case "IN_PROGRESS":
                estadoFinal = State.IN_PROGRESS.getDescription();
                break;
            case "CLOSED":
                estadoFinal = State.CLOSED.getDescription();
                break;
            default:
                throw new BadTicketRequestException("Estado no valido: " + nuevoEstado + 
                    ". Estados permitidos: OPEN, IN_PROGRESS, CLOSED");
        }

        // Actualizar solo el estado
        ticketExistente.setEstado(estadoFinal);
        ticketExistente.setFecha(LocalDateTime.now());

        // Guardar cambios
        Ticket ticketActualizado = ticketRepository.save(ticketExistente);

        // Obtener usuarios para la respuesta
        var usuarioSolicitante = userRepository.findById(ticketActualizado.getUsuarioId())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        var usuarioSoporte = userRepository.findById(ticketActualizado.getTecnicoAsignadoId())
                .orElseThrow(() -> new UserNotFoundException("Usuario asignado no encontrado"));

        return TicketMapper.toDTO(ticketActualizado, usuarioSolicitante.getCorreo(), usuarioSoporte.getCorreo());
    }
}
