package com.pgpain.unilol.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.core.match.Match;
import com.pgpain.unilol.enumeration.FilaDeProcessoEnum;
import com.pgpain.unilol.enumeration.TipoEvento;
import com.pgpain.unilol.model.EventoDaPartida;
import com.pgpain.unilol.model.FilaDeProcesso;
import com.pgpain.unilol.model.Item;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.repository.EventoDaPartidaRepository;
import com.pgpain.unilol.repository.FilaDeProcessoRepository;
import com.pgpain.unilol.repository.ItemRepository;
import com.pgpain.unilol.service.EventoDaPartidaService;
import com.pgpain.unilol.service.FilaDeProcessoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@EnableAsync
public class EventoDaPartidaServiceImpl implements EventoDaPartidaService {

	private static final String KILLER_ID = "killerId";
	private static final String ASSISTING_PARTICIPANTS = "assistingParticipants";
	@Autowired
	private FilaDeProcessoService filaDeProcessoService;
	@Autowired
	private FilaDeProcessoRepository filaDeProcessoRepository;

	private ItemRepository itemRepository;
	private EventoDaPartidaRepository eventoDaPartidaRepository;

	public EventoDaPartidaServiceImpl(EventoDaPartidaRepository eventoDaPartidaRepository,
			ItemRepository itemRepository) {
		super();
		this.eventoDaPartidaRepository = eventoDaPartidaRepository;
		this.itemRepository = itemRepository;
	}

	@Override
	@Async
	public List<EventoDaPartida> saveAll(Partida partida) {
		List<EventoDaPartida> eventoDaPartidas = new ArrayList<>();

		Match match = Orianna.matchWithId(partida.getIdExterno()).get();

		log.debug("==============Inicio da Criacao da Timeline====================");

		String jsonString = match.getTimeline().toJSON();
		// criar json element
		JsonElement je = new Gson().fromJson(jsonString, JsonElement.class);
		JsonObject jo = je.getAsJsonObject();
		JsonElement jsonElement = jo.get("data");
		JsonArray jArray = jsonElement.getAsJsonArray();

		// logica para pegar data
		jArray.forEach(arrayTimeline -> {
			if (arrayTimeline.getAsJsonObject().has("data")) {
				JsonElement eachTimeline = arrayTimeline.getAsJsonObject().get("data");
				JsonArray cadaFrame = eachTimeline.getAsJsonArray();

				cadaFrame.forEach(data -> {
					EventoDaPartida eventoBanco = new EventoDaPartida();
					eventoBanco.setTempo(data.getAsJsonObject().get("timestamp").getAsLong());
					eventoBanco.setPartida(partida);

					if (data.getAsJsonObject().has("position")) {
						JsonObject positionJson = data.getAsJsonObject().get("position").getAsJsonObject();
						eventoBanco.setPosicaoX(positionJson.get("x").getAsLong());
						eventoBanco.setPosicaoY(positionJson.get("y").getAsLong());
					}

					// se tiver participantId
					if (data.getAsJsonObject().has("participantId")) {
						eventoBanco.setParticipantId(data.getAsJsonObject().get("participantId").getAsInt());
					}

					// se tiver participants
					if (data.getAsJsonObject().has(ASSISTING_PARTICIPANTS)) {
						if (data.getAsJsonObject().get(ASSISTING_PARTICIPANTS).isJsonArray()) {
							JsonArray jsonArrayAssistents = data.getAsJsonObject().get(ASSISTING_PARTICIPANTS)
									.getAsJsonArray();
							jsonArrayAssistents.forEach(assistente -> {
								if (eventoBanco.getAssistentes() != null)
									eventoBanco.setAssistentes(eventoBanco.getAssistentes() + assistente + ",");
								else
									eventoBanco.setAssistentes(assistente.toString() + ",");
							});

						} else {
							eventoBanco
									.setAssistentes(data.getAsJsonObject().get(ASSISTING_PARTICIPANTS).getAsString());
						}

					}

					// se tiver item
					if (data.getAsJsonObject().has("itemId")) {
						Optional<Item> itemNovo = itemRepository
								.findFirstByItemIdApi(data.getAsJsonObject().get("itemId").getAsInt());
						if (itemNovo.isPresent())
							eventoBanco.setItem(itemNovo.get());
					}

					if (data.getAsJsonObject().get("type").getAsString()
							.equalsIgnoreCase(TipoEvento.WARD_PLACED.getTipo())) {
						eventoBanco.setTipo(TipoEvento.WARD_PLACED.getTipo());

						if (data.getAsJsonObject().has("wardType"))
							eventoBanco.setWardType(data.getAsJsonObject().get("wardType").getAsString());
						if (data.getAsJsonObject().has("creatorId"))
							eventoBanco.setParticipantId(data.getAsJsonObject().get("creatorId").getAsInt());
					}

					if (data.getAsJsonObject().get("type").getAsString()
							.equalsIgnoreCase(TipoEvento.SKILL_LEVEL_UP.getTipo())) {
						eventoBanco.setTipo(TipoEvento.SKILL_LEVEL_UP.getTipo());
						eventoBanco.setSkill(data.getAsJsonObject().get("skill").getAsInt());
					}

					if (data.getAsJsonObject().get("type").getAsString()
							.equalsIgnoreCase(TipoEvento.CHAMPION_KILL.getTipo())) {
						eventoBanco.setTipo(TipoEvento.CHAMPION_KILL.getTipo());

						if (data.getAsJsonObject().has(KILLER_ID))
							eventoBanco.setKillerId(data.getAsJsonObject().get(KILLER_ID).getAsInt());
						if (data.getAsJsonObject().has("victimId"))
							eventoBanco.setVictimId(data.getAsJsonObject().get("victimId").getAsInt());
					}

					if (data.getAsJsonObject().get("type").getAsString()
							.equalsIgnoreCase(TipoEvento.ITEM_PURCHASED.getTipo())) {
						eventoBanco.setTipo(TipoEvento.ITEM_PURCHASED.getTipo());

					}

					if (data.getAsJsonObject().get("type").getAsString()
							.equalsIgnoreCase(TipoEvento.BUILDING_KILL.getTipo())) {
						eventoBanco.setTipo(TipoEvento.BUILDING_KILL.getTipo());

						if (data.getAsJsonObject().has("buildingType"))
							eventoBanco.setBuildingType(data.getAsJsonObject().get("buildingType").getAsString());

						if (data.getAsJsonObject().has("laneType"))
							eventoBanco.setLaneType(data.getAsJsonObject().get("laneType").getAsString());
						if (data.getAsJsonObject().has("turretType"))
							eventoBanco.setTurretType(data.getAsJsonObject().get("turretType").getAsString());

						if (data.getAsJsonObject().has(KILLER_ID))
							eventoBanco.setKillerId(data.getAsJsonObject().get(KILLER_ID).getAsInt());

						eventoBanco.setTime(data.getAsJsonObject().get("team").getAsString());

					}

					if (data.getAsJsonObject().get("type").getAsString()
							.equalsIgnoreCase(TipoEvento.ELITE_MONSTER_KILL.getTipo())) {
						eventoBanco.setTipo(TipoEvento.ELITE_MONSTER_KILL.getTipo());
						if (data.getAsJsonObject().has("monsterSubType"))
							eventoBanco.setMonsterSubType(data.getAsJsonObject().get("monsterSubType").getAsString());
						if (data.getAsJsonObject().has(KILLER_ID))
							eventoBanco.setKillerId(data.getAsJsonObject().get(KILLER_ID).getAsInt());
					}

					if (data.getAsJsonObject().get("type").getAsString()
							.equalsIgnoreCase(TipoEvento.ITEM_DESTROYED.getTipo())) {
						eventoBanco.setTipo(TipoEvento.ITEM_DESTROYED.getTipo());

					}
					try {
						eventoDaPartidas.add(eventoDaPartidaRepository.save(eventoBanco));
					} catch (Exception e) {
						log.info("evento não processado para partida " + partida.getIdExterno());
					}

				});
			}
		});
		log.debug("==============Termino da Criacao do Evento da Partida====================");

		filaDeProcessoService.saveFilaDeProcesso(null, partida, FilaDeProcessoEnum.STATUS_SUCESSO.getValue(),
				FilaDeProcessoEnum.TIPO_EVENTO.getValue());

		FilaDeProcesso processado = filaDeProcessoService.getFilaDeProcessoByUsuarioAndPartidaAndTipoAndStatus(null,
				partida, FilaDeProcessoEnum.TIPO_EVENTO.getValue(), FilaDeProcessoEnum.STATUS_PROCESSANDO.getValue());
		try {
			if (processado != null) {
				processado.setStatus(FilaDeProcessoEnum.STATUS_PROCESSADO.getValue());
				filaDeProcessoRepository.save(processado);
			}
		} catch (Exception e) {
			log.info("Nao foi possível acessar dados da fila de processo");
		}

		return eventoDaPartidas;
	}

