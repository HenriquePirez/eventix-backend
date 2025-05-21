package com.eventix.eventix.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eventix.eventix.domain.Evento;
import com.eventix.eventix.dtos.ConfirmarDTO;
import com.eventix.eventix.dtos.evento.EventoDTO;
import com.eventix.eventix.dtos.evento.EventoListarDTO;
import com.eventix.eventix.services.EventoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/evento")
public class EventoController {
  
  @Autowired
  private EventoService eventoService;

  @PostMapping("/admin/criar")
  @PreAuthorize("hasAnyRole('ADMIN')")
  @Operation(description = "Dado os dados, cria um evento.", responses = {
    @ApiResponse(responseCode = "200", description = "Caso o evento seja criado com sucesso."),
    @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
    @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")
    })
  public ResponseEntity<Evento> salvar(@RequestBody EventoDTO evento) {
    Evento novoEvento = eventoService.salvar(evento);
    return ResponseEntity.status(HttpStatus.CREATED).body(novoEvento);    
  }
  
  @DeleteMapping("/admin/deletar/{id}")
  @PreAuthorize("hasAnyRole('ADMIN')")
  @Operation(description = "Dado o id, o evento é deletado.", responses = {
    @ApiResponse(responseCode = "200", description = "Caso o evento seja deletado com sucesso."),
    @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
    @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")
    })
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletar (@PathVariable Long id) throws Exception{
    eventoService.deletar(id);
  }

  @GetMapping("/admin/listar")
  @PreAuthorize("hasAnyRole('ADMIN')")
  @Operation(description = "Lista todos os eventos.", responses = {
    @ApiResponse(responseCode = "200", description = "Caso os eventos sejam listados com sucesso."),
    @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
    @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")
    })
  public ResponseEntity<List<EventoListarDTO>> listar () {
    List<EventoListarDTO> eventos = eventoService.listar();
    return ResponseEntity.ok(eventos);
  }

  @GetMapping("/admin/buscar/{id}")
  @PreAuthorize("hasAnyRole('ADMIN')")
  @Operation(description = "Dado o id, busca o evento.", responses = {
    @ApiResponse(responseCode = "200", description = "Caso o evento seja encontardo com sucesso."),
    @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
    @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")
    })
  public ResponseEntity<Evento> buscar(@PathVariable Long id) {
    Evento evento = eventoService.buscarPorId(id);
    return ResponseEntity.ok(evento);
  }
  
  @GetMapping("/admin/buscarPorUsuario/{id}")
  @PreAuthorize("hasAnyRole('ADMIN')")
  @Operation(description = "Dado o id, busca o evento.", responses = {
    @ApiResponse(responseCode = "200", description = "Caso o evento seja encontardo com sucesso."),
    @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
    @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")
    })
  public ResponseEntity<List<Evento>> buscarPorUsuario(@PathVariable Long id) {
    List<Evento> evento = eventoService.listarEventosPorUsuario(id);
    return ResponseEntity.ok(evento);
  }

  @PostMapping("/confirmarPresenca")
  public boolean confirmarPresenca(@RequestBody ConfirmarDTO dto) throws Exception {
      return eventoService.confirmarPresenca(dto.evento_id(), dto.user_id());
  }
  
}  
