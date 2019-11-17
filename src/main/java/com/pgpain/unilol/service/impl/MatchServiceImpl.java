package com.pgpain.unilol.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.core.match.MatchHistory;
import com.merakianalytics.orianna.types.core.match.MatchHistory.Builder;
import com.merakianalytics.orianna.types.core.match.Participant;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import com.pgpain.unilol.dto.CampeaoDto;
import com.pgpain.unilol.dto.ItemDto;
import com.pgpain.unilol.dto.MatchDto;
import com.pgpain.unilol.dto.MatchHistoryDto;
import com.pgpain.unilol.dto.ParticipanteDto;
import com.pgpain.unilol.dto.RunaDto;
import com.pgpain.unilol.dto.SpellDto;
import com.pgpain.unilol.dto.UsuarioDto;
import com.pgpain.unilol.model.Campeao;
import com.pgpain.unilol.model.DetalhePartidaUsuario;
import com.pgpain.unilol.model.Item;
import com.pgpain.unilol.model.ItemDaPartidaDoUsuario;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.model.PartidaDoUsuario;
import com.pgpain.unilol.model.Runa;
import com.pgpain.unilol.model.Spell;
import com.pgpain.unilol.model.Usuario;
import com.pgpain.unilol.repository.CampeaoRepository;
import com.pgpain.unilol.repository.ItemDaPartidaDoUsuarioRepository;
import com.pgpain.unilol.repository.ItemRepository;
import com.pgpain.unilol.repository.PartidaDoUsuarioRepository;
import com.pgpain.unilol.repository.RunaRepository;
import com.pgpain.unilol.repository.SpellRepository;
import com.pgpain.unilol.repository.UsuarioRepository;
import com.pgpain.unilol.service.MatchService;
import com.pgpain.unilol.util.DataBaseOperation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@EnableAsync
public class MatchServiceImpl implements MatchService {

	private static final String GLOBAL_DEFEAT = "global.defeat";
	private static final String GLOBAL_VICTORY = "global.victory";
	private static final int TOTAL_PARTIDA = 20;
	private UsuarioRepository usuarioRepository;
	private PartidaDoUsuarioRepository partidaDoUsuarioRepository;
	private ItemDaPartidaDoUsuarioRepository itemDaPartidaDoUsuarioRepository;
	private DataBaseOperation dataBaseOperation;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private SpellRepository spellRepository;
	@Autowired
	private RunaRepository runaRepository;
	@Autowired
	private CampeaoRepository campeaoRepository;

	public MatchServiceImpl(UsuarioRepository usuarioRepository, PartidaDoUsuarioRepository partidaDoUsuarioRepository,
			ItemDaPartidaDoUsuarioRepository itemDaPartidaDoUsuarioRepository, DataBaseOperation dataBaseOperation) {
		super();
		this.usuarioRepository = usuarioRepository;
		this.partidaDoUsuarioRepository = partidaDoUsuarioRepository;
		this.itemDaPartidaDoUsuarioRepository = itemDaPartidaDoUsuarioRepository;
		this.dataBaseOperation = dataBaseOperation;
	}