	@Override
	public List<EventoDaPartida> getEventoByPartidaAndParticipantId(Partida partida, Integer participantId) {

		return eventoDaPartidaRepository.findEventoByParticipantIdAndPartida(partida, participantId);
	}

	@Override
	public List<EventoDaPartida> getEventoByPartidaAndTime(Partida partida, String time) {
		List<EventoDaPartida> eventoDaPartidas = new ArrayList<>();

		// encontrar participantes do time
		List<String> participantes = new ArrayList<>();

		if ("100".equals(time)) {
			participantes.add("1");
			participantes.add("2");
			participantes.add("3");
			participantes.add("4");
			participantes.add("5");
		} else {
			participantes.add("6");
			participantes.add("7");
			participantes.add("8");
			participantes.add("9");
			participantes.add("10");
		}
		// eventos do time específico 100 ou 200
		eventoDaPartidas.addAll(eventoDaPartidaRepository.findAllByPartidaAndTime(partida, time));

		// eventos com cada participantId (sem contar assistentes) e sem ter time
		eventoDaPartidas.addAll(eventoDaPartidaRepository.findAllByParticipantIdIn(partida, participantes));

		// todos os eventos de champion_kill envolvem os dois times
		eventoDaPartidas
				.addAll(eventoDaPartidaRepository.findAllByPartidaAndTipo(partida, TipoEvento.CHAMPION_KILL.getTipo()));

		return eventoDaPartidas;
	}

}
