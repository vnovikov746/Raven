package com.example.raven;

import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Contacts extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		
		TableLayout contactsTable = (TableLayout) findViewById(R.id.contactsTable);
		contactsTable.setStretchAllColumns(true);
		contactsTable.bringToFront();
		
		contactsTable.removeAllViews();
		
		TableRow tr = new TableRow(this);
		contactsTable.addView(tr);
		
		for(int i = 0; i < HistoryActivity.mPeopleList.size(); i++)
		{
			tr = new TableRow(this);
			TextView tv = new TextView(this);
			String txt = "";
			for(Map.Entry<String, String> entry : HistoryActivity.mPeopleList
					.get(i).entrySet())
			{
				if(!entry.getKey().contains("Type"))
				{
					txt += entry.getValue() + "\n";
				}
			}
			txt += "\n";
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

// Spinner
// @Override
// protected void onCreate(Bundle savedInstanceState)
// {
// super.onCreate(savedInstanceState);
// setContentView(R.layout.activity_contacts);
//
// /*
// * The Spinner to choose id
// */
// contactsSpinner = (Spinner) findViewById(R.id.contactsSpinner);
// List<String> contactsList = new ArrayList<String>();
//
// for(int i = 0; i < HistoryActivity.mPeopleList.size(); i++)
// {
// String txt = "";
// for(Map.Entry<String, String> entry : HistoryActivity.mPeopleList
// .get(i).entrySet())
// {
// if(!entry.getKey().contains("Type"))
// {
// txt += entry.getValue() + "\n";
// }
// }
// txt += "\n";
// contactsList.add(txt);
//
// ArrayAdapter<String> dataAdapterContacts = new ArrayAdapter<String>(
// this, android.R.layout.simple_spinner_item, contactsList);
// dataAdapterContacts
// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
// contactsSpinner.setAdapter(dataAdapterContacts);
// }
// }

// Table
// @Override
// protected void onCreate(Bundle savedInstanceState)
// {
// super.onCreate(savedInstanceState);
// setContentView(R.layout.activity_contacts);
//
// TableLayout contactsTable = (TableLayout) findViewById(R.id.contactsTable);
// contactsTable.setStretchAllColumns(true);
// contactsTable.bringToFront();
//
// contactsTable.removeAllViews();
//
// TableRow tr = new TableRow(this);
// contactsTable.addView(tr);
//
// for(int i = 0; i < HistoryActivity.mPeopleList.size(); i++)
// {
// tr = new TableRow(this);
// TextView tv = new TextView(this);
// String txt = "";
// for(Map.Entry<String, String> entry : HistoryActivity.mPeopleList
// .get(i).entrySet())
// {
// if(!entry.getKey().contains("Type"))
// {
// txt += entry.getValue() + "\n";
// }
// }
// txt += "\n";
// tv.setText(txt);
// tr.addView(tv);
// contactsTable.addView(tr);
// }
// }
