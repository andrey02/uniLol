package com.pgpain.unilol.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.merakianalytics.orianna.types.core.match.Participant;
import com.merakianalytics.orianna.types.core.staticdata.SummonerSpell;
import com.pgpain.unilol.model.Item;
import com.pgpain.unilol.model.ItemDaPartidaDoUsuario;
import com.pgpain.unilol.model.PartidaDoUsuario;
import com.pgpain.unilol.model.Runa;
import com.pgpain.unilol.model.Spell;
import com.pgpain.unilol.repository.ItemDaPartidaDoUsuarioRepository;
import com.pgpain.unilol.repository.ItemRepository;
import com.pgpain.unilol.repository.RunaRepository;
import com.pgpain.unilol.repository.SpellRepository;
import com.pgpain.unilol.service.ItemDaPartidaDoUsuarioService;

@Service
public class ItemDaPartidaDoUsuarioServiceImpl implements ItemDaPartidaDoUsuarioService {

	private ItemRepository itemRepository;
	private ItemDaPartidaDoUsuarioRepository itemDaPartidaDoUsuarioRepository;
	private RunaRepository runaRepository;
	private SpellRepository spellRepository;

	public ItemDaPartidaDoUsuarioServiceImpl(ItemRepository itemRepository,
			ItemDaPartidaDoUsuarioRepository itemDaPartidaDoUsuarioRepository, RunaRepository runaRepository,
			SpellRepository spellRepository) {
		super();
		this.itemRepository = itemRepository;
		this.itemDaPartidaDoUsuarioRepository = itemDaPartidaDoUsuarioRepository;
		this.runaRepository = runaRepository;
		this.spellRepository = spellRepository;
	}

	@Override
	public List<ItemDaPartidaDoUsuario> saveAll(Participant participant, PartidaDoUsuario partidaDoUsuario) {
		List<ItemDaPartidaDoUsuario> itemDaPartidaDoUsuarios = new ArrayList<>();
		// items
		participant.getItems().forEach(itemNew -> {
			Optional<Item> item = itemRepository.findFirstByItemIdApi(itemNew.getId());
			ItemDaPartidaDoUsuario itemDaPartidaDoUsuario = new ItemDaPartidaDoUsuario();
			itemDaPartidaDoUsuario.setPartidaDoUsuario(partidaDoUsuario);
			
			if (item.isPresent()) {
				itemDaPartidaDoUsuario.setItem(item.get());
				itemDaPartidaDoUsuario.setNome(item.get().getNome());
			}
			
			itemDaPartidaDoUsuarioRepository.save(itemDaPartidaDoUsuario);
			itemDaPartidaDoUsuarios.add(itemDaPartidaDoUsuario);
		});

		// runa
		participant.getRuneStats().forEach(runaNew -> {
			Optional<Runa> runa = runaRepository.findFirstByRunaIdApi(runaNew.getRune().getId());
			ItemDaPartidaDoUsuario itemDaPartidaDoUsuario = new ItemDaPartidaDoUsuario();
			itemDaPartidaDoUsuario.setPartidaDoUsuario(partidaDoUsuario);
			if (runa.isPresent()) {
				itemDaPartidaDoUsuario.setRuna(runa.get());
				itemDaPartidaDoUsuario.setNome(runa.get().getNome());
			}
			itemDaPartidaDoUsuarioRepository.save(itemDaPartidaDoUsuario);
			itemDaPartidaDoUsuarios.add(itemDaPartidaDoUsuario);
		});
		ItemDaPartidaDoUsuario itemDaPartidaDoUsuarioD = new ItemDaPartidaDoUsuario();
		ItemDaPartidaDoUsuario itemDaPartidaDoUsuarioF = new ItemDaPartidaDoUsuario();
		// feitico

		SummonerSpell summonerSpellD = participant.getSummonerSpellD();
		SummonerSpell summonerSpellF = participant.getSummonerSpellF();
		
		Optional<Spell> spellD = spellRepository.findTopBySpellIdApi(summonerSpellD.getKey());
		if (spellD.isPresent()) {
			itemDaPartidaDoUsuarioD.setFeitico(spellD.get());
			itemDaPartidaDoUsuarioD.setNome(spellD.get().getNome());
		}
		
		
		itemDaPartidaDoUsuarioD.setPartidaDoUsuario(partidaDoUsuario);
		itemDaPartidaDoUsuarioRepository.save(itemDaPartidaDoUsuarioD);
		itemDaPartidaDoUsuarios.add(itemDaPartidaDoUsuarioD);


		Optional<Spell> spellF = spellRepository.findTopBySpellIdApi(summonerSpellF.getKey());
		if (spellF.isPresent()) {
			itemDaPartidaDoUsuarioF.setFeitico(spellF.get());
			itemDaPartidaDoUsuarioF.setNome(spellF.get().getNome());
		}
		itemDaPartidaDoUsuarioF.setPartidaDoUsuario(partidaDoUsuario);
		itemDaPartidaDoUsuarioRepository.save(itemDaPartidaDoUsuarioF);
		itemDaPartidaDoUsuarios.add(itemDaPartidaDoUsuarioF);

		return itemDaPartidaDoUsuarios;
	}

}
