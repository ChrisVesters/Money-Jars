package com.cvesters.moneyjars.common.bdo;

import org.apache.commons.lang3.Validate;

public record Pagination(int page, int size) {

	public Pagination {
		Validate.isTrue(page >= 0);
		Validate.isTrue(size > 0);
	}
}
