package com.pgpain.unilol.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the partida database table.
 * 
 */
@Entity
@NamedQuery(name = "Partida.findAll", query = "SELECT p FROM Partida p")
public class Partida implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPartida;

	private Long idExterno;

	private String dataCriacao;

	private String tipoPartida;

	// bi-directional many-to-one association to PartidaDoUsuario
	@JsonIgnore
	@OneToMany(mappedBy = "partida", cascade = CascadeType.ALL)
	private List<PartidaDoUsuario> partidaDoUsuarios;

	@JsonIgnore
	@OneToMany(mappedBy = "partida", cascade = CascadeType.ALL)
	private List<FilaDeProcesso> filaDeProcessos;

	private Long duracaoPartida;

	@JsonIgnore
	@OneToMany(mappedBy = "partida", cascade = CascadeType.ALL)
	private List<EventoDaPartida> eventosDaPartida;

	public Long getIdExterno() {
		return idExterno;
	}

	public void setIdExterno(Long idExterno) {
		this.idExterno = idExterno;
	}

	public Long getIdPartida() {
		return this.idPartida;
	}

	public void setIdPartida(Long idPartida) {
		this.idPartida = idPartida;
	}

	public String getDataCriacao() {
		return this.dataCriacao;
	}

	public void setDataCriacao(String dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getTipoPartida() {
		return this.tipoPartida;
	}

	public void setTipoPartida(String tipoPartida) {
		this.tipoPartida = tipoPartida;
	}

	public List<PartidaDoUsuario> getPartidaDoUsuarios() {
		return this.partidaDoUsuarios;
	}

	public void setPartidaDoUsuarios(List<PartidaDoUsuario> partidaDoUsuarios) {
		this.partidaDoUsuarios = partidaDoUsuarios;
	}

	public PartidaDoUsuario addPartidaDoUsuario(PartidaDoUsuario partidaDoUsuario) {
		getPartidaDoUsuarios().add(partidaDoUsuario);
		partidaDoUsuario.setPartida(this);

		return partidaDoUsuario;
	}

	public PartidaDoUsuario removePartidaDoUsuario(PartidaDoUsuario partidaDoUsuario) {
		getPartidaDoUsuarios().remove(partidaDoUsuario);
		partidaDoUsuario.setPartida(null);

		return partidaDoUsuario;
	}

	public Long getDuracaoPartida() {
		return duracaoPartida;
	}

	public void setDuracaoPartida(Long duracaoPartida) {
		this.duracaoPartida = duracaoPartida;
	}

	public List<EventoDaPartida> getEventosDaPartida() {
		return eventosDaPartida;
	}

	public void setEventosDaPartida(List<EventoDaPartida> eventosDaPartida) {
		this.eventosDaPartida = eventosDaPartida;
	}

	public List<FilaDeProcesso> getFilaDeProcessos() {
		return filaDeProcessos;
	}

	public void setFilaDeProcessos(List<FilaDeProcesso> filaDeProcessos) {
		this.filaDeProcessos = filaDeProcessos;
	}

}