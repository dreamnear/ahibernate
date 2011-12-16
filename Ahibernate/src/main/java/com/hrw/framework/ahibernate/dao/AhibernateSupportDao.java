package com.hrw.framework.ahibernate.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.hrw.framework.ahibernate.annotation.Column;
import com.hrw.framework.ahibernate.annotation.Id;
import com.hrw.framework.ahibernate.annotation.OneToMany;
import com.hrw.framework.ahibernate.sqlite.AhibernateSQLiteOpenHelper;
import com.hrw.framework.ahibernate.table.TableUtils;

public class AhibernateSupportDao<T> extends ContextWrapper implements DaoSupport<T> {
	public AhibernateSupportDao(Context base) {
		super(base);
		// TODO Auto-generated constructor stub
	}

	Annotation columnAnnotation = null;
	Annotation idAnnotation = null;
	Annotation generatedValueAnnotation = null;
	Annotation oneToOneAnnotation = null;
	Annotation manyToOneAnnotation = null;
	Annotation joinColumnAnnotation = null;
	Annotation enumeratedAnnotation = null;
	Annotation versionAnnotation = null;
	public String nullColumnHack;
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
		String statement = buildInsertSql(entity);
		Object[] args = buildArgs(entity);
		return insert(statement, args);
	}

	private Object[] buildArgs(Object entity) throws IllegalArgumentException,
			IllegalAccessException {
		List<Object> args = new ArrayList<Object>();
//		@SuppressWarnings("rawtypes")
//		Class clazz = entity.getClass();
//		Field[] fields = clazz.getDeclaredFields();
//		Annotation[] fieldAnnotations = null;
//		for (Field field : fields) {
//			fieldAnnotations = field.getAnnotations();
//			if (fieldAnnotations.length != 0
//					&& field.getAnnotation(OneToMany.class) == null) {
//				field.setAccessible(true);
//				args.add(field.get(entity));
//			}
//		}
		args.add(1L);
		args.add("test");
		return args.toArray();
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

}
