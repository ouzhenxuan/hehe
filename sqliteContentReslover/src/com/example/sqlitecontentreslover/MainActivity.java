package com.example.sqlitecontentreslover;

import java.security.PublicKey;
import java.util.ArrayList;


import com.example.vo.persons;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText insertName ,insertphone,newName, newphone,deletename;
	Button btn_insert, btn_update ,btn_delete,btn_readDate,btn_readone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		insertName =  (EditText) findViewById(R.id.ed_userName);
		insertphone =  (EditText) findViewById(R.id.ed_telnum);
		newName =  (EditText) findViewById(R.id.ed_new_updata_name);
		newphone =  (EditText) findViewById(R.id.ed_new_phone);
		deletename =  (EditText) findViewById(R.id.ed_oldname);
		
		btn_insert = (Button) findViewById(R.id.btn_insterData);
		btn_update = (Button) findViewById(R.id.btn_updata);
		btn_delete = (Button) findViewById(R.id.btn_delete);
		btn_readDate = (Button) findViewById(R.id.btn_readTheDate);
		btn_readone = (Button) findViewById(R.id.btn_readone);
		
		btn_insert.setOnClickListener(listent);
		btn_update.setOnClickListener(listent);
		btn_delete.setOnClickListener(listent);
		btn_readDate.setOnClickListener(listent);
		btn_readone.setOnClickListener(listent);
	}
	
	OnClickListener listent = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Uri uri = null;
			ContentValues values;
			
			ContentResolver resolver = getContentResolver();
			String name = null;
			String phone = null;
			Cursor cursor =null;
			switch (v.getId()) {
			case R.id.btn_insterData:
				uri = Uri.parse("content://com.example.contentProvider/insert");
				 name = insertName.getText().toString(); 
				 phone = insertphone.getText().toString();
				if(TextUtils.isEmpty(name)){
					Toast.makeText(MainActivity.this, "名字不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(phone)){
					Toast.makeText(MainActivity.this, "电话不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				values = new ContentValues();
				values.put("name", name);
				values.put("number", phone);
				resolver.insert(uri, values);
				Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
				break;
			case R.id.btn_updata:
				uri = Uri.parse("content://com.example.contentProvider/update");
				String updateNameV = newName.getText().toString();
				String updatePhoneV = newphone.getText().toString();
				if(TextUtils.isEmpty(updateNameV)){
					Toast.makeText(MainActivity.this, "名字不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(updateNameV)){
					Toast.makeText(MainActivity.this, "电话不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				values = new ContentValues();
				values.put("name", updateNameV);
				values.put("number", updatePhoneV);
				resolver.update(uri, values, "name = ?", new String[]{updateNameV});
				Toast.makeText(MainActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
				break;
			case R.id.btn_delete:
				uri = Uri.parse("content://com.example.contentProvider/delete");
				String delNameV = deletename.getText().toString();
				if(TextUtils.isEmpty(delNameV)){
					Toast.makeText(MainActivity.this, "名字不能为空", Toast.LENGTH_SHORT).show();
					
					return;
				}
				resolver.delete(uri, "name = ?", new String[]{delNameV});
				Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
				break;
			case R.id.btn_readone:
				uri = Uri.parse("content://com.example.contentProvider/query");
				String queryName = deletename.getText().toString();
				if(TextUtils.isEmpty(queryName)){
					Toast.makeText(MainActivity.this, "名字不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				 cursor = resolver.query(uri, new String[]{"id","name","number"}, 
						"name = ?", new String[]{queryName}, null);
				if(cursor !=null){
					while(cursor.moveToNext()){
						int id = cursor.getInt(0);
						 name = cursor.getString(1);
						 phone = cursor.getString(2);
						
						new AlertDialog.Builder(MainActivity.this)
						 .setTitle("联系人信息") 
						 .setMessage("姓名："+name+"电话："+phone)
						 	.setPositiveButton("确定", null)
						 	.show();
					}
					cursor.close();
				}
				break;
			case R.id.btn_readTheDate:
				uri = Uri.parse("content://com.example.contentProvider/query");
				 cursor = resolver.query(uri, new String[]{"id","name","number"}, 
						null, null, null);
				ArrayList< persons> list = new ArrayList<persons>();
				persons person = null;
				if(cursor !=null){
					while(cursor.moveToNext()){
						int id = cursor.getInt(0);
						 name = cursor.getString(1);
						 phone = cursor.getString(2);
						person = new persons(id, name, phone);
						list.add(person);
					}
					cursor.close();
					Intent intent = new Intent(MainActivity.this,secendActivity.class);
					intent.putParcelableArrayListExtra("persons", list);
					startActivity(intent);
					
				}
				
				break;
			default:
				break;
			}
			
		}
	};
	
	public void readDate(View view){
		Uri uri = Uri.parse("content://com.example.contentProvider/query");
		ContentResolver resolver = getContentResolver();
		Cursor cursor = resolver.query(uri, null, 
				null, null, null);
		while(cursor.moveToNext()){
			int id = cursor.getInt(0);
			String name = cursor.getString(1);
			String phone = cursor.getString(2);
			System.out.println("id :"+id +"name :"+name +"phone "+ phone);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
