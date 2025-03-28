package com.eventix.eventix.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventix.eventix.domain.Funcao;
import com.eventix.eventix.dtos.FuncaoDTO;
import com.eventix.eventix.services.FuncaoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/funcao")
public class FuncaoController {
  
  @Autowired
  private FuncaoService funcaoService;

  @PostMapping("/criar")
  public ResponseEntity<Funcao> postMethodName(@RequestBody FuncaoDTO funcao) {
      Funcao novaFuncao = funcaoService.salvar(funcao);
      return ResponseEntity.status(HttpStatus.CREATED).body(novaFuncao);
  }

  @GetMapping("/listar")
  public ResponseEntity<List<Funcao>> listar() {
    List<Funcao> funcoes = funcaoService.listar();
    return ResponseEntity.ok(funcoes);
  }

  @GetMapping("/buscar/{id}")
  public ResponseEntity<Funcao> buscar(@PathVariable Long id) {
    Funcao funcao = funcaoService.buscarPorId(id);
    return ResponseEntity.ok(funcao);
  }
  
}
