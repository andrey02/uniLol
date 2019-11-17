package com.pgpain.unilol.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pgpain.unilol.enumeration.FilaDeProcessoEnum;
import com.pgpain.unilol.enumeration.TimelineEnum;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.model.PartidaDoUsuario;
import com.pgpain.unilol.model.Timeline;
import com.pgpain.unilol.model.Usuario;
import com.pgpain.unilol.repository.TimelineRepository;
import com.pgpain.unilol.service.FilaDeProcessoService;
import com.pgpain.unilol.service.PartidaDoUsuarioService;
import com.pgpain.unilol.service.PartidaService;
import com.pgpain.unilol.service.TimelineService;
import com.pgpain.unilol.service.UsuarioService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@Api(value = "Operacoes referentes aos detalhes das partida do usuário. Totais acumulados dos dados")
public class TimelineController {

	private static final String TEAM_BLUE = "100";
	private static final String TEAM_RED = "200";
	@Autowired
	private TimelineService timelineService;
	@Autowired
	private PartidaService partidaService;
	@Autowired
	private PartidaDoUsuarioService partidaDoUsuarioService;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	FilaDeProcessoService filaDeProcessoService;
	@Autowired
	private TimelineRepository timelineRepository;

	@GetMapping(value = "/timelines/{idExterno}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna as timelines de todos os participantes de uma detemrinada partida")
	public ResponseEntity<List<Timeline>> getDetalhesByPartida(
			@ApiParam(value = "Id externo disponibilizado pela riot válido. Ex.: 15647", required = true) @PathVariable Long idExterno) {

		log.debug("REST request to get timeline information : {}", idExterno);

		Partida partida = partidaService.findByIdExterno(idExterno);
		if (partida == null)
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

		if (filaDeProcessoService.getFilaDeProcessoByUsuarioAndPartidaAndTipoAndStatus(null, partida,
				FilaDeProcessoEnum.TIPO_TIMELINE.getValue(), FilaDeProcessoEnum.STATUS_PROCESSANDO.getValue()) != null)
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(timelineService.getTimelinesByPartida(partida), HttpStatus.OK);
	}

