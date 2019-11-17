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

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Region;
import com.pgpain.unilol.dto.MatchHistoryDto;
import com.pgpain.unilol.dto.PosicaoDto;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.model.PartidaDoUsuario;
import com.pgpain.unilol.model.Usuario;
import com.pgpain.unilol.repository.UsuarioRepository;
import com.pgpain.unilol.service.FilaDeProcessoService;
import com.pgpain.unilol.service.MatchService;
import com.pgpain.unilol.service.PartidaDoUsuarioService;
import com.pgpain.unilol.service.PartidaService;
import com.pgpain.unilol.service.UsuarioService;
import com.pgpain.unilol.util.OriannaConfiguration;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@Api(value = "Operacoes referentes ao historico de partidas")
public class MatchController {

	private MatchService matchService;
	private UsuarioService usuarioService;
	private OriannaConfiguration orianna;
	private PartidaService partidaService;
	private PartidaDoUsuarioService partidaDoUsuarioService;
	private FilaDeProcessoService filaDeProcessoService;
	@Autowired
	private UsuarioRepository usuarioRepository;

	public MatchController(MatchService matchService, UsuarioService usuarioService, OriannaConfiguration orianna,
			PartidaService partidaService, PartidaDoUsuarioService partidaDoUsuarioService,
			FilaDeProcessoService filaDeProcessoService) {
		this.matchService = matchService;
		this.usuarioService = usuarioService;
		this.orianna = orianna;
		this.partidaService = partidaService;
		this.partidaDoUsuarioService = partidaDoUsuarioService;
		this.filaDeProcessoService = filaDeProcessoService;

	}

	@GetMapping(value = "/match/{summonerName}/{region}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna lista das ultimas 20 partidas")
	public ResponseEntity<?> getMatchHistory(
			@ApiParam(value = "Entrar com um nome de invocador valido. Ex.: DreyKing", required = true) @PathVariable String summonerName,
			@ApiParam(value = "BRAZIL, " + "EUROPE_NORTH_EAST, " + "EUROPE_WEST, " + "JAPAN, " + "KOREA, "
					+ "LATIN_AMERICA_NORTH, " + "LATIN_AMERICA_SOUTH, " + "NORTH_AMERICA, " + "OCEANIA, " + "RUSSIA or "
					+ "TURKEY.", required = true) @PathVariable String region)
			throws InterruptedException {
		log.debug("REST request to get MatchHistory : {}", summonerName);

		MatchHistoryDto historico = null;

		if (!orianna.eRegiaoValida(region))
			return new ResponseEntity<>("Regiao inválida", HttpStatus.BAD_REQUEST);

		Orianna.setDefaultRegion(Region.valueOf(region));

		String accountId = usuarioService.eUsuarioValido(summonerName, region);
		if (accountId == null)
			return new ResponseEntity<>("Usuário inexistente", HttpStatus.BAD_REQUEST);

		// checar se tem no banco
		Usuario usuario = usuarioService.getUsuarioByAccountId(accountId);
		if (usuario == null || usuario.getStatus() == 0) {
			Usuario save = null;
			if (usuario == null)
				save = usuarioService.saveUsuario(summonerName, region, 1);
			else {
				usuario.setStatus((byte) 1);
				save = usuarioRepository.save(usuario);
			}

			if (save.getAccountId() == null)
				return new ResponseEntity<>("Usuario inexistente", HttpStatus.BAD_REQUEST);
			historico = matchService.getMatchHistoryFromAPI(save);
		} else {
			historico = matchService.getMatchHistoryFromDB(usuario);
		}

		return new ResponseEntity<>(historico, HttpStatus.OK);
	}

	@GetMapping(value = "/match/posicao/{idExterno}/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna posicao do invocador no mapa em determinada partida por todo o tempo do jogo")
	public ResponseEntity<List<Long[]>> getPosicaoByPartidaEUsuario(
			@ApiParam(value = "Id único da partida fornecido pela Api da Riot", required = true) @PathVariable Long idExterno,
			@ApiParam(value = "Id único do invocador disponibilizado pela Api ds Riot", required = true) @PathVariable String accountId) {

		// verificar se partida e usuario existe
		Partida partida = partidaService.findByIdExterno(idExterno);
		Usuario usuario = usuarioService.getUsuarioByAccountId(accountId);
		PartidaDoUsuario partidaDoUsuario = partidaDoUsuarioService.getByUsuarioAndPartida(usuario, partida);

		if (partida == null || usuario == null || partidaDoUsuario == null) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		}

		List<PosicaoDto> posicaoDto = partidaDoUsuarioService.getPosicaoDoUsuario(partidaDoUsuario);
		List<Long[]> posiArray = new ArrayList<>();

		// passar para array no formato que o D3 leia
		posicaoDto.forEach(posi -> {
			Long[] array = { posi.getX(), posi.getY() };
			posiArray.add(array);
		});

		return new ResponseEntity<>(posiArray, HttpStatus.OK);
	}

	@GetMapping(value = "/match/{summonerName}/{region}/update", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Atualiza informações do usuario removendo as antigas e adicionando as novas partidas. Retorna Um matchHistory das 20 últimas partidas atualizadas")
	public ResponseEntity<?> atualizarMatchHistory(
			@ApiParam(value = "Id único da partida fornecido pela Api da Riot", required = true) @PathVariable String summonerName,
			@ApiParam(value = "Id único do invocador disponibilizado pela Api ds Riot", required = true) @PathVariable String region)
			throws InterruptedException {
		MatchHistoryDto matchHistoryDTO = null;
		log.debug("REST request to get MatchHistory : {}", summonerName);

		// saber se o usuário está em processo.
		String accountId = usuarioService.eUsuarioValido(summonerName, region);
		if (accountId == null)
			return new ResponseEntity<>("Usuario Inválido", HttpStatus.BAD_REQUEST);
		Usuario usuario = usuarioService.getUsuarioByAccountId(accountId);
		if (usuario == null)
			return new ResponseEntity<>("Usuario Inválido", HttpStatus.BAD_REQUEST);

		if (usuario.getStatus() != 1)
			return new ResponseEntity<>("Usuario Inativo", HttpStatus.BAD_REQUEST);

		if (filaDeProcessoService.isUsuarioInProcess(usuario))
			return new ResponseEntity<>("Processando dados do usuário", HttpStatus.BAD_REQUEST);

		// checar se atualizou ou não
		if (!partidaDoUsuarioService.atualizarPartidasDosUsuarios(usuario))
			return new ResponseEntity<>("Não há partidas para atualizar", HttpStatus.BAD_REQUEST);

		// esperar inserir no banco e depois lançar matchHistory
		Thread.sleep(20000);// por volta de 1 min
		matchHistoryDTO = matchService.getMatchHistoryFromDB(usuario);

		return new ResponseEntity<>(matchHistoryDTO, HttpStatus.OK);
	}

}
