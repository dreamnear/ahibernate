package com.hrw.framework.ahibernate.sql.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.hrw.framework.ahibernate.annotation.Column;
import com.hrw.framework.ahibernate.annotation.Id;
import com.hrw.framework.ahibernate.annotation.OneToMany;
import com.hrw.framework.ahibernate.table.TableInfo;
import com.hrw.framework.ahibernate.table.TableUtils;

public class AhibernateSqlBuilder<T> implements SqlBuilder<T> {

	public String buildCreateTableSql(TableInfo tableInfo, boolean ifNotExists) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InsertData buildInsertData(Object object)
			throws IllegalArgumentException, IllegalAccessException {
		Class clazz = object.getClass();
		Field idField = TableUtils.extractIdField(clazz);
		Field[] fields = clazz.getDeclaredFields();
		Map<String, Object> columnAndValue = new HashMap<String, Object>();

		StringBuilder sb = new StringBuilder(256);
		StringBuilder sbValues = new StringBuilder(256);

		InsertData insertData = new InsertData();
		insertData.setTableName(TableUtils.extractTableName(clazz));
		insertData.setIdColumn(idField.getName());

		Annotation[] fieldAnnotations = null;
		for (Field field : fields) {
			field.setAccessible(true);
			fieldAnnotations = field.getAnnotations();
			if (fieldAnnotations.length != 0) {
				for (Annotation annotation : fieldAnnotations) {
					String columnName = null;
					if (annotation instanceof Id) {
						if (((Id) annotation).autoGenerate()) {
							continue;
						} else {
							columnName = ((Id) annotation).name();
						}
					} else if (annotation instanceof Column) {
						columnName = ((Column) annotation).name();
					} else if (annotation instanceof OneToMany) {
						continue;
						// Ignore
					}
					columnAndValue.put((columnName != null && !columnName
							.equals("")) ? columnName : field.getName(), field
							.get(object));
				}

			}
		}
		// builder insert sql.
		sb.append("INSERT INTO ");
		sb.append(insertData.getTableName());
		sb.append(" (");
		sbValues.append(" (");
		Boolean isFirst = true;
		for (Entry entry : columnAndValue.entrySet()) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(", ");
				sbValues.append(", ");
			}
			sb.append(entry.getKey());
			sbValues.append("?");
		}
		sb.append(")");
		sbValues.append(")");
		sb.append(" values");
		sb.append(sbValues);
		insertData.setColumnAndValue(columnAndValue);
		insertData.setInsertSql(sb.toString());
		return insertData;
	}

	// SELECT * FROM avpig_tingshu_book;
	public QueryData buildQueryData(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String buildQueryAllSql(Class clazz) {
		StringBuilder sb = new StringBuilder(256);
		sb.append("SELECT * FROM ");
		sb.append(TableUtils.extractTableName(clazz));
		return sb.toString();
	}
}
