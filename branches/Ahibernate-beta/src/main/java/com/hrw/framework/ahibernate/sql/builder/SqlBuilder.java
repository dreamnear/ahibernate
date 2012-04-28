package com.hrw.framework.ahibernate.sql.builder;

import com.hrw.framework.ahibernate.table.TableInfo;

public interface SqlBuilder<T> {
	String buildCreateTableSql(TableInfo tableInfo, boolean ifNotExists);

	/**
	 * build the data for insert sql.
	 * 
	 * @param object
	 *            .
	 * @return InsertData.
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	InsertData buildInsertData(Object object) throws IllegalArgumentException,
			IllegalAccessException;

	/**
	 * bulid the data for query sql.
	 * 
	 * @param object
	 * @return
	 */
	QueryData buildQueryData(Object object);

	String buildQueryAllSql(Class<T> clazz);

	String buidlDeleteSql(T object);

}
