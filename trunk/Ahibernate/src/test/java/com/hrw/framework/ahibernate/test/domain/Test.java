package com.hrw.framework.ahibernate.test.domain;

import com.hrw.framework.ahibernate.table.TableUtils;
import com.hrw.framework.ahibernate.test.domain.Book;

public class Test {

	/**
	 * @param args
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static void main(String[] args) throws SecurityException,
			NoSuchFieldException {
//		Book book = new Book();
//		TableInfo tableInfo = new TableInfo();
//		tableInfo.setTableName("test");
//		tableInfo.setIdField( book.getClass().getDeclaredField("id"));
//		Map<String, Field> fieldNameMap = new HashMap<String, Field>();
//		Field[] fs = book.getClass().getDeclaredFields();
//		fieldNameMap.put("bookName", book.getClass().getDeclaredField("bookName"));
//		fieldNameMap.put("id", book.getClass().getDeclaredField("id"));
//		tableInfo.setFieldNameMap(fieldNameMap);
		String s = TableUtils.buildCreateTableStatements(TableUtils.extractTableInfo(Book.class), false);
		System.out.println(s);
	}

}
