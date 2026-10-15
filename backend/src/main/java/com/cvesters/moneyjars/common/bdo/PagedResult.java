package com.cvesters.moneyjars.common.bdo;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

public record PagedResult<T>(List<T> content, int page, int size,
		long totalElements) {

	public PagedResult {
		Objects.requireNonNull(content);
		Validate.isTrue(page >= 0);
		Validate.isTrue(size > 0);
		Validate.isTrue(content.size() <= size);
		Validate.isTrue(totalElements >= 0);
	}
}
