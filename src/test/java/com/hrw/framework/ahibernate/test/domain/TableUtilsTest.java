package com.hrw.framework.ahibernate.test.domain;

import junit.framework.Assert;
import org.junit.Test;

import com.hrw.framework.ahibernate.table.TableUtils;

@SuppressWarnings("unused")
public class TableUtilsTest {

	@Test
	public void testBuildCreateTableStatements() {
		String result = TableUtils.buildCreateTableStatements(
				TableUtils.extractTableInfo(Book.class), false);
		Assert.assertEquals(
				"CREATE TABLE book (id INTEGER PRIMARY KEY, book_name TEXT)",
				result);

	}

	@Test
	public void testbuildInsertTableStatements() {
		String result = TableUtils.buildInsertTableStatements(TableUtils
				.extractTableInfo(Book.class));
		Assert.assertEquals(
				"INSERT INTO book (id, book_name) values (?, ?)",
				result);
	}

}
