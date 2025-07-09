package com.uca.parcialfinalncapas.controller;

import com.uca.parcialfinalncapas.dto.request.LoginRequest;
import com.uca.parcialfinalncapas.dto.response.GeneralResponse;
import com.uca.parcialfinalncapas.dto.response.LoginResponse;
import com.uca.parcialfinalncapas.service.AuthService;
import com.uca.parcialfinalncapas.utils.ResponseBuilderUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador para manejo de autenticacion
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint para login de usuarios
     * Recibe credenciales (correo y password) y retorna token JWT
     * 
     * @param loginRequest 
     * @return 
     */
    @PostMapping("/login")
    public ResponseEntity<GeneralResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        // Llamar al servicio de autenticacion para validar credenciales
        LoginResponse loginResponse = authService.login(loginRequest);
        
        // Retornar respuesta exitosa con el token JWT
        return ResponseBuilderUtil.buildResponse(
                "Usuario autenticado correctamente",
                HttpStatus.OK,
                loginResponse
        );
    }
} 