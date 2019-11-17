package com.pgpain.unilol.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pgpain.unilol.model.Usuario;
import com.pgpain.unilol.repository.UsuarioRepository;
import com.pgpain.unilol.service.PartidaDoUsuarioService;

import lombok.extern.slf4j.Slf4j;

@Component
@EnableScheduling
@Slf4j
public class Agendador {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	PartidaDoUsuarioService partidaDoUsuarioService;

	@Scheduled(cron = "${cron.expression.1}")
	public void atualizarPartidasDosUsuarios() { 

		try {
			// pegar usuario status = 1
			List<Usuario> usuariosAtivos = usuarioRepository.findAllByStatus((byte) 1);

			if (!usuariosAtivos.isEmpty()) {
				usuariosAtivos.forEach(usuario -> partidaDoUsuarioService.atualizarPartidasDosUsuarios(usuario));
			}
		} catch (Exception e) {
			log.info("Nao foi possível agendar partidas");
		}

	}

}
