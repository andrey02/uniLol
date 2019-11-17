package com.pgpain.unilol.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgpain.unilol.model.Partida;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {

	Optional<Partida> findByIdExterno(Long id);
}
