package com.pgpain.unilol.enumeration;

public enum TipoEvento {
	CHAMPION_KILL("CHAMPION_KILL"), WARD_PLACED("WARD_PLACED"), ITEM_PURCHASED("ITEM_PURCHASED"),
	BUILDING_KILL("BUILDING_KILL"), SKILL_LEVEL_UP("SKILL_LEVEL_UP"), ITEM_DESTROYED("ITEM_DESTROYED"),
	ELITE_MONSTER_KILL("ELITE_MONSTER_KILL");
	
	private final String tipo;
	
	TipoEvento(String tipo) {
		this.tipo = tipo;
	}
	
	public String getTipo() {
		return this.tipo;
	}
}
