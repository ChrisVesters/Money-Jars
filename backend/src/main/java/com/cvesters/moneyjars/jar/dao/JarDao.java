package com.cvesters.moneyjars.jar.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

import com.cvesters.moneyjars.jar.bdo.Jar;

@Getter
@Setter
@Entity
@Table(name = "jars")
public class JarDao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(lombok.AccessLevel.NONE)
	private Long id;

	private String name;
	private String description;
	private BigDecimal balance = BigDecimal.ZERO;

	public static JarDao fromBdo(final Jar bdo) {
		Objects.requireNonNull(bdo);

		// TODO: or private constructor?
		JarDao dao = new JarDao();
		dao.id = bdo.getId();
		dao.name = bdo.getName();
		dao.description = bdo.getDescription();
		dao.balance = bdo.getBalance();
		return dao;
	}

	public Jar toBdo() {
		return new Jar(id, name, description, balance);
	}
}
