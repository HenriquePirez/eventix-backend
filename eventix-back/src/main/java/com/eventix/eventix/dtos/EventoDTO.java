package com.eventix.eventix.dtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public record EventoDTO(
  String nome, 
  String local,

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
  LocalDate data, 

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
  LocalTime horario,
   
  List<UsuarioEventoDTO> participantes ) {}
