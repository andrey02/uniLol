package com.pgpain.unilol.service;

import java.util.List;

import com.merakianalytics.orianna.types.core.match.Participant;
import com.pgpain.unilol.model.ItemDaPartidaDoUsuario;
import com.pgpain.unilol.model.PartidaDoUsuario;

public interface ItemDaPartidaDoUsuarioService {
	List<ItemDaPartidaDoUsuario> saveAll(Participant participant, PartidaDoUsuario partidaDoUsuario);
}
