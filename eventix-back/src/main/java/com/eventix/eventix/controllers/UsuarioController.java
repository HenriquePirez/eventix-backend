package com.eventix.eventix.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eventix.eventix.domain.Funcao;
import com.eventix.eventix.domain.Usuario;
import com.eventix.eventix.dtos.UsuarioDTO;
import com.eventix.eventix.dtos.UsuarioEditarDTO;
import com.eventix.eventix.services.FuncaoService;
import com.eventix.eventix.services.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


@RestController
@RequestMapping("/usuario")
public class UsuarioController {
  
  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private FuncaoService funcaoService;

  @PostMapping("/criar")
  @Operation(description = "Dado os dados, cadastra um usuário.", responses = {
            @ApiResponse(responseCode = "200", description = "Caso o usuário seja inserido com sucesso."),
            @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
            @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")
    })
  public ResponseEntity<Usuario> salvar (@RequestBody UsuarioDTO dto) {
    Usuario novoUsuario = new Usuario();
    novoUsuario.setEmail(dto.getEmail());
    novoUsuario.setSenha(dto.getSenha());
    novoUsuario.setNome(dto.getNome());
    novoUsuario.setDataNascimento(dto.getDataNascimento());
    novoUsuario.setSexo(dto.getSexo());
    
    // Associar funções ao usuário
    if (dto.getFuncoesIds() != null) {
        Set<Funcao> funcoes = new HashSet<>();
        for (Long funcaoId : dto.getFuncoesIds()) {
            Funcao funcao = funcaoService.buscarPorId(funcaoId);
            funcoes.add(funcao);
        }
        novoUsuario.setFuncoes(funcoes);
    }
    usuarioService.salvar(novoUsuario);
    return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
  }

  @PutMapping("/editar/{id}")
  @Operation(description = "Dado o id, o usuário é editado.", responses = {
            @ApiResponse(responseCode = "200", description = "Caso o usuário seja editado com sucesso."),
            @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
            @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")
    })
  public ResponseEntity<Usuario> editar(@PathVariable Long id, @RequestBody UsuarioEditarDTO usuarioAtualizado) {
      Usuario usuario = usuarioService.editar(id, usuarioAtualizado);     
      return ResponseEntity.ok(usuario);
  }  

  @DeleteMapping("/deletar/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(description = "Dado o id, o usuário é deletado.", responses = {
    @ApiResponse(responseCode = "200", description = "Caso o usuário seja deletado com sucesso."),
    @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
    @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")
    })
  public void deletar(@PathVariable Long id) throws Exception{
      usuarioService.deletar(id);
  }
  
  @GetMapping("/listar")
  @Operation(description = "Lista todos os usuários.", responses = {
    @ApiResponse(responseCode = "200", description = "Caso os usuários sejam editados com sucesso."),
    @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
    @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")
   })
  public ResponseEntity<List<Usuario>> listarTodosUsuarios() {
    List<Usuario> usuarios = usuarioService.listar();
    return ResponseEntity.ok(usuarios);
  }

  @GetMapping("/buscar/{id}")
  @Operation(description = "Dado o id, busca o usuário.", responses = {
    @ApiResponse(responseCode = "200", description = "Caso o usuário seja encontrado com sucesso."),
    @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
    @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")
   })
  public ResponseEntity<Usuario> buscar(@PathVariable Long id) {
    Usuario user = usuarioService.buscarPorId(id);
    return ResponseEntity.ok(user);
  }
  
}
