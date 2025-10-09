package com.cvesters.moneyjars;

import org.springframework.boot.SpringApplication;

public class TestMoneyJarsApplication {

	public static void main(String[] args) {
		SpringApplication.from(MoneyJarsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
