package com.hrw.framework.ahibernate.dao;

import android.content.ContentValues;

public class AhibernateCompileToContentValues<T> {
	public OrmContentValuesBuilder ormBuilder;

	public ContentValues compile(T object) throws IllegalArgumentException,
			IllegalAccessException {
		ormBuilder = OrmContentValuesBuilder.getInstance();
		return ormBuilder.bulid(object);
	}

}
