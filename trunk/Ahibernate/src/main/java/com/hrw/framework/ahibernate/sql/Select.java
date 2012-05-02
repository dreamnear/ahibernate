
package com.hrw.framework.ahibernate.sql;

import java.util.Map;

public class Select extends Operate {
    private Object entity;

    private Map<String, String> where;

    public Select(Object entity) {
        super(entity.getClass());
        this.entity = entity;
    }

    public Select(Object entity, Map<String, String> where) {
        super(entity.getClass());
        this.entity = entity;
        this.where = where;
    }

    public String toStatementString() {
        return buildSelectSql(getTableName(), where);
    }

}
