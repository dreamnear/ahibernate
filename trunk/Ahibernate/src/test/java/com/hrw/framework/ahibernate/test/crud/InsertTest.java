package com.hrw.framework.ahibernate.test.crud;

import org.junit.Assert;

import com.hrw.framework.ahibernate.sql.Insert;
import com.hrw.framework.ahibernate.test.domain.Book;

public class InsertTest {
    
    public void testGetTableName() {
        Book book = new Book();
        Insert insert = new Insert(book);
        Assert.assertEquals("book", insert.getTableName());
    }

}
