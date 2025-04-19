package com.eventix.eventix.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventix.eventix.domain.Funcao;
import com.eventix.eventix.dtos.FuncaoDTO;
import com.eventix.eventix.services.FuncaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/funcao")
public class FuncaoController {
  
  @Autowired
  private FuncaoService funcaoService;

  @PostMapping("/criar")
  @Operation(description = "Dado os dados, cadastra uma função.", responses = {
            @ApiResponse(responseCode = "200", description = "Caso a função seja inserida com sucesso."),
            @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
            @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")
    })
  public ResponseEntity<Funcao> postMethodName(@RequestBody FuncaoDTO funcao) {
      Funcao novaFuncao = funcaoService.salvar(funcao);
      return ResponseEntity.status(HttpStatus.CREATED).body(novaFuncao);
  }

  @DeleteMapping("/deletar/{id}")
  @Operation(description = "Dado o id, a função é deletada.", responses = {
    @ApiResponse(responseCode = "200", description = "Caso a função seja deletada com sucesso."),
    @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
    @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")
    })
  public void deletar(@PathVariable Long id) throws Exception{
    funcaoService.deletar(id);
  }

  @GetMapping("/listar")
  @Operation(description = "Lista todas as funções.", responses = {
    @ApiResponse(responseCode = "200", description = "Caso as funções sejam listadas com sucesso."),
    @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
    @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")
    })
  public ResponseEntity<List<Funcao>> listar() {
    List<Funcao> funcoes = funcaoService.listar();
    return ResponseEntity.ok(funcoes);
  }

  @GetMapping("/buscar/{id}")
  @Operation(description = "Dado o ID, busca uma função.", responses = {
    @ApiResponse(responseCode = "200", description = "Caso a função seja encontrada com sucesso."),
    @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
    @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")
    })
  public ResponseEntity<Funcao> buscar(@PathVariable Long id) {
    Funcao funcao = funcaoService.buscarPorId(id);
    return ResponseEntity.ok(funcao);
  }
}
