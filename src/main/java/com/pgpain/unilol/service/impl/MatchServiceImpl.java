package com.pgpain.unilol.service.impl;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.core.match.MatchHistory;
import com.merakianalytics.orianna.types.core.match.MatchHistory.Builder;
import com.merakianalytics.orianna.types.data.match.MatchList;
import com.merakianalytics.orianna.types.dto.match.Match;
import com.pgpain.unilol.dto.MatchDTO;
import com.pgpain.unilol.dto.MatchHistoryDTO;
import com.pgpain.unilol.service.MatchService;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

@Service
public class MatchServiceImpl implements MatchService {

	@Override
	public Match getMatchById(Long matchId) {
		return null;
	}

	@Override
	public MatchHistoryDTO getMatchHistory(String summonerName, String region) {
		MatchHistoryDTO matchHistoryDTO = new MatchHistoryDTO();

		Builder matchFilter = MatchHistory.forSummoner(Orianna.summonerNamed(summonerName).get())
				.withEndTime(DateTime.now()).withStartTime(DateTime.now().minusDays(2));
		MatchHistory matchHistory = matchFilter.get();
		
		matchHistoryDTO.setMatchs(matchHistory.getCoreData());

		return matchHistoryDTO;
	}
}
