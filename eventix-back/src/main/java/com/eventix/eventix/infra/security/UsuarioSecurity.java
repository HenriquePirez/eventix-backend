package com.eventix.eventix.infra.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.eventix.eventix.domain.Usuario;

@Component("usuarioSecurity")
public class UsuarioSecurity {

    public boolean isOwner(Long userId, Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return usuario.getId().equals(userId);
    }
}

