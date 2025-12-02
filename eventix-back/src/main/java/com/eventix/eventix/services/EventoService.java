package com.eventix.eventix.services;

import java.util.*;
import java.util.stream.Collectors;

import com.eventix.eventix.dtos.UsuarioFuncaoDTO;
import com.eventix.eventix.dtos.evento.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.eventix.eventix.domain.Evento;
import com.eventix.eventix.domain.Funcao;
import com.eventix.eventix.domain.Usuario;
import com.eventix.eventix.domain.UsuarioEvento;
import com.eventix.eventix.dtos.FuncaoDTO;
import com.eventix.eventix.repository.EventoRepository;
import com.eventix.eventix.repository.FuncaoRepository;
import com.eventix.eventix.repository.UsuarioEventoRepository;
import com.eventix.eventix.repository.UsuarioRepository;

@Service
public class EventoService {

  @Autowired
  private EventoRepository eventoRepository;

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private FuncaoRepository funcaoRepository;

  @Autowired
  private UsuarioEventoRepository usuarioEventoRepository;

  public Evento salvar(EventoDTO eventoDTO) {

    // Cria o evento básico
    Evento evento = new Evento();
    evento.setNomeEvento(eventoDTO.nome());
    evento.setLocal(eventoDTO.local());
    evento.setData(eventoDTO.data());
    evento.setHorario(eventoDTO.horario());

    // evento = eventoRepository.save(evento);

    for (UsuarioEventoDTO usuarioEvento : eventoDTO.participantes()) {

      Usuario usuario = usuarioRepository.findById(usuarioEvento.usuarioId())
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado"));

      Funcao funcao = funcaoRepository.findById(usuarioEvento.funcaoId())
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado"));

      evento.adicionarParticipante(usuario, funcao);
    }

    return eventoRepository.save(evento);
  }

  @Transactional
  public Evento editar(Long id, EventoEditarDTO eventoAtualizado) {

    Evento evento = eventoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado"));

    // Atualiza dados básicos
    evento.setNomeEvento(eventoAtualizado.nomeEvento());
    evento.setLocal(eventoAtualizado.local());
    evento.setData(eventoAtualizado.data());
    evento.setHorario(eventoAtualizado.horario());

    Set<UsuarioEvento> novosParticipantes = new HashSet<>();

    for (UsuarioFuncaoDTO dto : eventoAtualizado.participantes()) {

      Usuario usuario = usuarioRepository.findById(dto.usuarioId())
              .orElseThrow(() -> new ResponseStatusException(
                      HttpStatus.NOT_FOUND, "Usuário não encontrado: " + dto.usuarioId()));

      Funcao funcao = funcaoRepository.findById(dto.funcaoId())
              .orElseThrow(() -> new ResponseStatusException(
                      HttpStatus.NOT_FOUND, "Função não encontrada: " + dto.funcaoId()));

      UsuarioEvento ue = new UsuarioEvento();
      ue.setEvento(evento);
      ue.setUsuario(usuario);
      ue.setFuncao(funcao);
      ue.setConfirmado(false);

      novosParticipantes.add(ue);
    }

    // Substitui os antigos pelos novos
    evento.getParticipantes().clear();
    evento.getParticipantes().addAll(novosParticipantes);

    return eventoRepository.save(evento);
  }


  public void deletar(Long id) throws Exception {

    Optional<Evento> evento = eventoRepository.findById(id);

    if (evento.isPresent()) {
      eventoRepository.delete(evento.get());

    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado!");
    }
  }

  public List<EventoListarDTO> listar() {

    // Consulta todos os eventos
    List<Evento> eventos = eventoRepository.findAll();

    // Cria lista de response
    List<EventoListarDTO> response = new ArrayList<>();

    // Percorre todos os eventos
    for (Evento evento : eventos) {

      // Instancia uma lista de usuarios para cada evento da lista de response
      List<UsuarioEventoListarDTO> listaAux = new ArrayList<>();

      // Instancia cada usuario do evento e adiciona na lista
      for (UsuarioEvento usuarioEvento : evento.getParticipantes()) {
        UsuarioEventoListarDTO aux = new UsuarioEventoListarDTO(usuarioEvento.getUsuario().getNome(),
            usuarioEvento.getFuncao().getNomeFuncao());
        listaAux.add(aux);
      }
      // Instancia o objeto da lista de response
      EventoListarDTO objetoAux = new EventoListarDTO(evento.getId(), evento.getNomeEvento(), evento.getLocal(), evento.getData(),
          evento.getHorario(), listaAux);

      // Adiciona o objeto na lista de response
      response.add(objetoAux);
    }

    return response;
  }

  public Evento buscarPorId(Long id) {
    return eventoRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado"));
  }

  public List<EventoFuncaoDTO> buscarEventosParaUsuario(Long usuarioId) {

    List<Evento> eventosDoUsuario = eventoRepository.findEventosByUsuarioId(usuarioId);

    return eventosDoUsuario.stream()
        .map(evento -> converterParaDTO(evento, usuarioId))
        .collect(Collectors.toList());
  }

