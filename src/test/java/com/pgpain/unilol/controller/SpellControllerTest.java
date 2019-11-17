package com.pgpain.unilol.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
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

import com.pgpain.unilol.model.Spell;
import com.pgpain.unilol.service.SpellService;

public class SpellControllerTest {

	public static final String SPELL_ID_API = "flash";

	@Mock
	private SpellService spellService;

	@InjectMocks
	SpellController spellController;

	MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		mockMvc = MockMvcBuilders.standaloneSetup(spellController).build();
	}

	@Test
	public void testGetSpellByIdApi() throws Exception {

		// given
		Spell spell = new Spell();
		spell.setCaminhoImagem("url");
		spell.setSpellIdApi(SPELL_ID_API);
		spell.setIdSpell(23L);

		// when

		when(spellService.getSpellByIdApi(anyString())).thenReturn(spell);

		// then
		mockMvc.perform(get("/api/spell/" + SPELL_ID_API).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.spellIdApi", equalTo(SPELL_ID_API)));

	}

	@Test
	public void testSpellIdApiFalse() throws Exception {

		when(spellService.getSpellByIdApi(anyString())).thenReturn(null);

		mockMvc.perform(get("/api/spell/" + SPELL_ID_API).contentType(MediaType.APPLICATION_JSON))
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
		Spell spell = new Spell();
		spell.setCaminhoImagem("url");
		spell.setSpellIdApi(SPELL_ID_API);
		spell.setIdSpell(23L);

		Spell spell2 = new Spell();
		spell.setCaminhoImagem("2121");
		spell.setSpellIdApi(SPELL_ID_API);
		spell.setIdSpell(23L);

		List<Spell> campeoes = new ArrayList<>();
		campeoes.add(spell);
		campeoes.add(spell2);

		String[] id = { "teste1", "teste2", "teste3", "teste4" };
		List<String> ids = Arrays.asList(id);

		when(spellService.getSpells(ids)).thenReturn(campeoes);

		mockMvc.perform(get("/api/spells?ids=54,754,786,324").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].spellIdApi", equalTo(SPELL_ID_API)));

	}

	@Test
	public void testGetCampeoesEmpty() throws Exception {

		List<Spell> campeoes = new ArrayList<>();

		String[] id = { "54", "754", "786", "324" };
		List<String> ids = Arrays.asList(id);

		when(spellService.getSpells(ids)).thenReturn(campeoes);

		mockMvc.perform(get("/api/spells?ids=54,754,786,324").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

	}

	@Test
	public void testGetNomesEmpty() throws Exception {

		List<String> nomes = new ArrayList<>();

		mockMvc.perform(get("/api/campeoes/get?nomes=").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
}