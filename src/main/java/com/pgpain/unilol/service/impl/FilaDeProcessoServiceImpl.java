package com.pgpain.unilol.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgpain.unilol.model.FilaDeProcesso;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.model.Usuario;
import com.pgpain.unilol.repository.FilaDeProcessoRepository;
import com.pgpain.unilol.repository.PartidaDoUsuarioRepository;
import com.pgpain.unilol.service.FilaDeProcessoService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FilaDeProcessoServiceImpl implements FilaDeProcessoService {

	@Autowired
	FilaDeProcessoRepository filaDeProcessoRepository;
	@Autowired
	PartidaDoUsuarioRepository partidaDoUsuarioRepository;

	@Override
	public List<FilaDeProcesso> getFilaDeProcessoByUsuarioAndPartida(Usuario usuario, Partida partida) {

		return new ArrayList<>();
	}

	@Override
	public boolean isUsuarioInProcess(Usuario usuario) {
		List<Partida> partidasDoUsuario = partidaDoUsuarioRepository.findPartidaByUsuario(usuario);

		Date date = new Date();
		date.setTime(date.getTime() - 30000); // pegar data atual menos 5 segundos
		List<FilaDeProcesso> processoAnteriores = filaDeProcessoRepository.findAllWithHoraRegistroAfter(date);
		if (!processoAnteriores.isEmpty()) {
			for (FilaDeProcesso processo : processoAnteriores) {
				if(processo.getUsuario() != null) {
					if (usuario.getNome().equals(processo.getUsuario().getNome())) {
						return true;
					}
				}
				
				if (partidasDoUsuario.contains(processo.getPartida())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isPartidaInProcess(Partida usuario) {
		return false;
	}

	@Override
	public List<FilaDeProcesso> getFilaDeProcessoByPartidaAndTipo(Partida partida, String tipo) {
		return new ArrayList<>();
	}

	@Override
	public FilaDeProcesso saveFilaDeProcesso(Usuario usuario, Partida partida, String status, String tipo) {
		FilaDeProcesso filaDeProcesso = new FilaDeProcesso();

		if (usuario != null)
			filaDeProcesso.setUsuario(usuario);

		if (partida.getIdPartida() == null)
			return null;

		filaDeProcesso.setPartida(partida);
		filaDeProcesso.setHoraRegistro(new Date());
		filaDeProcesso.setStatus(status);
		filaDeProcesso.setTipo(tipo);

		return filaDeProcessoRepository.save(filaDeProcesso);
	}

	@Override
	public FilaDeProcesso getFilaDeProcessoByUsuarioAndPartidaAndTipoAndStatus(Usuario usuario, Partida partida,
			String tipo, String status) {
		log.info("retornando status da fila de processo");

		if (usuario == null)
			return filaDeProcessoRepository.findByPartidaAndTipoAndStatus(partida, tipo, status);

		return filaDeProcessoRepository.findByUsuarioAndPartidaAndTipoAndStatus(usuario, partida, tipo, status);
	}

}
