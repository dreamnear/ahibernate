package com.hrw.framework.ahibernate.dao;

import java.lang.reflect.Field;

import android.content.ContentValues;

import com.hrw.framework.ahibernate.annotation.Column;
import com.hrw.framework.ahibernate.annotation.Table;

public class OrmBuilder {
	protected static OrmBuilder instance;
	public ContentValues mContentValues;
	public String tableName;
	public String nullColumnHack;

	// private static String TABLE_NAME = "tableName";

	public static OrmBuilder getInstance() {
		if (null == instance) {
			instance = new OrmBuilder();
		}
		return instance;
	}

	public <T> ContentValues bulid(T domain) throws IllegalArgumentException,
			IllegalAccessException {
		mContentValues = new ContentValues();
		// Table table = domain.getClass().getAnnotation(Table.class);
		// mContentValues.put(TABLE_NAME, table.name());
		for (Field f : domain.getClass().getDeclaredFields()) {
			if (f.getAnnotations().length != 0) {
				f.setAccessible(true);
				f.getType().getName();
				// Class.forName(f.getType().getName()).newInstance();
				mContentValues.put(f.getAnnotation(Column.class).name(),
						f.get(domain).toString());
			}
		}
		tableName = domain.getClass().getAnnotation(Table.class).name();

		// for (Annotation annotation : domain.getClass().getAnnotations()) {
		// if(annotation.)
		// }

		// domain.getClass().isAnnotationPresent(Table.class);
		return mContentValues;

	}

}
