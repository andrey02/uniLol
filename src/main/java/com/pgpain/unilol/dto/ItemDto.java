package com.pgpain.unilol.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {

	String name;
	int itemIdApi;
	String caminhoImagem;
	com.merakianalytics.orianna.types.core.staticdata.Item item;

	public ItemDto(String name, int itemIdApi, String caminhoImagem) {
		this.name = name;
		this.itemIdApi = itemIdApi;
		this.caminhoImagem = caminhoImagem;
	}

	public ItemDto() {
	}

}
