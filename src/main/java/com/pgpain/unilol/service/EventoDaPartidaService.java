package com.pgpain.unilol.service;

import java.util.List;

import com.pgpain.unilol.model.EventoDaPartida;
import com.pgpain.unilol.model.Partida;

public interface EventoDaPartidaService {
	List<EventoDaPartida> saveAll(Partida partida);

	List<EventoDaPartida> getEventoByPartidaAndParticipantId(Partida partida, Integer participantId);

	List<EventoDaPartida> getEventoByPartidaAndTime(Partida partida, String time);
}
