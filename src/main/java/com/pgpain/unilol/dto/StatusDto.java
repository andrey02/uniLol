package com.pgpain.unilol.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusDto {
	
	private CombatScoreDto combate;
	private VisionScoreDto visao;
	private GoldScoreDto ouro;
	private Double mediaKda;
	private Integer totalPartidas;
	private Long tempoJogado;
	private List<FuncaoDto> funcao;
	

}
