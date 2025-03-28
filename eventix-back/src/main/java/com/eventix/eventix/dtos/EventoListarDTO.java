package com.eventix.eventix.dtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record EventoListarDTO (
  String nome, 
  String local,
  LocalDate data, 
  LocalTime horario,
  List<UsuarioEventoListarDTO> participantesFuncoes
){}
