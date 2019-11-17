package com.pgpain.unilol.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CombatScoreDto extends ScoreDto {

	private Double participacaoAbates;
	private Double danoCompartilhado;
	private Double danoPorMorte;
	private Double pontuacaoDeUtilidade;
	
}
