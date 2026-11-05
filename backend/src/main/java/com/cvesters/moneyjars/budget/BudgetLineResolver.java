package com.cvesters.moneyjars.budget;

import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.budget.dto.BudgetLineDto;
import com.cvesters.moneyjars.category.CategoryService;
import com.cvesters.moneyjars.category.bdo.Category;
import com.cvesters.moneyjars.category.dto.CategoryDto;

@Controller
public class BudgetLineResolver {

	private final CategoryService categoryService;

	public BudgetLineResolver(final CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@SchemaMapping(typeName = "BudgetLine", field = "category")
	public CategoryDto category(final BudgetLineDto line) {
		final Category category = categoryService.get(line.getCategoryId());
		return new CategoryDto(category);
	}
}
