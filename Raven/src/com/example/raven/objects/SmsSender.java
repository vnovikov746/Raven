package com.example.raven.objects;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.raven.HistoryActivity;
import com.example.raven.db.Constants;
import com.example.raven.db.RavenDAL;
import com.example.raven.dict.Translator;
import com.example.raven.services.ServiceHandler;


public class SmsSender {
	private Context mContext;
	private RavenDAL dal = HistoryActivity.dal;
	private AppPreferences _appPrefs = dal.AppPreferences();
	
	public SmsSender(Context context) {
		mContext = context;
	}
	
	public void send(String phoneNo, String original) {
		if (_appPrefs.getBoolean(AppPreferences.TRANSLATE_OUT))
			send(phoneNo, original, true);
		else
			send(phoneNo, original, false);
	}
	
	public void send(String phoneNo, String original, boolean translate) {
		String translated = original;
		
		//check SIM is in
		if (!checkSimState()) return;
		
		//check Internet connection
		if (isInternetConnected()) {
			
			//check if user want to translate
			if (translate) { 
				translated = translate(original);
				if (translated.equals(original)) {
					Toast.makeText(mContext, "Unable to translate text", Toast.LENGTH_LONG).show();
					Toast.makeText(mContext, "Original text was sent.", Toast.LENGTH_LONG).show();
				}
			}
		}
		
		sendSMS(phoneNo, original, translated); // method to send message
	}
	
	private boolean isInternetConnected() {
		if (ServiceHandler.getConnectivityStatus(mContext) == ServiceHandler.TYPE_NOT_CONNECTED)
		{
			Toast.makeText(mContext, "Not connected to Internet", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
	
	private String translate(String message) {
		Translator t = Raven.SetService(Raven.YANDEX);
		
		if (_appPrefs.getBoolean(AppPreferences.TRANSLATE_OUT)) {
			String from = _appPrefs.getString(AppPreferences.TRNASLATE_TO);
			if (from == "")
				from = "he";
			String to = "en";
		
			String translated = t.translate(from, to, message);
			String original = message;
			
			if (translated == "")
				message = original;
			else
				message = translated;
		}
		
		
		Log.d("Tranlation:", message);
		
		return message;
	}
	
	private boolean checkSimState() {

		TelephonyManager telMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
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
				return true;
			case TelephonyManager.SIM_STATE_UNKNOWN:
				// do something
				break;
		}
		Log.d("checkSimState", "false");
		return false;
		
	}

	
	private void displayAlert()
	{
		new AlertDialog.Builder(mContext).setMessage("Sim card not available")
				.setCancelable(false)
				.setPositiveButton("ok", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int id)
					{
						Log.d("SIM", "I am inside ok");
						dialog.cancel();
					}
				}).show();
	}
	
	
	
	private void sendSMS(String phoneNumber, String original, String message)
	{
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";
		
		TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		
		String countryCode = CountryCodeMap.COUNTRIES
				.get(tm.getSimCountryIso());
		if(phoneNumber.startsWith(countryCode))
		{
			phoneNumber = "0" + phoneNumber.substring(countryCode.length());
		}
		
		dal.addMessage(original, message, phoneNumber, Constants.SENT_BY_ME,
				Constants.NOT_READ, Constants.NOT_SENT);
		

		
		PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0,
				new Intent(SENT), 0);
		
		PendingIntent deliveredPI = PendingIntent.getBroadcast(mContext, 0,
				new Intent(DELIVERED), 0);
		
		// ---when the SMS has been sent---
		mContext.registerReceiver(new BroadcastReceiver()
		{
			
			@Override
			public void onReceive(Context arg0, Intent arg1)
			{
				switch(getResultCode())
				{
					case Activity.RESULT_OK:
						// Toast.makeText(Chat.this, "SMS sent",
						// Toast.LENGTH_SHORT).show();
						Log.d("CHAT SMS", "SMS SENT");
						break;
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
						Toast.makeText(mContext, "Generic failure",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NO_SERVICE:
						Toast.makeText(mContext, "No service",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NULL_PDU:
						Toast.makeText(mContext, "Null PDU",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_RADIO_OFF:
						Toast.makeText(mContext, "Radio off",
								Toast.LENGTH_SHORT).show();
						break;
				
				}
			}
		}, new IntentFilter(SENT));
		
		// ---when the SMS has been delivered---
		mContext.registerReceiver(new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context arg0, Intent arg1)
			{
				switch(getResultCode())
				{
					case Activity.RESULT_OK:
						// Toast.makeText(Chat.this, "SMS delivered",
						// Toast.LENGTH_LONG).show();
						Log.d("SMS CHAT", "DELIVERED");
						break;
					case Activity.RESULT_CANCELED:
						Toast.makeText(mContext, "SMS not delivered",
								Toast.LENGTH_LONG).show();
						break;
				}
			}
		}, new IntentFilter(DELIVERED));
		
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
	}
	
}
