package com.eventix.eventix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventix.eventix.domain.Funcao;

@Repository
public interface FuncaoRepository extends JpaRepository<Funcao, Long>{
  Funcao findByNomeFuncao(String nomeFuncao);
}
