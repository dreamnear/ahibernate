
package com.hrw.framework.ahibernate.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hrw.framework.ahibernate.annotation.Column;
import com.hrw.framework.ahibernate.annotation.Id;
import com.hrw.framework.ahibernate.annotation.OneToMany;
import com.hrw.framework.ahibernate.builder.DataBuilder;
import com.hrw.framework.ahibernate.builder.InsertData;
import com.hrw.framework.ahibernate.sql.Insert;
import com.hrw.framework.ahibernate.sql.Update;
import com.hrw.framework.ahibernate.table.TableUtils;

public abstract class AhibernateHelper<T> extends SQLiteOpenHelper {
    private static String TAG = "AhibernateHelper";

    private Boolean DEBUG = true;

    /**
     * Create a helper object to create, open, and/or manage a database. This
     * method always returns very quickly. The database is not actually created
     * or opened until one of getWritableDatabase or getReadableDatabase is
     * called.
     * 
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public AhibernateHelper(Context context, String name, CursorFactory factory, int version) {
        // create database when fist call
        // SQLiteOpenHelper.getWritableDatabase().
        super(context, name, factory, version);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should
     * happen.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        if (DEBUG) {
            Log.i(TAG, "onCreate");
        }
        // create tables db.execSQL("");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (DEBUG) {
            Log.i(TAG, "onUpgrade");
        }
        // TODO Auto-generated method stub
    }

    /**
     * called when you need create tables. before call super.onCreate(db). <br>
     * public void onCreate(SQLiteDatabase db) <br>
     * { // create tables <br>
     * createTables(db,true,Book.class,Author.class);<br>
     * super.onCreate(db) <br>
     * 
     * @param db
     * @param clazzs
     * @param ifNotExists if true will add IF NOT EXISTS clause
     */
    @SuppressWarnings({
            "rawtypes", "unchecked"
    })
    public void createTables(boolean ifNotExists, Class... clazzs) {
        SQLiteDatabase db = getWritableDatabase();
        for (Class clazz : clazzs) {
            db.execSQL(TableUtils.buildCreateTableStatements(TableUtils.extractTableInfo(clazz),
                    ifNotExists));
        }
    }

    @SuppressWarnings({
            "rawtypes", "unchecked"
    })
    public void dropTables(boolean ifNotExists, Class... clazzs) {
        SQLiteDatabase db = getWritableDatabase();
        for (Class clazz : clazzs) {
            db.execSQL(TableUtils.buildDropTableStatements(TableUtils.extractTableInfo(clazz), true));
        }
    }

    /**
     * @param objects
     * @return i- how many line effected.
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public int save(T... objects) throws IllegalArgumentException, IllegalAccessException {
        SQLiteDatabase db = getWritableDatabase();
        int i = -1;
        for (Object object : objects) {
            String insertSql = new Insert(object).toStatementString();
            db.execSQL(insertSql);
            i++;
        }
        return i;
    }

    public int update(T object) {
        SQLiteDatabase db = getWritableDatabase();
        int i = -1;
        String updateSql = new Update(object).toStatementString();
        db.execSQL(updateSql);
        i++;
        return i;
    }

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

    /**
     * delete entity.
     * 
     * @param t
     * @return int
     */
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

}
