package com.example.sqlitecontentobserver;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.R.string;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.ContentObservable;
import android.database.ContentObserver;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	
	TextView t;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("heheda", "haha");
		ContentResolver resolver = getContentResolver();
		Uri uri = Uri.parse("content://com.example.contentProvider");
		
		 t = (TextView) findViewById(R.id.tv_content);
		
		resolver.registerContentObserver(uri, true, new myobserver(new Handler()));
	}

	public class myobserver extends ContentObserver{

		public myobserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}
		public void  onChange(boolean selfChange){
			super.onChange(selfChange);
			Toast.makeText(MainActivity.this, "The sqlite has change", 0).show();
			
			Uri uri = Uri.parse("content://com.example.contentProvider/query");
			ContentResolver resolver = getContentResolver();
			Cursor cursor = resolver.query(uri, null, null, null, null);
			String text = "";
			while(cursor.moveToNext()){
				int id = cursor.getInt(0);
				String name = cursor.getString(1);
				String phone = cursor.getString(2);
				System.out.println("name"+name +"phone "+phone);
				Log.d("heheda", "name: "+name +"  phone: "+phone);
				text += "name: "+name +"  phone: "+phone + "\n";
			}
			t.setText(text);
			cursor.close();			
		}
		
		
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
