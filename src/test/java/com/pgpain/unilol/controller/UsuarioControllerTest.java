package com.pgpain.unilol.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.pgpain.unilol.model.Usuario;
import com.pgpain.unilol.service.UsuarioService;
import com.pgpain.unilol.util.OriannaConfiguration;

public class UsuarioControllerTest {

	public static final String NAME = "DreyKing";

	@Mock
	UsuarioService usuarioService;
	@Mock
	OriannaConfiguration orianna;

	@InjectMocks
	UsuarioController usuarioController;

	MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
	}

	@Test
	public void testGetUsuarioByNome() throws Exception {

		// given
		Usuario usuario = new Usuario();
		usuario.setAccountId("teste id");
		usuario.setNome(NAME);
		// when
		when(orianna.eRegiaoValida(anyString())).thenReturn(true);
		//when(usuarioService.eUsuarioValido(anyString(), anyString())).thenReturn(true);
		when(usuarioService.getUsuarioByNome(anyString())).thenReturn(usuario);
		// then
		mockMvc.perform(get("/api/usuario/" + NAME + "/BRAZIL").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.nome", equalTo(NAME)));

	}

	@Test
	public void testRegionFalse() throws Exception {

		when(orianna.eRegiaoValida(anyString())).thenReturn(false);

		mockMvc.perform(get("/api/usuario/" + NAME + "/BRAZIL").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testUsuarioFalse() throws Exception {

		when(orianna.eRegiaoValida(anyString())).thenReturn(true);
		//when(usuarioService.eUsuarioValido(anyString(), anyString())).thenReturn(false);

		mockMvc.perform(get("/api/usuario/" + NAME + "/BRAZIL").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

}
