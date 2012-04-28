
package com.hrw.framework.ahibernate.sql;

import java.util.Map;

public class Delete extends Operate {
    private Object entity;

    private Map<String, String> where;

    /**
     * delete entity by id.
     * @param entity
     */
    public Delete(Object entity) {
        super(entity.getClass());
        this.entity = entity;
    }

    /**
     * delete entity by where,if where is null it will delete all records.
     * @param entity
     * @param where
     */
    public Delete(Object entity, Map<String, String> where) {
        super(entity.getClass());
        this.entity = entity;
        this.where = where;
    }

    public String toStatementString() {
        return buildDeleteSql(getTableName(), where);
    }
}
