package com.eventix.eventix.dtos;

import java.time.LocalDate;

import com.eventix.eventix.domain.enums.sexoEnum;

public record RegisterRequestDTO (String nome, String email, String senha, sexoEnum sexo, LocalDate dataNascimento) { }
