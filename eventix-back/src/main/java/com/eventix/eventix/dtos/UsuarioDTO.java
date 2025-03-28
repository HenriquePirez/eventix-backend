package com.eventix.eventix.dtos;

import java.time.LocalDate;
import java.util.Set;

import com.eventix.eventix.domain.enums.sexoEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {

  private String email;
    private String senha;
    private String nome;
    private LocalDate dataNascimento;
    private sexoEnum sexo;

    private Set<Long> funcoesIds;
}
