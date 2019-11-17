package com.pgpain.unilol.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Timeline implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idTimeline;

	@ManyToOne
	@JoinColumn(name = "idPartidaDoUsuario", referencedColumnName = "idPartidaDoUsuario")
	private PartidaDoUsuario partidaDoUsuario;

	private Long tempo;
	private Long ouroAdquirido;
	private Long level;
	private Long ouro;
	private Long qtdCreep;
	private Long experiencia;
	private Long qtdMonstroNeutro;
	private Long posicaoX;
	private Long posicaoY;

}
