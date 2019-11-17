package com.pgpain.unilol.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisionScoreDto extends ScoreDto {
	
	private Double controleDeObjetivo;
	private Double relacaoDeWard;
	private Double kdaAos15min;
	private Double conversaoDeAbates;
}
