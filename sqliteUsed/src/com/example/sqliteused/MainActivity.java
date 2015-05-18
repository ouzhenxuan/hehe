package com.example.sqliteused;

import java.util.ArrayList;
import java.util.List;

import com.example.server.presonDbOpenHelper;
import com.example.sqliteused.R;
import com.example.sqliteused.R.id;
import com.example.sqliteused.R.layout;
import com.example.sqliteused.R.menu;
import com.example.vo.persons;

import android.R.integer;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.SyncStateContract.Helpers;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	SharedPreferences sp;
	presonDbOpenHelper helper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sp = this.getSharedPreferences("version", Context.MODE_PRIVATE);
		int vsion = sp.getInt("version", 0);
		
		final EditText ed_userName =  (EditText) findViewById(R.id.ed_userName);
		final EditText ed_telnum =  (EditText) findViewById(R.id.ed_telnum);
		final EditText ed_oldname =  (EditText) findViewById(R.id.ed_oldname);
		final EditText ed_new_phone =  (EditText) findViewById(R.id.ed_new_phone);
		final EditText ed_new_updata_name =  (EditText) findViewById(R.id.ed_new_updata_name);
		
		Button setupData = (Button) findViewById(R.id.btn_setupSql);
		Button btn_udDataSql = (Button) findViewById(R.id.btn_udDataSql);
		Button btn_insterData = (Button) findViewById(R.id.btn_insterData);
		Button btn_delete = (Button) findViewById(R.id.btn_delete);
		Button btn_lookSql = (Button) findViewById(R.id.btn_lookSql);
		Button btn_updata = (Button) findViewById(R.id.btn_updata);
		
		final TextView t = (TextView) findViewById(R.id.versionID);
		t.setText(vsion+"");
		
		setupData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int vsion = sp.getInt("version", 0);
				if(vsion==0){//创建了数据库了
					helper = new presonDbOpenHelper(MainActivity.this, 1);
					helper.getWritableDatabase();	
					//更新数据库的版本
					Editor ed = sp.edit();
					ed.putInt("version", 1);
					ed.commit();
					t.setText(1+"");
				}else {
					Toast.makeText(MainActivity.this, "数据库已经创建过了，不能重复创建", 0).show();
				}	
			}
		});
		
		btn_udDataSql.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				int vsion = sp.getInt("version",0);
				if(vsion==0){
					Toast.makeText(MainActivity.this, "还没有创建数据库", 0).show();
				}else {
					int newVsion = vsion + 1;
					System.out.println(newVsion);
					helper = new presonDbOpenHelper(MainActivity.this, newVsion);
					helper.getWritableDatabase();	
					Editor ed = sp.edit();
					ed.putInt("version", newVsion);
					ed.commit();
					t.setText(newVsion+"");
					Toast.makeText(MainActivity.this, "数据库已经更新", 0).show();
				}
			}
		});
		
		btn_insterData.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				int vsion = sp.getInt("version",0);
				String name = ed_userName.getText().toString();
				String phone = ed_telnum.getText().toString();
				if (vsion<1) {
					Toast.makeText(MainActivity.this, "真的没有创建数据库啊哥", 0).show();
					return ;
				}
				if(name.length()==0||phone.length()==0){
					Toast.makeText(MainActivity.this, "真的没有输入内容啊", 0).show();
					return ;
				}
				helper = new presonDbOpenHelper(MainActivity.this, vsion);
				
				
				SQLiteDatabase db = helper.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put("name", name);
				values.put("number", phone);
				long id = db.insert("person", null, values);
				db.close();
				if (id>0) {
					Toast.makeText(MainActivity.this, "插入成功", 0).show();
				}
			}
		});
		
		btn_delete.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				int vsion = sp.getInt("version",0);
				String oldname = ed_oldname.getText().toString();
				if (vsion<1) {
					Toast.makeText(MainActivity.this, "真的没有创建数据库啊哥", 0).show();
					return ;
				}
				if(oldname.length()==0){
					Toast.makeText(MainActivity.this, "真的没有输入内容啊", 0).show();
					return ;
				}
				
				helper = new presonDbOpenHelper(MainActivity.this, vsion);
				
				SQLiteDatabase db = helper.getWritableDatabase();
				
				int number = db.delete("person", "name=?", new String[]{oldname});
				if (number>0) {
					Toast.makeText(MainActivity.this, "删除成功", 0).show();
				}else {
					Toast.makeText(MainActivity.this, "删除失败，可能没有该名字", 0).show();
				}
			}
		});
		
		btn_lookSql.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("unchecked")
			public void onClick(View arg0) {
				List<persons> data = null;
				data = new ArrayList<persons>();
				int vsion = sp.getInt("version", 0);
				if (vsion<1) {
					Toast.makeText(MainActivity.this, "还真的是没有创建数据库啊哥", 0).show();
					return ;
				}
				
				helper = new presonDbOpenHelper(MainActivity.this, vsion);
				SQLiteDatabase db = helper.getReadableDatabase();
				Cursor c = db.rawQuery("select * from person", null);	
				while(c.moveToNext()){
					
					int id = c.getInt(c.getColumnIndex("id"));
					String name = c.getString(c.getColumnIndex("name"));
					String phone = c.getString(c.getColumnIndex("number"));
					persons p = new persons(id, name, phone);
					data.add(p);
				}
				c.close();
				db.close();
				
				Intent in = new Intent(MainActivity.this,SecondActivity.class);
				in.putParcelableArrayListExtra("persons", (ArrayList<? extends Parcelable>) data);
				startActivity(in);
				
			}
		});
		
		btn_updata.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				int vsion = sp.getInt("version",0);
				
				String newPhone = ed_new_phone.getText().toString();
				String changeName = ed_new_updata_name.getText().toString();
				if (vsion<1) {
					Toast.makeText(MainActivity.this, "真的没有创建数据库啊哥", 0).show();
					return ;
				}
				if(changeName.length()==0||newPhone.length()==0){
					Toast.makeText(MainActivity.this, "真的没有输入内容啊", 0).show();
					return ;
				}
				
				helper = new presonDbOpenHelper(MainActivity.this, vsion);
				String oldname = ed_oldname.getText().toString();
				SQLiteDatabase db = helper.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put("number", newPhone);
				int num = db.update("person", values, "name=?", new String [] {changeName});
				db.close();
				if (num>0) {
					Toast.makeText(MainActivity.this, "更新成功", 0).show();
				}
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
