package com.pgpain.unilol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pgpain.unilol.model.ItemDaPartidaDoUsuario;

@Repository
public interface ItemDaPartidaDoUsuarioRepository extends JpaRepository<ItemDaPartidaDoUsuario, Long> {

	@Query("select u from ItemDaPartidaDoUsuario u where id_partida_do_usuario = ?1")
	List<ItemDaPartidaDoUsuario> findAllByIdPartidaDoUsuario(Long idPartidaDoUsuario);

}
