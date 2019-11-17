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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pgpain.unilol.dto.SpellDto;
import com.pgpain.unilol.model.Spell;
import com.pgpain.unilol.service.SpellService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@Api(value = "Operacoes referentes as spells do jogo")
public class SpellController {

	@Autowired
	private SpellService spellService;

	@GetMapping(value = "/spell/{spellIdApi}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados do Spell")
	public ResponseEntity<?> getSpellByNome(
			@ApiParam(value = "Id de Spell valido. Ex.: 15647", required = true) @PathVariable String spellIdApi) {

		log.debug("REST request to get Spell information : {}", spellIdApi);

		return new ResponseEntity<>(spellService.getSpellByIdApi(spellIdApi), HttpStatus.OK);
	}

	@GetMapping(value = "/spells", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados de uma lista de Spells a partir de uma lista de ids da riot")
	public ResponseEntity<List<Spell>> getCampeoesByIdApi(
			@ApiParam(value = "Id de spells validos. Ex.: api/spells?ids=SummonerFlash,SummonerBoost,SummonerGhost...", required = true) @RequestParam List<String> ids) {

		log.debug("REST request to get Campeao[] information : {}");

		if (ids.isEmpty())
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(spellService.getSpells(ids), HttpStatus.OK);
	}

	@GetMapping(value = "/spell/{spellIdApi}/detail", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados do Spell detalhados")
	public ResponseEntity<SpellDto> getSpellDetailByIdApi(
			@ApiParam(value = "Id de spell valido. Ex.: 15647", required = true) @PathVariable String spellIdApi) {

		log.debug("REST request to get spell information : {}", spellIdApi);

		return new ResponseEntity<>(spellService.getSpellDetailByIdApi(spellIdApi), HttpStatus.OK);
	}

}