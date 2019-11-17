package com.pgpain.unilol.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchDto {

	CampeaoDto campeaoDTO;
	Integer morte;
	Integer abate;
	Integer asistencia;
	Integer goldTotal;
	String resultado;
	Long duracaoPartida;
	Integer levelCampeao;
	String mapa;
	Long matchId;
	String linguagem;
	Long participantId;
	List<ItemDto> build;
	List<RunaDto> runas;
	List<SpellDto> spells;
	List<ParticipanteDto> participants;
	String creationTime;
	String laneType;
	String role;

}
