
package com.hrw.framework.ahibernate.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.hrw.framework.ahibernate.builder.DataBuilder;
import com.hrw.framework.ahibernate.builder.InsertData;
import com.hrw.framework.ahibernate.table.TableUtils;

public abstract class AhibernateHelper<T> extends SQLiteOpenHelper {

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
        // create tables db.execSQL("");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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
            String insertSql = DataBuilder.buildInsertSql(object);
            db.execSQL(insertSql);
            i++;
        }
        return i;
    }
//
//    public int delete(T... objects) throws IllegalArgumentException, IllegalAccessException {
//        SQLiteDatabase db = getWritableDatabase();
//        int i = -1;
//        for (Object object : objects) {
//            InsertData insertData = DataBuilder.buildInsertSql(object);
//            db.execSQL(insertData.getInsertSql(), insertData.getColumnAndValue().values().toArray());
//            i++;
//        }
//        return i;
//    }

}
