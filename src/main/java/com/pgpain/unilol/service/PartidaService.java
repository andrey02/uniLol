package com.pgpain.unilol.service;

import com.pgpain.unilol.dto.MatchDto;
import com.pgpain.unilol.model.Partida;

public interface PartidaService {
	
	Partida savePartida(MatchDto matchDTO);
	Partida findByIdExterno(Long idExterno);

}
