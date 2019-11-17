package com.pgpain.unilol.dto;

import java.util.List;

import com.pgpain.unilol.model.DetalhePartidaUsuario;
import com.pgpain.unilol.model.ItemDaPartidaDoUsuario;
import com.pgpain.unilol.model.PartidaDoUsuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalheDaPartidaDoUsuarioDto {
	PartidaDoUsuario partidaDoUsuario;
	DetalhePartidaUsuario detalhePartidaUsuario;
	List<ItemDaPartidaDoUsuario> itemsDaPartidaDoUsuario;
}
