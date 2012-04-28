
package com.hrw.framework.ahibernate.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.hrw.framework.ahibernate.annotation.Column;
import com.hrw.framework.ahibernate.annotation.Id;
import com.hrw.framework.ahibernate.annotation.OneToMany;
import com.hrw.framework.ahibernate.sql.Delete;
import com.hrw.framework.ahibernate.sql.Insert;
import com.hrw.framework.ahibernate.sql.Select;
import com.hrw.framework.ahibernate.sql.Update;

public class AhibernateDao<T> {
    private SQLiteDatabase db;

    private String TAG = "AhibernateDao";

    public AhibernateDao(SQLiteDatabase db) {
        this.db = db;
    }

    public int insert(T entity) {
        String sql;
        sql = new Insert(entity).toStatementString();
        Log.i(TAG, "insert sql:" + sql);
        SQLiteStatement stmt = null;
        try {
            stmt = db.compileStatement(sql);
            long rowId = stmt.executeInsert();
            return 1;
        } catch (android.database.SQLException e) {
            Log.e(TAG, "inserting to database failed: " + sql, e);
            return -1;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public List<T> queryList(T entity, Map<String, String> where) {
        String sql = new Select(entity, where).toStatementString();
        List<T> queryList = new ArrayList<T>();
        Cursor cursor = db.rawQuery(sql, null);
        Class clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                try {
                    T t = (T) clazz.newInstance();
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

    public void update(T entity, Map<String, String> where) {
        String sql = new Update(entity, where).toStatementString();
        SQLiteStatement stmt = null;
        try {
            stmt = db.compileStatement(sql);
            stmt.execute();
        } catch (android.database.SQLException e) {
            Log.e(TAG, e.getMessage() + " sql:" + sql);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public void delete(T entity, Map<String, String> where) {
        String sql = new Delete(entity, where).toStatementString();
        SQLiteStatement stmt = null;
        try {
            stmt = db.compileStatement(sql);
            stmt.execute();
        } catch (android.database.SQLException e) {
            Log.e(TAG, e.getMessage() + " sql:" + sql);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
}
