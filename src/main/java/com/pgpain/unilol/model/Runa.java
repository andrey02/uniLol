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
 * The persistent class for the runa database table.
 * 
 */
@Entity
@NamedQuery(name = "Runa.findAll", query = "SELECT r FROM Runa r")
public class Runa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idRuna;

	private int runaIdApi;

	private String nome;
	private String path;

	private String caminhoImagem;

	@JsonIgnore
	@OneToMany(mappedBy = "runa")
	private List<ItemDaPartidaDoUsuario> itemDaPartidaDoUsuarios;

	public Long getIdRuna() {
		return this.idRuna;
	}

	public void setIdRuna(Long idRuna) {
		this.idRuna = idRuna;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<ItemDaPartidaDoUsuario> getItemDaPartidaDoUsuarios() {
		return itemDaPartidaDoUsuarios;
	}

	public void setItemDaPartidaDoUsuarios(List<ItemDaPartidaDoUsuario> itemDaPartidaDoUsuarios) {
		this.itemDaPartidaDoUsuarios = itemDaPartidaDoUsuarios;
	}

	public int getRunaIdApi() {
		return runaIdApi;
	}

	public void setRunaIdApi(int runaIdApi) {
		this.runaIdApi = runaIdApi;
	}

}