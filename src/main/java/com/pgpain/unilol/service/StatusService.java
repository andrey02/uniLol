package com.pgpain.unilol.service;

import com.pgpain.unilol.dto.StatusDto;
import com.pgpain.unilol.model.Campeao;
import com.pgpain.unilol.model.Usuario;

public interface StatusService {
	
	StatusDto getStatusByUsuario(Usuario usuario);
	StatusDto getStatusByUsuarioAndCampeao(Usuario usuario, Campeao campeao);
}
