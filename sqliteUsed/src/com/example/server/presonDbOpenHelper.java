package com.example.server;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class presonDbOpenHelper extends SQLiteOpenHelper {

	public presonDbOpenHelper(Context context,  int version) {
		super(context, "person.db", null, version);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("���ݿⴴ���ɹ�");
		db.execSQL("create table person (id integer primary key autoincrement,name varchar(20),number varchar(20))");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

		System.out.println("���������ݿ�");
	}

}
