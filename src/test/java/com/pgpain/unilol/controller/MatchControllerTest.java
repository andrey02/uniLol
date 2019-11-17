package com.pgpain.unilol.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.model.PartidaDoUsuario;
import com.pgpain.unilol.model.Usuario;
import com.pgpain.unilol.service.MatchService;
import com.pgpain.unilol.service.PartidaDoUsuarioService;
import com.pgpain.unilol.service.PartidaService;
import com.pgpain.unilol.service.UsuarioService;
import com.pgpain.unilol.util.OriannaConfiguration;

public class MatchControllerTest {

	public static final String NAME = "DreyKing";

	@Mock
	UsuarioService usuarioService;
	@Mock
	MatchService matchService;
	@Mock
	OriannaConfiguration orianna;
	@Mock
	PartidaService partidaService;
	@Mock
	PartidaDoUsuarioService partidaDoUsuarioService;
	
	@InjectMocks
	MatchController matchController;
	

	MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		mockMvc = MockMvcBuilders.standaloneSetup(matchController).build();
	}

	@Test
	public void testgetMatchHistory() throws Exception {

		// given
		Usuario usuario = new Usuario();
		usuario.setAccountId("teste id");
		usuario.setNome(NAME);
		UsuarioDto usuarioDTO = new UsuarioDto();
		usuarioDTO.setAccountId("teste id");
		usuarioDTO.setNome(NAME);

		MatchHistoryDto matchHistoryDTO = new MatchHistoryDto();
		matchHistoryDTO.setUsuario(usuarioDTO);

		// when
		when(orianna.eRegiaoValida(anyString())).thenReturn(true);
		when(usuarioService.eUsuarioValido(anyString(), anyString())).thenReturn("teste id");
		when(usuarioService.getUsuarioByNome(anyString())).thenReturn(usuario);
		when(matchService.getMatchHistoryFromDB(usuario)).thenReturn(matchHistoryDTO);

		// then
		mockMvc.perform(get("/api/match/" + NAME + "/BRAZIL").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.usuario", equalTo(NAME)));

	}

	@Test
	public void testRegionFalse() throws Exception {

		when(orianna.eRegiaoValida(anyString())).thenReturn(false);

		mockMvc.perform(get("/api/match/" + NAME + "/BRAZIL").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testUsuarioFalse() throws Exception {

		when(orianna.eRegiaoValida(anyString())).thenReturn(true);
		when(usuarioService.eUsuarioValido(anyString(), anyString())).thenReturn("teste id");

		mockMvc.perform(get("/api/match/" + NAME + "/BRAZIL").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testgetMatchHistoryAccountIdNull() throws Exception {

		// given
		Usuario usuario = new Usuario();
		usuario.setAccountId(null);
		usuario.setNome(NAME);
		UsuarioDto usuarioDTO = new UsuarioDto();
		usuarioDTO.setAccountId("teste id");
		usuarioDTO.setNome(NAME);

		MatchHistoryDto matchHistoryDTO = new MatchHistoryDto();
		matchHistoryDTO.setUsuario(usuarioDTO);

		// when
		when(orianna.eRegiaoValida(anyString())).thenReturn(true);
		when(usuarioService.eUsuarioValido(anyString(), anyString())).thenReturn("teste id");
		when(usuarioService.getUsuarioByNome(anyString())).thenReturn(null);
		when(usuarioService.saveUsuario(anyString(), anyString(), anyInt())).thenReturn(usuario);

		when(matchService.getMatchHistoryFromDB(usuario)).thenReturn(matchHistoryDTO);

		// then
		mockMvc.perform(get("/api/match/" + NAME + "/BRAZIL").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

	}

	@Test
	public void testgetMatchHistoryBadRequest() throws Exception {

		// given
		Usuario usuario = new Usuario();
		usuario.setAccountId("test id");
		usuario.setNome(NAME);

		MatchHistoryDto matchHistoryDTO = new MatchHistoryDto();
		//matchHistoryDTO.setUsuario(NAME);

		// when
		when(orianna.eRegiaoValida(anyString())).thenReturn(true);
		//when(usuarioService.eUsuarioValido(anyString(), anyString())).thenReturn(true);
		when(usuarioService.getUsuarioByNome(anyString())).thenReturn(null);
		when(usuarioService.saveUsuario(anyString(), anyString(), anyInt())).thenReturn(usuario);

		when(matchService.getMatchHistoryFromAPI(usuario)).thenReturn(matchHistoryDTO);

		// then
		mockMvc.perform(get("/api/match/" + NAME + "/BRAZIL").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.usuario", equalTo(NAME)));

	}
	
	@Test
	public void testPosicaoPartidaUsuario() throws Exception {
		//given
		Partida partida = new Partida();
		Usuario usuario = new Usuario();
		PartidaDoUsuario partidaDoUsuario = new PartidaDoUsuario();
		
		List<PosicaoDto> posicaoDto = new ArrayList<>();
		//when
		when(partidaService.findByIdExterno(anyLong())).thenReturn(partida);
		when(usuarioService.getUsuarioByAccountId(anyString())).thenReturn(usuario);
		when(partidaDoUsuarioService.getByUsuarioAndPartida(usuario,partida)).thenReturn(partidaDoUsuario);
		
		
		//then
	}
}