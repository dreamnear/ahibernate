
package com.hrw.framework.ahibernate.table;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class TableInfo {
    private Object target;

    private Map<String, String> columns;

    private String tableName;

    private Field idField;

    private Map<String, Field> fieldNameMap;

    public TableInfo(Class clazz) {
        this.tableName = TableUtils.getTableName(clazz);
        this.columns = TableUtils.getTableColumns(clazz);
    }

    public String getTableName() {
        return tableName;
    }

    public Field getIdField() {
        return idField;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
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

    public Map<String, String> getColumns() {
        return this.columns;
    }

}
