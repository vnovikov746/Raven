package com.example.raven;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.raven.db.Constants;
import com.example.raven.db.RavenDAL;
import com.example.raven.objects.ContactObserverService;
import com.example.raven.objects.Message;
import com.example.raven.objects.SmsReceiver;

public class HistoryActivity extends Activity
{
	private RavenDAL dal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		Intent contactService = new Intent(this, ContactObserverService.class);
		
		startService(contactService);
		
		dal = new RavenDAL(this);
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
