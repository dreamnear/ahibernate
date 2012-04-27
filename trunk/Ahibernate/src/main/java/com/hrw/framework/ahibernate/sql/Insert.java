
package com.hrw.framework.ahibernate.sql;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.hrw.framework.ahibernate.annotation.Column;
import com.hrw.framework.ahibernate.annotation.Id;
import com.hrw.framework.ahibernate.annotation.OneToMany;
import com.hrw.framework.ahibernate.table.TableUtils;

public class Insert {

    private Object entity;

    private String tableName;

    public Insert(Object entity) {
        this.entity = entity;
        this.tableName = TableUtils.extractTableName(entity.getClass());
    }

    public String getTableName() {
        return tableName;
    }

    public Map<String, String> getInsertColumns() throws IllegalArgumentException,
            IllegalAccessException {
        Map<String, String> insertColumns = new HashMap<String, String>();
        Class clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Annotation[] fieldAnnotations = null;
        for (Field field : fields) {
            fieldAnnotations = field.getAnnotations();
            if (fieldAnnotations.length != 0) {
                for (Annotation annotation : fieldAnnotations) {
                    String columnName = null;
                    if (annotation instanceof Id) {
                        columnName = ((Id) annotation).name();
                    } else if (annotation instanceof Column) {
                        columnName = ((Column) annotation).name();
                    } else if (annotation instanceof OneToMany) {
                        continue;
                        // Ignore
                    }
                    field.setAccessible(true);
                    insertColumns.put((columnName != null && !columnName.equals("")) ? columnName
                            : field.getName(), field.get(entity) == null ? null : field.get(entity)
                            .toString());
                }
            }
        }
        return insertColumns;
    }

}
