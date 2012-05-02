
package com.hrw.framework.ahibernate.test.table;

import org.junit.Assert;
import org.junit.Test;

import android.database.sqlite.SQLiteDatabase;

import com.hrw.framework.ahibernate.table.TableUtils;
import com.hrw.framework.ahibernate.test.domain.Book;

public class TableUtilsTest {
    @Test
    public void testCreateTable() {
        SQLiteDatabase db = null;
        Assert.assertEquals(1, TableUtils.createTable(db, Book.class, true));
    }

}
