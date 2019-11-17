package com.pgpain.unilol.util;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.core.match.Match;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SandBox {

	public static void main(String[] args) {
		Orianna.setRiotAPIKey("RGAPI-2309b496-ba28-43e0-90e9-e8f8b674d5f2");
		Orianna.setDefaultRegion(Region.BRAZIL);
		Match match = Orianna.matchWithId(1640144547).withRegion(Region.BRAZIL).get();

		String jsonString = match.getTimeline().toJSON();
		// for each on data and forech on participants frame
		JsonElement je = new Gson().fromJson(jsonString, JsonElement.class);
		JsonObject jo = je.getAsJsonObject();
		JsonElement jsonElement = jo.get("data");
		JsonArray asJsonArray = jsonElement.getAsJsonArray();

		// logica para pegar data
		/*
		 * asJsonArray.forEach(time -> { if(time.getAsJsonObject().has("data")) {
		 * JsonElement jsonElement2 = time.getAsJsonObject().get("data"); JsonArray
		 * asJsonArray2 = jsonElement2.getAsJsonArray(); asJsonArray2.forEach(data -> {
		 * 
		 * //tipo de evento
		 * if(data.getAsJsonObject().get("type").getAsString().equalsIgnoreCase(
		 * "BUILDING_KILL")) { System.out.println(data.getAsJsonObject()); }
		 * 
		 * //System.out.println(data.getAsJsonObject().get("timestamp"));
		 * 
		 * }); } });
		 */

		// logica para pegar participantFrame
		asJsonArray.forEach(time -> {
			if (time.getAsJsonObject().has("participantFrames")) {
				JsonElement jsonElement2 = time.getAsJsonObject().get("participantFrames");
				JsonObject asJsonArray2 = jsonElement2.getAsJsonObject();
				JsonObject asJsonObject = asJsonArray2.get("9").getAsJsonObject();
				log.info(asJsonObject.toString());

			}
		});

	}

}
