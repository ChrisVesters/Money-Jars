package com.cvesters.moneyjars.category.dto;

import com.cvesters.moneyjars.category.bdo.CategoryAction;

public final class CategoryActionDto {

	private CategoryActionDto() {
	}

	public static record Create(String name, String description) {

		public CategoryAction.Create toBdo() {
			return new CategoryAction.Create(name, description);
		}
	}

	public static record Update(String name, String description) {

		public CategoryAction.Update toBdo() {
			return new CategoryAction.Update(name, description);
		}
	}
}
