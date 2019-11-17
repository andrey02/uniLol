package com.pgpain.unilol.dto;

import com.merakianalytics.orianna.types.core.staticdata.ReforgedRune;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RunaDto {
	String name;
	int runaIdApi;
	String caminhoImagem;
	ReforgedRune runa;

	public RunaDto(String name, int runaIdApi,String caminhoImagem) {
		super();
		this.name = name;
		this.runaIdApi = runaIdApi;
		this.caminhoImagem=caminhoImagem;
	}

	public RunaDto() {
	}
}
