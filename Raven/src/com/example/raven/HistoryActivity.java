package com.example.raven;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raven.db.Constants;
import com.example.raven.db.RavenDAL;
import com.example.raven.objects.ContactObserverService;
import com.example.raven.objects.Message;
import com.example.raven.objects.SmsReceiver;

public class HistoryActivity extends Activity
{
	public static RavenDAL dal;
	public static ArrayList<Map<String, String>> mPeopleList;
	
	private SharedPreferences settings;
	private Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		
		settings = getSharedPreferences(Constants.SHARED_PROCESS_SETTINGS,
				MODE_MULTI_PROCESS);
		
		dal = new RavenDAL(this);
		
		int updateContacts = settings.getInt(
				Constants.SHARED_PROCESS_SETTINGS_UPDATE_CONTACTS,
				Constants.UPDATE_CONTACTS);
		
		int createServiceInstance = settings.getInt(
				Constants.SHARED_PROCESS_SETTINGS_SERVICE_INSTANCE,
				Constants.CREATE_SERVICE_INSTANCE);
		
		if(createServiceInstance == Constants.CREATE_SERVICE_INSTANCE)
		{
			Intent contactService = new Intent(this,
					ContactObserverService.class);
			startService(contactService);
		}
		
		if(updateContacts == Constants.UPDATE_CONTACTS)
		{
			Toast.makeText(this, "Contact List Updating....",
					Toast.LENGTH_LONG * 2).show();
			mPeopleList = dal.getAllContacts();
			editor = settings.edit();
			editor.putInt(Constants.SHARED_PROCESS_SETTINGS_UPDATE_CONTACTS,
					Constants.DONT_UPDATE_CONTACTS);
			editor.commit();
		}
		// mPeopleList = dal.getAllContacts();
		// if(mPeopleList.isEmpty())
		// {
		// Toast.makeText(this, "Contact List Updating....",
		// Toast.LENGTH_LONG * 2).show();
		//
		// Toast.makeText(this, "Enjoy", Toast.LENGTH_SHORT).show();
		// mPeopleList = dal.getAllContacts();
		// }
		showHistory();
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		showHistory();
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
			messages.add(new Message("No messages", null,
					new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale
							.getDefault()).format(new Date()), "Raven Support",
					Constants.RECEIVED, Constants.READ, Constants.NOT_SENT));
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
	
	public void onUpdateClick(View v)
	{
		
		ContentResolver contentResolver = getContentResolver();
		Cursor cursor = contentResolver.query(Uri.parse("content://sms/inbox"),
				null, null, null, null);
		
		int indexBody = cursor.getColumnIndex(SmsReceiver.BODY);
		int indexAddr = cursor.getColumnIndex(SmsReceiver.ADDRESS);
		
		if(indexBody < 0 || !cursor.moveToFirst())
		{
			return;
		}
		
		do
		{
			// String str = "Sender: " + cursor.getString( indexAddr ) + "\n" +
			// cursor.getString( indexBody );
			// smsList.add( str );
			dal.addMessage(cursor.getString(indexBody), null,
					cursor.getString(indexAddr), Constants.SENT_BY_ME,
					Constants.READ, Constants.NOT_SENT);
		}
		while(cursor.moveToNext());
		
		showHistory();
	}
}
