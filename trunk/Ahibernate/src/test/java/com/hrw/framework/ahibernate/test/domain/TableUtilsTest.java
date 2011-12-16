package com.hrw.framework.ahibernate.test.domain;

import junit.framework.Assert;

import com.hrw.framework.ahibernate.table.TableUtils;
import org.junit.Test;

public class TableUtilsTest {
	@Test
	public void testBuildCreateTableStatements() {
		String result = TableUtils.buildCreateTableStatements(
				TableUtils.extractTableInfo(Book.class), false);
		Assert.assertEquals("CREATE TABLE book (id INTEGER PRIMARY KEY, bookName TEXT)",
				result);

	}
}
