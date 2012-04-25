
package com.hrw.framework.ahibernate.sql;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.hrw.framework.ahibernate.annotation.Column;
import com.hrw.framework.ahibernate.annotation.Id;
import com.hrw.framework.ahibernate.annotation.OneToMany;
import com.hrw.framework.ahibernate.builder.DataBuilder;
import com.hrw.framework.ahibernate.table.TableUtils;

public class Update {
    private Object entity;

    private String tableName;

    private Map<String, String> where;

    public Update(Object entity) {
        this.entity = entity;
        try {
            this.where = getDefaultWhereField();
        } catch (IllegalArgumentException e) {
            this.where = null;
        } catch (IllegalAccessException e) {
            this.where = null;
        }
        this.tableName = TableUtils.extractTableName(entity.getClass());
    }

    public Update(Object entity, Map<String, String> where) {
        this.entity = entity;
        this.where = where;
        this.tableName = TableUtils.extractTableName(entity.getClass());
    }

    public Map<String, String> getDefaultWhereField() throws IllegalArgumentException,
            IllegalAccessException {
        Map<String, String> defaultWhereField = new HashMap<String, String>();
        Class clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Annotation[] fieldAnnotations = null;
        for (Field field : fields) {
            fieldAnnotations = field.getAnnotations();
            if (fieldAnnotations.length != 0) {
                for (Annotation annotation : fieldAnnotations) {
                    String columnName = null;
                    if (annotation instanceof Id) {
                        // do not update id.default primary key
                        columnName = ((Id) annotation).name();
                        field.setAccessible(true);
                        defaultWhereField.put(
                                (columnName != null && !columnName.equals("")) ? columnName : field
                                        .getName(), field.get(entity) == null ? null : field
                                        .get(entity).toString());
                    }
                 
                }
            }
        }
        return defaultWhereField;

    }

    @SuppressWarnings("rawtypes")
    public Map<String, String> getUpdateFields() throws IllegalArgumentException,
            IllegalAccessException {
        Map<String, String> updateFields = new HashMap<String, String>();
        Class clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Annotation[] fieldAnnotations = null;
        for (Field field : fields) {
            fieldAnnotations = field.getAnnotations();
            if (fieldAnnotations.length != 0) {
                for (Annotation annotation : fieldAnnotations) {
                    String columnName = null;
                    if (annotation instanceof Id) {
                        // do not update id.default primary key
                        // columnName = ((Id) annotation).name();
                        continue;
                    } else if (annotation instanceof Column) {
                        columnName = ((Column) annotation).name();
                    } else if (annotation instanceof OneToMany) {
                        continue;
                        // Ignore
                    }
                    field.setAccessible(true);
                    updateFields.put((columnName != null && !columnName.equals("")) ? columnName
                            : field.getName(), field.get(entity) == null ? null : field.get(entity)
                            .toString());
                }
            }
        }
        return updateFields;
    }

    public String testToStatementString() throws IllegalArgumentException, IllegalAccessException {
        return DataBuilder.buildUpdateSql(tableName, getUpdateFields(), where);
    }

    public String getTableName() {
        return tableName;
    }

    public Map<String, String> getWhereFiled() {
        return where;
    }

}
