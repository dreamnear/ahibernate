
package com.hrw.framework.ahibernate.table;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import android.database.sqlite.SQLiteDatabase;

import com.hrw.framework.ahibernate.annotation.Column;
import com.hrw.framework.ahibernate.annotation.Id;
import com.hrw.framework.ahibernate.annotation.OneToMany;
import com.hrw.framework.ahibernate.annotation.Table;
import com.hrw.framework.ahibernate.dao.AhibernatePersistence;

public class TableUtils {
    public static boolean DEBUG = false;

    private static String DEFAULT_FOREIGN_KEY_SUFFIX = "_id";

    public static TableInfo extractTableInfo(Class clazz) {
        TableInfo tableInfo = new TableInfo(clazz);
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
        }
        tableInfo.setFieldNameMap(fieldNameMap);
        return tableInfo;
    }

    public static String buildDropTableStatement(TableInfo tableInfo) {
        // DROP TABLE IF EXISTS avpig_tingshu_book
        StringBuilder sb = new StringBuilder(256);
        sb.append("DROP TABLE ");
        sb.append("IF EXISTS ");
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

    public static String buildCreateTableStatement(TableInfo tableInfo, boolean ifNotExists) {
        // CREATE TABLE IF NOT EXISTS hrw_playlist (id INTEGER PRIMARY KEY,name
        // TEXT CHECK( name != '' ),add_date INTEGER,modified_date INTEGER);
        StringBuilder sb = new StringBuilder(256);
        sb.append("CREATE TABLE ");

        if (ifNotExists) {
            sb.append("IF NOT EXISTS ");
        }

        sb.append(tableInfo.getTableName());
        sb.append(" (");

        Iterator iter = null;
        iter = tableInfo.getFieldNameMap().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry e = (Map.Entry) iter.next();
            Field f = (Field) e.getValue();
            if (f.getType().getSimpleName().equals("Long")) {
                sb.append(e.getKey() + " INTEGER");
            }
            // if (f.getType().getSimpleName().equals("List")) {
            // sb.append(entry.getKey()+DEFAULT_FOREIGN_KEY_SUFFIX +
            // " INTEGER");
            // }
            if (f.getType().getSimpleName().equals("String")) {
                sb.append(e.getKey() + " TEXT");
            }
            Field idFiled = tableInfo.getIdField();
            // and primary key here
            if (idFiled != null && idFiled.getName().equals(f.getName())) {
                sb.append(" PRIMARY KEY");
            }

            if (iter.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public static String extractTableName(Class clazz) {
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

    public static Field extractIdField(Class clazz) {
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

    public static int createTable(SQLiteDatabase db, boolean ifNotExists, Class... entityClasses) {
        int i = -1;
        for (Class clazz : entityClasses) {
            TableInfo tableInfo = extractTableInfo(clazz);
            String sql = buildCreateTableStatement(tableInfo, ifNotExists);
            if (!DEBUG) {
                db.execSQL(sql);
            }
            i = 1;
        }

        return i;
    }

    public static int dropTable(SQLiteDatabase db, Class... entityClasses) {
        int i = -1;
        for (Class clazz : entityClasses) {
            TableInfo tableInfo = extractTableInfo(clazz);
            String sql = buildDropTableStatement(tableInfo);
            if (!DEBUG) {
                db.execSQL(sql);
            }
            i = 1;
        }
        return i;
    }

    @SuppressWarnings({
            "rawtypes", "unchecked"
    })
    public static String getTableName(Class clazz) {
        Table table = (Table) clazz.getAnnotation(Table.class);
        String name = null;
        if (isTableNameEmpty(table)) {
            name = table.name();
        } else {
            // if the name isn't specified, it is the class name lowercased
            name = clazz.getSimpleName().toLowerCase();
        }
        return name;
    }

    private static boolean isTableNameEmpty(Table table) {
        return table != null && StringUtils.isBlank(table.name());
    }

    public static Map<String, String> getTableColumns(Class clazz) {
        Map<String, String> columns = new HashMap<String, String>();
        Annotation[] fieldAnnotations = null;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            fieldAnnotations = field.getAnnotations();
            if (fieldAnnotations.length != 0) {
                for (Annotation annotation : fieldAnnotations) {
                    String columnName = null;
                    if (annotation instanceof Id) {
                        columnName = ((Id) annotation).name();
                    } else if (annotation instanceof Column) {
                        columnName = ((Column) annotation).name();
                    }
                    columns.put(field.getName(), !StringUtils.isBlank(columnName) ? columnName
                            : field.getName());
                }
            }
        }
        return columns;
    }
}
