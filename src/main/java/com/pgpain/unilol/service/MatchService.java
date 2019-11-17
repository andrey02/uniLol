package com.pgpain.unilol.service;

import com.pgpain.unilol.dto.MatchHistoryDto;
import com.pgpain.unilol.model.Usuario;

public interface MatchService {


    MatchHistoryDto getMatchHistoryFromAPI(Usuario usuario);
    
    MatchHistoryDto getMatchHistoryFromDB(Usuario usuario);
    
    MatchHistoryDto getMatchHistoryFromAPI(Usuario usuario, Long startIndex, Long finalIndex);
    
    
    
}
