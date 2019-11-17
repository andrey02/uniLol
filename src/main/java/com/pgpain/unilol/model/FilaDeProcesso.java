package com.pgpain.unilol.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
//@Table(indexes = {@Index(columnList = ("idPartida"),unique = false) })
public class FilaDeProcesso implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idFilaDeProcesso;

	// bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "idPartida")
	private Partida partida;

	// PROCESSANDO, SUCESSO ou FALHA.
	@Column(nullable = false)
	private String status;

	// EVENTO(partida), TIMELINE(usuario e partida) e DETALHE(usuario e partida).
	@Column(nullable = false)
	private String tipo;

	@Temporal(TemporalType.TIMESTAMP)
	private Date horaRegistro;

}
