package com.cvesters.moneyjars.transaction;

import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.jar.JarService;
import com.cvesters.moneyjars.jar.bdo.Jar;
import com.cvesters.moneyjars.jar.dto.JarDto;
import com.cvesters.moneyjars.transaction.dto.JarTransferTransactionDto;

@Controller
public class JarTransferTransactionResolver {

	private final JarService jarService;

	public JarTransferTransactionResolver(final JarService jarService) {
		this.jarService = jarService;
	}

	@SchemaMapping(typeName = "JarTransferTransaction", field = "sourceJar")
	public JarDto sourceJar(final JarTransferTransactionDto transaction) {
		final Jar jar = jarService.get(transaction.getSourceJarId());
		return new JarDto(jar);
	}

	@SchemaMapping(typeName = "JarTransferTransaction", field = "targetJar")
	public JarDto targetJar(final JarTransferTransactionDto transaction) {
		final Jar jar = jarService.get(transaction.getTargetJarId());
		return new JarDto(jar);
	}
}
