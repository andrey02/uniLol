package com.pgpain.unilol.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the partida_do_usuario database table.
 * 
 */
@Entity
@Table(name = "partida_do_usuario")
public class PartidaDoUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPartidaDoUsuario;

	private String time;
	private String laneType;
	private String role;
	private int participantId;

	// bi-directional many-to-one association to ItemDaPartidaDoUsuario

	@JsonIgnore
	@OneToMany(mappedBy = "partidaDoUsuario", cascade = CascadeType.ALL)
	private List<ItemDaPartidaDoUsuario> itemDaPartidaDoUsuarios;

	// bi-directional many-to-one association to Campeao
	@ManyToOne
	@JoinColumn(name = "idCampeao")
	private Campeao campeao;

	// bi-directional many-to-one association to Partida

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "idPartida")
	private Partida partida;

	// bi-directional many-to-one association to Usuario

	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;

	@JsonIgnore
	@OneToOne(mappedBy = "partidaDoUsuario", cascade = CascadeType.ALL)
	private DetalhePartidaUsuario detalhePartidaUsuario;

	@JsonIgnore
	@OneToMany(mappedBy = "partidaDoUsuario", cascade = CascadeType.ALL)
	private List<Timeline> timeline;

	public Long getIdPartidaDoUsuario() {
		return idPartidaDoUsuario;
	}

	public void setIdPartidaDoUsuario(Long idPartidaDoUsuario) {
		this.idPartidaDoUsuario = idPartidaDoUsuario;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getParticipantId() {
		return participantId;
	}

	public void setParticipantId(int participantId) {
		this.participantId = participantId;
	}

	public List<ItemDaPartidaDoUsuario> getItemDaPartidaDoUsuarios() {
		return itemDaPartidaDoUsuarios;
	}

	public ItemDaPartidaDoUsuario addItemDaPartidaDoUsuarios(ItemDaPartidaDoUsuario itemDaPartidaDoUsuario) {
		getItemDaPartidaDoUsuarios().add(itemDaPartidaDoUsuario);
		itemDaPartidaDoUsuario.setPartidaDoUsuario(this);

		return itemDaPartidaDoUsuario;
	}

	public Campeao getCampeao() {
		return campeao;
	}

	public void setCampeao(Campeao campeao) {
		this.campeao = campeao;
	}

	public Partida getPartida() {
		return partida;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public DetalhePartidaUsuario getDetalhePartidaUsuario() {
		return detalhePartidaUsuario;
	}

	public void setDetalhePartidaUsuario(DetalhePartidaUsuario detalhePartidaUsuario) {
		this.detalhePartidaUsuario = detalhePartidaUsuario;
	}

	public List<Timeline> getTimeline() {
		return timeline;
	}

	public Timeline addTimeline(Timeline timeline) {
		getTimeline().add(timeline);
		timeline.setPartidaDoUsuario(this);

		return timeline;
	}

	public String getLaneType() {
		return laneType;
	}

	public void setLaneType(String laneType) {
		this.laneType = laneType;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}