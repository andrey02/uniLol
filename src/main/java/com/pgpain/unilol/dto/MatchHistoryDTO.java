package com.pgpain.unilol.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchHistoryDto {

	List<MatchDto> partidas;
	UsuarioDto usuario;
	String dataInicial;
	String dataFinal;
	Boolean updatable = false;

}
