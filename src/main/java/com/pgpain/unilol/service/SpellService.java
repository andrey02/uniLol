package com.pgpain.unilol.service;

import java.util.List;

import com.pgpain.unilol.dto.SpellDto;
import com.pgpain.unilol.model.Spell;

public interface SpellService {
	Spell getSpellByIdApi(String id);

	List<Spell> getSpells(List<String> ids);

	SpellDto getSpellDetailByIdApi(String spellIdApi);
}
