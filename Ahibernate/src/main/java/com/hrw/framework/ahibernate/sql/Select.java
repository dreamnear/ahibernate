
package com.hrw.framework.ahibernate.sql;

import com.hrw.framework.ahibernate.table.TableUtils;

public class Select {
    private Object entity;

    private String tableName;

    public Select(Object entity) {
        this.entity = entity;
        this.tableName = TableUtils.extractTableName(entity.getClass());
    }

    public String getTableName() {
        return tableName;
    }

}
