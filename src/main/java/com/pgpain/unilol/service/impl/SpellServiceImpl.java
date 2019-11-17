package com.pgpain.unilol.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.core.searchable.SearchableList;
import com.merakianalytics.orianna.types.core.staticdata.SummonerSpell;
import com.merakianalytics.orianna.types.core.staticdata.SummonerSpells;
import com.pgpain.unilol.dto.SpellDto;
import com.pgpain.unilol.model.Spell;
import com.pgpain.unilol.repository.SpellRepository;
import com.pgpain.unilol.service.SpellService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SpellServiceImpl implements SpellService {

	@Autowired
	SpellRepository spellRepository;

	@Override
	public Spell getSpellByIdApi(String id) {
		log.info("capturando informações de spell");

		Optional<Spell> spell = spellRepository.findTopBySpellIdApi(id);

		if (spell.isPresent())
			return spell.get();
		return new Spell();
	}

	@Override
	public List<Spell> getSpells(List<String> ids) {
		List<Spell> spells = new ArrayList<>();
		ids.forEach(id -> {
			Optional<Spell> spell = spellRepository.findTopBySpellIdApi(id);
			if (spell.isPresent())
				spells.add(spell.get());
		});

		return spells;
	}

	@Override
	public SpellDto getSpellDetailByIdApi(String spellIdApi) {
		SpellDto spellDTO = new SpellDto();

		Optional<Spell> spellDb = spellRepository.findTopBySpellIdApi(spellIdApi);
		Orianna.getSummonerSpells();
		// chamando para preencher

		SearchableList<SummonerSpell> filter = SummonerSpells.get().filter(each -> each.getKey().equals(spellIdApi));
		SummonerSpell spell = filter.get(0);

		spell.getCooldowns();
		spell.getCosts();
		spell.getDescription();
		spell.getResourceDescription();
		spell.getTooltip();

		spellDTO.setSpell(spell);
		if (spellDb.isPresent()) {
			spellDTO.setCaminhoImagem(spellDb.get().getCaminhoImagem());
			spellDTO.setId(spellIdApi);
			spellDTO.setNome(spellDb.get().getNome());
		}
		return spellDTO;
	}

}