  public boolean confirmarPresenca(Long id_evento, Long id_user) throws Exception {

    Optional<Evento> evento = eventoRepository.findById(id_evento);

    if (evento.isPresent()) {
      Optional<Usuario> user = usuarioRepository.findById(id_user);
      if (user.isPresent()) {
        for (UsuarioEvento usuarioEvento : evento.get().getParticipantes()) {
          if (usuarioEvento.getUsuario().equals(user.get())) {
            if (!usuarioEvento.isConfirmado()) {
              usuarioEvento.setConfirmado(true);
              usuarioEventoRepository.save(usuarioEvento);
            } else {
              throw new Exception("Usuario ja esta confirmado nesse evento");
            }
          }
        }
      } else {
        throw new Exception("Usuario nao encontrado");
      }

    } else {
      throw new Exception("Evento nao encontrado");
    }
    return true;
  }

  // ao recusar a presença, retira o evento da lista de eventos do usuário
  public boolean recusarPresenca(Long id_evento, Long id_user) throws Exception {

    Optional<Evento> evento = eventoRepository.findById(id_evento);

    if (evento.isPresent()) {
      Optional<Usuario> user = usuarioRepository.findById(id_user);
      if (user.isPresent()) {
        UsuarioEvento usuarioEventoToRemove = null;
        for (UsuarioEvento usuarioEvento : evento.get().getParticipantes()) {
          if (usuarioEvento.getUsuario().equals(user.get())) {
            usuarioEventoToRemove = usuarioEvento;
            break;
          }
        }
        if (usuarioEventoToRemove != null) {
          evento.get().getParticipantes().remove(usuarioEventoToRemove);
          usuarioEventoRepository.delete(usuarioEventoToRemove);
        } else {
          throw new Exception("Usuario nao esta participando desse evento");
        }
      } else {
        throw new Exception("Usuario nao encontrado");
      }

    } else {
      throw new Exception("Evento nao encontrado");
    }
    return true;

  }

  private EventoFuncaoDTO converterParaDTO(Evento evento, Long usuarioId) {

    EventoFuncaoDTO dto = new EventoFuncaoDTO();

    // Mapeia os dados básicos do evento para o DTO
    dto.setId(evento.getId());
    dto.setNomeEvento(evento.getNomeEvento());
    dto.setLocal(evento.getLocal());
    dto.setData(evento.getData());
    dto.setHorario(evento.getHorario());

    evento.getParticipantes().stream()
        // Filtra a lista de participantes para achar o que corresponde ao usuarioId
        .filter(participante -> participante.getUsuario().getId().equals(usuarioId))
        // Pega o primeiro que encontrar (deve haver apenas um)
        .findFirst()
        // Se encontrou, extrai a função e a define no DTO
        .ifPresent(participante -> {
          Funcao funcao = participante.getFuncao();
          if (funcao != null) {
            dto.setFuncaoDoUsuario(new FuncaoDTO(funcao.getId(), funcao.getNomeFuncao()));
          }
        });

    List<UsuarioNomeDTO> listaDeNomes = evento.getParticipantes().stream()
        .map(participante -> {
          Usuario usuario = participante.getUsuario();
          return new UsuarioNomeDTO(usuario.getId(), usuario.getNome());
        })
        .collect(Collectors.toList());
    dto.setParticipantes(listaDeNomes);

    return dto;
  }

  public List<EventoFuncaoDTO> listarEventosNaoConfirmadosPorUsuario(Long usuarioId) {
    System.out.println("Buscando eventos não confirmados para o usuário com ID: " + usuarioId);
    List<UsuarioEvento> participacoesNaoConfirmadas = usuarioEventoRepository
        .findByUsuarioIdAndConfirmadoFalse(usuarioId);

    // 2. Extrai e retorna apenas os eventos a partir das associações
    return participacoesNaoConfirmadas.stream()
            .map(ue -> converterParaEventoFuncaoDTO(ue.getEvento(), usuarioId))
            .collect(Collectors.toList());
  }

  public List<EventoFuncaoDTO> listarEventosConfirmadosPorUsuario(Long usuarioId) {
    List<UsuarioEvento> participacoesConfirmadas = usuarioEventoRepository.findByUsuarioIdAndConfirmadoTrue(usuarioId);

    //retornar lista de EventoFuncaoDTO
    return participacoesConfirmadas.stream()
        .map(ue -> converterParaEventoFuncaoDTO(ue.getEvento(), usuarioId))
        .collect(Collectors.toList());
  }

  // converter evento para eventoFuncaoDTO
  private EventoFuncaoDTO converterParaEventoFuncaoDTO(Evento evento, Long usuarioId) {
    EventoFuncaoDTO dto = new EventoFuncaoDTO();
    dto.setId(evento.getId());
    dto.setNomeEvento(evento.getNomeEvento());
    dto.setLocal(evento.getLocal());
    dto.setData(evento.getData());
    dto.setHorario(evento.getHorario());

    for (UsuarioEvento ue : evento.getParticipantes()) {
      if (ue.getUsuario().getId().equals(usuarioId)) {
        Funcao funcao = ue.getFuncao();
        dto.setFuncaoDoUsuario(new FuncaoDTO(funcao.getId(), funcao.getNomeFuncao()));
        break;
      }
    }

    List<UsuarioNomeDTO> listaDeNomes = evento.getParticipantes().stream()
        .map(participante -> {
          Usuario usuario = participante.getUsuario();
          return new UsuarioNomeDTO(usuario.getId(), usuario.getNome());
        })
        .collect(Collectors.toList());
    dto.setParticipantes(listaDeNomes);

    return dto;
  }
}
