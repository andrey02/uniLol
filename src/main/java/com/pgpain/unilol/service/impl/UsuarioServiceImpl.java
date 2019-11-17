package com.pgpain.unilol.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import com.pgpain.unilol.dto.ParticipanteDto;
import com.pgpain.unilol.dto.UsuarioDto;
import com.pgpain.unilol.model.Usuario;
import com.pgpain.unilol.repository.UsuarioRepository;
import com.pgpain.unilol.service.UsuarioService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsuarioServiceImpl implements UsuarioService {

	UsuarioRepository usuarioRepository;

	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public Usuario getUsuarioById(Long id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		if (usuario.isPresent())
			return usuario.get();
		return null;
	}

	@Override
	public void setUsuario(Usuario usuario) {
		if (usuario != null)
			usuarioRepository.save(usuario);
	}

	@Override
	public Usuario getUsuarioByNome(String id) {
		return usuarioRepository.findByNome(id);
	}

	@Override
	public Usuario saveUsuario(ParticipanteDto participanteDTO) {
		Optional<Usuario> findByNome = usuarioRepository.findTopByAccountId(participanteDTO.getAccountId());
		Summoner summoner = Summoner.named(participanteDTO.getNome())
				.withRegion(Region.valueOf(participanteDTO.getRegiao())).get();

		if (findByNome.isPresent()) {
			return findByNome.get();
		} else {
			Usuario usuario = new Usuario();
			usuario.setAccountId(participanteDTO.getAccountId());
			usuario.setRegiao(participanteDTO.getRegiao());
			usuario.setNome(participanteDTO.getNome());
			if (summoner.getProfileIcon() != null)
				usuario.setIconeUrl(summoner.getProfileIcon().getImage().getURL());
			// para nao fazer update das partidas - scheduler
			usuario.setStatus((byte) 0);

			return usuarioRepository.save(usuario);
		}
	}

	@Override
	public String eUsuarioValido(String nome, String regiao) {
		// checar se ele existe
		Summoner summoner = Summoner.named(nome).withRegion(Region.valueOf(regiao)).get();
		if (summoner == null || "".equals(nome) || nome == null) {
			return null;
		}
		return summoner.getAccountId();

	}

	@Override
	public Usuario saveUsuario(String nome, String regiao, int status) {
		Usuario usuario = new Usuario();
		usuario.setAccountId(Summoner.named(nome).withRegion(Region.valueOf(regiao)).get().getAccountId());
		usuario.setNome(nome);
		usuario.setIconeUrl(
				Summoner.named(nome).withRegion(Region.valueOf(regiao)).get().getProfileIcon().getImage().getURL());
		usuario.setStatus((byte) status);
		usuario.setRegiao(regiao);
		usuarioRepository.save(usuario);
		return usuarioRepository.save(usuario);
	}

	@Override
	public Usuario getUsuarioByAccountId(String id) {
		Optional<Usuario> usuarioOp = usuarioRepository.findTopByAccountId(id);
		if (!usuarioOp.isPresent()) {
			log.error("usuario não encontrada com o accountId: " + id);
			return null;
		}
		return usuarioOp.get();

	}

	@Override
	public List<UsuarioDto> list() {
		List<UsuarioDto> usuarioDtos = new ArrayList<>();
		try {
			List<Usuario> usuarios = usuarioRepository.findAllByStatus((byte) 1);

			usuarios.forEach(usuario -> {
				UsuarioDto novo = new UsuarioDto();
				novo.setAccountId(usuario.getAccountId());
				novo.setIconUrl(usuario.getIconeUrl());
				novo.setNome(usuario.getNome());
				usuarioDtos.add(novo);
			});

		} catch (Exception e) {
			log.info("Nao foi possível listar todos os usuarios");
		}
		return usuarioDtos;
	}

}
