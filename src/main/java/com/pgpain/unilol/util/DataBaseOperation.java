package com.pgpain.unilol.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.core.match.Match;
import com.merakianalytics.orianna.types.core.match.Participant;
import com.pgpain.unilol.dto.MatchDto;
import com.pgpain.unilol.dto.MatchHistoryDto;
import com.pgpain.unilol.dto.ParticipanteDto;
import com.pgpain.unilol.enumeration.FilaDeProcessoEnum;
import com.pgpain.unilol.model.Campeao;
import com.pgpain.unilol.model.DetalhePartidaUsuario;
import com.pgpain.unilol.model.FilaDeProcesso;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.model.PartidaDoUsuario;
import com.pgpain.unilol.model.Timeline;
import com.pgpain.unilol.model.Usuario;
import com.pgpain.unilol.repository.CampeaoRepository;
import com.pgpain.unilol.repository.DetalhePartidaUsuarioRepository;
import com.pgpain.unilol.repository.FilaDeProcessoRepository;
import com.pgpain.unilol.repository.PartidaDoUsuarioRepository;
import com.pgpain.unilol.repository.PartidaRepository;
import com.pgpain.unilol.repository.TimelineRepository;
import com.pgpain.unilol.service.DetalhePartidaUsuarioService;
import com.pgpain.unilol.service.EventoDaPartidaService;
import com.pgpain.unilol.service.FilaDeProcessoService;
import com.pgpain.unilol.service.ItemDaPartidaDoUsuarioService;
import com.pgpain.unilol.service.PartidaService;
import com.pgpain.unilol.service.TimelineService;
import com.pgpain.unilol.service.UsuarioService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@EnableAsync
public class DataBaseOperation {

	private static final Integer TIME_ID = 100;
	private static final String DERROTA = "global.defeat";
	private static final String VITORIA = "global.victory";
	private static final int PARTICIPANTES = 10;

	@Autowired
	private PartidaRepository partidaRepository;
	@Autowired
	private PartidaDoUsuarioRepository partidaDoUsuarioRepository;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private DetalhePartidaUsuarioService detalhePartidaUsuarioService;
	@Autowired
	private DetalhePartidaUsuarioRepository detalhePartidaUsuarioRepository;
	@Autowired
	private ItemDaPartidaDoUsuarioService itemDaPartidaDoUsuarioService;
	@Autowired
	private TimelineService timelineService;
	@Autowired
	private TimelineRepository timelineRepository;
	@Autowired
	private EventoDaPartidaService eventoDaPartidaService;
	@Autowired
	private PartidaService partidaService;
	@Autowired
	private FilaDeProcessoService filaDeProcessoService;
	@Autowired
	private FilaDeProcessoRepository filaDeProcessoRepository;
	@Autowired
	CampeaoRepository campeaoRepository;

