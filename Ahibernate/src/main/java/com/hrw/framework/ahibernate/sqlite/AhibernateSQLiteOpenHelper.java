
package com.hrw.framework.ahibernate.sqlite;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.hrw.framework.ahibernate.annotation.Column;
import com.hrw.framework.ahibernate.annotation.Id;
import com.hrw.framework.ahibernate.annotation.OneToMany;
import com.hrw.framework.ahibernate.builder.AhibernateSqlBuilder;
import com.hrw.framework.ahibernate.builder.DataBuilder;
import com.hrw.framework.ahibernate.builder.InsertData;
import com.hrw.framework.ahibernate.builder.SqlBuilder;
import com.hrw.framework.ahibernate.table.TableUtils;

public abstract class AhibernateSQLiteOpenHelper<T> extends SQLiteOpenHelper {
    private final static String TAG = "AhibernateSQLiteOpenHelper";

    private SqlBuilder<T> sqlBuilder;

    private static String EMPTY_SQL = "DELETE FROM ";

    private ArrayList<String> buildTableStatements = new ArrayList<String>();

    private ArrayList<String> buildInitDataStatements = new ArrayList<String>();

    public AhibernateSQLiteOpenHelper(Context context, String name, CursorFactory factory,
            int version) {
        super(context, name, factory, version);
        sqlBuilder = new AhibernateSqlBuilder<T>();
    }

    public void prepareCreateInitDataStatements(Object... objects) {
        for (Object object : objects) {
            InsertData insertData = buildInsertData(object);
            try {
                buildInitDataStatements.add(bindArgs(insertData.getInsertSql(), insertData
                        .getColumnAndValue().values().toArray()));
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    @SuppressWarnings({
            "rawtypes", "unchecked"
    })
    public void prepareCreateTableStatements(Class... clazzs) {
        Class[] clazzss = clazzs;
        for (Class clazz : clazzss) {
            buildTableStatements.add(TableUtils.buildCreateTableStatement(
                    TableUtils.extractTableInfo(clazz), true));
        }
    }

    @SuppressWarnings({
            "rawtypes", "unchecked"
    })
    public void dropTableStatements(Class... clazzs) {
        Class[] clazzss = clazzs;
        for (Class clazz : clazzss) {
            buildTableStatements.add(TableUtils.buildDropTableStatement(TableUtils
                    .extractTableInfo(clazz)));
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String stmt : buildTableStatements) {
            Log.i(TAG, "Create table statement:" + stmt);
            db.execSQL(stmt);
        }
        for (String stmt : buildInitDataStatements) {
            Log.i(TAG, "Init data statement:" + stmt);
            db.execSQL(stmt);
        }

    }

    public void truncate(Class<T> clazz) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(EMPTY_SQL + TableUtils.extractTableName(clazz));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String stmt : buildTableStatements) {
            Log.i(TAG, "Drop table statement:" + stmt);
            db.execSQL(stmt);
        }

    }

    public String buildInsertSql(T entity) {
        return TableUtils
                .buildInsertTableStatements(TableUtils.extractTableInfo(entity.getClass()));
    }

    public int insert(String statement, Object[] args) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();
        SQLiteStatement stmt = null;
        try {
            stmt = db.compileStatement(statement);
            bindArgs(stmt, args);
            long rowId = stmt.executeInsert();
            return (int) rowId;
        } catch (android.database.SQLException e) {
            throw new SQLException("inserting to database failed: " + statement, e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public int save(T entity) {
        InsertData insertData = buildInsertData(entity);
        try {
            return insert(insertData.getInsertSql(), insertData.getColumnAndValue().values()
                    .toArray());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public InsertData buildInsertData(Object entity) {
        try {
            return sqlBuilder.buildInsertData(entity);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    // TODO
    public List<T> get(Class<T> clazz, String fieldName, String fieldValue) {
        List<T> queryList = new ArrayList<T>();
        SQLiteDatabase db = getReadableDatabase();
        String tableName = TableUtils.extractTableName(clazz);
        String sql = DataBuilder.buildQueryByFieldSql(tableName, fieldName, fieldValue);
        Cursor cursor = db.rawQuery(sql, null);
        Field[] fields = clazz.getDeclaredFields();
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                try {
                    T t = clazz.newInstance();
                    Annotation[] fieldAnnotations = null;
                    for (Field field : fields) {
                        field.setAccessible(true);
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
                                if (field.getType().getSimpleName().equals("Long")) {
                                    field.set(
                                            t,
                                            cursor.getLong(cursor
                                                    .getColumnIndexOrThrow((columnName != null && !columnName
                                                            .equals("")) ? columnName : field
                                                            .getName())));
                                } else if (field.getType().getSimpleName().equals("String")) {
                                    field.set(
                                            t,
                                            cursor.getString(cursor
                                                    .getColumnIndexOrThrow((columnName != null && !columnName
                                                            .equals("")) ? columnName : field
                                                            .getName())));
                                }

                            }
                        }
                    }
                    queryList.add(t);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
        cursor.close();
        return queryList;
    }

    public int delete(T t) {
        SQLiteDatabase db = getWritableDatabase();
        String tableName = TableUtils.extractTableName(t.getClass());
        String filedName = TableUtils.extractIdField(t.getClass()).getName();
        String fieldValue;
        try {
            fieldValue = TableUtils.getFieldValue(filedName, t).toString();
            return db.delete(tableName, filedName + " = ?", new String[] {
                fieldValue
            });
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    @SuppressWarnings("null")
    private String bindArgs(String stmt, Object[] args) throws SQLException {
        if (args == null) {
            return stmt;
        }
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg != null) {
                stmt = stmt.replaceFirst("\\?", "'" + arg.toString() + "'");
            }
        }
        return stmt;
    }

    private void bindArgs(SQLiteStatement stmt, Object[] args) throws SQLException {
        if (args == null) {
            return;
        }
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg == null) {
                stmt.bindNull(i + 1);
            } else {
                if (arg instanceof Long) {
                    stmt.bindLong(i + 1, Long.valueOf(arg.toString()));
                } else if (arg instanceof String) {
                    stmt.bindString(i + 1, arg.toString());
                } else {
                    throw new SQLException("Unknown sql argument type " + arg.getClass().getName());
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> getAll(Class<T> clazz) {
        List<T> queryList = new ArrayList<T>();
        String sql = sqlBuilder.buildQueryAllSql(clazz);
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        Field[] fields = clazz.getDeclaredFields();
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                try {
                    T t = clazz.newInstance();
                    Annotation[] fieldAnnotations = null;
                    for (Field field : fields) {
                        field.setAccessible(true);
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
                                if (field.getType().getSimpleName().equals("Long")) {
                                    field.set(
                                            t,
                                            cursor.getLong(cursor
                                                    .getColumnIndexOrThrow((columnName != null && !columnName
                                                            .equals("")) ? columnName : field
                                                            .getName())));
                                } else if (field.getType().getSimpleName().equals("String")) {
                                    field.set(
                                            t,
                                            cursor.getString(cursor
                                                    .getColumnIndexOrThrow((columnName != null && !columnName
                                                            .equals("")) ? columnName : field
                                                            .getName())));
                                }

                            }
                        }
                    }
                    queryList.add(t);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
        cursor.close();
        return queryList;
    }

}
