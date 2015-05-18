package com.example.sqlitecontentreslover;

import java.util.List;

import com.example.vo.persons;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class secendActivity extends Activity {

	private List<persons> data = null;
	private ListView listview;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second);
		
		Intent intent = getIntent();  
		data = intent.getParcelableArrayListExtra("persons");
		
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		
		listview = (ListView) findViewById(R.id.listView1);
		ListAdapter mAdapter = new MyBaseAdapter();
		listview.setAdapter(mAdapter);
		initView();
	}
	
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
		Toast.makeText(this, "你真是按了返回键了，别不承认", 1).show();
		this.finish();
		break;
		}
		return false;
	}
	
	private void initView() {
		listview = (ListView) findViewById(R.id.listView1);
		listview.setOnItemClickListener(new MyOnItemClickListener());
	}
	
	
	class MyBaseAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = View.inflate(secendActivity.this, R.layout.item, null);
			TextView name = (TextView) view.findViewById(R.id.tv_name);
			TextView number = (TextView) view.findViewById(R.id.tv_tel);
			name.setText(data.get(position).getName());
			number.setText(data.get(position).getNumber());
		
			
			return view;
			
		}
	
	}

	private class MyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			
			String name = data.get(position).getName();
			String Number = data.get(position).getNumber();
			
			
			new AlertDialog.Builder(secendActivity.this)
			 .setTitle("姓名："+name) 
			 .setMessage("电话："+Number)
			 	.setPositiveButton("确定", null)
			 	.show();
		}
	}
	
	public void back(View v){
		this.finish();
		
	}
}
