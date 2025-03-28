package com.eventix.eventix.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eventix.eventix.domain.Funcao;
import com.eventix.eventix.dtos.FuncaoDTO;
import com.eventix.eventix.repository.FuncaoRepository;

@Service
public class FuncaoService {
  @Autowired
  private FuncaoRepository funcaoRepository;

  public Funcao salvar(FuncaoDTO funcao) {
    Funcao novo = new Funcao();
    novo.setNomeFuncao(funcao.nomeFuncao());
    return funcaoRepository.save(novo);
  }

  public Funcao editar(Long id, Funcao funcaoAtualizada) {

    Optional<Funcao> funcaoExistente = funcaoRepository.findById(id);

    if (funcaoExistente.isPresent()) {

      Funcao funcao = funcaoExistente.get();
      funcao.setNomeFuncao(funcaoAtualizada.getNomeFuncao());
      return funcaoRepository.save(funcao);

    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcao não encontrada");
    }
  }

  public void deletar (Long id) throws Exception {

    Optional<Funcao> funcao = funcaoRepository.findById(id);
    
    if (funcao.isPresent()) {
      funcaoRepository.delete(funcao.get());
      
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Função não encontrada!");
    }
  }

  public List<Funcao> listar () {
    return funcaoRepository.findAll();
  }

  public Funcao buscarPorId (Long id) {
    return funcaoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Função não encontrada"));
  }
}
