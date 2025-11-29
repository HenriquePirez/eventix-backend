package com.eventix.eventix.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eventix.eventix.domain.Evento;
import com.eventix.eventix.domain.Funcao;
import com.eventix.eventix.domain.Usuario;
import com.eventix.eventix.domain.UsuarioEvento;
import com.eventix.eventix.dtos.FuncaoDTO;
import com.eventix.eventix.dtos.evento.EventoDTO;
import com.eventix.eventix.dtos.evento.EventoFuncaoDTO;
import com.eventix.eventix.dtos.evento.EventoListarDTO;
import com.eventix.eventix.dtos.evento.UsuarioEventoDTO;
import com.eventix.eventix.dtos.evento.UsuarioEventoListarDTO;
import com.eventix.eventix.dtos.evento.UsuarioNomeDTO;
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

  public Evento editar(Long id, Evento eventoAtualizado) {

    Optional<Evento> eventoExistente = eventoRepository.findById(id);

    if (eventoExistente.isPresent()) {

      Evento evento = eventoExistente.get();
      evento.setNomeEvento(eventoAtualizado.getNomeEvento());
      evento.setLocal(eventoAtualizado.getLocal());
      evento.setData(eventoAtualizado.getData());
      evento.setHorario(eventoAtualizado.getHorario());
      // Alteração da senha será feito ainda
      return eventoRepository.save(evento);

    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado");
    }
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
      EventoListarDTO objetoAux = new EventoListarDTO(evento.getNomeEvento(), evento.getLocal(), evento.getData(),
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

  public List<Evento> listarEventosNaoConfirmadosPorUsuario(Long usuarioId) {
    List<UsuarioEvento> participacoesNaoConfirmadas = usuarioEventoRepository
        .findByUsuarioIdAndConfirmadoFalse(usuarioId);

    // 2. Extrai e retorna apenas os eventos a partir das associações
    return participacoesNaoConfirmadas.stream()
        .map(UsuarioEvento::getEvento) // Para cada UsuarioEvento, pega o Evento associado
        .collect(Collectors.toList());
  }

  public List<Evento> listarEventosConfirmadosPorUsuario(Long usuarioId) {
    List<UsuarioEvento> participacoesConfirmadas = usuarioEventoRepository.findByUsuarioIdAndConfirmadoTrue(usuarioId);

    return participacoesConfirmadas.stream()
        .map(UsuarioEvento::getEvento)
        .collect(Collectors.toList());
  }
}
