package com.eventix.eventix.dtos;

import com.eventix.eventix.domain.enums.sexoEnum;

import java.time.LocalDate;
import java.util.Set;

public record UsuarioEditarDTO (sexoEnum sexo, String nome, LocalDate dataNascimento, Set<Long> funcoesIds) {

}
