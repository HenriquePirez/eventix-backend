package com.eventix.eventix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventix.eventix.domain.Evento;
import java.util.Optional;
import java.util.Set;
import com.eventix.eventix.domain.UsuarioEvento;


public interface EventoRepository extends JpaRepository<Evento, Long>{
  Optional<Evento> findByParticipantes(Set<UsuarioEvento> participantes);
}
