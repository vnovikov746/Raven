package com.example.raven;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.raven.db.RavenDAL;

public class Contacts extends Activity
{
	// private SharedPreferences settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		
		// if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		// {
		// settings = getSharedPreferences(Constants.SHARED_PROCESS_SETTINGS,
		// 0 | MODE_MULTI_PROCESS);
		// }
		// else
		// {
		// settings = getSharedPreferences(Constants.SHARED_PROCESS_SETTINGS,
		// 0);
		// }
		
		HistoryActivity.dal = new RavenDAL(this);
		
		// int updateContacts = settings.getInt(
		// Constants.SHARED_PROCESS_SETTINGS_UPDATE_CONTACTS,
		// Constants.UPDATE_CONTACTS);
		// if(updateContacts == Constants.UPDATE_CONTACTS)
		// {
		// try
		// {
		// settings.wait();
		// }
		// catch(InterruptedException e)
		// {
		// e.printStackTrace();
		// }
		HistoryActivity.mPeopleList = HistoryActivity.dal.getAllContacts();
		// }
		
		TableLayout contactsTable = (TableLayout) findViewById(R.id.contactsTable);
		contactsTable.setStretchAllColumns(true);
		contactsTable.bringToFront();
		
		contactsTable.removeAllViews();
		
		TableRow tr = new TableRow(this);
		contactsTable.addView(tr);
		
		for(int i = 0; i < HistoryActivity.mPeopleList.size(); i++)
		{
			Map<String, String> NamePhoneType = HistoryActivity.mPeopleList
					.get(i);
			
			tr = new TableRow(this);
			TextView tv = new TextView(this);
			tv.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					TableRow tr = (TableRow) v.getParent();
					TextView items = (TextView) tr.getChildAt(0);
					String phone = items.getText().toString().split("\n")[1];
					Context context = getApplicationContext();
					Intent intent = new Intent(context, NewMessage.class);
					intent.putExtra("phoneNum", phone);
					startActivity(intent);
				}
			});
			String txt = "";
			txt += NamePhoneType.get("Name") + "\n";
			txt += NamePhoneType.get("Phone") + "\n\n";
			tv.setText(txt);
			tr.addView(tv);
			contactsTable.addView(tr);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contacts, menu);
		return true;
	}
	
}
