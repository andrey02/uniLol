package com.pgpain.unilol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pgpain.unilol.model.PartidaDoUsuario;
import com.pgpain.unilol.model.Timeline;

@Repository
public interface TimelineRepository extends JpaRepository<Timeline, Long> {

	@Query("select u.tempo from Timeline u where u.partidaDoUsuario = :param")
	List<Long> findTempoByPartidaDoUsuario(@Param("param") PartidaDoUsuario partidaDoUsuario);

	Timeline findByPartidaDoUsuarioAndTempo(PartidaDoUsuario partidaDoUsuario, Long tempo);

	@Query("select SUM(u.ouroAdquirido) from Timeline u where u.partidaDoUsuario = :param AND u.tempo <= 900000")
	Integer findOuroAos15(@Param("param") PartidaDoUsuario partidaDoUsuario);

	@Query("select SUM(u.qtdCreep) from Timeline u where u.partidaDoUsuario = :param AND u.tempo <= 900000")
	Integer findCsAos15(@Param("param")PartidaDoUsuario partidaDoAdversario);

}
