package com.cvesters.moneyjars.jar.bdo;

import java.math.BigDecimal;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

public final class JarAction {

	private JarAction() {
	}

	public static record Create(String name, String description,
			BigDecimal goal) {

		public Create {
			Validate.notBlank(name);
			Objects.requireNonNull(description);
		}

		public Jar toBdo() {
			return new Jar(name, description, goal);
		}
	}

	public static record Update(String name, String description,
			BigDecimal goal) {

		public Update {
			Validate.notBlank(name);
			Objects.requireNonNull(description);
		}

		public void applyOn(final Jar jar) {
			Objects.requireNonNull(jar);

			jar.setName(name);
			jar.setDescription(description);
			jar.setGoal(goal);
		}
	}

}
