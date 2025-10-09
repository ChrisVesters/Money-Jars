package com.cvesters.jar;

import org.springframework.stereotype.Service;

import com.cvesters.jar.bdo.Jar;
import com.cvesters.jar.dao.JarDao;

@Service
public class JarStorageGateway {

		private final JarRepository jarRepository;

	public JarStorageGateway(JarRepository jarRepository) {
		this.jarRepository = jarRepository;
	}

	public Jar createJar(final Jar jar) {
		final JarDao dao = JarDao.fromBdo(jar);
		final JarDao created = jarRepository.save(dao);
		return created.toBdo();
	}
}
