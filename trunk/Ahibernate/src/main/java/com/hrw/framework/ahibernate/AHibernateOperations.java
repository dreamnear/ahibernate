package com.hrw.framework.ahibernate;

import java.sql.SQLException;


public interface AHibernateOperations {
	int save(Object entity) throws IllegalArgumentException, IllegalAccessException, SQLException;
}