	@Async
	public void loadToDB(MatchHistoryDto matchHistoryDTO) {

		log.info("---------------INICIANDO PROCESSO DE SALVAR PARTIDAS NO BANCO----------------------");

		List<Partida> partidas = new ArrayList<>();
		List<PartidaDoUsuario> partidaDosUsuarios = new ArrayList<>();

		int tamanhoPartidas = matchHistoryDTO.getPartidas().size();

		for (int i = tamanhoPartidas - 1; i >= 0; i--) { // salvando ao contrário pra as mais recentes serem as de maior
															// id

			log.info("~~~~~~~~~~~~~~~~~~~~~~~~SALVANDO DADOS DA PARTIDA " + (i + 1)
					+ "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			MatchDto matchDTO = matchHistoryDTO.getPartidas().get(i);

			// se partida ja estiver salva vai para o próximo

			Optional<Partida> findByIdExterno = partidaRepository.findByIdExterno(matchDTO.getMatchId());
			if (findByIdExterno.isPresent()) {
				log.info("Partida existente no banco");
				continue;
			}

			if (matchDTO.getMatchId() != null) {

				// partidaSalva
				Partida partida = partidaService.savePartida(matchDTO);
				partidas.add(partida);

				partidaDosUsuarios.addAll(inserirPartidaDetalheItem(matchDTO, partida));

				log.info("~~~~~~~~~~~~~~~~~~~~~~ENCERRANDO DADOS DA PARTIDA " + (i + 1)
						+ "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			}
		}

		inserirTimeline(partidaDosUsuarios);

		partidas.forEach(partida -> {
			// salvar evento da partida ASYNC
			filaDeProcessoService.saveFilaDeProcesso(null, partida, FilaDeProcessoEnum.STATUS_PROCESSANDO.getValue(),
					FilaDeProcessoEnum.TIPO_EVENTO.getValue());
			eventoDaPartidaService.saveAll(partida);
		});
		log.info("ENCERRANDO PROCESSO DE SALVAR PARTIDAS NO BANCO");
	}

	private List<PartidaDoUsuario> inserirPartidaDetalheItem(MatchDto matchDTO, Partida partida) {

		List<PartidaDoUsuario> lDoUsuarios = new ArrayList<>();

		Match match = Orianna.matchWithId(matchDTO.getMatchId()).get();
		String regiao = match.getRegion().name();
		for (int j = 0; j < PARTICIPANTES; j++) {
			// salvar Usuario
			Participant participant = match.getParticipants().get(j);
			ParticipanteDto participanteDTO = matchDTO.getParticipants().get(j);
			participanteDTO.setRegiao(regiao);
			// salvando no banco e retornando os usuarios salvos
			Usuario usuarioParticipante = usuarioService.saveUsuario(participanteDTO);

			// Criar PartidaDoInvocador
			PartidaDoUsuario partidaDoUsuario = createPartidaDoUsuario(usuarioParticipante, partida);

			partidaDoUsuario.setParticipantId(j + 1);
			// salvar
			PartidaDoUsuario partidaDoUsuarioSalva = partidaDoUsuarioRepository.save(partidaDoUsuario);
			lDoUsuarios.add(partidaDoUsuarioSalva);

			// salvar detalhePartidaInvocador
			FilaDeProcesso processando1 = filaDeProcessoService.saveFilaDeProcesso(usuarioParticipante, partida,
					FilaDeProcessoEnum.STATUS_PROCESSANDO.getValue(), FilaDeProcessoEnum.TIPO_DETALHE.getValue());

			DetalhePartidaUsuario detalhePartidaUsuario = detalhePartidaUsuarioService.create(participant);

			if (participant.getTeam().getSide().getId() == TIME_ID && match.getBlueTeam().isWinner()) {
				detalhePartidaUsuario.setResultado(VITORIA);
			} else if (participant.getTeam().getSide().getId() == 200 && match.getRedTeam().isWinner()) {
				detalhePartidaUsuario.setResultado(VITORIA);
			} else {
				detalhePartidaUsuario.setResultado(DERROTA);
			}

			detalhePartidaUsuario.setPartidaDoUsuario(partidaDoUsuarioSalva);

			// salvar item da partida(feitico,item e runa)
			try {
				detalhePartidaUsuarioRepository.save(detalhePartidaUsuario);
				itemDaPartidaDoUsuarioService.saveAll(participant, partidaDoUsuarioSalva);

				filaDeProcessoService.saveFilaDeProcesso(usuarioParticipante, partida,
						FilaDeProcessoEnum.STATUS_SUCESSO.getValue(), FilaDeProcessoEnum.TIPO_DETALHE.getValue());
			} catch (Exception e) {
				filaDeProcessoService.saveFilaDeProcesso(usuarioParticipante, partida,
						FilaDeProcessoEnum.STATUS_FALHA.getValue(), FilaDeProcessoEnum.TIPO_DETALHE.getValue());
				log.info("falha ao inserir detalhes ou itens da partida do usuario"
						+ partidaDoUsuarioSalva.getUsuario().getIdUsuario());
			}

			processando1.setStatus(FilaDeProcessoEnum.STATUS_PROCESSADO.getValue());
			filaDeProcessoRepository.save(processando1);
		}

		return lDoUsuarios;
	}

	private void inserirTimeline(List<PartidaDoUsuario> partidaDoUsuarios) {

		partidaDoUsuarios.forEach(partidaDoUsuarioSalva -> {
			Partida partida = partidaDoUsuarioSalva.getPartida();
			Usuario usuario = partidaDoUsuarioSalva.getUsuario();

			Match match = Orianna.matchWithId(partida.getIdExterno()).get();

			FilaDeProcesso processando1 = filaDeProcessoService.saveFilaDeProcesso(usuario, partida,
					FilaDeProcessoEnum.STATUS_PROCESSANDO.getValue(), FilaDeProcessoEnum.TIPO_TIMELINE.getValue());

			try {
				List<Timeline> timelines = timelineService
						.create(Integer.toString(partidaDoUsuarioSalva.getParticipantId()), match);
				for (Timeline timeline : timelines) {
					timeline.setPartidaDoUsuario(partidaDoUsuarioSalva);
					timelineRepository.save(timeline);
				}
				filaDeProcessoService.saveFilaDeProcesso(usuario, partida, FilaDeProcessoEnum.STATUS_SUCESSO.getValue(),
						FilaDeProcessoEnum.TIPO_TIMELINE.getValue());
			} catch (Exception e) {
				filaDeProcessoService.saveFilaDeProcesso(usuario, partida, FilaDeProcessoEnum.STATUS_FALHA.getValue(),
						FilaDeProcessoEnum.TIPO_TIMELINE.getValue());
			}

			processando1.setStatus(FilaDeProcessoEnum.STATUS_PROCESSADO.getValue());
			filaDeProcessoRepository.save(processando1);

		});

	}

	public PartidaDoUsuario createPartidaDoUsuario(Usuario usuario, Partida partida) {
		Match match = Orianna.matchWithId(partida.getIdExterno()).get();
		Participant participante = match.getParticipants()
				.find(parti -> parti.getSummoner().getAccountId().equals(usuario.getAccountId()));

		PartidaDoUsuario partidaDoUsuario = new PartidaDoUsuario();
		partidaDoUsuario.setPartida(partida);
		partidaDoUsuario.setLaneType(participante.getLane().toString());
		partidaDoUsuario.setRole(participante.getRole().toString());
		partidaDoUsuario.setUsuario(usuario);

		// campeao
		Optional<Campeao> campBanco = campeaoRepository.findFirstByCampeaoIdApi(participante.getChampion().getId());
		if (campBanco.isPresent())
			partidaDoUsuario.setCampeao(campBanco.get());

		partidaDoUsuario.setTime(Integer.toString(participante.getTeam().getSide().getId()));

		return partidaDoUsuario;
	}

}
