package com.example.raven;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.raven.adapters.ContactsCursorAdapter;
import com.example.raven.db.RavenDAL;
import com.example.raven.objects.ContactList;

public class Contacts extends Activity implements OnItemClickListener
{
	private RavenDAL dal = HistoryActivity.dal;
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		
		ContactList.updateList(this);
		
		list = (ListView)findViewById(R.id.contactList);
		Cursor c = dal.getAllContactsCursor();
		ContactsCursorAdapter mca = new ContactsCursorAdapter(this,c);
		list.setAdapter(mca);
		list.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int onItemClick, long id)
	{
		Cursor c = (Cursor)arg0.getItemAtPosition(onItemClick);
		String phone = c.getString(2);//contact phone in the table
		c.close();
		Intent intent = new Intent(this, NewMessage.class);
		intent.putExtra("phoneNum", phone);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contacts, menu);
		return true;
	}
}
