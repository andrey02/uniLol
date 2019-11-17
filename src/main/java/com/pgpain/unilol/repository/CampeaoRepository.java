package com.pgpain.unilol.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgpain.unilol.model.Campeao;

@Repository
public interface CampeaoRepository extends JpaRepository<Campeao, Long> {

	Optional<Campeao> findFirstByCampeaoIdApi(Integer id);

	Optional<Campeao> findByNome(String nome);

}
