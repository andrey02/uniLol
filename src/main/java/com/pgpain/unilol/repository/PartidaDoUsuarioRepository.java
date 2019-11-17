package com.pgpain.unilol.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pgpain.unilol.model.Campeao;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.model.PartidaDoUsuario;
import com.pgpain.unilol.model.Usuario;

@Repository
public interface PartidaDoUsuarioRepository extends JpaRepository<PartidaDoUsuario, Long> {

	
	List<PartidaDoUsuario> findTop20ByUsuario(Usuario usuario);// pegar as mais novas
	
	PartidaDoUsuario findTopByUsuarioOrderByIdPartidaDoUsuarioDesc(Usuario usuario);

	List<PartidaDoUsuario> findAllByPartida(Partida partida);
	
	@Query("Select u from PartidaDoUsuario u where usuario = ?1 order by idPartidaDoUsuario asc")
	List<PartidaDoUsuario> findAllByUsuarioByOrderByIdPartidaDoUsuarioAsc(Usuario usuario);//pegar as mais antigas
	
	Optional<PartidaDoUsuario> findByUsuarioAndPartida(Usuario usuario, Partida partida);
	
	Long countByUsuario(Usuario usuario);

	Optional<PartidaDoUsuario> findByPartidaAndParticipantId(Partida partida, Integer participantId);


	List<PartidaDoUsuario> findAllByUsuarioAndLaneTypeOrderByCampeao(Usuario usuario, String role);

	List<PartidaDoUsuario> findAllByUsuarioAndCampeao(Usuario usuario, Campeao campeao);

	List<PartidaDoUsuario> findAllByPartidaAndTime(Partida partida, String time);

	PartidaDoUsuario findTopByPartidaAndTimeNotAndLaneType(Partida partida, String time, String laneType);
	PartidaDoUsuario findTopByPartidaAndTimeNotAndRole(Partida partida, String time, String role);

	List<PartidaDoUsuario> findAllByUsuarioAndRoleOrderByCampeao(Usuario usuario, String string);
	
	@Query("SELECT u.partida from PartidaDoUsuario u where u.usuario = ?1")
	List<Partida> findPartidaByUsuario(Usuario usuario);

}
