package com.pgpain.unilol.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.merakianalytics.orianna.Orianna;
import com.pgpain.unilol.dto.ItemDto;
import com.pgpain.unilol.model.Item;
import com.pgpain.unilol.repository.ItemRepository;
import com.pgpain.unilol.service.ItemService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	ItemRepository itemRepository;

	@Override
	public Item getItemByIdApi(Integer id) {
		log.info("capturando informações de item");

		Optional<Item> item = itemRepository.findFirstByItemIdApi(id);

		if (item.isPresent())
			return item.get();
		return new Item();
	}

	@Override
	public List<Item> getItems(List<Integer> ids) {
		List<Item> items = new ArrayList<>();
		ids.forEach(id -> {
			Optional<Item> item = itemRepository.findFirstByItemIdApi(id);
			if (item.isPresent())
				items.add(item.get());
		});

		return items;
	}

	@Override
	public ItemDto getItemDetailByIdApi(Integer itemIdApi) {
		ItemDto itemDTO = new ItemDto();

		Optional<Item> itemDb = itemRepository.findFirstByItemIdApi(itemIdApi);
		com.merakianalytics.orianna.types.core.staticdata.Item item = Orianna.itemWithId(itemIdApi).get();
		//chamando para preencher
		item.getBasePrice();
		item.getDescription();
		item.getGroup();
		item.getPlaintext();
		item.getSanitizedDescription();
		item.getSellPrice();
		item.getBuildsFrom();
		item.getBuildsInto();
		item.getStats();
		item.getRequiredChampion();
		
		itemDTO.setItem(item);
		if (itemDb.isPresent()) {
			itemDTO.setCaminhoImagem(itemDb.get().getCaminhoImagem());
			itemDTO.setItemIdApi(itemIdApi);
			itemDTO.setName(itemDb.get().getNome());
		}
		return itemDTO;
	}

}
