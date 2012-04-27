
package com.hrw.framework.ahibernate.test.crud;

import org.junit.Assert;
import org.junit.Test;

import com.hrw.framework.ahibernate.sql.Insert;
import com.hrw.framework.ahibernate.test.domain.Book;

public class InsertTest {

    private String EXPECTED_INSERT_SQL = "INSERT INTO book (id, book_name) values (1, newbook)";

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

    @Test
    public void testToStatementString() throws IllegalArgumentException, IllegalAccessException {
        Book book = new Book();
        book.setBookName("newbook");
        book.setId(1L);
        Insert insert = new Insert(book);
        Assert.assertEquals(EXPECTED_INSERT_SQL, insert.toStatementString());
    }

}
