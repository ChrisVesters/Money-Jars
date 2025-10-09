package com.cvesters.jar;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cvesters.jar.bdo.Jar;
import com.cvesters.jar.dto.CreateJarRequest;
import com.cvesters.jar.dto.JarDto;

@RestController
@RequestMapping("/api/jars")
public class JarController {

	private final JarService jarService;

	public JarController(JarService jarService) {
		this.jarService = jarService;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JarDto> createJar(@RequestBody CreateJarRequest req) {
		final Jar jar = req.toBdo();
		final Jar created = jarService.createJar(jar);

		final JarDto dto = JarDto.fromBdo(created);

		return ResponseEntity
				.created(ServletUriComponentsBuilder.fromCurrentRequest()
						.path("/{id}")
						.buildAndExpand(created.getId())
						.toUri())
				.body(dto);
	}
}
