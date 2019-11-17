package com.pgpain.unilol.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipanteDto {

	String nome;
	String team;
	CampeaoDto campeaoDTO;
	String accountId;
	Integer participantId;
	Integer danoCausado;
	String regiao;
	String laneType;
	String role;
}
