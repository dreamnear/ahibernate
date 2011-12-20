package com.hrw.framework.ahibernate.sqlite;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.hrw.framework.ahibernate.annotation.Column;
import com.hrw.framework.ahibernate.annotation.Id;
import com.hrw.framework.ahibernate.annotation.OneToMany;
import com.hrw.framework.ahibernate.sql.builder.AhibernateSqlBuilder;
import com.hrw.framework.ahibernate.sql.builder.InsertData;
import com.hrw.framework.ahibernate.sql.builder.SqlBuilder;
import com.hrw.framework.ahibernate.table.TableUtils;

public class AhibernateSQLiteOpenHelper<T> extends SQLiteOpenHelper {
	private final static String TAG = "AhibernateDaoSupport";
	SqlBuilder sqlBuilder;

	private ArrayList<String> buildTableStatements = new ArrayList<String>();

	public AhibernateSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		sqlBuilder = new AhibernateSqlBuilder();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void prepareCreateTableStatements(Class... clazzs) {
		Class[] clazzss = clazzs;
		for (Class clazz : clazzss) {
			buildTableStatements.add(TableUtils.buildCreateTableStatements(
					TableUtils.extractTableInfo(clazz), true));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void dropTableStatements(Class... clazzs) {
		Class[] clazzss = clazzs;
		for (Class clazz : clazzss) {
			buildTableStatements.add(TableUtils.buildDropTableStatements(
					TableUtils.extractTableInfo(clazz), true));
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (String stmt : buildTableStatements) {
			Log.i(TAG, "Create table statement:" + stmt);
			db.execSQL(stmt);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (String stmt : buildTableStatements) {
			Log.i(TAG, "Drop table statement:" + stmt);
			db.execSQL(stmt);
		}

	}

	public String buildInsertSql(Object entity) {
		return TableUtils.buildInsertTableStatements(TableUtils
				.extractTableInfo(entity.getClass()));
	}

	public int insert(String statement, Object[] args) throws SQLException {
		SQLiteDatabase db = getWritableDatabase();
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

	public int save(Object entity) {
		try {
			InsertData insertData = sqlBuilder.buildInsertData(entity);
			return insert(insertData.getInsertSql(), insertData
					.getColumnAndValue().values().toArray());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;

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
	public List<T> getAll(Class<T> clazz) {
		List<T> queryList = new ArrayList<T>();
		String sql = sqlBuilder.buildQueryAllSql(clazz);
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		Field[] fields = clazz.getDeclaredFields();
		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				try {
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
								if (field.getType().getSimpleName()
										.equals("Long")) {
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
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

			}
		}
		cursor.close();
		return queryList;
	}

}
