package com.uca.parcialfinalncapas.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateRequest {
    private Long id;
    private String nombre;
    private String password;
    private String nombreRol;
}
