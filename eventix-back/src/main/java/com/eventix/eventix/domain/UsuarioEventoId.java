package com.eventix.eventix.domain;

import java.io.Serializable;
import java.util.Objects;

public class UsuarioEventoId implements Serializable {
  private Long evento;
  private Long usuario;
  
  // Construtor, equals e hashCode
  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      UsuarioEventoId that = (UsuarioEventoId) o;
      return Objects.equals(evento, that.evento) &&
             Objects.equals(usuario, that.usuario);
  }
  
  @Override
  public int hashCode() {
      return Objects.hash(evento, usuario);
  }
}
