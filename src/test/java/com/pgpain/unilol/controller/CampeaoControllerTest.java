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
import com.pgpain.unilol.model.Campeao;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.model.PartidaDoUsuario;
import com.pgpain.unilol.model.Usuario;
import com.pgpain.unilol.service.CampeaoService;

public class CampeaoControllerTest {

	public static final Integer CAMPEAO_ID_API = 3489;

	@Mock
	private CampeaoService campeaoService;

	@InjectMocks
	CampeaoController campeaoController;

	MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		mockMvc = MockMvcBuilders.standaloneSetup(campeaoController).build();
	}

	@Test
	public void testGetCampeaoByIdApi() throws Exception {

		// given
		Campeao campeao = new Campeao();
		campeao.setCaminhoImagem("url");
		campeao.setCampeaoIdApi(CAMPEAO_ID_API);
		campeao.setIdCampeao(23L);

		// when

		when(campeaoService.getCampeaoByIdApi(anyInt())).thenReturn(campeao);

		// then
		mockMvc.perform(get("/api/campeao/" + CAMPEAO_ID_API).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.campeaoIdApi", equalTo(CAMPEAO_ID_API)));

	}

	@Test
	public void testCampeaoIdApiFalse() throws Exception {

		when(campeaoService.getCampeaoByIdApi(anyInt())).thenReturn(null);

		mockMvc.perform(get("/api/campeao/" + CAMPEAO_ID_API).contentType(MediaType.APPLICATION_JSON))
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
				Campeao campeao = new Campeao();
				campeao.setCaminhoImagem("url");
				campeao.setCampeaoIdApi(CAMPEAO_ID_API);
				campeao.setIdCampeao(23L);

				Campeao campeao2 = new Campeao();
				campeao.setCaminhoImagem("2121");
				campeao.setCampeaoIdApi(CAMPEAO_ID_API);
				campeao.setIdCampeao(23L);

				List<Campeao> campeoes = new ArrayList<>();
				campeoes.add(campeao);
				campeoes.add(campeao2);

				Integer[] id = { 54, 754, 786, 324 };
				List<Integer> ids = Arrays.asList(id);

				when(campeaoService.getCampeoes(ids)).thenReturn(campeoes);

				mockMvc.perform(get("/api/campeoes?ids=54,754,786,324").contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk()).andExpect(jsonPath("$[0].campeaoIdApi", equalTo(CAMPEAO_ID_API)));

		

	}

	@Test
	public void testGetCampeoesEmpty() throws Exception {

		List<Campeao> campeoes = new ArrayList<>();

		Integer[] id = { 54, 754, 786, 324 };
		List<Integer> ids = Arrays.asList(id);

		when(campeaoService.getCampeoes(ids)).thenReturn(campeoes);

		mockMvc.perform(get("/api/campeoes?ids=54,754,786,324").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

	}

	@Test
	public void testGetCampeoesByNome() throws Exception {

		// given
				Campeao campeao = new Campeao();
				campeao.setCaminhoImagem("url");
				campeao.setCampeaoIdApi(CAMPEAO_ID_API);
				campeao.setIdCampeao(23L);

				Campeao campeao2 = new Campeao();
				campeao.setCaminhoImagem("2121");
				campeao.setCampeaoIdApi(CAMPEAO_ID_API);
				campeao.setIdCampeao(23L);

				List<Campeao> campeoes = new ArrayList<>();
				campeoes.add(campeao);
				campeoes.add(campeao2);

				String[] nome = { "vayne", "kaisa", "ahri", "ashe"};
				List<String> nomes = Arrays.asList(nome);

				when(campeaoService.getCampeoesByNome(nomes)).thenReturn(campeoes);

				mockMvc.perform(get("/api/campeoes/get?nomes=vayne,kaisa,ahri,ashe").contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk()).andExpect(jsonPath("$[0].campeaoIdApi", equalTo(CAMPEAO_ID_API)));


	}
	
	@Test
	public void testGetNomesEmpty() throws Exception {
		
		List<String> nomes = new ArrayList<>();


		mockMvc.perform(get("/api/campeoes/get?nomes=").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testGetCampeoesNomeEmpty() throws Exception {

		List<Campeao> campeoes = new ArrayList<>();

		String[] nome = { "vayne", "kaisa", "ahri", "ashe"};
		List<String> nomes = Arrays.asList(nome);

		when(campeaoService.getCampeoesByNome(nomes)).thenReturn(campeoes);

		mockMvc.perform(get("/api/campeoes/get?nomes=andrei,TEIXEIRA,ABABA,TESTANDO").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

	}

}
