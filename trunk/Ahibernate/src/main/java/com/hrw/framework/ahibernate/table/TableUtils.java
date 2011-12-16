package com.hrw.framework.ahibernate.table;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.hrw.framework.ahibernate.annotation.Table;
import com.hrw.framework.ahibernate.dao.AhibernatePersistence;

public class TableUtils {

	public static <T> TableInfo extractTableInfo(Class<T> clazz) {
		TableInfo tableInfo = new TableInfo();
		tableInfo.setTableName(extractTableName(clazz));
		Field[] fields = clazz.getDeclaredFields();
		Map<String, Field> fieldNameMap = new HashMap<String, Field>();
		for (Field field : fields) {
			fieldNameMap.put(field.getName(), field);
		}
		tableInfo.setFieldNameMap(fieldNameMap);
		return tableInfo;
	}

	public static String buildCreateTableStatements(TableInfo tableInfo,
			boolean ifNotExists) {
		// CREATE TABLE IF NOT EXISTS hrw_playlist (id INTEGER PRIMARY KEY,name
		// TEXT CHECK( name != '' ),add_date INTEGER,modified_date INTEGER);
		StringBuilder sb = new StringBuilder(256);
		sb.append("CREATE TABLE ");
		if (ifNotExists) {
			sb.append("IF NOT EXISTS ");
		}
		sb.append(tableInfo.getTableName());
		sb.append(" (");
		Boolean isFirst = true;
		Field idFiled = tableInfo.getIdField();
		for (@SuppressWarnings("rawtypes")
		Entry entry : tableInfo.getFieldNameMap().entrySet()) {
			Field f = (Field) entry.getValue();
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(", ");
			}
			if (f.getType().getSimpleName().equals("Long")) {
				sb.append(entry.getKey() + " INTEGER");
			}
			if (f.getType().getSimpleName().equals("String")) {
				sb.append(entry.getKey() + " TEXT");
			}
			// and primary key here
			if (idFiled != null && idFiled.getName().equals(f.getName())) {
				sb.append(" PRIMARY KEY");
			}

		}
		sb.append(")");
		return sb.toString();
	}

	public static <T> String extractTableName(Class<T> clazz) {
		Table table = clazz.getAnnotation(Table.class);
		String name = null;
		if (table != null && table.name() != null && table.name().length() > 0) {
			name = table.name();
		} else {
			/*
			 * NOTE: to remove javax.persistence usage, comment the following
			 * line out
			 */
			name = AhibernatePersistence.getEntityName(clazz);
			if (name == null) {
				// if the name isn't specified, it is the class name lowercased
				name = clazz.getSimpleName().toLowerCase();
			}
		}
		return name;
	}
}
