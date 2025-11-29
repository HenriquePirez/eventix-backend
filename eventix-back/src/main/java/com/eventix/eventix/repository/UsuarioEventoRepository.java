package com.eventix.eventix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventix.eventix.domain.Usuario;
import com.eventix.eventix.domain.UsuarioEvento;
import com.eventix.eventix.domain.UsuarioEventoId;

import java.util.List;
import java.util.Optional;

public interface UsuarioEventoRepository extends JpaRepository<UsuarioEvento, UsuarioEventoId> {
  Optional<UsuarioEvento> findByUsuario(Usuario usuario);

  List<UsuarioEvento> findByUsuarioIdAndConfirmadoFalse(Long usuarioId);

  List<UsuarioEvento> findByUsuarioIdAndConfirmadoTrue(Long usuarioId);

}
