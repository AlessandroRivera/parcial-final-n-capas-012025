package com.uca.parcialfinalncapas.repository;

import com.uca.parcialfinalncapas.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Esta interfaz define el repositorio para la entidad User.
 * Permite realizar operaciones CRUD sobre los usuarios.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCorreo(String correo);
}
