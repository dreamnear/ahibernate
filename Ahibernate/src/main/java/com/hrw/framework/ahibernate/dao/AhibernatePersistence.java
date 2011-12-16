package com.hrw.framework.ahibernate.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AhibernatePersistence {
	/**
	 * Return the javax.persistence.Entity annotation name for the class
	 * argument or null if none or if there was no entity name.
	 */
	public static String getEntityName(Class<?> clazz) {
		Annotation entityAnnotation = null;
		for (Annotation annotation : clazz.getAnnotations()) {
			Class<?> annotationClass = annotation.annotationType();
			if (annotationClass.getName().equals("javax.persistence.Entity")) {
				entityAnnotation = annotation;
			}
		}

		if (entityAnnotation == null) {
			return null;
		}
		try {
			Method method = entityAnnotation.getClass().getMethod("name");
			String name = (String) method.invoke(entityAnnotation);
			if (name != null && name.length() > 0) {
				return name;
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new IllegalStateException(
					"Could not get entity name from class " + clazz, e);
		}
	}
}
