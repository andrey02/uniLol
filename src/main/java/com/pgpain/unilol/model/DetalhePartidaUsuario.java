package com.pgpain.unilol.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
//@Table(indexes = {@Index(columnList = ("idPartidaDoUsuario"),unique = true) })
public class DetalhePartidaUsuario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idDetalhePartidaUsuario;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idPartidaDoUsuario")
	private PartidaDoUsuario partidaDoUsuario;

	private Integer goldTotal;
	private int morte;
	private int abate;
	private int assistencia;
	private int nivelCampeao;
	private String resultado;
	private Integer danoCausado;
	private Integer creepScore;
	private Integer combatScore;
	private Integer visionScore;
	private Integer score;
	private Integer objectiveScore;
	private Integer teamObjectives;
	private Integer wardsKilled;
	private Integer wardsPlaced;
	private Long crowdControlDealt;
	private Integer damageHealed;
	private Integer damageMitigated;

}
