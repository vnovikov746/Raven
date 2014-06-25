package com.example.raven;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

import Objects.Message;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import db.Constants;
import db.RavenDAL;

public class HistoryActivity extends Activity
{
	public static ArrayList<Map<String, String>> mPeopleList;
	
	private RavenDAL dal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		dal = new RavenDAL(this);
		mPeopleList = new ArrayList<Map<String, String>>();
		PopulatePeopleList();
		showHistory();
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		showHistory();
	}
	
	public void PopulatePeopleList()
	{
		mPeopleList.clear();
		Cursor people = getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		while(people.moveToNext())
		{
			String contactName = people.getString(people
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			
			String contactId = people.getString(people
					.getColumnIndex(ContactsContract.Contacts._ID));
			String hasPhone = people
					.getString(people
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			
			if((Integer.parseInt(hasPhone) > 0))
			{
				// You know have the number so now query it like this
				Cursor phones = getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				while(phones.moveToNext())
				{
					// store numbers and display a dialog letting the user
					// select which.
					String phoneNumber = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					
					String numberType = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
					
					Map<String, String> NamePhoneType = new HashMap<String, String>();
					
					NamePhoneType.put("Name", contactName);
					NamePhoneType.put("Phone", phoneNumber);
					
					if(numberType.equals("0"))
					{
						NamePhoneType.put("Type", "Work");
					}
					else if(numberType.equals("1"))
					{
						NamePhoneType.put("Type", "Home");
					}
					else if(numberType.equals("2"))
					{
						NamePhoneType.put("Type", "Mobile");
					}
					else
					{
						NamePhoneType.put("Type", "Other");
					}
					
					// Then add this map to the list.
					mPeopleList.add(NamePhoneType);
				}
				phones.close();
			}
		}
		people.close();
	}
	
	public void showHistory()
	{
		LinkedList<Message> messages = dal.getAllLastMessages();
		
		TableLayout historyTable = (TableLayout) findViewById(R.id.historyTable);
		historyTable.setStretchAllColumns(true);
		historyTable.bringToFront();
		
		historyTable.removeAllViews();
		
		TableRow tr = new TableRow(this);
		historyTable.addView(tr);
		
		if(messages.size() == 0)
		{
			messages.add(new Message("No messages",
					new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale
							.getDefault()).format(new Date()),
					Constants.RECEIVED, "Raven Service"));
		}
		
		for(int i = 0; i < messages.size(); i++)
		{
			tr = new TableRow(this);
			TextView tv1 = new TextView(this);
			tv1.setText("" + messages.get(i).getContactPhoneNum() + " "
					+ messages.get(i).getMessageTime() + " "
					+ messages.get(i).getMessageTxt());
			
			tr.addView(tv1);
			
			historyTable.addView(tr);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history, menu);
		return true;
	}
	
	public void onSendNew(View v)
	{
		Intent intent = new Intent(this, NewMessage.class);
		startActivity(intent);
	}
}
