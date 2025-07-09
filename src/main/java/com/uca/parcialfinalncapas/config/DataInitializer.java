package com.uca.parcialfinalncapas.config;

import com.uca.parcialfinalncapas.entities.User;
import com.uca.parcialfinalncapas.repository.UserRepository;
import com.uca.parcialfinalncapas.utils.enums.Rol;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Crear usuarios de prueba solo si no existen
        if (userRepository.count() == 0) {
            // Usuario con rol USER
            User user1 = User.builder()
                    .nombre("Juan Perez")
                    .correo("user@test.com")
                    .password("password123")
                    .nombreRol(Rol.USER.getValue())
                    .build();

            // Usuario con rol TECH
            User user2 = User.builder()
                    .nombre("Maria Garcia")
                    .correo("tech@test.com")
                    .password("password123")
                    .nombreRol(Rol.TECH.getValue())
                    .build();

            userRepository.save(user1);
            userRepository.save(user2);

            System.out.println("Usuarios de prueba creados:");
            System.out.println("- USER: user@test.com / password123");
            System.out.println("- TECH: tech@test.com / password123");
        }
    }
} 