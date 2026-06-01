package com.cvesters.moneyjars.jar.dto;

import com.cvesters.moneyjars.jar.bdo.JarAction;

public final class JarActionDto {

	private JarActionDto() {
	}

	public static record Create(String name, String description) {

		public JarAction.Create toBdo() {
			return new JarAction.Create(name, description);
		}
	}

	public static record Update(String name, String description) {

		public JarAction.Update toBdo() {
			return new JarAction.Update(name, description);
		}
	}

}
