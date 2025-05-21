package com.eventix.eventix.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventix.eventix.domain.Usuario;
import com.eventix.eventix.domain.enums.UserRole;
import com.eventix.eventix.dtos.LoginRequestDTO;
import com.eventix.eventix.dtos.LoginResponseDTO;
import com.eventix.eventix.dtos.RegisterRequestDTO;
import com.eventix.eventix.infra.security.TokenService;
import com.eventix.eventix.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  @Autowired
  private TokenService tokenService;

  @Autowired
  private UsuarioRepository usuarioRepository;

  private final PasswordEncoder passwordEncoder;

  @PostMapping("login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body) throws Exception{
        Usuario user = this.usuarioRepository.findByEmail(body.email()).orElseThrow(() -> new Exception("Usuario não encontrado"));
        if(passwordEncoder.matches(body.senha(), user.getSenha())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new LoginResponseDTO(user.getNome(), token));
        }
        return ResponseEntity.badRequest().build();
    }

  @PostMapping("register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body) throws Exception{
        Optional<Usuario> user = this.usuarioRepository.findByEmail(body.email());

        if(user.isEmpty()) {
            Usuario newUser = new Usuario();
            newUser.setRole(UserRole.USER);
            newUser.setSenha(passwordEncoder.encode(body.senha()));
            newUser.setEmail(body.email());
            newUser.setNome(body.nome());
            newUser.setSexo(body.sexo());
            newUser.setDataNascimento(body.dataNascimento());
            this.usuarioRepository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new LoginResponseDTO(newUser.getNome(), token));
        }
        return ResponseEntity.badRequest().build();
    }
}
