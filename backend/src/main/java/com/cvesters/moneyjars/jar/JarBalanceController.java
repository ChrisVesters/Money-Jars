package com.cvesters.moneyjars.jar;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.jar.dto.JarBalanceHistoryDto;
import com.cvesters.moneyjars.jar.dto.JarBalanceQueryDto;

@Controller
public class JarBalanceController {

	private final JarBalanceService jarBalanceService;

	public JarBalanceController(final JarBalanceService jarBalanceService) {
		this.jarBalanceService = jarBalanceService;
	}

	@QueryMapping
	public List<JarBalanceHistoryDto> queryJarBalanceHistory(
			@Argument final JarBalanceQueryDto req) {
		return jarBalanceService.getHistory(req.jarIds())
				.stream()
				.map(JarBalanceHistoryDto::new)
				.toList();
	}

}
