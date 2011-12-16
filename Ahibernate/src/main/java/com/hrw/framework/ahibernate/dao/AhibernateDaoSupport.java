package com.hrw.framework.ahibernate.dao;

import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hrw.framework.ahibernate.table.TableUtils;

public class AhibernateDaoSupport<T> extends SQLiteOpenHelper {
	private final static String TAG = "AhibernateDaoSupport";
	private ArrayList<String> buildTableStatements = new ArrayList<String>();

	public AhibernateDaoSupport(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public void createTableStatements(Class<T>... clazzs) {
		Class<T>[] clazzss = clazzs;
		for (Class<T> clazz : clazzss) {
			buildTableStatements.add(TableUtils.buildCreateTableStatements(TableUtils.extractTableInfo(clazz), true));
		}
	}
	
	public void dropTableStatements(Class<T>... clazzs) {
		Class<T>[] clazzss = clazzs;
		for (Class<T> clazz : clazzss) {
			buildTableStatements.add(TableUtils.buildDropTableStatements(TableUtils.extractTableInfo(clazz), true));
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
			Log.i(TAG, "Create table statement:" + stmt);
			db.execSQL(stmt);
		}

	}

}
