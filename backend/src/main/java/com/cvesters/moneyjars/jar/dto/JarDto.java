package com.cvesters.moneyjars.jar.dto;

import java.util.Objects;

import lombok.Getter;

import com.cvesters.moneyjars.jar.bdo.Jar;

@Getter
public class JarDto {

	private Long id;
	private String name;
	private String description;

	public JarDto(final Jar bdo) {
		Objects.requireNonNull(bdo);

		this.id = bdo.getId();
		this.name = bdo.getName();
		this.description = bdo.getDescription();
	}
}
