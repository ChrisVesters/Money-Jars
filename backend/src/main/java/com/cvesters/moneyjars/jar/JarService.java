package com.cvesters.moneyjars.jar;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.jar.bdo.Jar;
import com.cvesters.moneyjars.jar.bdo.JarAction;

@Service
public class JarService {

	private final JarStorageGateway storage;

	public JarService(final JarStorageGateway storage) {
		this.storage = storage;
	}

	public List<Jar> getAll() {
		return storage.getAll();
	}

	public Optional<Jar> find(final long id) {
		return storage.find(id);
	}

	public Jar create(final JarAction.Create action) {
		Objects.requireNonNull(action);

		final Jar jar = action.toBdo();
		return storage.create(jar);
	}

	public Jar update(final long id, final JarAction.Update action) {
		Objects.requireNonNull(action);

		final Jar jar = storage.find(id)
				.orElseThrow(MissingEntityException::new);
		action.applyOn(jar);

		return storage.update(jar);
	}

	public void delete(final long id) {
		storage.delete(id);
	}
}
