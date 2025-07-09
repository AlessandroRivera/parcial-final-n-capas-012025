package com.uca.parcialfinalncapas.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "El correo no puede estar vacio")
    private String correo;
    
    @NotBlank(message = "La contraseña no puede estar vacia")
    private String password;
} 