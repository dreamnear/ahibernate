
package com.hrw.framework.ahibernate.test.builder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.hrw.framework.ahibernate.builder.DataBuilder;
import com.hrw.framework.ahibernate.test.domain.Book;

public class SqlBuilderTest {

    @Before
    public void setUp() {
    }

    @Test
    public void testbuildInsertSql() throws IllegalArgumentException, IllegalAccessException {
        Book book = new Book();
        book.setBookName("test");
        book.setId(1L);
        String sql = DataBuilder.buildInsertSql(book);
        System.out.println(sql);
        Assert.assertNotNull(sql);
    }

    @Test
    public void testbuildQueryAllSql() throws IllegalArgumentException, IllegalAccessException {
        String sql = DataBuilder.buildQueryAllSql(Book.class);
        System.out.println(sql);
        Assert.assertNotNull(sql);
    }
}
