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

import com.pgpain.unilol.model.DetalhePartidaUsuario;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.model.PartidaDoUsuario;
import com.pgpain.unilol.model.Timeline;
import com.pgpain.unilol.model.Usuario;
import com.pgpain.unilol.service.PartidaDoUsuarioService;
import com.pgpain.unilol.service.PartidaService;
import com.pgpain.unilol.service.UsuarioService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@Api(value = "Operacoes referentes ao historico de partidas")
public class PartidaDoUsuarioController {

	private UsuarioService usuarioService;
	private PartidaService partidaService;
	private PartidaDoUsuarioService partidaDoUsuarioService;

	public PartidaDoUsuarioController(UsuarioService usuarioService, PartidaService partidaService,
			PartidaDoUsuarioService partidaDoUsuarioService) {
		this.usuarioService = usuarioService;
		this.partidaService = partidaService;
		this.partidaDoUsuarioService = partidaDoUsuarioService;
	}

	@GetMapping(value = "/partidadousuario/{idExterno}/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados da partidaDoUsuario, como: o participant id de identificação dentro da partida, o campeao utilizado e mais")
	public ResponseEntity<PartidaDoUsuario> getPartidaDoUsuario(
			@ApiParam(value = "Id único da partida fornecido pela Api da Riot", required = true) @PathVariable Long idExterno,
			@ApiParam(value = "Id único do invocador disponibilizado pela Api ds Riot", required = true) @PathVariable String accountId) {
		log.info("Pegar informações da partida do usuario" + idExterno);

		// verificar se partida e usuario existe
		Partida partida = partidaService.findByIdExterno(idExterno);
		if (partida == null)
			return new ResponseEntity<>(new PartidaDoUsuario(), HttpStatus.BAD_REQUEST);
		Usuario usuario = usuarioService.getUsuarioByAccountId(accountId);
		if (usuario == null)
			return new ResponseEntity<>(new PartidaDoUsuario(), HttpStatus.BAD_REQUEST);
		PartidaDoUsuario partidaDoUsuario = partidaDoUsuarioService.getByUsuarioAndPartida(usuario, partida);

		if (partidaDoUsuario == null) {
			return new ResponseEntity<>(new PartidaDoUsuario(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(partidaDoUsuario, HttpStatus.OK);
	}

	@GetMapping(value = "/partidadousuario/timeline/{idExterno}/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados de timeline da partidaDoUsuario, como: gold,experiencia,qtd de creep e mais")
	public ResponseEntity<List<Timeline>> getTimelinePartidaDoUsuario(
			@ApiParam(value = "Id único da partida fornecido pela Api da Riot", required = true) @PathVariable Long idExterno,
			@ApiParam(value = "Id único do invocador disponibilizado pela Api ds Riot", required = true) @PathVariable String accountId) {
		log.info("Pegar informações da timeline da partida do usuario");

		// verificar se partida e usuario existe
		Partida partida = partidaService.findByIdExterno(idExterno);
		if (partida == null)
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		Usuario usuario = usuarioService.getUsuarioByAccountId(accountId);
		if (usuario == null)
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		PartidaDoUsuario partidaDoUsuario = partidaDoUsuarioService.getByUsuarioAndPartida(usuario, partida);
		if (partidaDoUsuario == null) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		}

		List<Timeline> timeline = partidaDoUsuario.getTimeline();
		if (timeline.isEmpty())
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
		return new ResponseEntity<>(timeline, HttpStatus.OK);
	}

	@GetMapping(value = "/partidadousuario/detalhes/{idExterno}/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados dos detalhes da partidaDoUsuario, como: gold,experiencia,qtd de creep e mais")
	public ResponseEntity<DetalhePartidaUsuario> getDetalhesPartidaDoUsuario(
			@ApiParam(value = "Id único da partida fornecido pela Api da Riot", required = true) @PathVariable Long idExterno,
			@ApiParam(value = "Id único do invocador disponibilizado pela Api ds Riot", required = true) @PathVariable String accountId) {
		log.info("Pegar informações da timeline da partida do usuario");

		// verificar se partida e usuario existe
		Partida partida = partidaService.findByIdExterno(idExterno);
		if (partida == null)
			return new ResponseEntity<>(new DetalhePartidaUsuario(), HttpStatus.BAD_REQUEST);
		Usuario usuario = usuarioService.getUsuarioByAccountId(accountId);
		if (usuario == null)
			return new ResponseEntity<>(new DetalhePartidaUsuario(), HttpStatus.BAD_REQUEST);
		PartidaDoUsuario partidaDoUsuario = partidaDoUsuarioService.getByUsuarioAndPartida(usuario, partida);
		if (partidaDoUsuario == null) {
			return new ResponseEntity<>(new DetalhePartidaUsuario(), HttpStatus.BAD_REQUEST);
		}

		DetalhePartidaUsuario detalhePartidaUsuario = partidaDoUsuario.getDetalhePartidaUsuario();
		if (detalhePartidaUsuario == null)
			return new ResponseEntity<>(new DetalhePartidaUsuario(), HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(detalhePartidaUsuario, HttpStatus.OK);
	}

	@GetMapping(value = "/partidadousuario/{idExterno}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados das partidasDosUsuarios.")
	public ResponseEntity<List<PartidaDoUsuario>> getPartidasDosUsuariosbyPartida(
			@ApiParam(value = "Id único da partida fornecido pela Api da Riot", required = true) @PathVariable Long idExterno) {
		log.info("Pegar informações das partidas dos usuarios");

		// verificar se partida e usuario existe
		Partida partida = partidaService.findByIdExterno(idExterno);
		if (partida == null)
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		List<PartidaDoUsuario> partidaDoUsuario = partidaDoUsuarioService.getPartidaDoUsuarioByPartida(partida);
		if (partidaDoUsuario == null) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(partidaDoUsuario, HttpStatus.OK);
	}
}
