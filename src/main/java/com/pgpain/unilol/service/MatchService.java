package com.pgpain.unilol.service;

import com.merakianalytics.orianna.types.dto.match.Match;
import com.pgpain.unilol.dto.MatchHistoryDTO;

public interface MatchService {

    Match getMatchById(Long matchId);

    MatchHistoryDTO getMatchHistory(String summonerName, String region);
}
