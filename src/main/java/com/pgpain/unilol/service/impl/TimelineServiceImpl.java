package com.pgpain.unilol.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.merakianalytics.orianna.types.core.match.Match;
import com.pgpain.unilol.model.Partida;
import com.pgpain.unilol.model.PartidaDoUsuario;
import com.pgpain.unilol.model.Timeline;
import com.pgpain.unilol.service.TimelineService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TimelineServiceImpl implements TimelineService {

	@Override
	public List<Timeline> create(String participantId, Match match) {
		List<Timeline> timelines = new ArrayList<>();

		log.info("==============Inicio da Criacao da Timeline====================");

		String jsonString = match.getTimeline().toJSON();
		// criar json element
		JsonElement je = new Gson().fromJson(jsonString, JsonElement.class);
		JsonObject jo = je.getAsJsonObject();
		JsonElement jsonElement = jo.get("data");
		JsonArray jArray = jsonElement.getAsJsonArray();

		// logica para pegar participantFrames
		jArray.forEach(time -> {
			Timeline timeline = new Timeline();
			if (time.getAsJsonObject().has("participantFrames")) {
				JsonElement jsonElement2 = time.getAsJsonObject().get("participantFrames");
				JsonObject asJsonArray2 = jsonElement2.getAsJsonObject();
				JsonObject asJsonObject = asJsonArray2.get(participantId).getAsJsonObject();
				timeline.setTempo(time.getAsJsonObject().get("timestamp").getAsLong());
				timeline.setOuroAdquirido(asJsonObject.get("goldEarned").getAsLong());
				timeline.setLevel(asJsonObject.get("level").getAsLong());

				if (asJsonObject.getAsJsonObject().has("gold"))
					timeline.setOuro(asJsonObject.get("gold").getAsLong());
				if (asJsonObject.getAsJsonObject().has("experience"))
					timeline.setExperiencia(asJsonObject.get("experience").getAsLong());
				if (asJsonObject.getAsJsonObject().has("neutralMinionsKilled"))
					timeline.setQtdMonstroNeutro(asJsonObject.get("neutralMinionsKilled").getAsLong());
				if (asJsonObject.getAsJsonObject().has("creepScore"))
					timeline.setQtdCreep(asJsonObject.get("creepScore").getAsLong());

				// position
				if (asJsonObject.getAsJsonObject().has("position")) {
					JsonObject positionJson = asJsonObject.getAsJsonObject().get("position").getAsJsonObject();
					timeline.setPosicaoX(positionJson.get("x").getAsLong());
					timeline.setPosicaoY(positionJson.get("y").getAsLong());
				}

				timelines.add(timeline);

			}
		});
		log.debug("==============Fim da Criacao da Timeline====================");

		return timelines;
	}

	@Override
	public List<Timeline> getTimelinesByPartida(Partida partida) {
		List<Timeline> timelines = new ArrayList<>();

		List<PartidaDoUsuario> partidaDoUsuarios = partida.getPartidaDoUsuarios();
		partidaDoUsuarios.forEach(partidaDoUsuario -> timelines.addAll(partidaDoUsuario.getTimeline()));

		return timelines;
	}

}
