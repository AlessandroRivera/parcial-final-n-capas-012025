package com.uca.parcialfinalncapas.dto.response;

import lombok.Builder;
import lombok.Data;

// DTO para respuesta de login exitoso con token JWT firmado
@Data
@Builder
public class LoginResponse {
    private String token;    // Token JWT firmado listo para usar en Authorization header
    private String tipo;     // Tipo de token (Bearer) para header Authorization
    private String correo;   // Correo del usuario autenticado
    private String nombreRol; // Rol del usuario (USER o TECH)
} 