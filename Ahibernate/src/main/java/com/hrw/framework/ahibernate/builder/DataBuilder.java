
package com.hrw.framework.ahibernate.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.hrw.framework.ahibernate.annotation.Column;
import com.hrw.framework.ahibernate.annotation.Id;
import com.hrw.framework.ahibernate.annotation.OneToMany;
import com.hrw.framework.ahibernate.table.TableUtils;

public class DataBuilder {
    @SuppressWarnings({
            "rawtypes", "unchecked"
    })
    public static String buildInsertSql(Object entity) throws IllegalArgumentException,
            IllegalAccessException {
        Class clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Object> columnAndValue = new HashMap<String, Object>();

        String tableName = TableUtils.extractTableName(clazz);

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
                    columnAndValue.put((columnName != null && !columnName.equals("")) ? columnName
                            : field.getName(), field.get(entity));
                }

            }
        }
        // builder insert sql.
        StringBuilder insertSql = new StringBuilder(256);
        StringBuilder insertSqlValues = new StringBuilder(256);
        insertSql.append("INSERT INTO ");
        insertSql.append(tableName);

        insertSql.append(" (");
        insertSqlValues.append(" (");
        Boolean isFirst = true;
        for (Entry entry : columnAndValue.entrySet()) {
            if (isFirst) {
                isFirst = false;
            } else {
                insertSql.append(", ");
                insertSqlValues.append(", ");
            }
            insertSql.append(entry.getKey());
            insertSqlValues.append(entry.getValue());
        }
        insertSql.append(")");
        insertSqlValues.append(")");
        insertSql.append(" values");
        insertSql.append(insertSqlValues);
        return insertSql.toString();
    }

    public static String buildQuerySql(String tableName, String fieldName, String fieldValue) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("SELECT * FROM ");
        sb.append(tableName);
        sb.append(" WHERE ");
        sb.append(fieldName);
        sb.append("=");
        sb.append("'");
        sb.append(fieldValue);
        sb.append("'");
        return sb.toString();
    }

    @SuppressWarnings({
            "rawtypes", "unchecked"
    })
    public static String buildQueryAllSql(Class clazz) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("SELECT * FROM ");
        sb.append(TableUtils.extractTableName(clazz));
        return sb.toString();
    }
}
