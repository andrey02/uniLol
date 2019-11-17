package com.pgpain.unilol.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.pgpain.unilol.dto.CampeaoCompletoDto;
import com.pgpain.unilol.model.Campeao;
import com.pgpain.unilol.repository.CampeaoRepository;
import com.pgpain.unilol.service.CampeaoService;

@Service
public class CampeaoServiceImpl implements CampeaoService {

	@Autowired
	private CampeaoRepository campeaoRepository;

	@Override
	public Campeao getCampeaoByIdApi(Integer id) {
		Optional<Campeao> campeao = campeaoRepository.findFirstByCampeaoIdApi(id);

		if (campeao.isPresent())
			return campeao.get();
		return null;
	}

	@Override
	public List<Campeao> getCampeoes(List<Integer> ids) {
		List<Campeao> campeoes = new ArrayList<>();
		ids.forEach(id -> {
			if (id != null && id != 0) {
				Optional<Campeao> campeao = campeaoRepository.findFirstByCampeaoIdApi(id);
				if (campeao.isPresent())
					campeoes.add(campeao.get());
			}

		});

		return campeoes;
	}

	@Override
	public List<Campeao> getCampeoesByNome(List<String> nomes) {
		List<Campeao> campeoes = new ArrayList<>();
		nomes.forEach(nome -> {

			if (nome != null && !("".equals(nome))) {
				Optional<Campeao> campeao = campeaoRepository.findByNome(nome);
				if (campeao.isPresent())
					campeoes.add(campeao.get());
			}

		});

		return campeoes;
	}

	@Override
	public List<CampeaoCompletoDto> getAllCampeoes() {
		List<CampeaoCompletoDto> campeoesCompletoDTO = new ArrayList<>();
		List<Campeao> totalCampeao = campeaoRepository.findAll();
		totalCampeao.forEach(campeao -> {
			CampeaoCompletoDto campeaoCompletoDTO = new CampeaoCompletoDto();
			campeaoCompletoDTO.setCampeao(campeao);

			// encontrar status na api da riot
			Champion champion = Orianna.championWithId(campeao.getCampeaoIdApi()).get();

			champion.getRecommendedItems();
			champion.getSkins();
			champion.getSpells();
			champion.getStats();
			champion.getDifficultyRating();
			champion.getEnemyTips();
			champion.getTags();
			champion.getAllyTips();

			campeaoCompletoDTO.setCampeaoDetails(champion);
			// itens recomendados

			campeoesCompletoDTO.add(campeaoCompletoDTO);

		});

		return campeoesCompletoDTO;
	}

}
