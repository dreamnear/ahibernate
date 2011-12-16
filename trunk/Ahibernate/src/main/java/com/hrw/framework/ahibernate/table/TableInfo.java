package com.hrw.framework.ahibernate.table;

import java.lang.reflect.Field;
import java.util.Map;

public class TableInfo {
	private String tableName;


	private Field idField;

	private Map<String, Field> fieldNameMap;

	public String getTableName() {
		return tableName;
	}

	public Field getIdField() {
		return idField;
	}

	public void setIdField(Field idField) {
		this.idField = idField;
	}

	public Map<String, Field> getFieldNameMap() {
		return fieldNameMap;
	}

	public void setFieldNameMap(Map<String, Field> fieldNameMap) {
		this.fieldNameMap = fieldNameMap;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


}
