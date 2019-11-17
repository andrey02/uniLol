package com.pgpain.unilol.dto;

import java.util.List;

import com.pgpain.unilol.model.PartidaDoUsuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuncaoDto {
	
	private String funcao; //JUNGLE, MIDDLE, TOP (lane_type), DUO_SUPPORT, DUO_CARRY (role)
	private List<PartidaDoUsuario> partidasDoUsuario; //order by champion
	private Double taxaDeVitoria;
	private String nota;
}
