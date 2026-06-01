package com.cvesters.moneyjars.jar.dao;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cvesters.moneyjars.jar.bdo.Jar;

@Getter
@Entity
@Table(name = "jars")
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class JarDao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String description;
	private BigDecimal balance;

	public JarDao(final Jar bdo) {
		Objects.requireNonNull(bdo);

		this.name = bdo.getName();
		this.description = bdo.getDescription();
		this.balance = bdo.getBalance();
	}

	public void updateWith(final Jar bdo) {
		Objects.requireNonNull(bdo);

		this.name = bdo.getName();
		this.description = bdo.getDescription();
	}

	public Jar toBdo() {
		return new Jar(id, name, description, balance);
	}
}
