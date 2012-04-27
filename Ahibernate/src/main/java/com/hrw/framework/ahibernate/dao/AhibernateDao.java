
package com.hrw.framework.ahibernate.dao;

import android.database.sqlite.SQLiteDatabase;

public class AhibernateDao {
    private SQLiteDatabase db;

    public AhibernateDao(SQLiteDatabase db) {
        this.db = db;
    }

}
