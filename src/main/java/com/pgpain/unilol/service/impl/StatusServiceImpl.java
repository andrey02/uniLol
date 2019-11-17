package com.pgpain.unilol.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgpain.unilol.dto.CombatScoreDto;
import com.pgpain.unilol.dto.FuncaoDto;
import com.pgpain.unilol.dto.GoldScoreDto;
import com.pgpain.unilol.dto.StatusDto;
import com.pgpain.unilol.dto.VisionScoreDto;
import com.pgpain.unilol.model.Campeao;
import com.pgpain.unilol.model.DetalhePartidaUsuario;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.model.PartidaDoUsuario;
import com.pgpain.unilol.model.Usuario;
import com.pgpain.unilol.repository.DetalhePartidaUsuarioRepository;
import com.pgpain.unilol.repository.EventoDaPartidaRepository;
import com.pgpain.unilol.repository.PartidaDoUsuarioRepository;
import com.pgpain.unilol.repository.TimelineRepository;
import com.pgpain.unilol.service.StatusService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StatusServiceImpl implements StatusService {

	private static final String MIDDLE = "MIDDLE";
	private static final String JUNGLE = "JUNGLE";
	private static final String ADC = "DUO_CARRY";
	private static final String SUP = "DUO_SUPPORT";
	private static final String TOP = "TOP";
	private static final String[] FUNCOES = { MIDDLE, JUNGLE, ADC, SUP, TOP };
	private static final String VITORIA = "global.victory";
	private static final int NUMERO_DE_FUNCOES = 5;

	@Autowired
	private PartidaDoUsuarioRepository partidaDoUsuarioRepository;
	@Autowired
	private EventoDaPartidaRepository eventoDaPartidaRepository;
	@Autowired
	private DetalhePartidaUsuarioRepository detalhePartidaUsuarioRepository;
	@Autowired
	private TimelineRepository timelineRepository;

	@Override
	public StatusDto getStatusByUsuario(Usuario usuario) {
		StatusDto statusDto = new StatusDto();

		List<FuncaoDto> funcaoDtos = new ArrayList<>();
		List<PartidaDoUsuario> toadasPartidasDoUsuario = new ArrayList<>();

		for (int i = 0; i < NUMERO_DE_FUNCOES; i++) {

			List<PartidaDoUsuario> partidasDoUsuario;
			if (FUNCOES[i].equals(MIDDLE) || FUNCOES[i].equals(JUNGLE) || FUNCOES[i].equals(TOP)) {
				partidasDoUsuario = partidaDoUsuarioRepository.findAllByUsuarioAndLaneTypeOrderByCampeao(usuario,
						FUNCOES[i]);
			} else {
				partidasDoUsuario = partidaDoUsuarioRepository.findAllByUsuarioAndRoleOrderByCampeao(usuario,
						FUNCOES[i]);
			}

			if (!partidasDoUsuario.isEmpty()) {
				FuncaoDto funcaoDto = new FuncaoDto();
				funcaoDto.setPartidasDoUsuario(partidasDoUsuario);
				funcaoDto.setFuncao(FUNCOES[i]);
				funcaoDto.setTaxaDeVitoria(getTaxaDeVitoria(partidasDoUsuario));
				funcaoDto.setNota(getNota(funcaoDto.getTaxaDeVitoria()));

				funcaoDtos.add(funcaoDto);
				toadasPartidasDoUsuario.addAll(partidasDoUsuario);

			}
		}

		try {
			statusDto = processStatusDto(funcaoDtos, toadasPartidasDoUsuario);
		} catch (Exception e) {
			log.info("Nao foi possível pegar os status do jogador" + usuario);
		}

		return statusDto;
	}

	@Override
	public StatusDto getStatusByUsuarioAndCampeao(Usuario usuario, Campeao campeao) {
		StatusDto statusDto = new StatusDto();

		List<FuncaoDto> funcaoDtos = new ArrayList<>();

		List<PartidaDoUsuario> partidasDoUsuario = partidaDoUsuarioRepository.findAllByUsuarioAndCampeao(usuario,
				campeao);

		if (!partidasDoUsuario.isEmpty()) {
			FuncaoDto funcaoDto = new FuncaoDto();
			funcaoDto.setPartidasDoUsuario(partidasDoUsuario);
			funcaoDto.setTaxaDeVitoria(getTaxaDeVitoria(partidasDoUsuario));
			funcaoDto.setNota(getNota(funcaoDto.getTaxaDeVitoria()));

			funcaoDtos.add(funcaoDto);
		}

		try {
			statusDto = processStatusDto(funcaoDtos, partidasDoUsuario);
		} catch (Exception e) {
			log.info("Nao foi possível pegar os status do jogador" + usuario);
		}

		return statusDto;
	}

	private Double getTaxaDeVitoria(List<PartidaDoUsuario> partidasDoUsuario) {
		Double media = 0D;
		for (PartidaDoUsuario partidaDoUsuario : partidasDoUsuario) {
			if (VITORIA.equals(partidaDoUsuario.getDetalhePartidaUsuario().getResultado()))
				media++;
		}

		media = media / partidasDoUsuario.size();

		media = (double) Math.round(media * 100) / 100;
		
		return media;
	}

	private String getNota(Double double1) {

		if (double1 < 1) {
			double1 *= 10.0;
		}
		
		

		if (double1 > 9 && double1 <= 10)
			return "S+";
		else if (double1 > 8 && double1 <= 9)
			return "S";
		else if (double1 > 7 && double1 <= 8)
			return "A+";
		else if (double1 > 6 && double1 <= 7)
			return "A";
		else if (double1 > 5 && double1 <= 6)
			return "A-";
		else if (double1 > 4 && double1 <= 5)
			return "B";
		else if (double1 > 3 && double1 <= 4)
			return "C";
		else if (double1 > 2 && double1 <= 3)
			return "C-";
		else if (double1 > 1 && double1 <= 2)
			return "D";
		else if (double1 >= 0 && double1 <= 1)
			return "E";
		else
			return "None";

	}

	private StatusDto processStatusDto(List<FuncaoDto> funcaoDtos, List<PartidaDoUsuario> partidasDoUsuario) {
		StatusDto statusDto = new StatusDto();

		statusDto.setFuncao(funcaoDtos);
		statusDto.setTotalPartidas(partidasDoUsuario.size());
		statusDto.setCombate(getCombatScore(partidasDoUsuario));
		statusDto.setMediaKda(getMediaKda(partidasDoUsuario));
		statusDto.setOuro(getGoldScore(partidasDoUsuario));
		statusDto.setTempoJogado(getTempoJogado(partidasDoUsuario));
		statusDto.setVisao(getVisionScore(partidasDoUsuario));

		return statusDto;
	}

	private Long getTempoJogado(List<PartidaDoUsuario> partidasDoUsuario) {
		// em minutos
		Long tempoJogado = 0L;
		for (PartidaDoUsuario partidaDoUsuario : partidasDoUsuario) {
			tempoJogado += partidaDoUsuario.getPartida().getDuracaoPartida();
		}
		return tempoJogado;
	}

	private Double getMediaKda(List<PartidaDoUsuario> partidasDoUsuario) {
		Double mediaKda = 0D;

		for (PartidaDoUsuario partidaDoUsuario : partidasDoUsuario) {
			DetalhePartidaUsuario detalhePartidaUsuario = partidaDoUsuario.getDetalhePartidaUsuario();
			if(detalhePartidaUsuario.getMorte() > 0) {
				mediaKda += (double) (detalhePartidaUsuario.getAbate()
						+ detalhePartidaUsuario.getAssistencia())
						/ detalhePartidaUsuario.getMorte();
			} else {
				mediaKda += (double) (detalhePartidaUsuario.getAbate()
						+ detalhePartidaUsuario.getAssistencia());
			}
				
		}
		mediaKda = mediaKda / partidasDoUsuario.size();
		
		
		mediaKda = (double) Math.round(mediaKda * 100) / 100;

		return mediaKda;
	}

	// =========== VISION SCORE E SUBFATORES =================
	private VisionScoreDto getVisionScore(List<PartidaDoUsuario> partidasDoUsuario) {
		VisionScoreDto visionScoreDto = new VisionScoreDto();

		
		visionScoreDto.setKdaAos15min(0D);
		for (PartidaDoUsuario partidaDoUsuario : partidasDoUsuario) {
			

			Partida partida = partidaDoUsuario.getPartida();
			int participantId = partidaDoUsuario.getParticipantId();
			
			
			Double morte  = eventoDaPartidaRepository.findMorteByPartidaEParticipantId(partida, participantId);
			Double kdaAos15;
			if(morte > 0) {
				kdaAos15 = (eventoDaPartidaRepository.findAbateByPartidaEParticipantId(partida, participantId)
						+ eventoDaPartidaRepository.findAssistenciaByPartidaEParticipantId(partida, participantId))
						/ morte;
			} else {
				kdaAos15 = (eventoDaPartidaRepository.findAbateByPartidaEParticipantId(partida, participantId)
						+ eventoDaPartidaRepository.findAssistenciaByPartidaEParticipantId(partida, participantId));
			}
			visionScoreDto.setKdaAos15min(visionScoreDto.getKdaAos15min() + kdaAos15);

		}
		int size = partidasDoUsuario.size();
		visionScoreDto.setKdaAos15min(visionScoreDto.getKdaAos15min() / size);

		List<Double> coCaRw = getCoCaRw(partidasDoUsuario);

		if (coCaRw.size() == 3) {
			visionScoreDto.setControleDeObjetivo(coCaRw.get(0));
			visionScoreDto.setConversaoDeAbates(coCaRw.get(1));
			visionScoreDto.setRelacaoDeWard(coCaRw.get(2));
		}
		
		visionScoreDto.setValor(((visionScoreDto.getControleDeObjetivo() + visionScoreDto.getRelacaoDeWard())/2)*10);
		visionScoreDto.setNota(getNota(visionScoreDto.getValor()));
		
		//ajustar casas decimais
		visionScoreDto.setValor((double) Math.round(visionScoreDto.getValor() * 100) / 100);
		visionScoreDto.setControleDeObjetivo((double) Math.round(visionScoreDto.getControleDeObjetivo() * 100) / 100);
		visionScoreDto.setRelacaoDeWard((double) Math.round(visionScoreDto.getRelacaoDeWard() * 100) / 100);
		visionScoreDto.setKdaAos15min((double) Math.round(visionScoreDto.getKdaAos15min() * 100) / 100);
		visionScoreDto.setConversaoDeAbates((double) Math.round(visionScoreDto.getConversaoDeAbates() * 100) / 100);
		
		
		return visionScoreDto;
	}

	/*
	 * Obtem o controle de objetivos(co), controle de abates (ca) e relação de ward
	 * (rw) a partir de uma lista de partidas do usuario
	 */
	private List<Double> getCoCaRw(List<PartidaDoUsuario> partidasDoUsuario) {
		List<Double> coCaRw = new ArrayList<>();
		int size = partidasDoUsuario.size();

		Double co = 0D;
		Double ca = 0D;
		Double rw = 0D;
		for (PartidaDoUsuario partidaDoUsuario : partidasDoUsuario) {
			Partida partida = partidaDoUsuario.getPartida();
			DetalhePartidaUsuario detalhePartidaUsuario = partidaDoUsuario.getDetalhePartidaUsuario();
			String time = partidaDoUsuario.getTime();
			
			List<Integer> participantes;
			if("100".equals(time)) {
				Integer[] array = {1,2,3,4,5};
				participantes = Arrays.asList(array);
			} else {
				Integer[] array = { 6, 7, 8,9,10};
				participantes = Arrays.asList(array);
			}

			// Controle de Objetivo
			Double findObjectivesByPartida = eventoDaPartidaRepository.findObjectivesByPartida(partida, participantes);
			
			if(findObjectivesByPartida > 0) {
				co += eventoDaPartidaRepository.findObjectivesByPartidaAndParticipantId(partida,
						partidaDoUsuario.getParticipantId())
						/ findObjectivesByPartida;

				// Conversao de Abate
				ca += detalhePartidaUsuario.getAbate() / findObjectivesByPartida;
			}
			
			// relacao de ward
			rw += (detalhePartidaUsuario.getWardsKilled() + detalhePartidaUsuario.getWardsPlaced())
					/ eventoDaPartidaRepository.findTotalWardPlacedByPartida(partida, participantes);

		}

		co = co / size;
		ca = ca / size;
		rw = rw / size;
		coCaRw.add(co);
		coCaRw.add(ca);
		coCaRw.add(rw);
		
		
		

		return coCaRw;
	}
	// =========== FIM VISION SCORE E SUBFATORES =================

	// =========== COMBAT SCORE E SUBFATORES =================
	private CombatScoreDto getCombatScore(List<PartidaDoUsuario> partidasDoUsuario) {
		CombatScoreDto combatScoreDto = new CombatScoreDto();

		
		combatScoreDto.setDanoPorMorte(0D);
		combatScoreDto.setPontuacaoDeUtilidade(0D);
		for (PartidaDoUsuario partidaDoUsuario : partidasDoUsuario) {
			DetalhePartidaUsuario detalhePartidaUsuario = partidaDoUsuario.getDetalhePartidaUsuario();

			

			// Dano por morte
			combatScoreDto
					.setDanoPorMorte(combatScoreDto.getDanoPorMorte() + (double) detalhePartidaUsuario.getDanoCausado()
							/ (detalhePartidaUsuario.getAbate() + detalhePartidaUsuario.getAssistencia()));

			// Pontuacao de Utilidade
			combatScoreDto.setPontuacaoDeUtilidade(combatScoreDto.getPontuacaoDeUtilidade()
					+ (double) (detalhePartidaUsuario.getDamageMitigated() + detalhePartidaUsuario.getDamageHealed()
							+ detalhePartidaUsuario.getCrowdControlDealt()) / 3);
		}

		int size = partidasDoUsuario.size();
		
		combatScoreDto.setDanoPorMorte(combatScoreDto.getDanoPorMorte() / size);
		combatScoreDto.setPontuacaoDeUtilidade(combatScoreDto.getPontuacaoDeUtilidade() / size);

		List<Double> danoCompPartAbate = getDanoCompPartAbate(partidasDoUsuario);
		combatScoreDto.setDanoCompartilhado(danoCompPartAbate.get(0));
		combatScoreDto.setParticipacaoAbates(danoCompPartAbate.get(1));

		//setar valor
		combatScoreDto.setValor(((combatScoreDto.getParticipacaoAbates()*6 + combatScoreDto.getDanoCompartilhado()*4)/10)*10);
		combatScoreDto.setNota(getNota(combatScoreDto.getValor()));
		
		
		//ajustar casas decimais
		combatScoreDto.setValor((double) Math.round(combatScoreDto.getValor() * 100) / 100);
		combatScoreDto.setParticipacaoAbates((double) Math.round(combatScoreDto.getParticipacaoAbates() * 100) / 100);
		combatScoreDto.setDanoCompartilhado((double) Math.round(combatScoreDto.getDanoCompartilhado() * 100) / 100);
		combatScoreDto.setDanoPorMorte((double) Math.round(combatScoreDto.getDanoPorMorte() * 100) / 100);
		combatScoreDto.setPontuacaoDeUtilidade((double) Math.round(combatScoreDto.getPontuacaoDeUtilidade() * 100) / 100);
		
		return combatScoreDto;
	}

	List<Double> getDanoCompPartAbate(List<PartidaDoUsuario> partidaDoUsuarios) {
		List<Double> danoCompPartAbate = new ArrayList<>();

		Double danoCompartilhado = 0D;
		Double participacaoAbates = 0D;
		for (PartidaDoUsuario partidaDoUsuario : partidaDoUsuarios) {

			Partida partida = partidaDoUsuario.getPartida();
			String time = partidaDoUsuario.getTime();
			DetalhePartidaUsuario detalhePartidaUsuario = partidaDoUsuario.getDetalhePartidaUsuario();

			// partida dos usuarios de um time x de uma partida x
			List<PartidaDoUsuario> partidasDoUsuariosTime = partidaDoUsuarioRepository.findAllByPartidaAndTime(partida,
					time);

			// Dano Compartilhado
			danoCompartilhado += (double) detalhePartidaUsuario.getDanoCausado()
					/ detalhePartidaUsuarioRepository.findTotalDanoCausadoPorTime(partidasDoUsuariosTime);

			// Participacao em abates
			participacaoAbates += (double) (detalhePartidaUsuario.getAbate() + detalhePartidaUsuario.getAssistencia())
					/ detalhePartidaUsuarioRepository.findAbateTotalPorTime(partidasDoUsuariosTime);
		}

		int size = partidaDoUsuarios.size();
		danoCompartilhado = danoCompartilhado / size;
		participacaoAbates = participacaoAbates / size;

		danoCompPartAbate.add(danoCompartilhado);
		danoCompPartAbate.add(participacaoAbates);

		return danoCompPartAbate;
	}
	// =========== FIM COMBAT SCORE E SUBFATORES =================

	// =========== GOLD SCORE E SUBFATORES =================
	private GoldScoreDto getGoldScore(List<PartidaDoUsuario> partidasDoUsuario) {
		GoldScoreDto goldScoreDto = new GoldScoreDto();

		
		goldScoreDto.setDanoPorOuro(0D);
		goldScoreDto.setCsPorMin(0D);
		for (PartidaDoUsuario partidaDoUsuario : partidasDoUsuario) {
			Partida partida = partidaDoUsuario.getPartida();
			DetalhePartidaUsuario detalhePartidaUsuario = partidaDoUsuario.getDetalhePartidaUsuario();
			

			// Dano por Ouro
			goldScoreDto.setDanoPorOuro(goldScoreDto.getDanoPorOuro()
					+ (double) detalhePartidaUsuario.getDanoCausado() / detalhePartidaUsuario.getGoldTotal());

			// Cs por min
			goldScoreDto.setCsPorMin(goldScoreDto.getCsPorMin()
					+ (double) detalhePartidaUsuario.getCreepScore() / partida.getDuracaoPartida());
		}

		int size = partidasDoUsuario.size();
		goldScoreDto.setDanoPorOuro(goldScoreDto.getDanoPorOuro() / size);
		goldScoreDto.setCsPorMin(goldScoreDto.getCsPorMin() / size);

		List<Double> vantagens = getVantagens(partidasDoUsuario);
		goldScoreDto.setVantagemCsAos15(vantagens.get(0));
		goldScoreDto.setVantagemDeOuro(vantagens.get(1));
		
		goldScoreDto.setValor(getGoldValue(goldScoreDto));
		goldScoreDto.setNota(getNota(goldScoreDto.getValor()));
		
		
		//ajustar casas decimais
		goldScoreDto.setValor((double) Math.round(goldScoreDto.getValor() * 100) / 100);
		goldScoreDto.setDanoPorOuro((double) Math.round(goldScoreDto.getDanoPorOuro() * 100) / 100);
		goldScoreDto.setVantagemCsAos15((double) Math.round(goldScoreDto.getVantagemCsAos15()));
		goldScoreDto.setVantagemDeOuro((double) Math.round(goldScoreDto.getVantagemDeOuro()* 100) / 100);
		goldScoreDto.setCsPorMin((double) Math.round(goldScoreDto.getCsPorMin() * 100) / 100);
		
		return goldScoreDto;
	}
	
	/*
	 * Gera um valor geral para o placar de gold. de 1 até 10. Calculo de regressão linear.
	 */
	private Double getGoldValue(GoldScoreDto goldScoreDto) {
		Double valor;
		
		Double vantagemCsAos15 = goldScoreDto.getVantagemCsAos15();
		if(vantagemCsAos15 <= -20) {
			valor = 0D;
		} else if(vantagemCsAos15 >= 25) {
			valor = 10D;
		} else {
			valor = ((vantagemCsAos15+20)/45)*10;
		}
		
		
		Double vantagemDeOuro = goldScoreDto.getVantagemDeOuro();
		if(vantagemDeOuro <= -2000) {
			valor += 0;
		} else if(vantagemDeOuro >= 2000) {
			valor += 10;
		} else {
			valor = ((vantagemDeOuro + 2000)/4000) * 10;
		}
		
		return valor/2;
	}

	List<Double> getVantagens(List<PartidaDoUsuario> partidaDoUsuarios) {
		List<Double> vantagens = new ArrayList<>();

		Double vantagemOuro15 = 0D;
		Double vantagemCs15 = 0D;
		
		for (PartidaDoUsuario partidaDoUsuario : partidaDoUsuarios) {
			Partida partida = partidaDoUsuario.getPartida();
			String time = partidaDoUsuario.getTime();
			String role = partidaDoUsuario.getRole();
			String laneType = partidaDoUsuario.getLaneType();

			PartidaDoUsuario partidaDoAdversario = null;
			if (laneType.equals(MIDDLE) || laneType.equals(JUNGLE) || laneType.equals(TOP)) {
				partidaDoAdversario = partidaDoUsuarioRepository.findTopByPartidaAndTimeNotAndLaneType(partida, time, laneType);
			} else {
				partidaDoAdversario = partidaDoUsuarioRepository.findTopByPartidaAndTimeNotAndRole(partida, time, role);
			}
			
			try {
				vantagemOuro15 += (timelineRepository.findOuroAos15(partidaDoUsuario)
						- timelineRepository.findOuroAos15(partidaDoAdversario));
				vantagemCs15 += (timelineRepository.findCsAos15(partidaDoUsuario)
						- timelineRepository.findCsAos15(partidaDoAdversario));
			} catch (Exception e) {
				log.info("nao foi possivel obter valores");
			}
			
		}
		int size = partidaDoUsuarios.size();
		vantagemCs15 = vantagemCs15 / size;
		vantagemOuro15 = vantagemOuro15 / size;

		vantagens.add(vantagemCs15);
		vantagens.add(vantagemOuro15);

		return vantagens;
	}
	// =========== FIM GOLD SCORE E SUBFATORES =================

}
