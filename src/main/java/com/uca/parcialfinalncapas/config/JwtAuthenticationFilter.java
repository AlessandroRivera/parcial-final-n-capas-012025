package com.uca.parcialfinalncapas.config;

import com.uca.parcialfinalncapas.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        // Obtener header Authorization de la peticion
        String authHeader = request.getHeader("Authorization");
        
        // Validar que el header contenga Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);
        
        try {
            String correo = jwtUtil.extractUsername(token);
            
            // Validar que el usuario no este ya autenticado en el contexto
            if (correo != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Validar que el token JWT sea valido
                if (jwtUtil.validateToken(token, correo)) {
                    
                    // Extraer rol del usuario del token
                    String rol = jwtUtil.extractRole(token);
                    
                    // Crear objeto de autenticacion con correo y rol
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                            correo,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol))
                        );
                    
                    // Establecer detalles de la peticion HTTP
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Establecer autenticacion en el contexto de seguridad de Spring
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            
        } catch (Exception e) {
            logger.error("Error validando token JWT: " + e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }
} 