	@Override
	public MatchHistoryDto getMatchHistoryFromAPI(Usuario usuario) {
		MatchHistoryDto matchHistoryDTO = new MatchHistoryDto();

		Builder matchFilter = MatchHistory.forSummoner(Orianna.summonerNamed(usuario.getNome()).get()).withStartIndex(0)
				.withEndIndex(TOTAL_PARTIDA);

		MatchHistory matchHistory = matchFilter.get();
		Summoner summoner = Summoner.named(usuario.getNome()).get();

		// setar MatchHistory
		UsuarioDto usuarioNovo = new UsuarioDto();
		usuarioNovo.setNome(usuario.getNome());
		usuarioNovo.setAccountId(usuario.getAccountId());
		usuarioNovo.setIconUrl(matchHistory.getSummoner().getProfileIcon().getImage().getURL());
		matchHistoryDTO.setUsuario(usuarioNovo);
		matchHistoryDTO.setDataFinal(DateTime.now().toString());

		// Pegar id das partidas
		matchHistoryDTO.setPartidas(new ArrayList<MatchDto>());
		List<Long> idDaPartidaAtual = new ArrayList<>();
		matchHistory.forEach(match -> idDaPartidaAtual.add(match.getId()));

		// lista de id dos champions
		List<Integer> idDoCampeaoAtual = new ArrayList<>();
		matchHistory.getCoreData().forEach(matchNew -> idDoCampeaoAtual.add(matchNew.getChampionId()));
		com.merakianalytics.orianna.types.core.match.Match match = null;
		for (int i = 0; i < TOTAL_PARTIDA; i++) {
			match = Orianna.matchWithId(idDaPartidaAtual.get(i)).withRegion(Region.valueOf(usuario.getRegiao())).get();

			MatchDto matchDTO = new MatchDto();
			matchDTO.setMatchId(match.getId());
			matchDTO.setLinguagem(match.getPlatform().getDefaultLocale());

			matchDTO.setDuracaoPartida(match.getDuration().getStandardMinutes());

			// selecionar participante
			Participant participante = match.getParticipants()
					.find(participant -> participant.getSummoner().equals(summoner));

			matchDTO.setMorte(participante.getStats().getDeaths());
			matchDTO.setAbate(participante.getStats().getKills());
			matchDTO.setAsistencia(participante.getStats().getAssists());
			matchDTO.setLaneType(participante.getLane().toString());
			matchDTO.setRole(participante.getRole().toString());
			matchDTO.setGoldTotal(participante.getStats().getGoldEarned());

			if (participante.getTeam().getSide().getId() == 100 && match.getBlueTeam().isWinner()) {
				matchDTO.setResultado(GLOBAL_VICTORY);
			} else if (participante.getTeam().getSide().getId() == 200 && match.getRedTeam().isWinner()) {
				matchDTO.setResultado(GLOBAL_VICTORY);
			} else {
				matchDTO.setResultado(GLOBAL_DEFEAT);
			}

			matchDTO.setLevelCampeao(participante.getStats().getChampionLevel());
			matchDTO.setMapa(match.getMode().name());

			// procurar campeao
			Optional<Campeao> campeao = campeaoRepository.findFirstByCampeaoIdApi(participante.getChampion().getId());
			if (campeao.isPresent())
				matchDTO.setCampeaoDTO(new CampeaoDto(campeao.get().getNome(), campeao.get().getCampeaoIdApi(),
						campeao.get().getCaminhoImagem()));

			// items

			List<ItemDto> itemDTO = new ArrayList<>();
			participante.getItems().forEach(itemNew -> {

				Optional<Item> item = itemRepository.findFirstByItemIdApi(itemNew.getId());
				if (item.isPresent())
					itemDTO.add(new ItemDto(item.get().getNome(), item.get().getItemIdapi(),
							item.get().getCaminhoImagem()));
				else
					itemDTO.add(new ItemDto(Integer.toString(itemNew.getId()), 0, ""));

			});
			matchDTO.setBuild(itemDTO);

			// spells
			List<SpellDto> spells = new ArrayList<>();

			Optional<Spell> spellD = spellRepository.findTopBySpellIdApi(participante.getSummonerSpellD().getKey());
			Optional<Spell> spellF = spellRepository.findTopBySpellIdApi(participante.getSummonerSpellF().getKey());

			if (spellD.isPresent())
				spells.add(new SpellDto(spellD.get().getNome(), spellD.get().getSpellIdApi(),
						spellD.get().getCaminhoImagem()));
			if (spellF.isPresent())
				spells.add(new SpellDto(spellF.get().getNome(), spellF.get().getSpellIdApi(),
						spellF.get().getCaminhoImagem()));
			matchDTO.setSpells(spells);

			// data
			matchDTO.setCreationTime(match.getCreationTime().toString());

			match.getParticipants().get(0);
			// participants
			AtomicInteger counter = new AtomicInteger(1);
			List<ParticipanteDto> participantes = new ArrayList<>();
			match.getParticipants().forEach(participant -> {
				ParticipanteDto part = new ParticipanteDto();

				part.setNome(participant.getSummoner().getName());
				part.setLaneType(participant.getLane().toString());
				part.setRole(participant.getRole().toString());
				Optional<Campeao> campeaoParti = campeaoRepository
						.findFirstByCampeaoIdApi(participant.getChampion().getId());
				if (campeaoParti.isPresent())
					part.setCampeaoDTO(new CampeaoDto(campeaoParti.get().getNome(),
							campeaoParti.get().getCampeaoIdApi(), campeaoParti.get().getCaminhoImagem()));

				part.setTeam(Integer.toString(participant.getTeam().getSide().getId())); // padronizar time
				part.setAccountId(participant.getSummoner().getAccountId());
				part.setRegiao(usuario.getRegiao());
				part.setParticipantId(counter.getAndIncrement());
				part.setDanoCausado(participant.getStats().getDamageDealt());
				participantes.add(part);
			});
			matchDTO.setParticipants(participantes);

			// runes
			List<RunaDto> runa = new ArrayList<>();
			matchDTO.setRunas(new ArrayList<>());
			participante.getRuneStats().forEach(runaNew -> {

				Optional<Runa> rune = runaRepository.findFirstByRunaIdApi(runaNew.getRune().getId());

				if (!rune.isPresent())
					runa.add(new RunaDto(runaNew.getRune().getName(), runaNew.getRune().getId(), ""));
				else
					runa.add(new RunaDto(rune.get().getNome(), rune.get().getRunaIdApi(),
							rune.get().getCaminhoImagem()));
			});
			matchDTO.setRunas(runa);

			// add matchDTo
			if (i == (TOTAL_PARTIDA - 1)) {
				matchHistoryDTO.setDataInicial(match.getCreationTime().toString());
			}
			matchHistoryDTO.getPartidas().add(matchDTO);

		}

		// método asyncrono que carrgega os dados no banco
		try {
			dataBaseOperation.loadToDB(matchHistoryDTO);
		} catch (Exception e) {
			log.debug("falha ao salvar matchHistory. Motivo: " + e.getMessage());
		}
		return matchHistoryDTO;
	}

