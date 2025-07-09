package com.uca.parcialfinalncapas.repository;

import com.uca.parcialfinalncapas.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Esta interfaz define el repositorio para la entidad Ticket.
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
