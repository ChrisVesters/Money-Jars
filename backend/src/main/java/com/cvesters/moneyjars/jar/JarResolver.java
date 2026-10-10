package com.cvesters.moneyjars.jar;

import java.math.BigDecimal;

import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.jar.dto.JarDto;
import com.cvesters.moneyjars.jarentry.JarEntryService;
import com.cvesters.moneyjars.jarentry.bdo.JarEntry;

@Controller
public class JarResolver {

	private final JarEntryService entryService;

	public JarResolver(final JarEntryService entryService) {
		this.entryService = entryService;
	}

	@SchemaMapping(typeName = "Jar", field = "balance")
	public BigDecimal balance(final JarDto jar) {
		return entryService.findLast(jar.getId())
				.map(JarEntry::getBalanceAfter)
				.orElse(BigDecimal.ZERO);
	}

}
