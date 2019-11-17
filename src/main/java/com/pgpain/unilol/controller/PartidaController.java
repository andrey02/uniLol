package com.pgpain.unilol.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgpain.unilol.model.EventoDaPartida;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.service.PartidaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@Api(value = "Operacoes referentes as partidas")
public class PartidaController {

	private PartidaService partidaService;

	public PartidaController(PartidaService partidaService) {
		this.partidaService = partidaService;
	}

	@GetMapping(value = "/partida/{idExterno}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados da partida")
	public ResponseEntity<Partida> getPartidaByIdExterno(
			@ApiParam(value = "Id único da partida fornecido pela Api da Riot", required = true) @PathVariable Long idExterno) {
		log.info("Pegar informações da partida do usuario para o id=" + idExterno);

		// verificar se partida
		Partida partida = partidaService.findByIdExterno(idExterno);
		if (partida == null)
			return new ResponseEntity<>(partida, HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(partida, HttpStatus.OK);
	}

	@GetMapping(value = "/partida/eventos/{idExterno}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados dos eventos da partida")
	public ResponseEntity<List<EventoDaPartida>> getEventoDePartidaByIdExterno(
			@ApiParam(value = "Id único da partida fornecido pela Api da Riot", required = true) @PathVariable Long idExterno) {
		log.info("Pegar informações dos eventos da partida do usuario para o id=" + idExterno);
		
		// verificar se partida existe
		Partida partida = partidaService.findByIdExterno(idExterno);
		if (partida == null)
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		
		List<EventoDaPartida> eventosDaPartida = partida.getEventosDaPartida();
		
		return new ResponseEntity<>(eventosDaPartida, HttpStatus.OK);
		
	}

}