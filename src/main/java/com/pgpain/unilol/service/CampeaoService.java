package com.pgpain.unilol.service;

import java.util.List;

import com.pgpain.unilol.dto.CampeaoCompletoDto;
import com.pgpain.unilol.model.Campeao;

public interface CampeaoService {

	Campeao getCampeaoByIdApi(Integer id);

	List<Campeao> getCampeoes(List<Integer> ids);

	List<Campeao> getCampeoesByNome(List<String> nomes);

	List<CampeaoCompletoDto> getAllCampeoes();
}
