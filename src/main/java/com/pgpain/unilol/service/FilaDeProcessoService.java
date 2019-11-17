package com.pgpain.unilol.service;

import java.util.List;

import com.pgpain.unilol.model.FilaDeProcesso;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.model.Usuario;

public interface FilaDeProcessoService {
	
	//Saber status da timeline e dos detalhes da partida do usuário
	List<FilaDeProcesso> getFilaDeProcessoByUsuarioAndPartida(Usuario usuario, Partida partida);
	FilaDeProcesso getFilaDeProcessoByUsuarioAndPartidaAndTipoAndStatus(Usuario usuario, Partida partida,String tipo,String status);
	//se o usuario ou está processando alguma coisa
	boolean isUsuarioInProcess(Usuario usuario);
	boolean isPartidaInProcess(Partida usuario);
	// saber status do EVENTO da PARTIDA
	List<FilaDeProcesso> getFilaDeProcessoByPartidaAndTipo(Partida partida, String tipo);
	//criar
	FilaDeProcesso saveFilaDeProcesso(Usuario usuario, Partida partida,String status,String tipo);
	
}
