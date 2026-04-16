package com.cvesters.moneyjars.jar;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.jar.bdo.Jar;
import com.cvesters.moneyjars.jar.dao.JarDao;

@Service
public class JarStorageGateway {

	private final JarRepository jarRepository;

	public JarStorageGateway(JarRepository jarRepository) {
		this.jarRepository = jarRepository;
	}

	public List<Jar> getJars() {
		return jarRepository.findAll().stream().map(JarDao::toBdo).toList();
	}

	// TODO: This should be not created, or update?
	// Instead it should just do a save.
	// The service should be responsible for uddating.
	// AAAAh, but what about the id?
	// Ah, that is part of the jar itself.
	public Jar createJar(final Jar jar) {
		final JarDao dao = JarDao.fromBdo(jar);
		final JarDao created = jarRepository.save(dao);
		return created.toBdo();
	}

	public Jar updateJar(final long id, final Jar jar) {
		final JarDao found = jarRepository.findById(id)
				.orElseThrow(IllegalArgumentException::new);

		found.setName(jar.getName());
		found.setDescription(jar.getDescription());

		final JarDao updated = jarRepository.save(found);
		return updated.toBdo();
	}

	public Optional<Jar> findJar(final long id) {
		return jarRepository.findById(id).map(JarDao::toBdo);
	}

	public void deleteJar(final long id) {
		jarRepository.deleteById(id);
	}

}
