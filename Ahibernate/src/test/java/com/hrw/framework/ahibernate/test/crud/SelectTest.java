
package com.hrw.framework.ahibernate.test.crud;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.hrw.framework.ahibernate.sql.Select;
import com.hrw.framework.ahibernate.test.domain.Book;

public class SelectTest {
    String EXPECTED_SELECT_SQL1 = "SELECT * FROM book";

    String EXPECTED_SELECT_SQL2 = "SELECT * FROM book WHERE id = 1";

    String EXPECTED_SELECT_SQL3 = "SELECT * FROM book WHERE id = 1 AND book_name = newbook";

    @Test
    public void testGetTableName() {
        Book book = new Book();
        Select select = new Select(book);
        Assert.assertEquals("book", select.getTableName());
    }

    @Test
    public void testToStatementString1() throws IllegalArgumentException, IllegalAccessException {
        Book book = new Book();
        Select select = new Select(book);
        Assert.assertEquals(EXPECTED_SELECT_SQL1, select.toStatementString());
    }

    @Test
    public void testToStatementString2() throws IllegalArgumentException, IllegalAccessException {
        Book book = new Book();
        Map<String, String> where = new HashMap<String, String>();
        where.put("id", "1");
        Select select = new Select(book, where);
        Assert.assertEquals(EXPECTED_SELECT_SQL2, select.toStatementString());
    }
    
    @Test
    public void testToStatementString3() throws IllegalArgumentException, IllegalAccessException {
        Book book = new Book();
        Map<String, String> where = new HashMap<String, String>();
        where.put("id", "1");
        where.put("book_name", "newbook");
        Select select = new Select(book, where);
        Assert.assertEquals(EXPECTED_SELECT_SQL3, select.toStatementString());
    }
}
