package com.uca.parcialfinalncapas.repository;

import com.uca.parcialfinalncapas.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Esta interfaz define el repositorio para la entidad Ticket.
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    /**
     * Busca tickets por ID de usuario
     * 
     * @param usuarioId ID del usuario
     * @return lista de tickets del usuario
     */
    List<Ticket> findByUsuarioId(Long usuarioId);
}
