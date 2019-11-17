package com.pgpain.unilol.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.core.match.Match;
import com.merakianalytics.orianna.types.core.match.MatchHistory;
import com.merakianalytics.orianna.types.core.match.MatchHistory.Builder;
import com.merakianalytics.orianna.types.core.match.Participant;
import com.pgpain.unilol.dto.PosicaoDto;
import com.pgpain.unilol.enumeration.TipoEvento;
import com.pgpain.unilol.model.Campeao;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.model.PartidaDoUsuario;
import com.pgpain.unilol.model.Usuario;
import com.pgpain.unilol.repository.CampeaoRepository;
import com.pgpain.unilol.repository.PartidaDoUsuarioRepository;
import com.pgpain.unilol.service.MatchService;
import com.pgpain.unilol.service.PartidaDoUsuarioService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PartidaDoUsuarioServiceImpl implements PartidaDoUsuarioService {

	private static final Long PARTIDAS_TOTAIS = 50L;
	private CampeaoRepository campeaoRepository;
	private PartidaDoUsuarioRepository partidaDoUsuarioRepository;
	@Autowired
	private MatchService matchService;

	public PartidaDoUsuarioServiceImpl(CampeaoRepository campeaoRepository,
			PartidaDoUsuarioRepository partidaDoUsuarioRepository) {
		super();
		this.campeaoRepository = campeaoRepository;
		this.partidaDoUsuarioRepository = partidaDoUsuarioRepository;
	}

	@Override
	public PartidaDoUsuario createPartidaDoUsuario(Usuario usuario, Partida partida) {
		Match match = Orianna.matchWithId(partida.getIdExterno()).get();
		Participant participante = match.getParticipants()
				.find(parti -> parti.getSummoner().getAccountId().equals(usuario.getAccountId()));

		PartidaDoUsuario partidaDoUsuario = new PartidaDoUsuario();
		partidaDoUsuario.setPartida(partida);
		partidaDoUsuario.setUsuario(usuario);

		// campeao
		Optional<Campeao> campBanco = campeaoRepository.findFirstByCampeaoIdApi(participante.getChampion().getId());
		if (campBanco.isPresent())
			partidaDoUsuario.setCampeao(campBanco.get());

		partidaDoUsuario.setTime(Integer.toString(participante.getTeam().getSide().getId()));

		return partidaDoUsuario;
	}

	@Override
	public PartidaDoUsuario getPartidaDoUsuarioById(Long id) {
		return new PartidaDoUsuario();
	}

	@Override
	public List<PartidaDoUsuario> getPartidaDoUsuarioByUsuario(Usuario usuario) {
		return new ArrayList<>();
	}

	@Override
	public PartidaDoUsuario getByUsuarioAndPartida(Usuario usuario, Partida partida) {
		Optional<PartidaDoUsuario> partidaOp = partidaDoUsuarioRepository.findByUsuarioAndPartida(usuario, partida);
		if (partidaOp.isPresent()) {
			return partidaOp.get();
		}
		log.error("PartidaDoUsuario não encontrada !!");
		return new PartidaDoUsuario();
	}

	@Override
	public List<PosicaoDto> getPosicaoDoUsuario(PartidaDoUsuario partidaDoUsuario) {
		List<PosicaoDto> posicaoDTOs = new ArrayList<>();

		if (partidaDoUsuario.getPartida().getEventosDaPartida() != null) {
			partidaDoUsuario.getPartida().getEventosDaPartida().forEach(evento -> {
				PosicaoDto poDto = new PosicaoDto();
				if ((evento.getPosicaoX() != null && evento.getPosicaoY() != null)
						&& !(evento.getTipo().equals(TipoEvento.ITEM_DESTROYED.getTipo())
								|| evento.getTipo().equals(TipoEvento.ITEM_PURCHASED.getTipo()))) {

					poDto.setX(evento.getPosicaoX());
					poDto.setY(evento.getPosicaoY());
					posicaoDTOs.add(poDto);

				}
			});
		}

		if (partidaDoUsuario.getTimeline() != null) {
			partidaDoUsuario.getTimeline().forEach(timeline -> {
				PosicaoDto poDto = new PosicaoDto();
				if (timeline.getPosicaoX() != null && timeline.getPosicaoY() != null) {
					poDto.setX(timeline.getPosicaoX());
					poDto.setY(timeline.getPosicaoY());
					posicaoDTOs.add(poDto);
				}
			});
		}

		return posicaoDTOs;
	}

	@Override
	public void removerPartidasDoUsuario(Long removeTotalPartida, Usuario usuario) {
		// pegar ordenado pelas menores partidas, as mais antigas
		List<PartidaDoUsuario> partidasAntigas = partidaDoUsuarioRepository
				.findAllByUsuarioByOrderByIdPartidaDoUsuarioAsc(usuario);

		for (int i = 0; i < removeTotalPartida; i++) {
			PartidaDoUsuario partidaDoUsuarioAntigas = partidasAntigas.get(i);
			partidaDoUsuarioRepository.delete(partidaDoUsuarioAntigas);
		}

	}

	@Override
	public void adicionarPartidasDoUsuario(Long adicionaTotalPartida, Usuario usuario) {
		// chama método de adicionarPartidaDoUsuario
		matchService.getMatchHistoryFromAPI(usuario, 0L, adicionaTotalPartida);
	}

	public boolean atualizarPartidasDosUsuarios(Usuario usuario) {

		// quantas partidas no banco
		Long nPartidaBanco = partidaDoUsuarioRepository.countByUsuario(usuario);

		// data da partida mais recente do usuario
		PartidaDoUsuario ultimaPartida = partidaDoUsuarioRepository
				.findTopByUsuarioOrderByIdPartidaDoUsuarioDesc(usuario);
		// .findTopByUsuarioByOrderByIdPartidaDesc(usuario); // com o maior id de
		// partida = mais atual
		String dataUltimaPartida = ultimaPartida.getPartida().getDataCriacao();

		// quantas partidas novas eu tenho na api a partir da mais recente
		Builder matchFilter = MatchHistory.forSummoner(Orianna.summonerNamed(usuario.getNome()).get())
				.withStartTime(new DateTime(dataUltimaPartida)).withEndTime(DateTime.now());

		MatchHistory matchHistory = matchFilter.get();

		Long nPartidaApi = matchHistory.getEndIndex() + 1L; // número do ultimo index +1 pra pegar o tamanho

		if (nPartidaApi > 0) {
			if (nPartidaBanco >= PARTIDAS_TOTAIS && nPartidaApi <= PARTIDAS_TOTAIS) {
				removerPartidasDoUsuario(nPartidaApi, usuario);
				adicionarPartidasDoUsuario(nPartidaApi, usuario);
				return true;
			} else if (nPartidaBanco < PARTIDAS_TOTAIS) {
				if (nPartidaApi <= (PARTIDAS_TOTAIS - nPartidaBanco)) {
					adicionarPartidasDoUsuario(nPartidaApi, usuario); // quantas partidas eu posso adicionar
					return true;
				} else {
					adicionarPartidasDoUsuario((PARTIDAS_TOTAIS - nPartidaBanco), usuario);
					return true;
				}

			} else {
				removerPartidasDoUsuario(PARTIDAS_TOTAIS, usuario);
				adicionarPartidasDoUsuario(PARTIDAS_TOTAIS, usuario);
				return true;
			}
		}

		return false;

	}

	@Override
	public PartidaDoUsuario getPartidaDoUsuarioByPartidaAndParticipantId(Partida partida, Integer participantId) {
		PartidaDoUsuario partidaDoUsuario = null;

		Optional<PartidaDoUsuario> partidaDoUsuarioDoBanco = partidaDoUsuarioRepository
				.findByPartidaAndParticipantId(partida, participantId);

		if (partidaDoUsuarioDoBanco.isPresent())
			partidaDoUsuario = partidaDoUsuarioDoBanco.get();

		return partidaDoUsuario;
	}

	@Override
	public List<PartidaDoUsuario> getPartidaDoUsuarioByPartida(Partida partida) {
		List<PartidaDoUsuario> partidaOp = partidaDoUsuarioRepository.findAllByPartida(partida);
		if (!partidaOp.isEmpty()) {
			return partidaOp;
		}
		log.error("PartidaDoUsuario não encontrada !!");
		return new ArrayList<>();
	}

}
