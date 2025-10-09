package com.cvesters.jar;

import org.springframework.data.repository.ListCrudRepository;

import com.cvesters.jar.dao.JarDao;

public interface JarRepository extends ListCrudRepository<JarDao, Long> {

}
