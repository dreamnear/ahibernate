package com.hrw.framework.ahibernate.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.hrw.framework.ahibernate.annotation.Column;
import com.hrw.framework.ahibernate.annotation.Id;
import com.hrw.framework.ahibernate.annotation.OneToMany;
import com.hrw.framework.ahibernate.sql.builder.InsertData;
import com.hrw.framework.ahibernate.table.TableUtils;

public abstract class AhibernateHelper extends SQLiteOpenHelper {

	/**
	 * Create a helper object to create, open, and/or manage a database. This
	 * method always returns very quickly. The database is not actually created
	 * or opened until one of getWritableDatabase or getReadableDatabase is
	 * called.
	 * 
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public AhibernateHelper(Context context, String name, CursorFactory factory,
			int version) {
		// create database when fist call
		// SQLiteOpenHelper.getWritableDatabase().
		super(context, name, factory, version);
	}

	/**
	 * Called when the database is created for the first time. This is where the
	 * creation of tables and the initial population of the tables should
	 * happen.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// create tables db.execSQL("");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	/**
	 * called when you need create tables. before call super.onCreate(db).
	 * 
	 * <br>
	 * public void onCreate(SQLiteDatabase db) <br>
	 * { // create tables <br>
	 * createTables(db,true,Book.class,Author.class);<br>
	 * super.onCreate(db) <br>
	 * 
	 * 
	 * @param db
	 * @param clazzs
	 * @param ifNotExists
	 *            if true will add IF NOT EXISTS clause
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void createTables(boolean ifNotExists, Class... clazzs) {
		SQLiteDatabase db = getWritableDatabase();
		for (Class clazz : clazzs) {
			db.execSQL(TableUtils.buildCreateTableStatements(
					TableUtils.extractTableInfo(clazz), ifNotExists));
		}
	}

	public int save(Object... objects) throws IllegalArgumentException,
			IllegalAccessException {
		SQLiteDatabase db = getWritableDatabase();
		for (Object object : objects) {
			InsertData insertData = buildInsertData(object);
			db.execSQL(insertData.getInsertSql(), insertData
					.getColumnAndValue().values().toArray());
		}
		return 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InsertData buildInsertData(Object entity)
			throws IllegalArgumentException, IllegalAccessException {
		Class clazz = entity.getClass();
		Field idField = TableUtils.extractIdField(clazz);
		Field[] fields = clazz.getDeclaredFields();
		Map<String, Object> columnAndValue = new HashMap<String, Object>();

		StringBuilder sb = new StringBuilder(256);
		StringBuilder sbValues = new StringBuilder(256);

		InsertData insertData = new InsertData();
		insertData.setTableName(TableUtils.extractTableName(clazz));
		insertData.setIdColumn(idField.getName());

		Annotation[] fieldAnnotations = null;
		for (Field field : fields) {
			field.setAccessible(true);
			fieldAnnotations = field.getAnnotations();
			if (fieldAnnotations.length != 0) {
				for (Annotation annotation : fieldAnnotations) {
					String columnName = null;
					if (annotation instanceof Id) {
						if (((Id) annotation).autoGenerate()) {
							continue;
						} else {
							columnName = ((Id) annotation).name();
						}
					} else if (annotation instanceof Column) {
						columnName = ((Column) annotation).name();
					} else if (annotation instanceof OneToMany) {
						continue;
						// Ignore
					}
					columnAndValue.put((columnName != null && !columnName
							.equals("")) ? columnName : field.getName(), field
							.get(entity));
				}

			}
		}
		// builder insert sql.
		sb.append("INSERT INTO ");
		sb.append(insertData.getTableName());
		sb.append(" (");
		sbValues.append(" (");
		Boolean isFirst = true;
		for (Entry entry : columnAndValue.entrySet()) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(", ");
				sbValues.append(", ");
			}
			sb.append(entry.getKey());
			sbValues.append("?");
		}
		sb.append(")");
		sbValues.append(")");
		sb.append(" values");
		sb.append(sbValues);
		insertData.setColumnAndValue(columnAndValue);
		insertData.setInsertSql(sb.toString());
		return insertData;
	}

}
