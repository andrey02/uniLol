package com.pgpain.unilol.service;

import java.util.List;

import com.merakianalytics.orianna.types.core.match.Match;
import com.merakianalytics.orianna.types.core.match.Participant;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.model.Timeline;

public interface TimelineService {
	
	List<Timeline> create(String participantId,Match match);

	List<Timeline> getTimelinesByPartida(Partida partida);

}
