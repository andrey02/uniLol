package com.pgpain.unilol.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pgpain.unilol.model.FilaDeProcesso;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.model.Usuario;

@Repository
public interface FilaDeProcessoRepository extends JpaRepository<FilaDeProcesso, Long> {

	@Query("select a from FilaDeProcesso a where a.horaRegistro <= :horaRegistro")
	List<FilaDeProcesso> findAllWithHoraRegistroBefore(@Param("horaRegistro") Date horaRegistro);

	FilaDeProcesso findByUsuarioAndPartidaAndTipoAndStatus(Usuario usuario, Partida partida, String tipo,
			String status);

	List<FilaDeProcesso> findByUsuarioAndPartidaAndTipo(Usuario usuario, Partida partida, String tipo);

	FilaDeProcesso findByPartidaAndTipoAndStatus(Partida partida, String tipo, String status);

	// example date format  new SimpleDateFormat("yyyy-MM-dd
	// HH:mm").parse("2017-12-15 10:00")
	// Date d = new Date(); creates a .NOW DateTime

	List<FilaDeProcesso> findAllByUsuarioAndStatus(Usuario usuario, String status);

	@Query("select u from FilaDeProcesso u where u.horaRegistro >= :horaRegistro")
	List<FilaDeProcesso> findAllWithHoraRegistroAfter(@Param("horaRegistro") Date horaRegistro);

}
