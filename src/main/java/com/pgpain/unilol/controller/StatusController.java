package com.pgpain.unilol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgpain.unilol.dto.StatusDto;
import com.pgpain.unilol.model.Campeao;
import com.pgpain.unilol.model.Usuario;
import com.pgpain.unilol.service.CampeaoService;
import com.pgpain.unilol.service.StatusService;
import com.pgpain.unilol.service.UsuarioService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@Api(value = "Operacoes referentes as analises estatisticas da aplicacao")
public class StatusController {
	
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private StatusService statusService;
	@Autowired
	private CampeaoService campeaoService;

	@GetMapping(value = "/status/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados do status do usuario das partidas armazenadas em banco")
	public ResponseEntity<StatusDto> getUsuarioStatus(
			@ApiParam(value = "Id de usuario valido. Ex.: JJy-ekQK04Hd-2U8D9XZIbhjiwD8TOVdxZS1OU-JPEt3KOQ", required = true) @PathVariable String accountId) {

		log.debug("REST request to get Usuario information : {}", accountId);
		
		Usuario usuario = usuarioService.getUsuarioByAccountId(accountId);
		if (usuario == null)
			return new ResponseEntity<>(new StatusDto(), HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(statusService.getStatusByUsuario(usuario), HttpStatus.OK);
	}
	
	@GetMapping(value = "/status/{accountId}/{campeaoIdApi}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados do status do campeao escolhido pelo usuario levando em conta as partidas armazenadas em banco")
	public ResponseEntity<StatusDto> getCampeaoStatusByUsuario(
			@ApiParam(value = "Id de usuario valido. Ex.: JJy-ekQK04Hd-2U8D9XZIbhjiwD8TOVdxZS1OU-JPEt3KOQ", required = true) @PathVariable String accountId,
			@ApiParam(value = "Id de campeao valido. Ex.: 123,684,343", required = true) @PathVariable Integer campeaoIdApi) {

		log.debug("REST request to get Usuario and campeao status : {}", accountId);
		
		Usuario usuario = usuarioService.getUsuarioByAccountId(accountId);
		if (usuario == null)
			return new ResponseEntity<>(new StatusDto(), HttpStatus.BAD_REQUEST);
		
		Campeao campeao = campeaoService.getCampeaoByIdApi(campeaoIdApi);
		if(campeao == null)
			return new ResponseEntity<>(new StatusDto(), HttpStatus.BAD_REQUEST);	

		return new ResponseEntity<>(statusService.getStatusByUsuarioAndCampeao(usuario, campeao), HttpStatus.OK);
	}
}
