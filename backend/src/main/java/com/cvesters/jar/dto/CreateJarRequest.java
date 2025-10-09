package com.cvesters.jar.dto;

import java.math.BigDecimal;

import com.cvesters.jar.bdo.Jar;

public record CreateJarRequest(String name, String description, BigDecimal balance) {

	// TODO: validation

	public Jar toBdo() {
		return new Jar(name, description, balance);
	}
}
