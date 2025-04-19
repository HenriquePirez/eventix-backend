package com.eventix.eventix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eventix.eventix.domain.Evento;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import com.eventix.eventix.domain.UsuarioEvento;


public interface EventoRepository extends JpaRepository<Evento, Long>{
  Optional<Evento> findByParticipantes(Set<UsuarioEvento> participantes);

  @Query("SELECT DISTINCT e FROM Evento e " +
           "JOIN e.participantes ue " +
           "WHERE ue.usuario.id = :usuarioId")
    List<Evento> findEventosByUsuarioId(@Param("usuarioId") Long usuarioId);
}
