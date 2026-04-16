package com.cvesters.moneyjars.jar.dto;

import java.math.BigDecimal;

import com.cvesters.moneyjars.jar.bdo.Jar;
import com.cvesters.moneyjars.jar.dao.JarDao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JarDto {

	private Long id;
	private String name;
	private String description;
	private BigDecimal balance; // TODO: float?

	public static JarDto fromBdo(final Jar bdo) {
		if (bdo == null)
			return null;
		return new JarDto(bdo.getId(), bdo.getName(), bdo.getDescription(), bdo.getBalance());
	}
}
