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
import com.example.raven.objects.SmsSender;

public class Chat extends Activity
{
	private RavenDAL dal = HistoryActivity.dal;
	private String phoneNo;
	public static ListView list;
	public static ChatCursorAdapter mca;
	private SmsSender sender;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		sender = new SmsSender(this);
		
		Intent intent = getIntent();
		phoneNo = intent.getStringExtra("phoneNum");
		if(phoneNo != null)
		{
			populateMessages(phoneNo);
		}
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
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
