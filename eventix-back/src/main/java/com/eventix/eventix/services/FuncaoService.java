package com.eventix.eventix.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.eventix.eventix.domain.Usuario;
import com.eventix.eventix.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eventix.eventix.domain.Funcao;
import com.eventix.eventix.dtos.FuncaoDTO;
import com.eventix.eventix.dtos.FuncaoListarDTO;
import com.eventix.eventix.repository.FuncaoRepository;

@Service
public class FuncaoService {
  @Autowired
  private FuncaoRepository funcaoRepository;

  @Autowired
  private UsuarioRepository usuarioRepository;

  public Funcao salvar(FuncaoDTO funcao) {
    Funcao novo = new Funcao();
    novo.setNomeFuncao(funcao.nomeFuncao());
    return funcaoRepository.save(novo);
  }

  public Funcao editar(Long id, Funcao funcaoAtualizada) throws java.lang.Exception {

    Optional<Funcao> funcaoExistente = funcaoRepository.findById(id);

    if (funcaoExistente.isPresent()) {

      Funcao funcao = funcaoExistente.get();
      funcao.setNomeFuncao(funcaoAtualizada.getNomeFuncao());
      return funcaoRepository.save(funcao);

    } else {
      throw new Exception("Funcao não encontrada");
    }
  }

  public void deletar(Long id) throws Exception {

    Optional<Funcao> funcao = funcaoRepository.findById(id);

    if (funcao.isPresent()) {
      funcaoRepository.delete(funcao.get());

    } else {
      throw new Exception("Função não encontrada!");
    }
  }

  public List<FuncaoListarDTO> listar() {
    return funcaoRepository.findAll()
        .stream()
        .map(funcao -> new FuncaoListarDTO(funcao))
        .collect(Collectors.toList());
  }

  public Funcao buscarPorId(Long id) throws Exception {
    return funcaoRepository.findById(id).orElseThrow(() -> new Exception("Função não encontrada"));
  }

  //buscar funções do usuario, dado o id do usuario
  public List<Funcao> listarFuncoesDoUsuario(Long usuarioId) {
    Usuario user = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    return new ArrayList<>(user.getFuncoes());
  }

}
