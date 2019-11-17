package com.pgpain.unilol.dto;

public class UsuarioDto {
	private String nome;
	private String accountId;
	private String iconUrl;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String image) {
		this.iconUrl = image;
	}
}
