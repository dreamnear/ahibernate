
package com.hrw.framework.ahibernate.test.crud;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.hrw.framework.ahibernate.sql.Update;
import com.hrw.framework.ahibernate.test.domain.Book;

public class UpdateTest {
    // UPDATE null SET id = null, book_name = testBook where id = 1 and name = 2
    private static String EXPECTED_UPDATE_SQL4 = "UPDATE book SET book_name = testBook where id = 1 and name = 2";

    private static String EXPECTED_UPDATE_SQL5 = "UPDATE book SET book_name = testBook where id = 1";
    @Test
    public void testGetUpdateFields1() throws IllegalArgumentException, IllegalAccessException {
        Book book = new Book();
        Update update = new Update(book);

        Assert.assertNotNull(update.getUpdateFields());
        Assert.assertNull(update.getUpdateFields().get("book_name"));
    }

    @Test
    public void testGetUpdateFields2() throws IllegalArgumentException, IllegalAccessException {
        Book book = new Book();
        book.setBookName("testBook");
        Update update = new Update(book);
        Assert.assertEquals("testBook", update.getUpdateFields().get("book_name"));
    }

    @Test
    public void testGetTableName() {
        Book book = new Book();
        book.setBookName("testBook");
        Update update = new Update(book);

        Assert.assertEquals("book", update.getTableName());
    }

    @Test
    public void testAddWhereClause1() throws IllegalArgumentException, IllegalAccessException {
        Book book = new Book();
        book.setId(1l);
        book.setBookName("testBook");
        Update update = new Update(book);
        Assert.assertNotNull(update.getWhereFiled().get("id"));
        Assert.assertEquals(EXPECTED_UPDATE_SQL5, update.testToStatementString());
    }

    @Test
    public void testToStatementString() throws IllegalArgumentException, IllegalAccessException {
        Book book = new Book();
        book.setBookName("testBook");
        HashMap<String, String> where = new HashMap<String, String>();
        where.put("id", "1");
        where.put("name", "2");
        Update update = new Update(book, where);

        Assert.assertEquals(EXPECTED_UPDATE_SQL4, update.testToStatementString());
    }
}
