package com.pgpain.unilol.service;

import java.util.List;

import com.pgpain.unilol.dto.RunaDto;
import com.pgpain.unilol.model.Runa;

public interface RunaService {
	Runa getRunaByIdApi(Integer id);

	List<Runa> getRunas(List<Integer> ids);

	RunaDto getRunaDetailByIdApi(Integer runaIdApi);
}
