package com.cvesters.jar;

import org.springframework.stereotype.Service;

import com.cvesters.jar.bdo.Jar;

@Service
public class JarService {

	private final JarStorageGateway storage;

	public JarService(final JarStorageGateway storage) {
		this.storage = storage;
	}

	public Jar createJar(final Jar jar) {
		// TODO: verify unique name
		return storage.createJar(jar);
	}
}
