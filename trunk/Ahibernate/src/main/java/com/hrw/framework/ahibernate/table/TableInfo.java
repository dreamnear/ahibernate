
package com.hrw.framework.ahibernate.table;

import java.lang.reflect.Field;
import java.util.Map;

public class TableInfo {
    private Object target;

    private Map<String, String> columns;

    private Map<String, String> columnsType;

    private String tableName;

    private String primaryKey;

    private Field idField;

    private Map<String, Field> fieldNameMap;

    public TableInfo(Class clazz) {
        this.tableName = TableUtils.getTableName(clazz);
        this.columns = TableUtils.getTableColumns(clazz);
        this.columnsType = TableUtils.getTableColumnsType(clazz);
        this.primaryKey = TableUtils.getPrimaryKey(clazz);
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

    public Map<String, String> getColumnsType() {
        return this.columnsType;
    }

    public String getPrimaryColoum() {
        return primaryKey;
    }

}
