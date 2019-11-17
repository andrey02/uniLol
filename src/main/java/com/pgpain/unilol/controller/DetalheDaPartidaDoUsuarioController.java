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

import com.pgpain.unilol.dto.DetalheDaPartidaDoUsuarioDto;
import com.pgpain.unilol.enumeration.FilaDeProcessoEnum;
import com.pgpain.unilol.model.DetalhePartidaUsuario;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.model.PartidaDoUsuario;
import com.pgpain.unilol.model.Usuario;
import com.pgpain.unilol.service.DetalhePartidaUsuarioService;
import com.pgpain.unilol.service.FilaDeProcessoService;
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
@Api(value = "Operacoes referentes aos detalhes das partida do usuário. Totais acumulados dos dados")
public class DetalheDaPartidaDoUsuarioController {

	@Autowired
	private DetalhePartidaUsuarioService detalhePartidaUsuarioService;
	@Autowired
	private PartidaService partidaService;
	@Autowired
	private PartidaDoUsuarioService partidaDoUsuarioService;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private FilaDeProcessoService filaDeProcessoService;

	@GetMapping(value = "/detalhes/{idExterno}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna os detalhes de todos os participantes de uma detemrinada partida")
	public ResponseEntity<List<DetalheDaPartidaDoUsuarioDto>> getDetalhesByPartida(
			@ApiParam(value = "Id externo disponibilizado pela riot válido. Ex.: 15647", required = true) @PathVariable Long idExterno) {

		log.debug("REST request to get detalheDaPartida information : {}", idExterno);

		Partida partida = partidaService.findByIdExterno(idExterno);
		if (partida == null)
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		
		if (filaDeProcessoService.getFilaDeProcessoByUsuarioAndPartidaAndTipoAndStatus(null, partida,
				FilaDeProcessoEnum.TIPO_DETALHE.getValue(), FilaDeProcessoEnum.STATUS_PROCESSANDO.getValue()) != null)
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

		List<DetalheDaPartidaDoUsuarioDto> detalhesByPartida = detalhePartidaUsuarioService.getDetalhesByPartida(partida);
		if(detalhesByPartida.isEmpty())
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity<>(detalhesByPartida, HttpStatus.OK);
	}

	@GetMapping(value = "/detalhe/{accountId}/{idExterno}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna o detalhe do participante de uma detemrinada partida")
	public ResponseEntity<DetalhePartidaUsuario> getDetalheByUsuarioAndPartida(
			@ApiParam(value = "Id único do invocador disponibilizado pela Api ds Riot", required = true) @PathVariable String accountId,
			@ApiParam(value = "Id único da partida fornecido pela Api da Riot", required = true) @PathVariable Long idExterno) {

		log.debug("REST request to get detalheDaPartida information : {}", idExterno);

		Usuario usuario = usuarioService.getUsuarioByAccountId(accountId);
		if(usuario == null)
			return new ResponseEntity<>(new DetalhePartidaUsuario(), HttpStatus.BAD_REQUEST);
		Partida partida = partidaService.findByIdExterno(idExterno);
		if(partida == null)
			return new ResponseEntity<>(new DetalhePartidaUsuario(), HttpStatus.BAD_REQUEST);
		PartidaDoUsuario partidaDoUsuario = partidaDoUsuarioService.getByUsuarioAndPartida(usuario, partida);
		if (partidaDoUsuario == null)
			return new ResponseEntity<>(new DetalhePartidaUsuario(), HttpStatus.BAD_REQUEST);

		DetalhePartidaUsuario detalhePartidaUsuario = partidaDoUsuario.getDetalhePartidaUsuario();
		if (detalhePartidaUsuario == null)
			return new ResponseEntity<>(new DetalhePartidaUsuario(), HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(detalhePartidaUsuario, HttpStatus.OK);
	}

}
