package com.eventix.eventix.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Evento {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nomeEvento;

  @Column(nullable = false)
  private String local;

  @Column(nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
  private LocalDate data;

  @Column(nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
  private LocalTime horario;

  @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<UsuarioEvento> participantes = new HashSet<>();

  // Métodos para gerenciar participantes
  public void adicionarParticipante(Usuario usuario, Funcao funcao) {
    UsuarioEvento usuarioEvento = new UsuarioEvento(this, usuario, funcao, false);
    participantes.add(usuarioEvento);
    usuario.getEventos().add(usuarioEvento);
  }
}
