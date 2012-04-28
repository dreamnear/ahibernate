
package com.hrw.framework.ahibernate.test.crud;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.hrw.framework.ahibernate.sql.Delete;
import com.hrw.framework.ahibernate.test.domain.Book;

public class DeleteTest {
    private String EXPECTED_DELETE_SQL1 = "DELETE FROM book";

    private String EXPECTED_DELETE_SQL2 = "DELETE FROM book WHERE id = 1";

    private String EXPECTED_DELETE_SQL3 = "DELETE FROM book WHERE id = 1 AND book_name = 'newbook'";

    @Test
    public void testGetTableName() {
        Book book = new Book();
        book.setBookName("testBook");
        Delete update = new Delete(book);
        Assert.assertEquals("book", update.getTableName());
    }

    @Test
    public void toStatementString1() {
        Book book = new Book();
        book.setBookName("testBook");
        Delete update = new Delete(book);
        Assert.assertEquals(EXPECTED_DELETE_SQL1, update.toStatementString());
    }

    @Test
    public void toStatementString2() {
        Book book = new Book();
        HashMap<String, String> where = new HashMap<String, String>();
        where.put("id", "1");
        Delete update = new Delete(book, where);
        Assert.assertEquals(EXPECTED_DELETE_SQL2, update.toStatementString());
    }

    @Test
    public void toStatementString3() {
        Book book = new Book();
        HashMap<String, String> where = new HashMap<String, String>();
        where.put("id", "1");
        where.put("book_name", "newbook");
        Delete update = new Delete(book, where);
        Assert.assertEquals(EXPECTED_DELETE_SQL3, update.toStatementString());
    }

}
