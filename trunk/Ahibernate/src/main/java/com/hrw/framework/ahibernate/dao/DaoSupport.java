package com.hrw.framework.ahibernate.dao;

import java.util.List;

import com.hrw.framework.ahibernate.AHibernateOperations;

public interface DaoSupport<T> extends AHibernateOperations {
	int delete(T t);

	List<T> getAll(Class<T> clazz) throws InstantiationException, IllegalAccessException;
 
}
