
package com.hrw.framework.ahibernate.test.crud;

import org.junit.Assert;
import org.junit.Test;

import com.hrw.framework.ahibernate.sql.Insert;
import com.hrw.framework.ahibernate.test.domain.Book;

public class InsertTest {
    @Test
    public void testGetTableName() {
        Book book = new Book();
        Insert insert = new Insert(book);
        Assert.assertEquals("book", insert.getTableName());
    }

    @Test
    public void testGetInsertColumns() throws IllegalArgumentException, IllegalAccessException {
        Book book = new Book();
        book.setBookName("newbook");
        Insert insert = new Insert(book);
        Assert.assertEquals("newbook", insert.getInsertColumns().get("book_name"));
    }

}
