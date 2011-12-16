package com.hrw.framework.ahibernate.dao;

import android.content.ContentValues;

public class AhibernateCompileToContentValues<T> {
	public OrmBuilder ormBuilder;

	public ContentValues compile(T object) throws IllegalArgumentException,
			IllegalAccessException {
		ormBuilder = OrmBuilder.getInstance();
		return ormBuilder.bulid(object);
	}

}
