package com.pgpain.unilol.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.core.staticdata.ReforgedRune;
import com.pgpain.unilol.dto.RunaDto;
import com.pgpain.unilol.model.Runa;
import com.pgpain.unilol.repository.RunaRepository;
import com.pgpain.unilol.service.RunaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RunaServiceImpl implements RunaService {

	@Autowired
	RunaRepository runaRepository;

	@Override
	public Runa getRunaByIdApi(Integer id) {
		log.info("capturando informações de runa");

		Optional<Runa> runa = runaRepository.findFirstByRunaIdApi(id);

		if (runa.isPresent())
			return runa.get();
		return new Runa();
	}

	@Override
	public List<Runa> getRunas(List<Integer> ids) {
		List<Runa> runas = new ArrayList<>();
		ids.forEach(id -> {
			Optional<Runa> runa = runaRepository.findFirstByRunaIdApi(id);
			if (runa.isPresent())
				runas.add(runa.get());
		});

		return runas;
	}

	@Override
	public RunaDto getRunaDetailByIdApi(Integer runaIdApi) {
		RunaDto runaDTO = new RunaDto();

		Optional<Runa> runaDb = runaRepository.findFirstByRunaIdApi(runaIdApi);
		ReforgedRune runa = Orianna.reforgedRuneWithId(runaIdApi).get();
		// chamando para preencher
		runa.getLongDescription();
		runa.getPath();
		runa.getSlot();

		runaDTO.setRuna(runa);
		if (runaDb.isPresent()) {
			runaDTO.setCaminhoImagem(runaDb.get().getCaminhoImagem());
			runaDTO.setRunaIdApi(runaIdApi);
			runaDTO.setName(runaDb.get().getNome());
		}
		return runaDTO;
	}

}
