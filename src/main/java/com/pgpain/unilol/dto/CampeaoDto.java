package com.pgpain.unilol.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampeaoDto {
	
	String nome;
	Integer campeaoIdApi;
	String caminhoImagem;
	
	public CampeaoDto(String nome, Integer campeaoIdApi,String caminhoImagem) {
		super();
		this.nome = nome;
		this.campeaoIdApi = campeaoIdApi;
		this.caminhoImagem = caminhoImagem;
	}
	
	public CampeaoDto() {}
	
}
