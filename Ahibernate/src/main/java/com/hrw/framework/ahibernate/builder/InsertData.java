package com.hrw.framework.ahibernate.builder;

import java.util.Map;

public class InsertData {
	private String tableName;
	private Map<String, Object> columnAndValue;
	private String IdColumn;
	private String insertSql;

	public String getInsertSql() {
		return insertSql;
	}

	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}

	public String getIdColumn() {
		return IdColumn;
	}

	public void setIdColumn(String idColumn) {
		IdColumn = idColumn;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Map<String, Object> getColumnAndValue() {
		return columnAndValue;
	}

	public void setColumnAndValue(Map<String, Object> columnAndValue) {
		this.columnAndValue = columnAndValue;
	}

}
