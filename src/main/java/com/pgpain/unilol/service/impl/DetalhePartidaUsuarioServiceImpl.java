package com.pgpain.unilol.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.merakianalytics.orianna.types.core.match.Participant;
import com.pgpain.unilol.dto.DetalheDaPartidaDoUsuarioDto;
import com.pgpain.unilol.model.DetalhePartidaUsuario;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.model.PartidaDoUsuario;
import com.pgpain.unilol.service.DetalhePartidaUsuarioService;
import com.pgpain.unilol.service.FilaDeProcessoService;

@Service
public class DetalhePartidaUsuarioServiceImpl implements DetalhePartidaUsuarioService {

	@Autowired
	FilaDeProcessoService filaDeProcessoService;

	@Override
	public DetalhePartidaUsuario create(Participant participant) {
		DetalhePartidaUsuario detalhePartidaUsuario;
		detalhePartidaUsuario = new DetalhePartidaUsuario();
		detalhePartidaUsuario.setAbate(participant.getStats().getKills());
		detalhePartidaUsuario.setAssistencia(participant.getStats().getAssists());
		detalhePartidaUsuario.setMorte(participant.getStats().getDeaths());
		detalhePartidaUsuario.setDanoCausado(participant.getStats().getDamageDealtToChampions());
		detalhePartidaUsuario.setGoldTotal(participant.getStats().getGoldEarned());
		detalhePartidaUsuario.setNivelCampeao(participant.getStats().getChampionLevel());
		detalhePartidaUsuario.setCreepScore(participant.getStats().getCreepScore());

		detalhePartidaUsuario.setCombatScore(participant.getStats().getCombatScore());
		detalhePartidaUsuario.setVisionScore(participant.getStats().getVisionScore());
		detalhePartidaUsuario.setScore(participant.getStats().getScore());

		detalhePartidaUsuario.setObjectiveScore(participant.getStats().getObjectiveScore());
		detalhePartidaUsuario.setTeamObjectives(participant.getStats().getTeamObjectives());
		detalhePartidaUsuario.setWardsKilled(participant.getStats().getWardsKilled());
		detalhePartidaUsuario.setWardsPlaced(participant.getStats().getWardsPlaced());

		// pontos utilitarios
		detalhePartidaUsuario.setCrowdControlDealt(participant.getStats().getCrowdControlDealtToChampions().getStandardSeconds());
		detalhePartidaUsuario.setDamageHealed(participant.getStats().getDamageHealed());
		detalhePartidaUsuario.setDamageMitigated(participant.getStats().getDamageMitigated());

		return detalhePartidaUsuario;
	}

	@Override
	public List<DetalheDaPartidaDoUsuarioDto> getDetalhesByPartida(Partida partida) {
		List<DetalheDaPartidaDoUsuarioDto> detalhePartidaUsuarios = new ArrayList<>();

		List<PartidaDoUsuario> partidaDoUsuarios = partida.getPartidaDoUsuarios();
		partidaDoUsuarios.forEach(partidaDoUsuario -> {
			DetalheDaPartidaDoUsuarioDto detalhePartidaUsuario = new DetalheDaPartidaDoUsuarioDto();
			detalhePartidaUsuario.setPartidaDoUsuario(partidaDoUsuario);
			detalhePartidaUsuario.setDetalhePartidaUsuario(partidaDoUsuario.getDetalhePartidaUsuario());
			detalhePartidaUsuario.setItemsDaPartidaDoUsuario(partidaDoUsuario.getItemDaPartidaDoUsuarios());

			detalhePartidaUsuarios.add(detalhePartidaUsuario);
		});

		return detalhePartidaUsuarios;
	}

}
