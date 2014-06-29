package com.example.raven;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raven.db.Constants;
import com.example.raven.objects.CountryCodeMap;
import com.example.raven.objects.Message;

public class Chat extends Activity
{
	private String phoneNo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		
		Intent intent = getIntent();
		phoneNo = intent.getStringExtra("phoneNum");
		if(phoneNo != null)
		{
			populateMessages(phoneNo);
		}
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
		ArrayList<Message> messages = HistoryActivity.dal.getChatWithContact(phoneNo);
		
		TableLayout chatTable = (TableLayout) findViewById(R.id.chatTable);
		chatTable.setStretchAllColumns(true);
		chatTable.bringToFront();
		
		chatTable.removeAllViews();
		
		TableRow tr = new TableRow(this);
		
		for(int i = 0; i < messages.size(); i++)
		{
			tr = new TableRow(this);
			TextView tv = new TextView(this);
			tv.setText("" + messages.get(i).getMessageTxt());
			tv.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{

				}
			});
			
			tr.addView(tv);
			chatTable.addView(tr);
		}
	}
	
	public void onSendClick(View v)
	{
		MultiAutoCompleteTextView smsTxt = (MultiAutoCompleteTextView) findViewById(R.id.SmsTxt2);
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
					sendSMS(phoneNo, message); // method to send message
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
		new AlertDialog.Builder(Chat.this).setMessage("Sim card not available")
				.setCancelable(false)
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
		HistoryActivity.dal.addMessage(message, null, phoneNumber,
				Constants.SENT_BY_ME, Constants.NOT_READ, Constants.NOT_SENT);
		
		PendingIntent sentPI = PendingIntent.getBroadcast(Chat.this, 0,
				new Intent(SENT), 0);
		
		PendingIntent deliveredPI = PendingIntent.getBroadcast(Chat.this, 0,
				new Intent(DELIVERED), 0);
		
		// ---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver()
		{
			
			@Override
			public void onReceive(Context arg0, Intent arg1)
			{
				switch(getResultCode())
				{
					case Activity.RESULT_OK:						
						Toast.makeText(Chat.this, "SMS sent",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
						Toast.makeText(Chat.this, "Generic failure",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NO_SERVICE:
						Toast.makeText(Chat.this, "No service",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NULL_PDU:
						Toast.makeText(Chat.this, "Null PDU",
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
						Toast.makeText(Chat.this, "SMS delivered",
								Toast.LENGTH_LONG).show();
						break;
					case Activity.RESULT_CANCELED:
						Toast.makeText(Chat.this, "SMS not delivered",
								Toast.LENGTH_LONG).show();
						break;
				}
			}
		}, new IntentFilter(DELIVERED));
		
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
		TableLayout chatTable = (TableLayout) findViewById(R.id.chatTable);
		TableRow tr = new TableRow(this);
		TextView tv = new TextView(this);
		tv.setText("" + message);
		tr.addView(tv);
		chatTable.addView(tr);
	}
}
