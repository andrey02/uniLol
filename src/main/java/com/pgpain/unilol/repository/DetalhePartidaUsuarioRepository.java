package com.pgpain.unilol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pgpain.unilol.model.DetalhePartidaUsuario;
import com.pgpain.unilol.model.PartidaDoUsuario;

@Repository
public interface DetalhePartidaUsuarioRepository extends JpaRepository<DetalhePartidaUsuario, Long> {

	@Query("select u from DetalhePartidaUsuario u where id_partida_do_usuario = ?1")
	DetalhePartidaUsuario findAllByIdPartidaDoUsuario(Long id);

	@Query("select SUM(u.danoCausado) from DetalhePartidaUsuario u where u.partidaDoUsuario in (?1)")
	Integer findTotalDanoCausadoPorTime(List<PartidaDoUsuario> partidasDoUsuariosTime);

	@Query("select SUM(u.abate) from DetalhePartidaUsuario u where u.partidaDoUsuario in (?1)")
	Integer findAbateTotalPorTime(List<PartidaDoUsuario> partidasDoUsuariosTime);
}
