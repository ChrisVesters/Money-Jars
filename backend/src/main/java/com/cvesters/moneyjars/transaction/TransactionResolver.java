package com.cvesters.moneyjars.transaction;

import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.jar.JarService;
import com.cvesters.moneyjars.jar.dto.JarDto;
import com.cvesters.moneyjars.transaction.dto.TransactionDto;

@Controller
public class TransactionResolver {

	private final JarService jarService;

	public TransactionResolver(final JarService jarService) {
		this.jarService = jarService;
	}

	@SchemaMapping(typeName = "Transaction", field = "jar")
	public JarDto jar(final TransactionDto transaction) {
		return jarService.find(transaction.getJarId())
				.map(JarDto::new)
				.orElse(null);
	}
}
