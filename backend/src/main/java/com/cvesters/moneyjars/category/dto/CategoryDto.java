package com.cvesters.moneyjars.category.dto;

import java.util.Objects;

import lombok.Getter;

import com.cvesters.moneyjars.category.bdo.Category;

@Getter
public class CategoryDto {

	private Long id;
	private String name;
	private String description;

	public CategoryDto(final Category category) {
		Objects.requireNonNull(category);

		this.id = category.getId();
		this.name = category.getName();
		this.description = category.getDescription();
	}
}
