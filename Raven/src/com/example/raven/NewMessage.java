package com.example.raven;

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
import android.widget.Toast;

import com.example.raven.adapters.NewMessageAdapter;
import com.example.raven.db.Constants;
import com.example.raven.db.RavenDAL;
import com.example.raven.objects.CountryCodeMap;

public class NewMessage extends Activity
{
	private AutoCompleteTextView mTxtPhoneNo;
	
	private RavenDAL dal = HistoryActivity.dal;
	private NewMessageAdapter mca;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_message);
		
		mTxtPhoneNo = (AutoCompleteTextView) findViewById(R.id.mmWhoNo);
		MultiAutoCompleteTextView smsTxt = (MultiAutoCompleteTextView) findViewById(R.id.SmsTxt);
		smsTxt.clearFocus();
		
		mTxtPhoneNo.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> av, View arg1, int index,
					long arg3)
			{
				Cursor c = (Cursor) av.getItemAtPosition(index);
				mTxtPhoneNo.setText("" + c.getString(2));
			}
		});
		
		Cursor c = dal.getAllContactsCursor();
		mca = new NewMessageAdapter(this, c);
		mTxtPhoneNo.setAdapter(mca);
		if(dal.getFlagValue(Constants.COLUMN_FLAG_UPDATE_CONTACTS) == Constants.UPDATE_CONTACTS)
		{
			c = dal.getAllContactsCursor();
			mca.changeCursor(c);
			dal.updateFlag(Constants.COLUMN_FLAG_UPDATE_CONTACTS,
					Constants.DONT_UPDATE_CONTACTS);
		}
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		if(dal.getFlagValue(Constants.COLUMN_FLAG_UPDATE_CONTACTS) == Constants.UPDATE_CONTACTS)
		{
			Cursor c = dal.getAllContactsCursor();
			mca.changeCursor(c);
		}
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
		
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String countryCode = tm.getSimCountryIso();
		if(phoneNo.startsWith("0"))
		{
			phoneNo = CountryCodeMap.COUNTRIES.get(countryCode)
					+ phoneNo.substring(1);
		}
		
		MultiAutoCompleteTextView smsTxt = (MultiAutoCompleteTextView) findViewById(R.id.SmsTxt);
		String message = smsTxt.getText().toString().trim();
		
		if(phoneNo.length() > 0 && message.length() > 0)
		{
			TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			int simState = telMgr.getSimState();
			switch(simState)
			{
				case TelephonyManager.SIM_STATE_ABSENT:
					displayAlert();
					break;
				case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
					// do something
					break;
				case TelephonyManager.SIM_STATE_PIN_REQUIRED:
					// do something
					break;
				case TelephonyManager.SIM_STATE_PUK_REQUIRED:
					// do something
					break;
				case TelephonyManager.SIM_STATE_READY:
					// do something
					if(!dal.isContactExist(phoneNo))
					{
						dal.addContact("", phoneNo, "temp", "", 0);
					}
					sendSMS(phoneNo, message); // method to send message
					
					Intent intent = new Intent(this, Chat.class);
					intent.putExtra("phoneNum", phoneNo);
					startActivity(intent);
					break;
				case TelephonyManager.SIM_STATE_UNKNOWN:
					// do something
					break;
			}
		}
		else
		{
			Toast.makeText(getBaseContext(),
					"Please enter both phone number and message.",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	private void displayAlert()
	{
		new AlertDialog.Builder(NewMessage.this)
				.setMessage("Sim card not available").setCancelable(false)
				.setPositiveButton("ok", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int id)
					{
						Log.d("I am inside ok", "ok");
						dialog.cancel();
					}
				}).show();
	}
	
	private void sendSMS(String phoneNumber, String message)
	{
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";
		
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		
		String countryCode = tm.getSimCountryIso();
		if(phoneNumber.startsWith("0"))
		{
			phoneNumber = CountryCodeMap.COUNTRIES.get(countryCode)
					+ phoneNumber.substring(1);
		}
		
		dal.addMessage(message, null, phoneNumber, Constants.SENT_BY_ME,
				Constants.NOT_READ, Constants.NOT_SENT);
		
		PendingIntent sentPI = PendingIntent.getBroadcast(NewMessage.this, 0,
				new Intent(SENT), 0);
		
		PendingIntent deliveredPI = PendingIntent.getBroadcast(NewMessage.this,
				0, new Intent(DELIVERED), 0);
		
		// ---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver()
		{
			
			@Override
			public void onReceive(Context arg0, Intent arg1)
			{
				switch(getResultCode())
				{
					case Activity.RESULT_OK:
						// Toast.makeText(NewMessage.this, "SMS sent",
						// Toast.LENGTH_SHORT).show();
						Log.d("NEW MESSAGE SMS", "SMS SENT");
						break;
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
						Toast.makeText(NewMessage.this, "Generic failure",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NO_SERVICE:
						Toast.makeText(NewMessage.this, "No service",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NULL_PDU:
						Toast.makeText(NewMessage.this, "Null PDU",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_RADIO_OFF:
						Toast.makeText(getBaseContext(), "Radio off",
								Toast.LENGTH_SHORT).show();
						break;
				
				}
			}
		}, new IntentFilter(SENT));
		
		// ---when the SMS has been delivered---
		registerReceiver(new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context arg0, Intent arg1)
			{
				switch(getResultCode())
				{
					case Activity.RESULT_OK:
						Toast.makeText(NewMessage.this, "SMS delivered",
								Toast.LENGTH_LONG).show();
						break;
					case Activity.RESULT_CANCELED:
						Toast.makeText(NewMessage.this, "SMS not delivered",
								Toast.LENGTH_LONG).show();
						break;
				}
			}
		}, new IntentFilter(DELIVERED));
		
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
	}
	
	public void onContactsClick(View v)
	{
		Intent intent = new Intent(this, Contacts.class);
		startActivity(intent);
	}
}
