package com.cvesters.moneyjars.jar;

import org.springframework.data.repository.ListCrudRepository;

import com.cvesters.moneyjars.jar.dao.JarDao;

public interface JarRepository extends ListCrudRepository<JarDao, Long> {

}
