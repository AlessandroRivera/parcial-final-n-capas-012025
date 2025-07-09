package com.uca.parcialfinalncapas.service.impl;

import com.uca.parcialfinalncapas.dto.request.LoginRequest;
import com.uca.parcialfinalncapas.dto.response.LoginResponse;
import com.uca.parcialfinalncapas.entities.User;
import com.uca.parcialfinalncapas.exceptions.AuthenticationException;
import com.uca.parcialfinalncapas.exceptions.UserNotFoundException;
import com.uca.parcialfinalncapas.repository.UserRepository;
import com.uca.parcialfinalncapas.service.AuthService;
import com.uca.parcialfinalncapas.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // Buscar usuario por correo
        User user = userRepository.findByCorreo(loginRequest.getCorreo())
                .orElseThrow(() -> new AuthenticationException("Credenciales invalidas"));

        // Validar password (en una implementacion real deberia estar hasheada)
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new AuthenticationException("Credenciales invalidas");
        }

        // Generar token JWT
        String token = jwtUtil.generateToken(user.getCorreo(), user.getNombreRol());

        // Retornar respuesta
        return LoginResponse.builder()
                .token(token)
                .tipo("Bearer")
                .correo(user.getCorreo())
                .nombreRol(user.getNombreRol())
                .build();
    }
} 