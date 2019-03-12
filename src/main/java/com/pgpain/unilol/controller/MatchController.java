package com.pgpain.unilol.controller;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Region;
import com.pgpain.unilol.dto.MatchHistoryDTO;
import com.pgpain.unilol.service.MatchService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@Api(value="Operacoes referentes ao historico de partidas")
public class MatchController {

	MatchService matchService;

	@Value("${API_KEY}")
	private String apiKey;

	public MatchController(MatchService matchService) {
		this.matchService = matchService;
	}

	@GetMapping(value = "/match/{summonerName}/{region}")
	@ApiOperation(value="Retorna lista das partidas dos últimos 10 dias")
	public ResponseEntity<MatchHistoryDTO> getMatchHistory(
			@ApiParam(value = "Entrar com um nome de invocador valido. Ex.: DreyKing", required = true) @PathVariable String summonerName,
			@ApiParam(value = "BRAZIL, " + "EUROPE_NORTH_EAST, " + "EUROPE_WEST, " + "JAPAN, " + "KOREA, " + "LATIN_AMERICA_NORTH, "
					+ "LATIN_AMERICA_SOUTH, " + "NORTH_AMERICA, " + "OCEANIA, " + "RUSSIA or "
					+ "TURKEY.", required = true) @PathVariable String region) {
		log.debug("REST request to get MatchHistory : {}", summonerName);

		Orianna.setRiotAPIKey(apiKey);
		Orianna.setDefaultRegion(Region.BRAZIL);
		// checar se usuario existe,se nao enviar nulo
		if (summonerName.equals("") || summonerName == null) {
			return null;
		}

		MatchHistoryDTO matchHistoryDTO = matchService.getMatchHistory(summonerName, region);

		if (matchHistoryDTO == null) {
			return null;
		}

		return new ResponseEntity<>(matchHistoryDTO, HttpStatus.OK);
	}
}
