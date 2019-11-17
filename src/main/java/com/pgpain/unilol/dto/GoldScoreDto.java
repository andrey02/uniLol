package com.pgpain.unilol.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoldScoreDto extends ScoreDto  {
	
	private Double danoPorOuro;
	private Double vantagemDeOuro;
	private Double vantagemCsAos15;
	private Double csPorMin;

}
