package com.example.raven;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.raven.adapters.HistoryCursorAdapter;
import com.example.raven.db.Constants;
import com.example.raven.db.RavenDAL;
import com.example.raven.objects.SmsReceiver;
import com.example.raven.services.ContactObserverService;

public class HistoryActivity extends Activity implements OnItemClickListener
{
	public static RavenDAL dal;
	public static HistoryCursorAdapter mca;
	
	// menu
	private final int groupId = 1;
	private final int NewMessageId = Menu.FIRST;
	private final int GlobalSettingsId = Menu.FIRST + 1;
	private final int AboutId = Menu.FIRST + 2;
	
	private ListView list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		
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
		
		showHistory();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int onItemClick,
			long id)
	{
		Cursor c = (Cursor) arg0.getItemAtPosition(onItemClick);
		String phone = c.getString(3);// phone in the table
		Intent intent = new Intent(this, Chat.class);
		intent.putExtra("phoneNum", phone);
		startActivity(intent);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		showHistory();
	}
	
	public void showHistory()
	{
		list = (ListView) findViewById(R.id.historyList);
		Cursor c = dal.getAllLastMessagesCursor();
		mca = new HistoryCursorAdapter(this, c);
		list.setAdapter(mca);
		list.setOnItemClickListener(this);
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
