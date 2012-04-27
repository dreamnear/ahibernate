
package com.hrw.framework.ahibernate.sql;

import java.util.Map;

import com.hrw.framework.ahibernate.builder.DataBuilder;
import com.hrw.framework.ahibernate.table.TableUtils;

public class Select {
    private Object entity;

    private String tableName;

    private Map<String, String> where;

    public Select(Object entity) {
        this.entity = entity;
        this.tableName = TableUtils.extractTableName(entity.getClass());
    }

    public Select(Object entity, Map<String, String> where) {
        this.entity = entity;
        this.tableName = TableUtils.extractTableName(entity.getClass());
        this.where = where;
    }

    public String getTableName() {
        return tableName;
    }

    public String toStatementString() {
        return DataBuilder.buildSelectSql(tableName, where);
    }

}
