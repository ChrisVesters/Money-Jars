package com.cvesters.moneyjars.category;

import org.springframework.data.repository.ListCrudRepository;

import com.cvesters.moneyjars.category.dao.CategoryDao;

public interface CategoryRepository
		extends ListCrudRepository<CategoryDao, Long> {

}
