package com.hrw.framework.ahibernate.builder;

import java.util.Map;

public class QueryData {
	private String tableName;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getQuerySql() {
		return querySql;
	}

	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}

	public Map<String, Object> getColumnAndValue() {
		return columnAndValue;
	}

	public void setColumnAndValue(Map<String, Object> columnAndValue) {
		this.columnAndValue = columnAndValue;
	}

	private String querySql;

	private Map<String, Object> columnAndValue;

}
