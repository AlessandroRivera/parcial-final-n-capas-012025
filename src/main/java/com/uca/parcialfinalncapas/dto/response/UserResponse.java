package com.uca.parcialfinalncapas.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long idUsuario;
    private String nombre;
    private String correo;
    private String nombreRol;
}
