package com.pgpain.unilol.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgpain.unilol.model.Spell;

@Repository
public interface SpellRepository extends JpaRepository<Spell, Long> {
	Optional<Spell> findTopBySpellIdApi(String id);
}
