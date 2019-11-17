package com.pgpain.unilol.service;

import java.util.List;

import com.merakianalytics.orianna.types.core.match.Participant;
import com.pgpain.unilol.dto.DetalheDaPartidaDoUsuarioDto;
import com.pgpain.unilol.model.DetalhePartidaUsuario;
import com.pgpain.unilol.model.Partida;

public interface DetalhePartidaUsuarioService {
	
	DetalhePartidaUsuario create(Participant participant);

	List<DetalheDaPartidaDoUsuarioDto> getDetalhesByPartida(Partida partida);
}
