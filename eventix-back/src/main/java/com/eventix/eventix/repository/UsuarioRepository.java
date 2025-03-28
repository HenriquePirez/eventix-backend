package com.eventix.eventix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventix.eventix.domain.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
 
  // Versão correta da query
  @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END " +
        "FROM Usuario u JOIN u.funcoes f " +
        "WHERE u.id = :usuarioId AND f.id = :funcaoId")
  boolean possuiFuncao(@Param("usuarioId") Long usuarioId, @Param("funcaoId") Long funcaoId);
}
