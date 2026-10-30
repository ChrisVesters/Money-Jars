package com.cvesters.moneyjars.category;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.category.bdo.Category;
import com.cvesters.moneyjars.category.bdo.CategoryAction;
import com.cvesters.moneyjars.category.dto.CategoryActionDto;
import com.cvesters.moneyjars.category.dto.CategoryDto;

@Controller
public class CategoryController {

	private final CategoryService categoryService;

	public CategoryController(final CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@QueryMapping
	public List<CategoryDto> getCategories() {
		return categoryService.getAll().stream().map(CategoryDto::new).toList();
	}

	@QueryMapping
	public CategoryDto getCategory(@Argument final long id) {
		final Category Category = categoryService.get(id);
		return new CategoryDto(Category);
	}

	@MutationMapping
	public CategoryDto createCategory(
			@Argument final CategoryActionDto.Create req) {
		final CategoryAction.Create action = req.toBdo();
		final Category created = categoryService.create(action);
		return new CategoryDto(created);
	}

	@MutationMapping
	public CategoryDto updateCategory(@Argument final long id,
			@Argument final CategoryActionDto.Update req) {
		final CategoryAction.Update action = req.toBdo();
		final Category updated = categoryService.update(id, action);
		return new CategoryDto(updated);
	}

	@MutationMapping
	public boolean deleteCategory(@Argument final long id) {
		categoryService.delete(id);
		return true;
	}
}
