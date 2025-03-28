package com.eventix.eventix.dtos;

import java.time.LocalDate;
import java.util.Set;

public record UsuarioEditarDTO (String email, String nome, String senha, LocalDate dataNascimento, Set<Long> funcoesIds) {

}
