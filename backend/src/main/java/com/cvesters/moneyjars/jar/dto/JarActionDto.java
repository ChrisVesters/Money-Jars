package com.cvesters.moneyjars.jar.dto;

import com.cvesters.moneyjars.jar.bdo.Jar;

public final class JarActionDto {

	private JarActionDto() {
	}

	public static record Update(String name,
			String description) {

		public Jar toBdo() {
			return new Jar(name, description);
		}
	}

}
