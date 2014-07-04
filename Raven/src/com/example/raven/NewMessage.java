package com.example.raven;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.raven.db.Constants;
import com.example.raven.db.RavenDAL;
import com.example.raven.dict.Translator;
import com.example.raven.objects.CountryCodeMap;
import com.example.raven.objects.Raven;
import com.example.raven.objects.SmsSender;

public class NewMessage extends Activity
{
	private AutoCompleteTextView mTxtPhoneNo;
	private SmsSender sender;
	private RavenDAL dal = HistoryActivity.dal;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_message);
		sender = new SmsSender(this);
		
		mTxtPhoneNo = (AutoCompleteTextView) findViewById(R.id.mmWhoNo);
		MultiAutoCompleteTextView smsTxt = (MultiAutoCompleteTextView) findViewById(R.id.SmsTxt);
		smsTxt.clearFocus();
		
		mTxtPhoneNo.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> av, View arg1, int index,
					long arg3)
			{
				@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) av
						.getItemAtPosition(index);
				String number = map.get("Phone");
				mTxtPhoneNo.setText("" + number);
				MultiAutoCompleteTextView smsTxt = (MultiAutoCompleteTextView) findViewById(R.id.SmsTxt);
				smsTxt.requestFocus();
				// Cursor c = (Cursor) av.getItemAtPosition(index);
				// mTxtPhoneNo.setText("" + c.getString(2));
			}
		});
		
		// Cursor c = dal.getAllContactsCursor();
		// mca = new NewMessageAdapter(this, c);
		// if(dal.getFlagValue(Constants.COLUMN_FLAG_UPDATE_CONTACTS) ==
		// Constants.UPDATE_CONTACTS)
		// {
		// c = dal.getAllContactsCursor();
		// mca.changeCursor(c);
		// dal.updateFlag(Constants.COLUMN_FLAG_UPDATE_CONTACTS,
		// Constants.DONT_UPDATE_CONTACTS);
		// }
		
		ArrayList<Map<String, String>> mPeopleList = dal.getAllContacts();
		SimpleAdapter mAdapter = new SimpleAdapter(this, mPeopleList,
				R.layout.custcont_view,
				new String[] { "Name", "Phone", "Type" }, new int[] {
						R.id.ccontName, R.id.ccontNo, R.id.ccontType });
		mTxtPhoneNo.setAdapter(mAdapter);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		// if(dal.getFlagValue(Constants.COLUMN_FLAG_UPDATE_CONTACTS) ==
		// Constants.UPDATE_CONTACTS)
		// {
		// Cursor c = dal.getAllContactsCursor();
		// mca.changeCursor(c);
		// }
		Intent intent = getIntent();
		try
		{
			String phoneNum = intent.getStringExtra("phoneNum");
			if(!phoneNum.equals(""))
			{
				mTxtPhoneNo.setText(phoneNum);
			}
		}
		catch(Exception e)
		{}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_message, menu);
		return true;
	}
	
	public void onSendClick(View v)
	{
		String phoneNo = mTxtPhoneNo.getText().toString().trim();
		MultiAutoCompleteTextView smsTxt = (MultiAutoCompleteTextView) findViewById(R.id.SmsTxt);
		String message = smsTxt.getText().toString().trim();
		
		
		if(phoneNo.length() > 0 && message.length() > 0)
		{

			if(!dal.isContactExist(phoneNo))
			{
				dal.addContact("", phoneNo, "temp", "", 0);
			}
			sender.send(phoneNo, message);
			
			Intent intent = new Intent(this, Chat.class);
			intent.putExtra("phoneNum", phoneNo);
			startActivity(intent);
		}
		else
		{
			Toast.makeText(getBaseContext(),
					"Please enter both phone number and message.",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void onContactsClick(View v)
	{
		Intent intent = new Intent(this, Contacts.class);
		startActivity(intent);
	}
}
