package com.pgpain.unilol.service;

import java.util.List;

import com.pgpain.unilol.dto.ItemDto;
import com.pgpain.unilol.model.Item;

public interface ItemService {
	Item getItemByIdApi(Integer id);

	List<Item> getItems(List<Integer> ids);

	ItemDto getItemDetailByIdApi(Integer itemIdApi);
}
