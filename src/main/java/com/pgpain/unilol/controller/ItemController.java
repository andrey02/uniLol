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
import com.pgpain.unilol.model.Item;
import com.pgpain.unilol.service.ItemService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@Api(value = "Operacoes referentes aos itens do jogo")
public class ItemController {

	@Autowired
	private ItemService itemService;

	@GetMapping(value = "/item/{itemIdApi}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados do Item")
	public ResponseEntity<Item> getItemByNome(
			@ApiParam(value = "Id de item valido. Ex.: 15647", required = true) @PathVariable Integer itemIdApi) {

		log.debug("REST request to get item information : {}", itemIdApi);

		return new ResponseEntity<>(itemService.getItemByIdApi(itemIdApi), HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/items", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados de uma lista de Items a partir de uma lista de ids da riot")
	public ResponseEntity<List<Item>> getItemsByIdApi(
			@ApiParam(value = "Id de items validos. Ex.: api/items?ids=103,429,30...", required = true) @RequestParam List<Integer> ids) {

		log.debug("REST request to get Campeao[] information : {}");

		if (ids.isEmpty())
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(itemService.getItems(ids), HttpStatus.OK);
	}
	
	@GetMapping(value = "/item/{itemIdApi}/detail", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna dados do Item detalhados")
	public ResponseEntity<ItemDto> getItemDetailByIdApi(
			@ApiParam(value = "Id de item valido. Ex.: 15647", required = true) @PathVariable Integer itemIdApi) {

		log.debug("REST request to get item information : {}", itemIdApi);

		return new ResponseEntity<>(itemService.getItemDetailByIdApi(itemIdApi), HttpStatus.OK);
	}
	
	
	
}