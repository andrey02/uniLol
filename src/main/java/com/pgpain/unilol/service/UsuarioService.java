package com.pgpain.unilol.service;

import java.util.List;

import com.pgpain.unilol.dto.ParticipanteDto;
import com.pgpain.unilol.dto.UsuarioDto;
import com.pgpain.unilol.model.Usuario;

public interface UsuarioService {
	
	Usuario	getUsuarioById(Long id);
	void setUsuario(Usuario usuario);
	Usuario	getUsuarioByNome(String id);
	Usuario saveUsuario(ParticipanteDto participanteDTO);
	Usuario saveUsuario(String nome,String regiao,int status);
	String eUsuarioValido(String nome,String regiao); //devolve account id
	Usuario getUsuarioByAccountId(String id);
	List<UsuarioDto> list();
}
