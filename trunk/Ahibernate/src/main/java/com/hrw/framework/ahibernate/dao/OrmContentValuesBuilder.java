package com.hrw.framework.ahibernate.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import android.content.ContentValues;

import com.hrw.framework.ahibernate.annotation.Column;
import com.hrw.framework.ahibernate.annotation.Table;

public class OrmContentValuesBuilder {
	protected static OrmContentValuesBuilder instance;
	public ContentValues mContentValues;
	public String tableName;
	public String nullColumnHack;
	Annotation columnAnnotation = null;
	Annotation idAnnotation = null;
	Annotation generatedValueAnnotation = null;
	Annotation oneToOneAnnotation = null;
	Annotation manyToOneAnnotation = null;
	Annotation joinColumnAnnotation = null;
	Annotation enumeratedAnnotation = null;
	Annotation versionAnnotation = null;

	// private static String TABLE_NAME = "tableName";

	public static OrmContentValuesBuilder getInstance() {
		if (null == instance) {
			instance = new OrmContentValuesBuilder();
		}
		return instance;
	}

	public <T> ContentValues bulid(T domain) throws IllegalArgumentException,
			IllegalAccessException {
		mContentValues = new ContentValues();
		// Table table = domain.getClass().getAnnotation(Table.class);
		// mContentValues.put(TABLE_NAME, table.name());
		for (Field field : domain.getClass().getDeclaredFields()) {
			if (field.getAnnotations().length != 0) {
				field.setAccessible(true);
				field.getType().getName();
				for (Annotation annotation : field.getAnnotations()) {
					Class<?> annotationClass = annotation.annotationType();
					if (annotationClass.getName().equals(
							"com.hrw.framework.ahibernate.annotation.Column")) {
						columnAnnotation = annotation;
						nullColumnHack = field.getAnnotation(Column.class)
								.name();
						mContentValues.put(nullColumnHack, field.get(domain)
								.toString());

					}
					if (annotationClass.getName().equals(
							"com.hrw.framework.ahibernate.annotation.Id")) {
						idAnnotation = annotation;
					}
					if (annotationClass
							.getName()
							.equals("com.hrw.framework.ahibernate.annotation.GeneratedValue")) {
						generatedValueAnnotation = annotation;
					}
					if (annotationClass.getName().equals(
							"com.hrw.framework.ahibernate.annotation.OneToOne")) {
						oneToOneAnnotation = annotation;
					}
					if (annotationClass
							.getName()
							.equals("com.hrw.framework.ahibernate.annotation.ManyToOne")) {
						manyToOneAnnotation = annotation;
					}
					if (annotationClass
							.getName()
							.equals("com.hrw.framework.ahibernate.annotation.JoinColumn")) {
						joinColumnAnnotation = annotation;
					}
					if (annotationClass
							.getName()
							.equals("com.hrw.framework.ahibernate.annotation.Enumerated")) {
						enumeratedAnnotation = annotation;
					}
					if (annotationClass.getName().equals(
							"com.hrw.framework.ahibernate.annotation.Version")) {
						versionAnnotation = annotation;
					}
				}

				// Class.forName(f.getType().getName()).newInstance();
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
