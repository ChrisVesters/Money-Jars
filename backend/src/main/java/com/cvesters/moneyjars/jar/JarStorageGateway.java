package com.cvesters.moneyjars.jar;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.jar.bdo.Jar;
import com.cvesters.moneyjars.jar.dao.JarDao;

@Service
public class JarStorageGateway {

	private final JarRepository jarRepository;

	public JarStorageGateway(final JarRepository jarRepository) {
		this.jarRepository = jarRepository;
	}

	public List<Jar> getAll() {
		return jarRepository.findAll().stream().map(JarDao::toBdo).toList();
	}

	public Optional<Jar> find(final long id) {
		return jarRepository.findById(id).map(JarDao::toBdo);
	}

	public Jar create(final Jar jar) {
		Objects.requireNonNull(jar);

		var dao = new JarDao(jar);
		final JarDao created = jarRepository.save(dao);

		return created.toBdo();
	}

	public Jar update(final Jar jar) {
		Objects.requireNonNull(jar);

		final JarDao found = jarRepository.findById(jar.getId())
				.orElseThrow(IllegalArgumentException::new);
		found.updateWith(jar);
		final JarDao updated = jarRepository.save(found);

		return updated.toBdo();
	}

	public void delete(final long id) {
		jarRepository.deleteById(id);
	}

}
