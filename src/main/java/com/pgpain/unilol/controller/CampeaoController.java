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

import com.pgpain.unilol.dto.CampeaoCompletoDto;
import com.pgpain.unilol.model.Campeao;
import com.pgpain.unilol.service.CampeaoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@Api(value = "Operacoes referentes aos campeões do jogo")
public class CampeaoController {

	@Autowired
	private CampeaoService campeaoService;

	@GetMapping(value = "/campeao/{campeaoIdApi}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados do Campeao")
	public ResponseEntity<Campeao> getCampeaoByIdApi(
			@ApiParam(value = "Id de campeao valido. Ex.: 15647", required = true) @PathVariable Integer campeaoIdApi) {

		log.debug("REST request to get Campeao information : {}", campeaoIdApi);

		Campeao campeao = campeaoService.getCampeaoByIdApi(campeaoIdApi);
		if (campeao == null)
			return new ResponseEntity<>(new Campeao(), HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(campeao, HttpStatus.OK);
	}

	@GetMapping(value = "/campeoes", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados de uma lista de Campeoes a partir de uma lista de ids da riot")
	public ResponseEntity<List<Campeao>> getCampeoesByIdApi(
			@ApiParam(value = "Id de campeoes validos. Ex.: api/campeoes?ids=103,429,30...", required = true) @RequestParam(required = false) List<Integer> ids) {

		log.debug("REST request to get Campeao[] information : {}");

		if (ids.isEmpty())
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

		List<Campeao> campeoes = campeaoService.getCampeoes(ids);
		if (campeoes.isEmpty())
			return new ResponseEntity<>(campeoes, HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(campeoes, HttpStatus.OK);
	}

	@GetMapping(value = "/campeoes/get", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados de uma lista de Campeoes a partir de uma lista de nomes da riot")
	public ResponseEntity<List<Campeao>> getCampeoesByNome(
			@ApiParam(value = "Nome de campeoes validos. Ex.: api/campeoes/get?nomes=vladimir,kaisa,nocturne...", required = true) @RequestParam List<String> nomes) {

		log.debug("REST request to get Campeao[] information : {}");

		if (nomes.isEmpty())
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

		List<Campeao> campeoes = campeaoService.getCampeoesByNome(nomes);
		if (campeoes.isEmpty())
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);

		return new ResponseEntity<>(campeoes, HttpStatus.OK);
	}

	@GetMapping(value = "/campeoes/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados de uma lista de Campeoes a partir de uma lista de nomes da riot")
	public ResponseEntity<List<CampeaoCompletoDto>> listAllCampeoes() {

		log.debug("REST request to get All campeaoDTO : {}");

		List<CampeaoCompletoDto> campeoes = campeaoService.getAllCampeoes();

		if (campeoes.isEmpty())
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_GATEWAY);

		return new ResponseEntity<>(campeoes, HttpStatus.OK);
	}

}
