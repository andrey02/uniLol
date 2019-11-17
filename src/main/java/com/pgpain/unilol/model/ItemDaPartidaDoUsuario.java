package com.pgpain.unilol.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the item_da_partida_do_usuario database table.
 * 
 */
@Entity
@Table(name = "item_da_partida_do_usuario")
@NamedQuery(name = "ItemDaPartidaDoUsuario.findAll", query = "SELECT i FROM ItemDaPartidaDoUsuario i")
public class ItemDaPartidaDoUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idItemPartida;

	private String nome;

	@ManyToOne
	@JoinColumn(name = "idItem", referencedColumnName = "idItem")
	private Item item;

	@ManyToOne
	@JoinColumn(name = "idRuna", referencedColumnName = "idRuna")
	private Runa runa;

	@ManyToOne
	@JoinColumn(name = "idSpell", referencedColumnName = "idSpell")
	private Spell feitico;

	// bi-directional many-to-one association to PartidaDoUsuario
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "idPartidaDoUsuario", referencedColumnName = "idPartidaDoUsuario")
	private PartidaDoUsuario partidaDoUsuario;

	public Long getIdItemPartida() {
		return idItemPartida;
	}

	public void setIdItemPartida(Long idItemPartida) {
		this.idItemPartida = idItemPartida;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public PartidaDoUsuario getPartidaDoUsuario() {
		return this.partidaDoUsuario;
	}

	public void setPartidaDoUsuario(PartidaDoUsuario partidaDoUsuario) {
		this.partidaDoUsuario = partidaDoUsuario;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Runa getRuna() {
		return runa;
	}

	public void setRuna(Runa runa) {
		this.runa = runa;
	}

	public Spell getFeitico() {
		return feitico;
	}

	public void setFeitico(Spell feitico) {
		this.feitico = feitico;
	}

}