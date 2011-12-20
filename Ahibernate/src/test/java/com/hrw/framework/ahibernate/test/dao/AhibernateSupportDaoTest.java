package com.hrw.framework.ahibernate.test.dao;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import android.test.mock.MockContext;

import com.hrw.framework.ahibernate.dao.AhibernateSupportDao;
import com.hrw.framework.ahibernate.sqlite.AhibernateSQLiteOpenHelper;
import com.hrw.framework.ahibernate.test.domain.Book;

public class AhibernateSupportDaoTest {
	AhibernateSupportDao target;

	@Before
	public void setUp() {
//		target = new AhibernateSupportDao(new MockContext());
//		target.setSqliteOpenHelper(new AhibernateSQLiteOpenHelper(new MockContext(), "AndroidPlayer.db", null, 1));
	}

	@Test
	public void testSave() throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException, SQLException {
		Book book = new Book();
		book.setBookName("test");
		book.setId(1L);
//		target.save(book);
//		Assert.assertNotNull(target.save(book));
	}
}
