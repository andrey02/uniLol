package com.pgpain.unilol.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.merakianalytics.orianna.types.common.Lane;
import com.merakianalytics.orianna.types.common.Role;
import com.merakianalytics.orianna.types.common.RunePath;
import com.merakianalytics.orianna.types.common.Tier;
import com.merakianalytics.orianna.types.core.match.Participant;
import com.merakianalytics.orianna.types.core.match.ParticipantStats;
import com.merakianalytics.orianna.types.core.match.ParticipantTimeline;
import com.merakianalytics.orianna.types.core.match.RuneStats;
import com.merakianalytics.orianna.types.core.match.Team;
import com.merakianalytics.orianna.types.core.searchable.SearchableList;
import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.merakianalytics.orianna.types.core.staticdata.Item;
import com.merakianalytics.orianna.types.core.staticdata.ProfileIcon;
import com.merakianalytics.orianna.types.core.staticdata.SummonerSpell;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import com.merakianalytics.orianna.types.data.CoreData;
import com.pgpain.unilol.model.DetalhePartidaUsuario;
import com.pgpain.unilol.service.DetalhePartidaUsuarioService;


public class DetalhePartidaUsuarioServiceImplTest {

	DetalhePartidaUsuarioService detalhePartida;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		detalhePartida = new DetalhePartidaUsuarioServiceImpl();
	}

	@SuppressWarnings("null")
	
	@Test(expected=NullPointerException.class)
	public void testCreate() {
		//given
		Participant participant = null;
		
		when(participant.getStats().getKills()).thenReturn(9);
		when(participant.getStats().getAssists()).thenReturn(10);
		when(participant.getStats().getDeaths()).thenReturn(5);
		when(participant.getStats().getDamageDealt()).thenReturn(10000);	           
		when(participant.getStats().getGoldEarned()).thenReturn(10000);	
		when(participant.getStats().getChampionLevel()).thenReturn(17);
		
		//when
		DetalhePartidaUsuario teste = detalhePartida.create(participant);
		
		//then
		assertEquals(9, teste.getAbate());
		assertEquals(5, teste.getMorte());
		assertEquals(10, teste.getAssistencia());
		
		assertEquals(17, teste.getNivelCampeao());
		

	}
}
