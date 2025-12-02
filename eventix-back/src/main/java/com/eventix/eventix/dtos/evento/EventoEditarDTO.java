package com.eventix.eventix.dtos.evento;

import com.eventix.eventix.dtos.UsuarioFuncaoDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record EventoEditarDTO (
                               String nomeEvento,
                               String local,
                               LocalDate data,
                               LocalTime horario,
                               List<UsuarioFuncaoDTO> participantes) {
}
