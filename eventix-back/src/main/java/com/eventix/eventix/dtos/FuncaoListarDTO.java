package com.eventix.eventix.dtos;

import com.eventix.eventix.domain.Funcao;

public class FuncaoListarDTO {

  private Long id;
  private String nome;

  public FuncaoListarDTO(Long id, String nome) {
    this.id = id;
    this.nome = nome;
  }

  public FuncaoListarDTO(Funcao funcao) {
    this(funcao.getId(), funcao.getNomeFuncao());
  }

  public Long getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }
}
