package com.cvesters.moneyjars.jar;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.jar.bdo.Jar;
import com.cvesters.moneyjars.jar.dto.JarActionDto;
import com.cvesters.moneyjars.jar.dto.JarDto;

@Controller
public class JarController {

	private final JarService jarService;

	public JarController(JarService jarService) {
		this.jarService = jarService;
	}

	@QueryMapping
	public List<JarDto> getJars() {
		return jarService.getJars().stream().map(JarDto::fromBdo).toList();
	}

	@QueryMapping
	public JarDto getJar(@Argument final long id) {
		return jarService.findJar(id).map(JarDto::fromBdo).orElse(null);
	}

	@MutationMapping
	public JarDto createJar(@Argument final JarActionDto.Update req) {
		final Jar jar = req.toBdo();
		final Jar created = jarService.createJar(jar);
		return JarDto.fromBdo(created);
	}

	@MutationMapping
	public JarDto updateJar(@Argument final long id, @Argument JarActionDto.Update req) {
		final Jar jar = req.toBdo();
		final Jar updated = jarService.updateJar(id, jar);
		return JarDto.fromBdo(updated);
	}

	@MutationMapping
	public boolean deleteJar(@Argument final long id) {
		jarService.deleteJar(id);
		return true;
	}
}
