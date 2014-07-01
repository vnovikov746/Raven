package com.example.raven;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.raven.db.Constants;
import com.example.raven.db.RavenDAL;
import com.example.raven.objects.ContactList;
import com.example.raven.objects.ContactObserverService;
import com.example.raven.objects.Message;
import com.example.raven.objects.SmsReceiver;

public class HistoryActivity extends Activity
{
	public static RavenDAL dal;
	public static String currentActivity;
	
	// menu
	private final int groupId = 1;
	private final int NewMessageId = Menu.FIRST;
	private final int GlobalSettingsId = Menu.FIRST + 1;
	private final int AboutId = Menu.FIRST + 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		
		currentActivity = "History";
		
		dal = new RavenDAL(this);
		
		dal.addFlag(Constants.COLUMN_FLAG_SERVICE_INSTANCE,
				Constants.CREATE_SERVICE_INSTANCE);
		
		int createServiceInstance;
		createServiceInstance = dal
				.getFlagValue(Constants.COLUMN_FLAG_SERVICE_INSTANCE);
		
		if(createServiceInstance == Constants.CREATE_SERVICE_INSTANCE)
		{
			dal.addFlag(Constants.COLUMN_FLAG_UPDATE_CONTACTS, -1);
			Intent contactService = new Intent(this,
					ContactObserverService.class);
			startService(contactService);
		}
		ContactList.updateList(this);
		
		// if(updateContacts == Constants.UPDATE_CONTACTS)
		// {
		// Toast.makeText(this, "Contact List Updating....",
		// Toast.LENGTH_LONG * 2).show();
		// mPeopleList = dal.getAllContacts();
		// dal.updateFlag(Constants.COLUMN_FLAG_UPDATE_CONTACTS,
		// Constants.DONT_UPDATE_CONTACTS);
		// }
		showHistory();
	}
	
	@Override
	public void onResume()
	{
		currentActivity = "History";
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
			TextView tv = new TextView(this);
			tv.setText("" + messages.get(i).getContactPhoneNum() + " "
					+ messages.get(i).getMessageTime() + " "
					+ messages.get(i).getMessageTxt());
			tv.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					TableRow tr = (TableRow) v.getParent();
					TextView items = (TextView) tr.getChildAt(0);
					String phone = items.getText().toString().split(" ")[0];
					Context context = getApplicationContext();
					Intent intent = new Intent(context, Chat.class);
					intent.putExtra("phoneNum", phone);
					startActivity(intent);
				}
			});
			
			tr.addView(tv);
			
			historyTable.addView(tr);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		
		Intent NewMessageIntent = new Intent(this, NewMessage.class);
		menu.add(groupId, NewMessageId, NewMessageId, "New message").setIntent(
				NewMessageIntent);
		
		Intent GlobalSettingsIntent = new Intent(this, GlobalSettings.class);
		menu.add(groupId, GlobalSettingsId, GlobalSettingsId, "Preferences")
				.setIntent(GlobalSettingsIntent);
		
		menu.add(groupId, AboutId, AboutId, "About").setIntent(
				GlobalSettingsIntent);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	public void onSendNew(View v)
	{
		// Translator t = Raven.SetService(Raven.YANDEX);
		// String text = t.translate("en-he", "text");
		// Map<String, String> m = t.getLangs();
		// String text = t.detect("hello");
		
		// Display SMS message
		// Toast.makeText(this, "RAVEN: " + text , Toast.LENGTH_SHORT).show();
		// Translator t = Raven.SetService(Raven.YANDEX);
		// String text = t.translate("en-he", "text");
		// Map<String, String> m = t.getLangs();
		// String text = t.detect("hello");
		
		// Display SMS message
		// Toast.makeText(this, "RAVEN: " + text , Toast.LENGTH_SHORT).show();
		
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
