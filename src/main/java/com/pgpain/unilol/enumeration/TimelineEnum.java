package com.pgpain.unilol.enumeration;

import java.util.List;

import com.pgpain.unilol.model.PartidaDoUsuario;

public enum TimelineEnum {
	OURO_ADQUIRIDO("ouroAdquirido"), LEVEL("level"), OURO("ouro"), QUANTIDADE_CREEP("qtdCreep"),
	EXPERIENCIA("experiencia"), QUANTIDADE_MONSTRO_NEUTRO("qtdMonstroNeutro");

	private final String tipo;

	TimelineEnum(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return this.tipo;
	}

	public static TimelineEnum fromString(String text) {
		for (TimelineEnum b : TimelineEnum.values()) {
			if (b.tipo.equalsIgnoreCase(text)) {
				return b;
			}
		}
		return null;
	}

	public static Long getSumQuery(TimelineEnum timelineEnum,List<PartidaDoUsuario> partidaDosUsuarios, Integer index) {
		
		Long soma = 0L;
		for(PartidaDoUsuario partidaDoUsuario : partidaDosUsuarios) {
			switch (timelineEnum) {
			case OURO_ADQUIRIDO:
				soma += partidaDoUsuario.getTimeline().get(index).getOuroAdquirido();
				break;
			case OURO:
				soma += partidaDoUsuario.getTimeline().get(index).getOuro();
				break;
			case QUANTIDADE_CREEP:
				soma += partidaDoUsuario.getTimeline().get(index).getQtdCreep();
				break;
			case EXPERIENCIA:
				soma += partidaDoUsuario.getTimeline().get(index).getExperiencia();
				break;
			case QUANTIDADE_MONSTRO_NEUTRO:
				soma += partidaDoUsuario.getTimeline().get(index).getQtdMonstroNeutro();
				break;
			default:
				break;
			}

		}
		return soma;
	}
}
