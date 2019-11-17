package com.pgpain.unilol.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the item database table.
 * 
 */
@Entity
@NamedQuery(name = "Item.findAll", query = "SELECT i FROM Item i")
public class Item implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idItem;

	private int itemIdApi;

	private String caminhoImagem;

	private String nome;

	@JsonIgnore
	@OneToMany(mappedBy = "item")
	private List<ItemDaPartidaDoUsuario> itemDaPartidaDoUsuarios;

	// bi-directional many-to-one association to PartidaDoUsuario
	@JsonIgnore
	@OneToMany(mappedBy = "item")
	private List<EventoDaPartida> eventoDaPartidas;

	public Long getIdItem() {
		return this.idItem;
	}

	public void setIdItem(Long idItem) {
		this.idItem = idItem;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCaminhoImagem() {
		return caminhoImagem;
	}

	public void setCaminhoImagem(String caminhoImagem) {
		this.caminhoImagem = caminhoImagem;
	}

	public int getItemIdapi() {
		return itemIdApi;
	}

	public void setItemIdapi(int itemIdapi) {
		this.itemIdApi = itemIdapi;
	}

}