package com.cvesters.jar.dto;

import java.math.BigDecimal;

import com.cvesters.jar.dao.JarDao;
import com.cvesters.jar.bdo.Jar;

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
	private BigDecimal balance;

	public static JarDto fromBdo(final Jar bdo) {
		if (bdo == null)
			return null;
		return new JarDto(bdo.getId(), bdo.getName(), bdo.getDescription(), bdo.getBalance());
	}
}
