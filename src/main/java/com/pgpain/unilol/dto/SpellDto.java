package com.pgpain.unilol.dto;

import com.merakianalytics.orianna.types.core.staticdata.SummonerSpell;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpellDto {

	String nome;
	String id;
	String caminhoImagem;
	SummonerSpell spell;

	public SpellDto(String nome, String id, String caminhoImagem) {
		super();
		this.nome = nome;
		this.id = id;
		this.caminhoImagem = caminhoImagem;
	}

	public SpellDto() {
	}
}
