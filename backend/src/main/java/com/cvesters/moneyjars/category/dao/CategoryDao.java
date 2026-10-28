package com.cvesters.moneyjars.category.dao;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cvesters.moneyjars.category.bdo.Category;

@Getter
@Entity
@Table(name = "categories")
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CategoryDao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String description;

	public CategoryDao(final Category category) {
		Objects.requireNonNull(category);

		this.name = category.getName();
		this.description = category.getDescription();
	}

	public void updateWith(final Category category) {
		Objects.requireNonNull(category);

		this.name = category.getName();
		this.description = category.getDescription();
	}

	public Category toBdo() {
		return new Category(id, name, description);
	}
}
