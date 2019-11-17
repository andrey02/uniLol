package com.pgpain.unilol.dto;

import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.pgpain.unilol.model.Campeao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampeaoCompletoDto {
	Campeao campeao;
	Champion campeaoDetails;
	
}
