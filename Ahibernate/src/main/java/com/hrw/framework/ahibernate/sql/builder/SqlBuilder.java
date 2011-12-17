package com.hrw.framework.ahibernate.sql.builder;

import com.hrw.framework.ahibernate.table.TableInfo;

public interface SqlBuilder {
	String buildCreateTableSql(TableInfo tableInfo, boolean ifNotExists);

	/**
	 * build insert sql.
	 * 
	 * @param data
	 *            .
	 * @return insert sql.
	 */
	String buildInsertSql(InsertData data);

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
}
