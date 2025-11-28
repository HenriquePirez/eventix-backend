package com.eventix.eventix.dtos.evento;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.eventix.eventix.dtos.FuncaoDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventoFuncaoDTO {
    private Long id;
    private String nomeEvento;
    private String local;
    private LocalDate data;
    private LocalTime horario;
    private FuncaoDTO funcaoDoUsuario;
    private List<UsuarioNomeDTO> participantes; 
}

