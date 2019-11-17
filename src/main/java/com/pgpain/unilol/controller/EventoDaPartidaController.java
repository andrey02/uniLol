package com.pgpain.unilol.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgpain.unilol.enumeration.FilaDeProcessoEnum;
import com.pgpain.unilol.model.EventoDaPartida;
import com.pgpain.unilol.model.FilaDeProcesso;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.model.PartidaDoUsuario;
import com.pgpain.unilol.service.EventoDaPartidaService;
import com.pgpain.unilol.service.FilaDeProcessoService;
import com.pgpain.unilol.service.PartidaDoUsuarioService;
import com.pgpain.unilol.service.PartidaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@Api(value = "Operacoes referentes aos eventos das partidas")
public class EventoDaPartidaController {

	@Autowired
	private PartidaService partidaService;
	@Autowired
	private PartidaDoUsuarioService partidaDoUsuarioService;
	@Autowired
	private FilaDeProcessoService filaDeProcessoService;
	@Autowired
	private EventoDaPartidaService eventoDaPartidaService;

	@GetMapping(value = "/evento/{idExterno}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna lista de todos os eventos da partida selecionada.")
	public ResponseEntity<List<EventoDaPartida>> getEventoByPartida(
			@ApiParam(value = "Id da partida disponibilizado pela api da Riot.", required = true) @PathVariable Long idExterno) {
		log.info("REST request to get EventoDaPartida information : " + idExterno);

		List<EventoDaPartida> eventoDaPartidas = new ArrayList<>();

		// checar se existe essa partida no banco
		Partida partida = partidaService.findByIdExterno(idExterno);
		if (partida == null)
			return new ResponseEntity<>(eventoDaPartidas, HttpStatus.BAD_REQUEST);

		// checar se o evento dela está em processamento
		FilaDeProcesso filaAtual = filaDeProcessoService.getFilaDeProcessoByUsuarioAndPartidaAndTipoAndStatus(null,
				partida, FilaDeProcessoEnum.TIPO_EVENTO.getValue(), FilaDeProcessoEnum.STATUS_PROCESSANDO.getValue());

		if (filaAtual != null)
			return new ResponseEntity<>(eventoDaPartidas, HttpStatus.BAD_REQUEST);

		eventoDaPartidas.addAll(partida.getEventosDaPartida());
		if (eventoDaPartidas.isEmpty())
			return new ResponseEntity<>(eventoDaPartidas, HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(eventoDaPartidas, HttpStatus.OK);
	}

	@GetMapping(value = "/evento/{idExterno}/{participantId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna lista dos eventos da partida selecionada por participante.")
	public ResponseEntity<List<EventoDaPartida>> getEventoDaPartidaByParticipantId(
			@ApiParam(value = "Id da partida disponibilizado pela api da Riot.", required = true) @PathVariable Long idExterno,
			@ApiParam(value = "Id do usuario na partida específica. Ex.: 1 a 10.", required = true) @PathVariable Integer participantId) {

		log.info("REST request para obter eventos da partida de um participantId específico : " + idExterno
				+ " e participant: " + participantId);

		List<EventoDaPartida> eventoDaPartidas = new ArrayList<>();

		// checar se existe essa partida no banco
		Partida partida = partidaService.findByIdExterno(idExterno);
		if (partida == null)
			return new ResponseEntity<>(eventoDaPartidas, HttpStatus.BAD_REQUEST);

		// checar se o evento dela está em processamento
		FilaDeProcesso filaAtual = filaDeProcessoService.getFilaDeProcessoByUsuarioAndPartidaAndTipoAndStatus(null,
				partida, FilaDeProcessoEnum.TIPO_EVENTO.getValue(), FilaDeProcessoEnum.STATUS_PROCESSANDO.getValue());

		if (filaAtual != null)
			return new ResponseEntity<>(eventoDaPartidas, HttpStatus.BAD_REQUEST);

		// checar se existe partida do usuario com essa partida e participantId
		PartidaDoUsuario partidaDoUsuario = partidaDoUsuarioService
				.getPartidaDoUsuarioByPartidaAndParticipantId(partida, participantId);

		if (partidaDoUsuario == null)
			return new ResponseEntity<>(eventoDaPartidas, HttpStatus.BAD_REQUEST);

		// lógica com filtros e sem filtros
		eventoDaPartidas.addAll(eventoDaPartidaService.getEventoByPartidaAndParticipantId(partida, participantId));

		return new ResponseEntity<>(eventoDaPartidas, HttpStatus.OK);
	}

	@GetMapping(value = "/evento/{idExterno}/{time}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna lista dos eventos da partida selecionada por time.")
	public ResponseEntity<List<EventoDaPartida>> getEventoDaPartidaAndTime(
			@ApiParam(value = "Id da partida disponibilizado pela api da Riot.", required = true) @PathVariable Long idExterno,
			@ApiParam(value = "Id do time na partida específica. Ex.: 100 ou 200.", required = true) @PathVariable String time) {

		log.info("REST request para obter eventos da partida de um participantId específico : " + idExterno
				+ " e time: " + time);

		List<EventoDaPartida> eventoDaPartidas = new ArrayList<>();

		// checar se existe essa partida no banco
		Partida partida = partidaService.findByIdExterno(idExterno);
		if (partida == null)
			return new ResponseEntity<>(eventoDaPartidas, HttpStatus.BAD_REQUEST);
		// checar se o evento dela está em processamento
		FilaDeProcesso filaAtual = filaDeProcessoService.getFilaDeProcessoByUsuarioAndPartidaAndTipoAndStatus(null,
				partida, FilaDeProcessoEnum.TIPO_EVENTO.getValue(), FilaDeProcessoEnum.STATUS_PROCESSANDO.getValue());

		if (filaAtual != null)
			return new ResponseEntity<>(eventoDaPartidas, HttpStatus.BAD_REQUEST);

		// lógica com eventos do time
		eventoDaPartidas.addAll(eventoDaPartidaService.getEventoByPartidaAndTime(partida, time));
		if (eventoDaPartidas.isEmpty())
			return new ResponseEntity<>(eventoDaPartidas, HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(eventoDaPartidas, HttpStatus.OK);
	}

}
