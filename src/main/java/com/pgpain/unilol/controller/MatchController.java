package com.pgpain.unilol.controller;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Region;
import com.pgpain.unilol.dto.MatchHistoryDTO;
import com.pgpain.unilol.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping ("/api")
public class MatchController {

    MatchService matchService;

    @Value("${API_KEY}")
    private String apiKey;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping (value = "/match/{summonerName}/{region}")
    public ResponseEntity<MatchHistoryDTO> getMatchHistory(@PathVariable String summonerName, @PathVariable String region){
        log.debug("REST request to get MatchHistory : {}", summonerName);

        Orianna.setRiotAPIKey(apiKey);
        Orianna.setDefaultRegion(Region.BRAZIL);
        //checar se usuario existe,se nao enviar nulo
        if(summonerName.equals("") || summonerName == null){
            return null;
        }

        MatchHistoryDTO matchHistoryDTO = matchService.getMatchHistory(summonerName,region);

        if (matchHistoryDTO == null) {
            return null;
        }

        return new ResponseEntity<>(matchHistoryDTO, HttpStatus.OK);
    }
}
