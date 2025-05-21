package com.eventix.eventix.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.eventix.eventix.domain.Usuario;
import com.eventix.eventix.dtos.UsuarioDTO;
import com.eventix.eventix.dtos.UsuarioEditarDTO;
import com.eventix.eventix.services.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
  
  @Autowired
  private UsuarioService usuarioService;

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("admin")
  @Operation(description = "Cadastro público de usuário", responses = {
    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
  })
  public ResponseEntity<Usuario> salvar(@RequestBody UsuarioDTO dto) throws Exception {
    Usuario novoUsuario = usuarioService.salvar(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') or @usuarioSecurity.isOwner(#id, authentication)")
  @Operation(description = "Edição de usuário", responses = {
    @ApiResponse(responseCode = "200", description = "Usuário editado"),
    @ApiResponse(responseCode = "403", description = "Acesso negado"),
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
  })
  public ResponseEntity<Usuario> editar(@PathVariable Long id, @RequestBody UsuarioEditarDTO usuarioAtualizado) {
    Usuario usuario = usuarioService.editar(id, usuarioAtualizado);     
    return ResponseEntity.ok(usuario);
  }  

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') or @usuarioSecurity.isOwner(#id, authentication)")
  @Operation(description = "Exclusão de usuário", responses = {
    @ApiResponse(responseCode = "204", description = "Usuário excluído"),
    @ApiResponse(responseCode = "403", description = "Acesso negado")
  })
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletar(@PathVariable Long id) throws Exception {
    usuarioService.deletar(id);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/admin")
  @Operation(description = "Listagem de todos os usuários (apenas ADMIN)")
  public ResponseEntity<List<Usuario>> listarTodosUsuarios() {
    List<Usuario> usuarios = usuarioService.listar();
    return ResponseEntity.ok(usuarios);
  }

  @GetMapping("/admin/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(description = "Busca de usuário por ID (apenas ADMIN)")
  public ResponseEntity<Usuario> buscar(@PathVariable Long id) {
    Usuario user = usuarioService.buscarPorId(id);
    return ResponseEntity.ok(user);
  }
}
