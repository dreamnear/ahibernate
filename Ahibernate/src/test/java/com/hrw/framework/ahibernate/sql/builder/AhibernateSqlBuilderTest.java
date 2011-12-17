package com.hrw.framework.ahibernate.sql.builder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.hrw.framework.ahibernate.test.domain.Book;

public class AhibernateSqlBuilderTest {
	SqlBuilder sqlBuilder;

	@Before
	public void setUp() {
		sqlBuilder = new AhibernateSqlBuilder();
	}

	@Test
	public void testbuildInsertData() throws IllegalArgumentException,
			IllegalAccessException {
		Book book = new Book();
		book.setBookName("test");
		book.setId(1L);
		InsertData insertData = sqlBuilder.buildInsertData(book);
		Assert.assertNotNull(insertData);
	}
}
