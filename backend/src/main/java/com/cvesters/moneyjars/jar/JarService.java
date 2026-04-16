package com.cvesters.moneyjars.jar;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.jar.bdo.Jar;

@Service
public class JarService {

	private final JarStorageGateway storage;

	public JarService(final JarStorageGateway storage) {
		this.storage = storage;
	}

	public List<Jar> getJars() {
		return storage.getJars();
	}

	public Jar createJar(final Jar jar) {
		// TODO: verify unique name!?
		return storage.createJar(jar);
	}

	public Jar updateJar(final long id, final Jar jar) {
		return storage.updateJar(id, jar);
	}

	public Optional<Jar> findJar(final long id) {
		return storage.findJar(id);
	}

	public void deleteJar(final long id) {
		storage.deleteJar(id);
	}
}
