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

import com.pgpain.unilol.dto.ItemDto;
import com.pgpain.unilol.dto.RunaDto;
import com.pgpain.unilol.model.Runa;
import com.pgpain.unilol.service.RunaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@Api(value = "Operacoes referentes as runas do jogo")
public class RunaController {

	@Autowired
	private RunaService runaService;

	@GetMapping(value = "/runa/{runaIdApi}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados do Runa")
	public ResponseEntity<?> getRunaByNome(
			@ApiParam(value = "Id de Runa valido. Ex.: 15647", required = true) @PathVariable Integer runaIdApi) {

		log.debug("REST request to get Runa information : {}", runaIdApi);

		return new ResponseEntity<>(runaService.getRunaByIdApi(runaIdApi), HttpStatus.OK);
	}

	@GetMapping(value = "/runas", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados de uma lista de Runas a partir de uma lista de ids da riot")
	public ResponseEntity<List<Runa>> getRunasByIdApi(
			@ApiParam(value = "Id de runas validos. Ex.: api/runas?ids=103,429,30...", required = true) @RequestParam List<Integer> ids) {

		log.debug("REST request to get Campeao[] information : {}");

		if (ids.isEmpty())
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(runaService.getRunas(ids), HttpStatus.OK);
	}
	
	@GetMapping(value = "/runa/{runaIdApi}/detail", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados da Runa detalhados")
	public ResponseEntity<RunaDto> getRunaDetailByIdApi(
			@ApiParam(value = "Id de runa valido. Ex.: 15647", required = true) @PathVariable Integer runaIdApi) {

		log.debug("REST request to get runa information : {}", runaIdApi);

		return new ResponseEntity<>(runaService.getRunaDetailByIdApi(runaIdApi), HttpStatus.OK);
	}

}