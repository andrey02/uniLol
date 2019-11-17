package com.pgpain.unilol.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.pgpain.unilol.dto.MatchHistoryDto;
import com.pgpain.unilol.dto.PosicaoDto;
import com.pgpain.unilol.dto.UsuarioDto;
import com.pgpain.unilol.model.Item;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.model.PartidaDoUsuario;
import com.pgpain.unilol.model.Usuario;
import com.pgpain.unilol.service.ItemService;

public class ItemControllerTest {

	public static final Integer ITEM_ID_API = 3489;

	@Mock
	private ItemService itemService;

	@InjectMocks
	ItemController itemController;

	MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
	}

	@Test
	public void testGetItemByIdApi() throws Exception {

		// given
		Item item = new Item();
		item.setCaminhoImagem("url");
		item.setItemIdapi(ITEM_ID_API);
		item.setIdItem(23L);

		// when

		when(itemService.getItemByIdApi(anyInt())).thenReturn(item);

		// then
		mockMvc.perform(get("/api/item/" + ITEM_ID_API).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.itemIdApi", equalTo(ITEM_ID_API)));

	}

	@Test
	public void testItemIdApiFalse() throws Exception {

		when(itemService.getItemByIdApi(anyInt())).thenReturn(null);

		mockMvc.perform(get("/api/item/" + ITEM_ID_API).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testGetIdsEmpty() throws Exception {
		
		List<Integer> ids = new ArrayList<>();


		mockMvc.perform(get("/api/campeoes?ids=").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testGetCampeoes() throws Exception {

		// given
				Item item = new Item();
				item.setCaminhoImagem("url");
				item.setItemIdapi(ITEM_ID_API);
				item.setIdItem(23L);

				Item item2 = new Item();
				item.setCaminhoImagem("2121");
				item.setItemIdapi(ITEM_ID_API);
				item.setIdItem(23L);

				List<Item> campeoes = new ArrayList<>();
				campeoes.add(item);
				campeoes.add(item2);

				Integer[] id = { 54, 754, 786, 324 };
				List<Integer> ids = Arrays.asList(id);

				when(itemService.getItems(ids)).thenReturn(campeoes);

				mockMvc.perform(get("/api/items?ids=54,754,786,324").contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk()).andExpect(jsonPath("$[0].itemIdApi", equalTo(ITEM_ID_API)));

		

	}

	@Test
	public void testGetCampeoesEmpty() throws Exception {

		List<Item> campeoes = new ArrayList<>();

		Integer[] id = { 54, 754, 786, 324 };
		List<Integer> ids = Arrays.asList(id);

		when(itemService.getItems(ids)).thenReturn(campeoes);

		mockMvc.perform(get("/api/items?ids=54,754,786,324").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

	}
	
	@Test
	public void testGetNomesEmpty() throws Exception {
		
		List<String> nomes = new ArrayList<>();


		mockMvc.perform(get("/api/campeoes/get?nomes=").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	

}
