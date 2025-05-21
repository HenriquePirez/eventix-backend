package com.eventix.eventix.infra.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.eventix.eventix.domain.Usuario;
import com.eventix.eventix.domain.enums.UserRole;
import com.eventix.eventix.repository.UsuarioRepository;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@eventix.com";
        
        if (!usuarioRepository.existsByEmail(adminEmail)) {
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail(adminEmail);
            admin.setSenha(passwordEncoder.encode("gpires")); 
            admin.setRole(UserRole.ADMIN); // Certifique-se que seu enum Role tem ADMIN
            
            usuarioRepository.save(admin);
            System.out.println("Admin criado com sucesso!");
        } else {
            System.out.println("Admin já existe no banco de dados");
        }
    }
}
