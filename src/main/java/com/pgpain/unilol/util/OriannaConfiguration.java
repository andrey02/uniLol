package com.pgpain.unilol.util;

import org.springframework.stereotype.Component;

import com.merakianalytics.orianna.types.common.Region;

@Component
public class OriannaConfiguration {
	
	public boolean eRegiaoValida(String region) {
		Region[] values = Region.values();
		for (Region me : values) {
		        if (me.name().equalsIgnoreCase(region))
		        	return true;
		}
		return false;
	}
}
