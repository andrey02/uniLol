package com.pgpain.unilol.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgpain.unilol.model.Runa;

@Repository
public interface RunaRepository extends JpaRepository<Runa, Long> {
	Optional<Runa> findFirstByRunaIdApi(int id);
}
