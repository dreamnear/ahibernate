
package com.hrw.framework.ahibernate.sql;

import java.util.Iterator;
import java.util.Map;

import com.hrw.framework.ahibernate.annotation.Table;
import com.hrw.framework.ahibernate.dao.AhibernatePersistence;

public class Operate {

    public Operate(Class clazz) {
        tableName = extractTableName(clazz);
    }

    public String extractTableName(Class clazz) {
        Table table = (Table) clazz.getAnnotation(Table.class);
        String name = null;
        if (table != null && table.name() != null && table.name().length() > 0) {
            name = table.name();
        } else {
            /*
             * NOTE: to remove javax.persistence usage, comment the following
             * line out
             */
            name = AhibernatePersistence.getEntityName(clazz);
            if (name == null) {
                // if the name isn't specified, it is the class name lowercased
                name = clazz.getSimpleName().toLowerCase();
            }
        }
        return name;
    }

    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public String buildSelectSql(String tableName, Map<String, String> where) {
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

    public String buildInsertSql(String tableName, Map<String, String> insertColumns) {
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

    public String buildUpdateSql(String tableName, Map<String, String> needUpdate,
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

    public String buildDeleteSql(String tableName, Map<String, String> where) {
        StringBuffer buf = new StringBuffer(tableName.length() + 10);
        buf.append("DELETE FROM ").append(tableName);
        if (where != null) {
            buf.append(" WHERE ");
            Iterator iter = where.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry e = (Map.Entry) iter.next();
                buf.append(e.getKey()).append(" = ").append(e.getValue());
                if (iter.hasNext()) {
                    buf.append(" AND ");
                }
            }
        }
        return buf.toString();
    }

}