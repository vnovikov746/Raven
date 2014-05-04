package com.example.raven;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.example.raven.adapters.ChatCursorAdapter;
import com.example.raven.db.RavenDAL;
import com.example.raven.objects.AppPreferences;
import com.example.raven.objects.SmsSender;

public class Chat extends Activity
{
	// menu
	private final int groupId = 1;
	private final int ChatSettingsId = Menu.FIRST;
	
	private RavenDAL dal = HistoryActivity.dal;
	private String phoneNo;
	public static ListView list;
	public static ChatCursorAdapter mca;
	private SmsSender sender;
	private AppPreferences _appPrefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		_appPrefs = new AppPreferences(getApplicationContext());
		setContentView(R.layout.activity_chat);
		sender = new SmsSender(this);
		
		Intent intent = getIntent();
		phoneNo = intent.getStringExtra("phoneNum");
		if(phoneNo != null)
		{
			populateMessages(phoneNo);
		}
		
		
		//get data from db settings per contact
		//do we always translate in?
//		if (translate in)?
//			_appPrefs.setBoolean(_appPrefs.TRANSLATE_IN, true);
//		else
//			_appPrefs.setBoolean(_appPrefs.TRANSLATE_IN, false);
//		if (translate out)?
//			_appPrefs.setBoolean(_appPrefs.TRANSLATE_OUT, true);
//		else
//			_appPrefs.setBoolean(_appPrefs.TRANSLATE_OUT, false);
		
		
		
	}
	
	@Override
	public void onResume()
	{
//		Intent intent = getIntent();
//		phoneNo = intent.getStringExtra("phoneNum");
//		if(phoneNo != null)
//		{
//			populateMessages(phoneNo);
//		}
		super.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		
		Intent ChatSettingsIntent = new Intent(this, ChatSettings.class);
		menu.add(groupId, ChatSettingsId, ChatSettingsId, "Chat settings")
				.setIntent(ChatSettingsIntent);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	public void populateMessages(String phoneNo)
	{
		list = (ListView) findViewById(R.id.chatList);
		Cursor c = dal.getChatWithContactCursor(phoneNo);
		mca = new ChatCursorAdapter(this, c);
		list.setAdapter(mca);
		list.setSelection(list.getAdapter().getCount()-1);
	}
	
	public void onSendClick(View v)
	{
		MultiAutoCompleteTextView smsTxt = (MultiAutoCompleteTextView) findViewById(R.id.SmsTxt2);
		String message = smsTxt.getText().toString().trim();
		
		if(phoneNo.length() > 0 && message.length() > 0)
		{
			
			sender.send(phoneNo, message);
			list.setSelection(list.getAdapter().getCount()-1);
			
			Cursor c = dal.getChatWithContactCursor(phoneNo);
			mca.changeCursor(c);
			
			smsTxt.setText("");
		}
		else
		{
			Toast.makeText(getBaseContext(),
					"Please enter both phone number and message.",
					Toast.LENGTH_SHORT).show();
		}
	}
	
}
