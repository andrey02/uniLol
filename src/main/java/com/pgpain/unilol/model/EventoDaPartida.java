package com.pgpain.unilol.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class EventoDaPartida implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEventoPartida;
	private String tipo;
	private int killerId;
	private int victimId;
	private Long posicaoX;
	private Long posicaoY;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "idPartida", referencedColumnName = "idPartida")
	private Partida partida;

	private Long tempo;
	private String assistentes;
	private int participantId;

	@ManyToOne
	@JoinColumn(name = "idItem", referencedColumnName = "idItem")
	private Item item;
	private String time;
	private int skill;

	private String wardType;
	private String turretType;
	private String laneType;
	private String buildingType;
	private String monsterSubType;

}
