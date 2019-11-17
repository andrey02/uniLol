package com.pgpain.unilol.enumeration;

public enum FilaDeProcessoEnum {
	STATUS_PROCESSANDO("PROCESSANDO"), STATUS_PROCESSADO("PROCESSADO"), STATUS_SUCESSO("SUCESSO"),
	STATUS_FALHA("FALHA"),TIPO_DETALHE("DETALHE"), TIPO_TIMELINE("TIMELINE"),TIPO_EVENTO("EVENTO");

	private final String value;

	FilaDeProcessoEnum(String tipo) {
		this.value = tipo;
	}

	public String getValue() {
		return this.value;
	}
}
