package com.pgpain.unilol.service;

import java.util.List;

import com.pgpain.unilol.dto.PosicaoDto;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.model.PartidaDoUsuario;
import com.pgpain.unilol.model.Usuario;

public interface PartidaDoUsuarioService {

	PartidaDoUsuario createPartidaDoUsuario(Usuario usuario, Partida partida);
	PartidaDoUsuario getPartidaDoUsuarioById(Long id);
	List<PartidaDoUsuario> getPartidaDoUsuarioByUsuario(Usuario usuario);
	PartidaDoUsuario getByUsuarioAndPartida(Usuario usuario,Partida partida);
	List<PosicaoDto> getPosicaoDoUsuario(PartidaDoUsuario partidaDoUsuario);
	
	void removerPartidasDoUsuario(Long removeTotalPartida, Usuario usuario);
	void adicionarPartidasDoUsuario(Long adicionaTotalPartida, Usuario usuario);
	boolean atualizarPartidasDosUsuarios(Usuario usuario);
	PartidaDoUsuario getPartidaDoUsuarioByPartidaAndParticipantId(Partida partida, Integer participantId);
	List<PartidaDoUsuario> getPartidaDoUsuarioByPartida(Partida partida);
}
