package com.pgpain.unilol.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgpain.unilol.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
	Optional<Item> findFirstByItemIdApi(int id);
}
