package com.hrw.framework.ahibernate.dao;

import com.hrw.framework.ahibernate.AHibernateOperations;

public interface DaoSupport<T> extends AHibernateOperations {
	int delete(T t);

}