	@Override
	public MatchHistoryDto getMatchHistoryFromDB(Usuario usuario) {
		MatchHistoryDto matchHistoryDTO = new MatchHistoryDto();

		usuario.setStatus((byte) 1);
		usuario = usuarioRepository.save(usuario);

		List<PartidaDoUsuario> partidas = partidaDoUsuarioRepository.findTop20ByUsuario(usuario);

		// usuarioDTO
		UsuarioDto usuarioDTO = new UsuarioDto();
		usuarioDTO.setAccountId(usuario.getAccountId());
		usuarioDTO.setNome(usuario.getNome());
		usuarioDTO.setIconUrl(usuario.getIconeUrl());
		matchHistoryDTO.setUsuario(usuarioDTO);
		matchHistoryDTO.setDataFinal(DateTime.now().toString());

		if (!partidas.isEmpty()) {

			matchHistoryDTO.setPartidas(new ArrayList<MatchDto>());
			List<Long> idDaPartidaAtual = new ArrayList<>();
			List<Long> idDoCampeaoAtual = new ArrayList<>();
			partidas.forEach(match -> {

				idDaPartidaAtual.add(match.getPartida().getIdPartida());
				if (match.getCampeao() != null)
					idDoCampeaoAtual.add(match.getCampeao().getIdCampeao());
			});

			Partida partida = null;
			int size = partidas.size();
			for (int i = size - 1; i >= 0; i--) {
				PartidaDoUsuario partidaDoUsuario = partidas.get(i);
				partida = partidaDoUsuario.getPartida();

				List<PartidaDoUsuario> outrosUsuarios = partida.getPartidaDoUsuarios();

				MatchDto matchDTO = new MatchDto();
				DetalhePartidaUsuario detalheInvocador = partidaDoUsuario.getDetalhePartidaUsuario();

				if (partidaDoUsuario.getCampeao() != null) {
					matchDTO.setCampeaoDTO(new CampeaoDto(partidaDoUsuario.getCampeao().getNome(),
							partidaDoUsuario.getCampeao().getCampeaoIdApi(),
							partidaDoUsuario.getCampeao().getCaminhoImagem()));

				}

				matchDTO.setDuracaoPartida(partida.getDuracaoPartida());
				matchDTO.setLaneType(partidaDoUsuario.getLaneType());
				matchDTO.setRole(partidaDoUsuario.getRole());

				// kda
				matchDTO.setMapa(partida.getTipoPartida());
				matchDTO.setAbate(detalheInvocador.getAbate());
				matchDTO.setAsistencia(detalheInvocador.getAssistencia());
				matchDTO.setMorte(detalheInvocador.getMorte());
				matchDTO.setMatchId(partida.getIdExterno());
				matchDTO.setGoldTotal(detalheInvocador.getGoldTotal());
				matchDTO.setResultado(detalheInvocador.getResultado());
				matchDTO.setLevelCampeao(detalheInvocador.getNivelCampeao());

				// items, spell, runa List<ItemDaPartidaDoUsuario> itemNovo =
				List<ItemDaPartidaDoUsuario> itemNovo = itemDaPartidaDoUsuarioRepository
						.findAllByIdPartidaDoUsuario(partidaDoUsuario.getIdPartidaDoUsuario());
				List<ItemDto> itemDTO = new ArrayList<>();
				List<SpellDto> spells = new ArrayList<>();
				List<RunaDto> runa = new ArrayList<>();
				matchDTO.setRunas(new ArrayList<>());
				itemNovo.forEach(itens -> {
					if (itens.getFeitico() != null) {
						spells.add(new SpellDto(itens.getNome(), itens.getFeitico().getSpellIdApi(),
								itens.getFeitico().getCaminhoImagem()));
					}
					if (itens.getItem() != null) {
						itemDTO.add(new ItemDto(itens.getNome(), itens.getItem().getItemIdapi(),
								itens.getItem().getCaminhoImagem()));
					}
					if (itens.getRuna() != null) {
						runa.add(new RunaDto(itens.getNome(), itens.getRuna().getRunaIdApi(),
								itens.getRuna().getCaminhoImagem()));
					}
				});
				matchDTO.setSpells(spells);
				matchDTO.setBuild(itemDTO);
				matchDTO.setRunas(runa);

				matchDTO.setCreationTime(partida.getDataCriacao());

				List<ParticipanteDto> participantes = new ArrayList<>();

				outrosUsuarios.forEach(parti -> {
					ParticipanteDto participants = new ParticipanteDto();
					if (parti.getCampeao() != null)
						participants.setCampeaoDTO(new CampeaoDto(parti.getCampeao().getNome(),
								parti.getCampeao().getCampeaoIdApi(), parti.getCampeao().getCaminhoImagem()));
					participants.setDanoCausado(parti.getDetalhePartidaUsuario().getDanoCausado());
					participants.setAccountId(parti.getUsuario().getAccountId());
					participants.setNome(parti.getUsuario().getNome());
					participants.setLaneType(parti.getLaneType());
					participants.setRole(parti.getRole());
					participants.setTeam(parti.getTime());
					participants.setParticipantId(parti.getParticipantId());
					participantes.add(participants);
				});
				matchDTO.setParticipants(participantes);

				matchHistoryDTO.setDataInicial(partida.getDataCriacao());
				matchHistoryDTO.getPartidas().add(matchDTO);
			}

		}
		return matchHistoryDTO;
	}

