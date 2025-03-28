package com.eventix.eventix.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eventix.eventix.domain.Funcao;
import com.eventix.eventix.domain.Usuario;
import com.eventix.eventix.domain.UsuarioEvento;
import com.eventix.eventix.dtos.UsuarioEditarDTO;
import com.eventix.eventix.repository.FuncaoRepository;
import com.eventix.eventix.repository.UsuarioEventoRepository;
import com.eventix.eventix.repository.UsuarioRepository;

@Service
public class UsuarioService {
  
  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private FuncaoRepository funcaoRepository;

  @Autowired
  private UsuarioEventoRepository usuarioEventoRepository;

  public Usuario salvar(Usuario user) {
    return usuarioRepository.save(user);
  }

  public Usuario editar(Long id, UsuarioEditarDTO usuarioAtualizado) {

    Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);

    if (usuarioExistente.isPresent()) {

      Usuario user = usuarioExistente.get();
      user.setDataNascimento(usuarioAtualizado.dataNascimento());
      user.setEmail(usuarioAtualizado.email());
      user.setNome(usuarioAtualizado.nome());

      // Atualiza funções
      if (usuarioAtualizado.funcoesIds() != null) {
        Set<Funcao> novasFuncoes = new HashSet<>();
        for (Long funcaoId : usuarioAtualizado.funcoesIds()) {
            Optional<Funcao> funcao = funcaoRepository.findById(funcaoId);
            if (funcao.isPresent()) {
              novasFuncoes.add(funcao.get());
            }
        }
        
        // Remove funções que não estão mais na lista
        user.getFuncoes().removeIf(funcao -> !novasFuncoes.contains(funcao));
        
        // Adiciona novas funções
        for (Funcao novaFuncao : novasFuncoes) {
            if (!user.getFuncoes().contains(novaFuncao)) {
              user.adicionarFuncao(novaFuncao);
            }
        }
    }

      // Alteração da senha será feito ainda
      return usuarioRepository.save(user);

    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
    }
  }

  public void deletar (Long id) throws Exception {

    Optional<Usuario> usuario = usuarioRepository.findById(id);
    
    if (usuario.isPresent()) {

      Optional<UsuarioEvento> usuarioPresente = usuarioEventoRepository.findByUsuario(usuario.get());

      if(usuarioPresente.isPresent()){
        throw new Exception("Usuario está relacionado a um ou mais evebtos e não pode ser excluído.");
        
      } else {
        usuarioRepository.delete(usuario.get());
      }
      
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!");
    }
  }

  public List<Usuario> listar () {
    return usuarioRepository.findAll();
  }

  public Usuario buscarPorId (Long id) {
    return usuarioRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
  }
}
