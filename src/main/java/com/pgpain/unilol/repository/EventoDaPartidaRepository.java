package com.pgpain.unilol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pgpain.unilol.model.EventoDaPartida;
import com.pgpain.unilol.model.Partida;

@Repository
//index por partida ou por tempo ou pelos dois ?
public interface EventoDaPartidaRepository extends JpaRepository<EventoDaPartida, Long> {

	@Query("SELECT u FROM EventoDaPartida u WHERE (partida=?1) AND (killerId=?2 or assistentes LIKE '%?2,%' or victimId=?2 or participantId=?2)")
	List<EventoDaPartida> findEventoByParticipantIdAndPartida(Partida partida, Integer participantId);

	List<EventoDaPartida> findAllByPartidaAndTime(Partida partida, String time);

	@Query("SELECT u FROM EventoDaPartida u WHERE (partida=?1) AND participantId IN (?2) AND time IS NULL")
	List<EventoDaPartida> findAllByParticipantIdIn(Partida partida, List<String> participantes);

	List<EventoDaPartida> findAllByPartidaAndTipo(Partida partida, String tipo);

	// Controle de abates
	@Query("SELECT count(u) FROM EventoDaPartida u WHERE u.partida=?1 AND (u.participantId=?2 or u.assistentes LIKE '%?2,%' or killerId=?2) AND (u.tipo='BUILDING_KILL' or u.tipo='ELITE_MONSTER_KILL')")
	Double findObjectivesByPartidaAndParticipantId(Partida partida, Integer participantId);

	@Query("SELECT count(u) FROM EventoDaPartida u WHERE u.partida=?1 AND (u.tipo='BUILDING_KILL' or u.tipo='ELITE_MONSTER_KILL') AND (u.participantId in ?2 or killerId in ?2)")
	Double findObjectivesByPartida(Partida partida, List<Integer> time);

	// Relacao de Ward
	@Query("SELECT count(u) FROM EventoDaPartida u WHERE u.partida=?1 AND u.tipo='WARD_PLACED' AND participantId in (?2)")
	Double findTotalWardPlacedByPartida(Partida partida, List<Integer> time);

	// Kda aos 15 min
	@Query("SELECT count(u) FROM EventoDaPartida u WHERE (partida=?1) AND (killerId=?2) AND tempo <= 900000")
	Double findAbateByPartidaEParticipantId(Partida partida, int participantId);

	@Query("SELECT count(u) FROM EventoDaPartida u WHERE (partida=?1) AND (assistentes LIKE '%?2,%') AND tempo <= 900000")
	Double findAssistenciaByPartidaEParticipantId(Partida partida, int participantId);

	@Query("SELECT count(u) FROM EventoDaPartida u WHERE (partida=?1) AND (victimId=?2) AND tempo <= 900000")
	Double findMorteByPartidaEParticipantId(Partida partida, int participantId);

}
