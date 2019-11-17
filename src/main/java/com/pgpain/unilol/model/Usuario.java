package com.pgpain.unilol.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idUsuario;

	private String accountId;

	private String nome;

	private String iconeUrl;

	private byte status = 0;

	private String regiao;

	// bi-directional many-to-one association to PartidaDoUsuario
	@JsonIgnore
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	private List<PartidaDoUsuario> partidaDoUsuarios;

	@JsonIgnore
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	private List<FilaDeProcesso> filaDeProcessos;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Long getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
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

	public PartidaDoUsuario addPartidaDoUsuario(PartidaDoUsuario partidaDoUsuario) {
		getPartidaDoUsuarios().add(partidaDoUsuario);
		partidaDoUsuario.setUsuario(this);

		return partidaDoUsuario;
	}

	public PartidaDoUsuario removePartidaDoUsuario(PartidaDoUsuario partidaDoUsuario) {
		getPartidaDoUsuarios().remove(partidaDoUsuario);
		partidaDoUsuario.setUsuario(null);

		return partidaDoUsuario;
	}

	public byte isStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public List<FilaDeProcesso> getFilaDeProcessos() {
		return filaDeProcessos;
	}

	public void setFilaDeProcessos(List<FilaDeProcesso> filaDeProcessos) {
		this.filaDeProcessos = filaDeProcessos;
	}

	public byte getStatus() {
		return status;
	}

	public void setPartidaDoUsuarios(List<PartidaDoUsuario> partidaDoUsuarios) {
		this.partidaDoUsuarios = partidaDoUsuarios;
	}

	public String getRegiao() {
		return regiao;
	}

	public void setRegiao(String regiao) {
		this.regiao = regiao;
	}

	public String getIconeUrl() {
		return iconeUrl;
	}

	public void setIconeUrl(String iconeUrl) {
		this.iconeUrl = iconeUrl;
	}

}