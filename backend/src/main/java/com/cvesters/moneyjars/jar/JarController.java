package com.cvesters.moneyjars.jar;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.jar.bdo.Jar;
import com.cvesters.moneyjars.jar.bdo.JarAction;
import com.cvesters.moneyjars.jar.dto.JarActionDto;
import com.cvesters.moneyjars.jar.dto.JarDto;

@Controller
public class JarController {

	private final JarService jarService;

	public JarController(final JarService jarService) {
		this.jarService = jarService;
	}

	@QueryMapping
	public List<JarDto> getJars() {
		return jarService.getAll().stream().map(JarDto::new).toList();
	}

	@QueryMapping
	public JarDto getJar(@Argument final long id) {
		return jarService.find(id).map(JarDto::new).orElse(null);
	}

	@MutationMapping
	public JarDto createJar(@Argument final JarActionDto.Create req) {
		final JarAction.Create jarAction = req.toBdo();
		final Jar created = jarService.create(jarAction);
		return new JarDto(created);
	}

	@MutationMapping
	public JarDto updateJar(@Argument final long id,
			@Argument JarActionDto.Update req) {
		final JarAction.Update jarAction = req.toBdo();
		final Jar updated = jarService.update(id, jarAction);
		return new JarDto(updated);
	}

	@MutationMapping
	public boolean deleteJar(@Argument final long id) {
		jarService.delete(id);
		return true;
	}
}
