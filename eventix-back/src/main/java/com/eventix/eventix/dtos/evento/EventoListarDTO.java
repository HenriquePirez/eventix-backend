package com.eventix.eventix.dtos.evento;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record EventoListarDTO (
  Long id,
  String nome, 
  String local,
  LocalDate data, 
  LocalTime horario,
  List<UsuarioEventoListarDTO> participantesFuncoes
){}
