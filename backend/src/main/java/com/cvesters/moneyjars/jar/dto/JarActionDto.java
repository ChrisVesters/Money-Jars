package com.cvesters.moneyjars.jar.dto;

import java.math.BigDecimal;
import java.util.Optional;

import com.cvesters.moneyjars.jar.bdo.JarAction;

public final class JarActionDto {

	private JarActionDto() {
	}

	public static record Create(String name, String description, String goal) {

		public JarAction.Create toBdo() {
			final BigDecimal goalValue = Optional.ofNullable(goal)
					.map(BigDecimal::new)
					.orElse(null);

			return new JarAction.Create(name, description, goalValue);
		}
	}

	public static record Update(String name, String description, String goal) {

		public JarAction.Update toBdo() {
			final BigDecimal goalValue = Optional.ofNullable(goal)
					.map(BigDecimal::new)
					.orElse(null);

			return new JarAction.Update(name, description, goalValue);
		}
	}

}
