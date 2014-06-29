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
import android.widget.Toast;

import com.example.raven.db.Constants;
import com.example.raven.db.RavenDAL;

public class Contacts extends Activity
{
	private RavenDAL dal = HistoryActivity.dal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		
		int updateContacts;
		updateContacts = dal
				.getFlagValue(Constants.COLUMN_FLAG_UPDATE_CONTACTS);
		
		if(updateContacts == Constants.UPDATE_CONTACTS)
		{
			Toast.makeText(this, "Contact List Updating....",
					Toast.LENGTH_LONG * 2).show();
			HistoryActivity.mPeopleList = dal.getAllContacts();
			dal.updateFlag(Constants.COLUMN_FLAG_UPDATE_CONTACTS,
					Constants.DONT_UPDATE_CONTACTS);
		}
		
		TableLayout contactsTable = (TableLayout) findViewById(R.id.contactsTable);
		contactsTable.setStretchAllColumns(true);
		contactsTable.bringToFront();
		
		contactsTable.removeAllViews();
		
		TableRow tr = new TableRow(this);
		
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
