package com.uca.parcialfinalncapas.service;

import com.uca.parcialfinalncapas.dto.request.LoginRequest;
import com.uca.parcialfinalncapas.dto.response.LoginResponse;

public interface AuthService {
    /**
     * Autentica un usuario y retorna un token JWT
     *
     * @param loginRequest datos de login (correo y password)
     * @return respuesta con token JWT
     */
    LoginResponse login(LoginRequest loginRequest);
} 