
package com.hrw.framework.ahibernate.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.hrw.framework.ahibernate.annotation.Column;
import com.hrw.framework.ahibernate.annotation.Id;
import com.hrw.framework.ahibernate.annotation.OneToMany;
import com.hrw.framework.ahibernate.table.TableUtils;

public class DataBuilder {
    // Update T1 Set Column1 = v1,Column2 =V2 Where key = V3;//TODO
    public static String buildUpdateSql(Object entity) throws IllegalArgumentException,
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
        // builder update sql.
        StringBuilder updateSql = new StringBuilder(256);

        updateSql.append("UPDATE ");
        updateSql.append(tableName);
        updateSql.append("SET ");

        for (Entry entry : columnAndValue.entrySet()) {
            updateSql.append(entry.getKey() + "=" + entry.getValue());
        }

        updateSql.append("WHERE ");
        return updateSql.toString();

    }

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

    public static String buildQueryByFieldSql(String tableName, String fieldName, String fieldValue) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("SELECT * FROM ");
        sb.append(tableName);
        sb.append(" WHERE ");
        sb.append(fieldName);
        sb.append(" = ");
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

    public static String buildUpdateSql(String tableName, Map<String, String> needUpdate,
            Map<String, String> where) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("UPDATE ");
        sb.append(tableName).append(" SET ");

        Iterator iter = needUpdate.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry e = (Map.Entry) iter.next();
            sb.append(e.getKey()).append(" = ").append(e.getValue());
            if (iter.hasNext()) {
                sb.append(", ");
            }
        }
        // for (Entry<String, String> entry : needUpdate.entrySet()) {
        // sb.append(" SET ");
        // sb.append(entry.getKey());
        // sb.append(" = ");
        // sb.append(entry.getValue());
        // }
        if (where != null) {
            sb.append(" where ");
            iter = where.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry e = (Map.Entry) iter.next();
                sb.append(e.getKey()).append(" = ").append(e.getValue());
                if (iter.hasNext()) {
                    sb.append(" and ");
                }
            }
        }
        return sb.toString();
    }

    public static String buildInsertSql(String tableName, Map<String, String> insertColumns) {
        StringBuilder columns = new StringBuilder(256);
        StringBuilder values = new StringBuilder(256);
        columns.append("INSERT INTO ");

        columns.append(tableName).append(" (");
        values.append("(");

        Iterator iter = insertColumns.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry e = (Map.Entry) iter.next();
            columns.append(e.getKey());
            values.append(e.getValue());
            if (iter.hasNext()) {
                columns.append(", ");
                values.append(", ");
            }
        }
        columns.append(") values ");
        values.append(")");
        columns.append(values);
        return columns.toString();
    }

    public static String buildSelectSql(String tableName, Map<String, String> where) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("SELECT * FROM ");
        sb.append(tableName);
        Iterator iter = null;
        if (where != null) {
            sb.append(" WHERE ");
            iter = where.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry e = (Map.Entry) iter.next();
                sb.append(e.getKey()).append(" = ").append(e.getValue());
                if (iter.hasNext()) {
                    sb.append(" AND ");
                }
            }
        }
        return sb.toString();
    }
}
