
package com.hrw.framework.ahibernate.test.crud;

import org.junit.Assert;
import org.junit.Test;

import com.hrw.framework.ahibernate.sql.Select;
import com.hrw.framework.ahibernate.test.domain.Book;

public class SelectTest {
    @Test
    public void testGetTableName() {
        Book book = new Book();
        Select select = new Select(book);
        Assert.assertEquals("book", select.getTableName());
    }

}
