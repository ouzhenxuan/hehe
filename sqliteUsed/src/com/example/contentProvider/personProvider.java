package com.example.contentProvider;

import com.example.server.presonDbOpenHelper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class personProvider extends ContentProvider {

	private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	private static final int INSERT = 1;
	private static final int DELETE = 2;
	private static final int UPDATE = 3;
	private static final int QUERY = 4;
	private static final int QUERYONE = 5;
	
	private presonDbOpenHelper helper ;
	static {
		matcher.addURI("com.example.contentProvider", "insert", INSERT);
		matcher.addURI("com.example.contentProvider", "delete", DELETE);
		matcher.addURI("com.example.contentProvider", "update", UPDATE);
		matcher.addURI("com.example.contentProvider", "query", QUERY);
		matcher.addURI("com.example.contentProvider", "query/#", QUERYONE);
		
	}
	
	@Override
	public int delete(Uri arg0, String whereClause, String[] whereArgs) {

		if (matcher.match(arg0) == DELETE) {
			SQLiteDatabase db = helper.getWritableDatabase();
//			db.delete("person", "name=?", new String[]{oldname});调用这个方法的写法
			db.delete("person", whereClause, whereArgs);
			
			Uri personUri = Uri.parse("content://com.example.contentProvider");
			getContext().getContentResolver().notifyChange(personUri, null);
		}else {
			throw new IllegalArgumentException("路径不匹配。不能删除");
		}
		
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		if (matcher.match(uri)==QUERY) {
			return "vnd.android.cursor.dir/person";
		}else if (matcher.match(uri)==QUERYONE) {
			return "vnd.android.cursor.item/person";
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		if (matcher.match(uri)==INSERT) {
			SQLiteDatabase db = helper.getWritableDatabase();
			db.insert("person", null, values);
			 Uri personUri = Uri.parse("content://com.example.contentProvider");
//			 Uri personUri = Uri.parse("content://com.example.contentProvider");
			getContext().getContentResolver().notifyChange(personUri, null);
		}else {
			throw new IllegalArgumentException("路径不匹配。不能插入");
		}
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub	
//		helper = new presonDbOpenHelper(getContext(),1);
		Context context = getContext();
		SharedPreferences sp = context.getSharedPreferences("version", context.MODE_PRIVATE);
		int verson = sp.getInt("version", 1);
		helper = new presonDbOpenHelper(getContext(),verson );
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		// TODO Auto-generated method stub
		if(matcher.match(uri) == QUERY){
			SQLiteDatabase db  = helper.getReadableDatabase();
			Cursor cursor = db.query("person", projection, selection, selectionArgs, null, null, sortOrder);
			return cursor;
		}else if(matcher.match(uri)== QUERYONE){
			long id = ContentUris.parseId(uri);
			SQLiteDatabase db = helper.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from person where id = ?",new String[]{id+""});
			return cursor;
		}else {
			throw new IllegalArgumentException("路径不匹配。不能查询");
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String whereClause, String[] whereArgs) {
		if (matcher.match(uri)==UPDATE) {
			SQLiteDatabase db = helper.getWritableDatabase();
			db.update("person", values, whereClause, whereArgs);
			Uri personUri = Uri.parse("content://com.example.contentProvider");
			getContext().getContentResolver().notifyChange(personUri, null);
		}else {
			throw new IllegalArgumentException("路径不匹配。不能更新");
		}
		return 0;
	}

}
