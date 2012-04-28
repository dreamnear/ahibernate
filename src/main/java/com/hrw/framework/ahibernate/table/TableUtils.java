
package com.hrw.framework.ahibernate.table;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.hrw.framework.ahibernate.annotation.Column;
import com.hrw.framework.ahibernate.annotation.Id;
import com.hrw.framework.ahibernate.annotation.OneToMany;
import com.hrw.framework.ahibernate.annotation.Table;
import com.hrw.framework.ahibernate.dao.AhibernatePersistence;

public class TableUtils {
    private static String DEFAULT_FOREIGN_KEY_SUFFIX = "_id";

    public static <T> TableInfo extractTableInfo(Class<T> clazz) {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(extractTableName(clazz));
        tableInfo.setIdField(extractIdField(clazz));
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Field> fieldNameMap = new HashMap<String, Field>();
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
                    fieldNameMap.put((columnName != null && !columnName.equals("")) ? columnName
                            : field.getName(), field);
                }
            }

            // if (fieldAnnotations.length != 0
            // && field.getAnnotation(OneToMany.class) == null) {
            //
            // if (null != field.getAnnotation(Id.class)) {
            // columnName = field.getAnnotation(Id.class).name();
            // }
            // fieldNameMap.put(
            // columnName != null ? columnName : field.getName(),
            // field);
            // }
        }
        tableInfo.setFieldNameMap(fieldNameMap);
        return tableInfo;
    }

    public static String buildDropTableStatements(TableInfo tableInfo, boolean ifExists) {
        // DROP TABLE IF EXISTS avpig_tingshu_book
        StringBuilder sb = new StringBuilder(256);
        sb.append("Drop TABLE ");
        if (ifExists) {
            sb.append("IF EXISTS ");
        }
        sb.append(tableInfo.getTableName());
        return sb.toString();
    }

    public static Object getFieldValue(String filedName, Object obj)
            throws IllegalArgumentException, SecurityException, IllegalAccessException,
            NoSuchFieldException {
        Field field = obj.getClass().getDeclaredField(filedName);
        field.setAccessible(true);
        return field.get(obj);
    }

    // Strubg query = "Select *  from tableName where x = xx";
    public static String buildQueryStatements(String tableName, String fieldName, String fieldValue) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("SELECT * FROM ");
        sb.append(tableName);
        sb.append(" WHERE ");
        sb.append(fieldName);
        sb.append("=");
        sb.append(fieldValue);
        return sb.toString();
    }

    // Strubg INSERT = "insert into myTable(date, time, cost) values (?,?,?)";
    public static String buildInsertTableStatements(TableInfo tableInfo) {
        // DROP TABLE IF EXISTS avpig_tingshu_book
        StringBuilder sb = new StringBuilder(256);
        StringBuilder sbValues = new StringBuilder(256);
        sb.append("INSERT INTO ");
        sb.append(tableInfo.getTableName());
        sb.append(" (");
        sbValues.append(" (");
        Boolean isFirst = true;
        for (@SuppressWarnings("rawtypes")
        Entry entry : tableInfo.getFieldNameMap().entrySet()) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(", ");
                sbValues.append(", ");
            }
            sb.append(entry.getKey());
            sbValues.append("?");
        }
        sb.append(")");
        sbValues.append(")");
        sb.append(" values");
        sb.append(sbValues);
        return sb.toString();
    }

    public static String buildCreateTableStatements(TableInfo tableInfo, boolean ifNotExists) {
        // CREATE TABLE IF NOT EXISTS hrw_playlist (id INTEGER PRIMARY KEY,name
        // TEXT CHECK( name != '' ),add_date INTEGER,modified_date INTEGER);
        StringBuilder sb = new StringBuilder(256);
        sb.append("CREATE TABLE ");

        if (ifNotExists) {
            sb.append("IF NOT EXISTS ");
        }
        sb.append(tableInfo.getTableName());
        sb.append(" (");
        Boolean isFirst = true;
        Field idFiled = tableInfo.getIdField();
        for (@SuppressWarnings("rawtypes")
        // CREATE TABLE book (id INTEGER PRIMARY KEY, , bookName TEXT)
        Entry entry : tableInfo.getFieldNameMap().entrySet()) {
            Field f = (Field) entry.getValue();
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(", ");
            }
            if (f.getType().getSimpleName().equals("Long")) {
                sb.append(entry.getKey() + " INTEGER");
            }
            // if (f.getType().getSimpleName().equals("List")) {
            // sb.append(entry.getKey()+DEFAULT_FOREIGN_KEY_SUFFIX +
            // " INTEGER");
            // }
            if (f.getType().getSimpleName().equals("String")) {
                sb.append(entry.getKey() + " TEXT");
            }
            // and primary key here
            if (idFiled != null && idFiled.getName().equals(f.getName())) {
                sb.append(" PRIMARY KEY");
            }

        }
        sb.append(")");
        return sb.toString();
    }

    public static <T> String extractTableName(Class<T> clazz) {
        Table table = clazz.getAnnotation(Table.class);
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

    public static <T> Field extractIdField(Class<T> clazz) {
        Field idField = null;
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotations().length != 0) {
                field.setAccessible(true);
                for (Annotation annotation : field.getAnnotations()) {
                    Class<?> annotationClass = annotation.annotationType();
                    if (annotationClass.getName().equals(
                            "com.hrw.framework.ahibernate.annotation.Id")) {
                        idField = field;
                    }
                }
            }
        }
        return idField;
    }
}
