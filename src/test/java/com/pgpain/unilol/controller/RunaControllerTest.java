package com.pgpain.unilol.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
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

import com.pgpain.unilol.model.Runa;
import com.pgpain.unilol.service.RunaService;

public class RunaControllerTest {

	public static final Integer RUNA_ID_API = 3489;

	@Mock
	private RunaService runaService;

	@InjectMocks
	RunaController runaController;

	MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		mockMvc = MockMvcBuilders.standaloneSetup(runaController).build();
	}

	@Test
	public void testGetRunaByIdApi() throws Exception {

		// given
		Runa runa = new Runa();
		runa.setCaminhoImagem("url");
		runa.setRunaIdApi(RUNA_ID_API);
		runa.setIdRuna(23L);

		// when

		when(runaService.getRunaByIdApi(anyInt())).thenReturn(runa);

		// then
		mockMvc.perform(get("/api/runa/" + RUNA_ID_API).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.runaIdApi", equalTo(RUNA_ID_API)));

	}

	@Test
	public void testRunaIdApiFalse() throws Exception {

		when(runaService.getRunaByIdApi(anyInt())).thenReturn(null);

		mockMvc.perform(get("/api/runa/" + RUNA_ID_API).contentType(MediaType.APPLICATION_JSON))
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
		Runa runa = new Runa();
		runa.setCaminhoImagem("url");
		runa.setRunaIdApi(RUNA_ID_API);
		runa.setIdRuna(23L);

		Runa runa2 = new Runa();
		runa.setCaminhoImagem("2121");
		runa.setRunaIdApi(RUNA_ID_API);
		runa.setIdRuna(23L);

		List<Runa> campeoes = new ArrayList<>();
		campeoes.add(runa);
		campeoes.add(runa2);

		Integer[] id = { 54, 754, 786, 324 };
		List<Integer> ids = Arrays.asList(id);

		when(runaService.getRunas(ids)).thenReturn(campeoes);

		mockMvc.perform(get("/api/runas?ids=54,754,786,324").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].runaIdApi", equalTo(RUNA_ID_API)));

	}

	@Test
	public void testGetCampeoesEmpty() throws Exception {

		List<Runa> campeoes = new ArrayList<>();

		Integer[] id = { 54, 754, 786, 324 };
		List<Integer> ids = Arrays.asList(id);

		when(runaService.getRunas(ids)).thenReturn(campeoes);

		mockMvc.perform(get("/api/runas?ids=54,754,786,324").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

	}

	@Test
	public void testGetNomesEmpty() throws Exception {

		List<String> nomes = new ArrayList<>();

		mockMvc.perform(get("/api/campeoes/get?nomes=").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
}