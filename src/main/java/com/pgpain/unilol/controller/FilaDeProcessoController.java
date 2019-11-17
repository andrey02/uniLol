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

import com.pgpain.unilol.model.Usuario;
import com.pgpain.unilol.service.FilaDeProcessoService;
import com.pgpain.unilol.service.UsuarioService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@Api(value = "Operações referentes a fila de processos da aplicação")
public class FilaDeProcessoController {
	
	
	@Autowired
	private FilaDeProcessoService filaDeProcessoService;
	@Autowired
	private UsuarioService usuarioService;
	
	
	@GetMapping(value = "/processo/usuario/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna se usuario está em processo ou não.")
	public ResponseEntity<String> isUsuarioInProcess(
			@ApiParam(value = "Id único do usuario na API da Riot. Ex.: 156dsjeudj47saa", required = true) @PathVariable String accountId) {
		
		log.info("Indicar se usuario está em processo: " + accountId);
		
		Usuario usuario = usuarioService.getUsuarioByAccountId(accountId);
		if (usuario == null)
			return new ResponseEntity<>("usuario inexistente", HttpStatus.BAD_REQUEST);
		
		if (filaDeProcessoService.isUsuarioInProcess(usuario))
			return new ResponseEntity<>("{\"status\":true}", HttpStatus.OK);
		
		return new ResponseEntity<>("{\"status\":false}", HttpStatus.OK);
	}
	
	

}
