package com.pgpain.unilol.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the spell database table.
 * 
 */
@Entity
public class Spell implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idSpell;

	private String spellIdApi;

	private String nome;

	private String caminhoImagem;

	@JsonIgnore
	@OneToMany(mappedBy = "runa")
	private List<ItemDaPartidaDoUsuario> itemDaPartidaDoUsuarios;

	public String getNome() {
		return nome;
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

	public Long getIdSpell() {
		return idSpell;
	}

	public void setIdSpell(Long idSpell) {
		this.idSpell = idSpell;
	}

	public String getSpellIdApi() {
		return spellIdApi;
	}

	public void setSpellIdApi(String spellIdApi) {
		this.spellIdApi = spellIdApi;
	}

	public List<ItemDaPartidaDoUsuario> getItemDaPartidaDoUsuarios() {
		return itemDaPartidaDoUsuarios;
	}

	public void setItemDaPartidaDoUsuarios(List<ItemDaPartidaDoUsuario> itemDaPartidaDoUsuarios) {
		this.itemDaPartidaDoUsuarios = itemDaPartidaDoUsuarios;
	}

}