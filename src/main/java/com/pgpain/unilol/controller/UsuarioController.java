package com.pgpain.unilol.controller;

import java.util.List;

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
import com.pgpain.unilol.dto.UsuarioDto;
import com.pgpain.unilol.model.Usuario;
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
@Api(value = "Operacoes referentes ao usuario da aplicacao")
public class UsuarioController {

	private UsuarioService usuarioService;
	private OriannaConfiguration orianna;

	public UsuarioController(UsuarioService usuarioService, OriannaConfiguration orianna) {
		super();
		this.usuarioService = usuarioService;
		this.orianna = orianna;
	}

	@GetMapping(value = "/usuario/{summonerName}/{region}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados do usuario")
	public ResponseEntity<UsuarioDto> getUsuarioByNome(
			@ApiParam(value = "Entrar com um nome de invocador valido. Ex.: DreyKing", required = true) @PathVariable String summonerName,
			@ApiParam(value = "BRAZIL, " + "EUROPE_NORTH_EAST, " + "EUROPE_WEST, " + "JAPAN, " + "KOREA, "
					+ "LATIN_AMERICA_NORTH, " + "LATIN_AMERICA_SOUTH, " + "NORTH_AMERICA, " + "OCEANIA, " + "RUSSIA or "
					+ "TURKEY.", required = true) @PathVariable String region) {
		UsuarioDto usuario = new UsuarioDto();

		log.debug("REST request to get User information : {}", summonerName);

		// checar se regiao e valida
		String accountId = usuarioService.eUsuarioValido(summonerName, region);
		if (!orianna.eRegiaoValida(region))
			return new ResponseEntity<>(usuario, HttpStatus.BAD_REQUEST);

		if (usuarioService.eUsuarioValido(summonerName, region) == null) {
			return new ResponseEntity<>(usuario, HttpStatus.BAD_REQUEST);
		} else {
			Usuario usuarioBanco = usuarioService.getUsuarioByAccountId(accountId);
			usuario.setAccountId(usuarioBanco.getAccountId());
			usuario.setNome(usuarioBanco.getNome());
		}

		return new ResponseEntity<>(usuario, HttpStatus.OK);
	}
	
	@GetMapping(value = "/regiao/{region}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Muda regiao padrao")
	public ResponseEntity<?> mudarRegiao(
			@ApiParam(value = "BRAZIL, " + "EUROPE_NORTH_EAST, " + "EUROPE_WEST, " + "JAPAN, " + "KOREA, "
					+ "LATIN_AMERICA_NORTH, " + "LATIN_AMERICA_SOUTH, " + "NORTH_AMERICA, " + "OCEANIA, " + "RUSSIA or "
					+ "TURKEY.", required = true) @PathVariable String region) {

		log.debug("Mudar regiao padrao");

		//checar se regiao e valida
				Region[] values = Region.values();
				int controle = 0;
				for (Region me : values) {
					if (me.name().equalsIgnoreCase(region))
						controle = 1;
				}
				if (controle == 0)
					return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);

				Orianna.setDefaultRegion(Region.valueOf(region));

		return new ResponseEntity<>("Regiao alterada", HttpStatus.OK);
	}
	
	@GetMapping(value = "/key/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Muda key da RIOT da aplicação")
	public ResponseEntity<?> mudarChaveApi(
			@ApiParam(value = "chave valida gerada no site da riot developer", required = true) @PathVariable String key) {

		log.debug("Mudar chave api da riot");

		Orianna.setRiotAPIKey(key);

		return new ResponseEntity<>("Chave alterada", HttpStatus.OK);
	}
	
	@GetMapping(value = "/usuario/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Lista todos os usuarios ativos da aplicação")
	public ResponseEntity<List<UsuarioDto>> list() {

		log.debug("Listar todos os usuários ativos da aplicação");

		return new ResponseEntity<>(usuarioService.list(), HttpStatus.OK);
	}

}
