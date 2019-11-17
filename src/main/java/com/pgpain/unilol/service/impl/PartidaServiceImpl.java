package com.pgpain.unilol.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgpain.unilol.dto.MatchDto;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.repository.PartidaRepository;
import com.pgpain.unilol.service.PartidaService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PartidaServiceImpl implements PartidaService {

	@Autowired
	private PartidaRepository partidaRepository;

	@Override
	public Partida savePartida(MatchDto matchDTO) {

		// Criar primeiro partida
		Partida partida = new Partida();
		partida.setIdExterno(matchDTO.getMatchId());
		partida.setTipoPartida(matchDTO.getMapa());
		partida.setDataCriacao(matchDTO.getCreationTime());
		partida.setDuracaoPartida(matchDTO.getDuracaoPartida());

		return partidaRepository.save(partida);
	}

	@Override
	public Partida findByIdExterno(Long idExterno) {

		Optional<Partida> partidaOp = partidaRepository.findByIdExterno(idExterno);
		if (partidaOp.isPresent()) {
			return partidaOp.get();
		}
		log.error("Partida não encontrada com o id externo: " + idExterno);
		return new Partida();
	}

}
