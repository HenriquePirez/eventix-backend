package com.eventix.eventix.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eventix.eventix.domain.Evento;
import com.eventix.eventix.dtos.EventoDTO;
import com.eventix.eventix.dtos.EventoListarDTO;
import com.eventix.eventix.services.EventoService;

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

  @PostMapping("/criar")
  public ResponseEntity<Evento> salvar(@RequestBody EventoDTO evento) {
    Evento novoEvento = eventoService.salvar(evento);
    return ResponseEntity.status(HttpStatus.CREATED).body(novoEvento);    
  }
  
  @DeleteMapping("/deletar/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletar (@PathVariable Long id) throws Exception{
    eventoService.deletar(id);
  }

  @GetMapping("/listar")
  public ResponseEntity<List<EventoListarDTO>> listar () {
    List<EventoListarDTO> eventos = eventoService.listar();
    return ResponseEntity.ok(eventos);
  }

  @GetMapping("buscar/{id}")
  public ResponseEntity<Evento> buscar(@PathVariable Long id) {
    Evento evento = eventoService.buscarPorId(id);
    return ResponseEntity.ok(evento);
  }
  
}
