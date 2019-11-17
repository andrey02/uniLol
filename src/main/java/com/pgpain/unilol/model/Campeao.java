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
 * The persistent class for the campeao database table.
 * 
 */
@Entity
@NamedQuery(name = "Campeao.findAll", query = "SELECT c FROM Campeao c")
public class Campeao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCampeao;

	private Integer campeaoIdApi;

	private String nome;

	private String caminhoImagem;

	// bi-directional many-to-one association to PartidaDoUsuario
	@JsonIgnore
	@OneToMany(mappedBy = "campeao")
	private List<PartidaDoUsuario> partidaDoUsuarios;

	public Long getIdCampeao() {
		return this.idCampeao;
	}

	public void setIdCampeao(Long idCampeao) {
		this.idCampeao = idCampeao;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<PartidaDoUsuario> getPartidaDoUsuarios() {
		return this.partidaDoUsuarios;
	}

	public void setPartidaDoUsuarios(List<PartidaDoUsuario> partidaDoUsuarios) {
		this.partidaDoUsuarios = partidaDoUsuarios;
	}

	public PartidaDoUsuario addPartidaDoUsuario(PartidaDoUsuario partidaDoUsuario) {
		getPartidaDoUsuarios().add(partidaDoUsuario);
		partidaDoUsuario.setCampeao(this);

		return partidaDoUsuario;
	}

	public PartidaDoUsuario removePartidaDoUsuario(PartidaDoUsuario partidaDoUsuario) {
		getPartidaDoUsuarios().remove(partidaDoUsuario);
		partidaDoUsuario.setCampeao(null);

		return partidaDoUsuario;
	}

	public Integer getCampeaoIdApi() {
		return campeaoIdApi;
	}

	public void setCampeaoIdApi(Integer campeaoIdApi) {
		this.campeaoIdApi = campeaoIdApi;
	}

	public String getCaminhoImagem() {
		return caminhoImagem;
	}

	public void setCaminhoImagem(String caminhoImagem) {
		this.caminhoImagem = caminhoImagem;
	}

}