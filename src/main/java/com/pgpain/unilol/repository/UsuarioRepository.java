package com.pgpain.unilol.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgpain.unilol.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Usuario findByNome(String nome);
	Optional<Usuario> findTopByAccountId(String accountId);
	List<Usuario> findAllByStatus(byte status);
	
	

}
