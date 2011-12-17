package com.hrw.framework.ahibernate.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.hrw.framework.ahibernate.annotation.Column;
import com.hrw.framework.ahibernate.annotation.Id;
import com.hrw.framework.ahibernate.annotation.OneToMany;
import com.hrw.framework.ahibernate.sql.builder.AhibernateSqlBuilder;
import com.hrw.framework.ahibernate.sql.builder.InsertData;
import com.hrw.framework.ahibernate.sql.builder.SqlBuilder;
import com.hrw.framework.ahibernate.sqlite.AhibernateSQLiteOpenHelper;
import com.hrw.framework.ahibernate.table.TableUtils;

public class AhibernateSupportDao<T> extends ContextWrapper implements
		DaoSupport<T> {
	SqlBuilder sqlBuilder;

	public AhibernateSupportDao(Context base) {
		super(base);
		sqlBuilder = new AhibernateSqlBuilder();
	}

	private AhibernateSQLiteOpenHelper<T> sqliteOpenHelper;

	// Strubg INSERT = "insert into myTable(date, time, cost) values (?,?,?)";

	public AhibernateSQLiteOpenHelper<T> getSqliteOpenHelper() {
		return sqliteOpenHelper;
	}

	public void setSqliteOpenHelper(
			AhibernateSQLiteOpenHelper<T> sqliteOpenHelper) {
		this.sqliteOpenHelper = sqliteOpenHelper;
	}

	public String buildInsertSql(Object entity) {
		return TableUtils.buildInsertTableStatements(TableUtils
				.extractTableInfo(entity.getClass()));
	}

	public int insert(String statement, Object[] args) throws SQLException {
		SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();
		SQLiteStatement stmt = null;
		try {
			stmt = db.compileStatement(statement);
			bindArgs(stmt, args);
			long rowId = stmt.executeInsert();
			return 1;
		} catch (android.database.SQLException e) {
			throw new SQLException(
					"inserting to database failed: " + statement, e);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public int save(Object entity) throws IllegalArgumentException,
			IllegalAccessException, SQLException {
		InsertData insertData = sqlBuilder.buildInsertData(entity);
		return insert(insertData.getInsertSql(), insertData.getColumnAndValue()
				.values().toArray());
	}

	public int delete(T t) {
		// TODO Auto-generated method stub
		return 0;
	}

	private void bindArgs(SQLiteStatement stmt, Object[] args)
			throws SQLException {
		if (args == null) {
			return;
		}
		for (int i = 0; i < args.length; i++) {
			Object arg = args[i];
			if (arg == null) {
				stmt.bindNull(i + 1);
			} else {
				if (arg instanceof Long) {
					stmt.bindLong(i + 1, Long.valueOf(arg.toString()));
				} else if (arg instanceof String) {
					stmt.bindString(i + 1, arg.toString());
				} else {
					throw new SQLException("Unknown sql argument type "
							+ arg.getClass().getName());
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll(Class<T> clazz) throws InstantiationException,
			IllegalAccessException {
		List<T> queryList = new ArrayList<T>();
		String sql = sqlBuilder.buildQueryAllSql(clazz);
		SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		Field[] fields = clazz.getDeclaredFields();
		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				T t = clazz.newInstance();
				Annotation[] fieldAnnotations = null;
				for (Field field : fields) {
					field.setAccessible(true);
					fieldAnnotations = field.getAnnotations();
					if (fieldAnnotations.length != 0) {
						for (Annotation annotation : fieldAnnotations) {
							String columnName = null;
							if (annotation instanceof Id) {
								columnName = ((Id) annotation).name();
							} else if (annotation instanceof Column) {
								columnName = ((Column) annotation).name();
							} else if (annotation instanceof OneToMany) {
								continue;
								// Ignore
							}
							if (field.getType().getSimpleName().equals("Long")) {
								field.set(
										t,
										cursor.getLong(cursor
												.getColumnIndexOrThrow((columnName != null && !columnName
														.equals("")) ? columnName
														: field.getName())));
							} else if (field.getType().getSimpleName()
									.equals("String")) {
								field.set(
										t,
										cursor.getString(cursor
												.getColumnIndexOrThrow((columnName != null && !columnName
														.equals("")) ? columnName
														: field.getName())));
							}

						}
					}
				}
				queryList.add(t);
			}
		}
		cursor.close();
		return queryList;
	}
}