	@Override
	public MatchHistoryDto getMatchHistoryFromAPI(Usuario usuario, Long startIndex, Long finalIndex) {
		MatchHistoryDto matchHistoryDTO = new MatchHistoryDto();

		Builder matchFilter = MatchHistory.forSummoner(Orianna.summonerNamed(usuario.getNome()).get())
				.withStartIndex(startIndex.intValue()).withEndIndex(finalIndex.intValue());

		MatchHistory matchHistory = matchFilter.get();
		Summoner summoner = Summoner.named(usuario.getNome()).get();

		UsuarioDto usuarioDTO = new UsuarioDto();
		usuarioDTO.setAccountId(usuario.getAccountId());
		usuarioDTO.setNome(usuario.getNome());
		usuarioDTO.setIconUrl(usuario.getIconeUrl());
		matchHistoryDTO.setUsuario(usuarioDTO);
		matchHistoryDTO.setDataFinal(DateTime.now().toString());

		// Pegar id das partidas
		matchHistoryDTO.setPartidas(new ArrayList<MatchDto>());
		List<Long> idDaPartidaAtual = new ArrayList<>();
		matchHistory.forEach(match -> idDaPartidaAtual.add(match.getId()));

		com.merakianalytics.orianna.types.core.match.Match match = null;
		for (int i = 0; i < finalIndex; i++) {
			match = Orianna.matchWithId(idDaPartidaAtual.get(i)).withRegion(Region.valueOf(usuario.getRegiao())).get();

			MatchDto matchDTO = new MatchDto();
			matchDTO.setMatchId(match.getId());
			matchDTO.setLinguagem(match.getPlatform().getDefaultLocale());

			matchDTO.setDuracaoPartida(match.getDuration().getStandardMinutes());

			// selecionar participante
			Participant participante = match.getParticipants()
					.find(participant -> participant.getSummoner().equals(summoner));

			matchDTO.setMorte(participante.getStats().getDeaths());
			matchDTO.setAbate(participante.getStats().getKills());
			matchDTO.setAsistencia(participante.getStats().getAssists());

			matchDTO.setGoldTotal(participante.getStats().getGoldEarned());

			if (participante.getTeam().getSide().getId() == 100 && match.getBlueTeam().isWinner()) {
				matchDTO.setResultado(GLOBAL_VICTORY);
			} else if (participante.getTeam().getSide().getId() == 200 && match.getRedTeam().isWinner()) {
				matchDTO.setResultado(GLOBAL_VICTORY);
			} else {
				matchDTO.setResultado(GLOBAL_DEFEAT);
			}

			matchDTO.setLevelCampeao(participante.getStats().getChampionLevel());
			matchDTO.setMapa(match.getMode().name());

			Optional<Campeao> campeao = campeaoRepository.findFirstByCampeaoIdApi(participante.getChampion().getId());
			if (campeao.isPresent())
				matchDTO.setCampeaoDTO(new CampeaoDto(campeao.get().getNome(), campeao.get().getCampeaoIdApi(),
						campeao.get().getCaminhoImagem()));

			// data
			matchDTO.setCreationTime(match.getCreationTime().toString());

			// participants
			AtomicInteger counter = new AtomicInteger(1);
			List<ParticipanteDto> participantes = new ArrayList<>();
			match.getParticipants().forEach(participant -> {
				ParticipanteDto part = new ParticipanteDto();

				part.setNome(participant.getSummoner().getName());

				Optional<Campeao> campeaoNovo = campeaoRepository
						.findFirstByCampeaoIdApi(participant.getChampion().getId());
				if (campeao.isPresent())
					matchDTO.setCampeaoDTO(new CampeaoDto(campeao.get().getNome(), campeao.get().getCampeaoIdApi(),
							campeao.get().getCaminhoImagem()));

				part.setCampeaoDTO(new CampeaoDto(campeaoNovo.get().getNome(), campeaoNovo.get().getCampeaoIdApi(),
						campeaoNovo.get().getCaminhoImagem()));

				part.setTeam(participant.getTeam().getSide().toString());
				part.setAccountId(participant.getSummoner().getAccountId());
				part.setParticipantId(counter.getAndIncrement());
				part.setDanoCausado(participant.getStats().getDamageDealt());
				part.setLaneType(participant.getLane().toString());
				part.setRole(participant.getRole().toString());
				participantes.add(part);
			});
			matchDTO.setParticipants(participantes);

			// add matchDTo
			if (i == (finalIndex - 1)) {
				matchHistoryDTO.setDataInicial(match.getCreationTime().toString());
			}
			matchHistoryDTO.getPartidas().add(matchDTO);

		}

		// método asyncrono que carrgega os dados no banco
		try {
			dataBaseOperation.loadToDB(matchHistoryDTO);
		} catch (Exception e) {
			log.debug("falha ao salvar matchHistory. Motivo: " + e.getMessage());
		}
		return matchHistoryDTO;
	}
}