	@GetMapping(value = "/timeline/{accountId}/{idExterno}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna a timeline do participante de uma detemrinada partida")
	public ResponseEntity<?> getTimelineByUsuarioAndPartida(
			@ApiParam(value = "Id único do invocador disponibilizado pela Api ds Riot", required = true) @PathVariable String accountId,
			@ApiParam(value = "Id único da partida fornecido pela Api da Riot", required = true) @PathVariable Long idExterno) {

		log.debug("REST request to get timeline information : {}", idExterno);

		Usuario usuario = usuarioService.getUsuarioByAccountId(accountId);
		if(usuario == null)
			return new ResponseEntity<>("usuario inválido", HttpStatus.BAD_REQUEST);
		Partida partida = partidaService.findByIdExterno(idExterno);
		if(partida == null)
			return new ResponseEntity<>("Partida inválida", HttpStatus.BAD_REQUEST);
		
		PartidaDoUsuario partidaDoUsuario = partidaDoUsuarioService.getByUsuarioAndPartida(usuario, partida);

		if (partidaDoUsuario == null)
			return new ResponseEntity<>(new ArrayList<Timeline>(), HttpStatus.BAD_REQUEST);

		if (filaDeProcessoService.getFilaDeProcessoByUsuarioAndPartidaAndTipoAndStatus(usuario, partida,
				FilaDeProcessoEnum.TIPO_TIMELINE.getValue(), FilaDeProcessoEnum.STATUS_PROCESSANDO.getValue()) != null)
			return new ResponseEntity<>("Timeline está em processo.", HttpStatus.BAD_REQUEST);

		List<Timeline> timelines = partidaDoUsuario.getTimeline();
		if (timelines == null)
			return new ResponseEntity<>(new ArrayList<Timeline>(), HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(timelines, HttpStatus.OK);
	}

	@GetMapping(value = "/timeline/{accountId}/{idExterno}/timestamp", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna os timestamps disponíveis da timeline do participante de uma determinada partida")
	public ResponseEntity<?> getTimestampByUsuarioAndPartida(
			@ApiParam(value = "Id único do invocador disponibilizado pela Api ds Riot", required = true) @PathVariable String accountId,
			@ApiParam(value = "Id único da partida fornecido pela Api da Riot", required = true) @PathVariable Long idExterno) {

		log.debug("REST request to get timeline information with filters: {}", idExterno);

		Usuario usuario = usuarioService.getUsuarioByAccountId(accountId);
		if(usuario == null)
			return new ResponseEntity<>("usuario inválido", HttpStatus.BAD_REQUEST);
		Partida partida = partidaService.findByIdExterno(idExterno);
		if(partida == null)
			return new ResponseEntity<>("Partida inválida", HttpStatus.BAD_REQUEST);
		
		PartidaDoUsuario partidaDoUsuario = partidaDoUsuarioService.getByUsuarioAndPartida(usuario, partida);

		if (partidaDoUsuario == null)
			return new ResponseEntity<>(new ArrayList<Long>(), HttpStatus.BAD_REQUEST);


		if (filaDeProcessoService.getFilaDeProcessoByUsuarioAndPartidaAndTipoAndStatus(usuario, partida,
				FilaDeProcessoEnum.TIPO_TIMELINE.getValue(), FilaDeProcessoEnum.STATUS_PROCESSANDO.getValue()) != null)
			return new ResponseEntity<>("Timeline em processo.", HttpStatus.BAD_REQUEST);

		List<Timeline> timelines = partidaDoUsuario.getTimeline();
		if (timelines == null)
			return new ResponseEntity<>(new ArrayList<Long>(), HttpStatus.BAD_REQUEST);

		List<Long> timestamps = new ArrayList<>();
		timelines.forEach(timeline -> timestamps.add(timeline.getTempo()));

		return new ResponseEntity<>(timestamps, HttpStatus.OK);
	}

	@GetMapping(value = "/timeline/grafico/{idExterno}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna os valores da timeline, em formato de gráfico, para os participantes de uma determinada partida")
	public ResponseEntity<List<List<Long>>> getGraphByParticipants(
			@ApiParam(value = "Id único da partida fornecido pela Api da Riot", required = true) @PathVariable Long idExterno,
			@ApiParam(value = "Todos os possíveis valores de gráfico: ouroAdquirido,level, ouro,qtdCreep, experiencia e qtdMonstroNeutro ", required = true) @RequestParam String timelineData,
			@ApiParam(value = "Participants da partida. Valores de 1 a 10", required = true) @RequestParam List<Integer> participantId) {

		log.debug("REST request to get timeline information with filters: {}", idExterno);

		List<List<Long>> dataGrafico = new ArrayList<>();

		if (participantId == null || participantId.isEmpty() || timelineData == null || "".equals(timelineData))
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

		TimelineEnum timelineTipo = TimelineEnum.fromString(timelineData);
		if (timelineTipo == null)
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

		// pegar por reflection atributo da timeline
		Timeline reflect = new Timeline();
		Class<? extends Timeline> class1 = reflect.getClass();
		Field f;
		try {
			f = class1.getDeclaredField(timelineTipo.getTipo());
		} catch (NoSuchFieldException|SecurityException e1) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		}
		f.setAccessible(true);

		Partida partida = partidaService.findByIdExterno(idExterno);

		// array de tempo
		List<Long> tempoDoJogo = timelineRepository.findTempoByPartidaDoUsuario(partida.getPartidaDoUsuarios().get(0));

		tempoDoJogo.forEach(tempo -> {
			List<Long> arrayData = new ArrayList<>();
			arrayData.add(tempo);
			for (Integer participante : participantId) {
				if (participante >= 1 && participante <= 10) {
					try {
						PartidaDoUsuario partidaDoUsuarioByPartidaAndParticipantId = partidaDoUsuarioService
								.getPartidaDoUsuarioByPartidaAndParticipantId(partida, participante);
						Timeline timelineAtual = timelineRepository
								.findByPartidaDoUsuarioAndTempo(partidaDoUsuarioByPartidaAndParticipantId, tempo);
						arrayData.add(((Long) f.get(timelineAtual)) == null ? 0 : ((Long) f.get(timelineAtual)));
					} catch (IllegalArgumentException|IllegalAccessException e) {
						log.info("nao encontrado");
					} 				
				}
			}
			dataGrafico.add(arrayData);
		});

		return new ResponseEntity<>(dataGrafico, HttpStatus.OK);
	}

	@GetMapping(value = "/timeline/grafico/{idExterno}/{teamId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna os valores da timeline, em formato de gráfico, para um dos times de uma determinada partida")
	public ResponseEntity<List<List<Long>>> getGraphByTime(
			@ApiParam(value = "Id único da partida fornecido pela Api da Riot", required = true) @PathVariable Long idExterno,
			@ApiParam(value = "Id de um dos times. 100 ou 200", required = true) @PathVariable String teamId,
			@ApiParam(value = "Todos os possíveis valores de gráfico, ver TimelineEnum.", required = true) @RequestParam String timelineData) {
		
		List<List<Long>> dataGrafico = new ArrayList<>();

		if (teamId == null || teamId.isEmpty() || timelineData == null || "".equals(timelineData))
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

		TimelineEnum timelineTipo = TimelineEnum.fromString(timelineData);
		if (timelineTipo == null)
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

		// pegar por reflection atributo da timeline
		Object reflect = Timeline.class;
		@SuppressWarnings("unchecked")
		Class<? extends Timeline> class1 = (Class<? extends Timeline>) reflect.getClass();
		Field f;
		try {
			f = class1.getDeclaredField(timelineTipo.getTipo());
		} catch (NoSuchFieldException| SecurityException e1) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		} 
		f.setAccessible(true);

		Partida partida = partidaService.findByIdExterno(idExterno);

		// array de tempo
		List<Long> tempoDoJogo = timelineRepository.findTempoByPartidaDoUsuario(partida.getPartidaDoUsuarios().get(0));

		//participantId por time
		List<Integer> participantId = new ArrayList<>();
		if(teamId.equals(TEAM_BLUE)) {
			Integer[] teamBlue = new Integer[] {1, 2, 3, 4, 5};
			participantId.addAll(Arrays.asList(teamBlue));
		}
		else if(teamId.equals(TEAM_RED)) {
			Integer[] teamRed = new Integer[] {6, 7, 8, 9, 10};
			participantId.addAll(Arrays.asList(teamRed));
		} else {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		}
			
			
		
		//pegar partidas dos usuarios por time
		List<PartidaDoUsuario> partidaDoUsuarios = new ArrayList<>();
		for (Integer participante : participantId) {
			if (participante >= 1 && participante <= 10) {
			try {
				partidaDoUsuarios.add(partidaDoUsuarioService
						.getPartidaDoUsuarioByPartidaAndParticipantId(partida, participante));
			} catch (Exception e) {
				log.info("Não encontrado");
			}
			}
		}
			
		tempoDoJogo.forEach(tempo -> {
			List<Long> arrayData = new ArrayList<>();
			arrayData.add(tempo);
			
			Long somaValor = TimelineEnum.getSumQuery(timelineTipo,partidaDoUsuarios,0);
			
			arrayData.add(somaValor== null? 0: somaValor);
			dataGrafico.add(arrayData);
		});

		return new ResponseEntity<>(dataGrafico, HttpStatus.OK);
		
	}

}
