package com.example.raven.objects;

import java.util.List;

import com.example.raven.R;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
//import android.widget.Toast;








import android.util.Log;

import com.example.raven.Chat;
import com.example.raven.HistoryActivity;
import com.example.raven.db.Constants;
import com.example.raven.db.RavenDAL;
import com.example.raven.dict.Translator;

public class SmsReceiver extends BroadcastReceiver
{
	// All available column names in SMS table
	// [_id, thread_id, address,
	// person, date, protocol, read,
	// status, type, reply_path_present,
	// subject, body, service_center,
	// locked, error_code, seen]
	
	public static final String SMS_EXTRA_NAME = "pdus";
	public static final String SMS_URI = "content://sms";
	
	public static final String ADDRESS = "address";
	public static final String PERSON = "person";
	public static final String DATE = "date";
	public static final String READ = "read";
	public static final String STATUS = "status";
	public static final String TYPE = "type";
	public static final String BODY = "body";
	public static final String SEEN = "seen";
	
	public static final int MESSAGE_TYPE_INBOX = 1;
	public static final int MESSAGE_TYPE_SENT = 2;
	
	public static final int MESSAGE_IS_NOT_READ = 0;
	public static final int MESSAGE_IS_READ = 1;
	
	public static final int MESSAGE_IS_NOT_SEEN = 0;
	public static final int MESSAGE_IS_SEEN = 1;
	
	// Change the password here or give a user possibility to change it
	public static final byte[] PASSWORD = new byte[] { 0x20, 0x32, 0x34, 0x47,
			(byte) 0x84, 0x33, 0x58 };
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		// Get SMS map from Intent
		Bundle extras = intent.getExtras();
		
//		String messages = "";
		
		if(extras != null)
		{
			// Get received SMS array
			Object[] smsExtra = (Object[]) extras.get(SMS_EXTRA_NAME);
			
			// Get ContentResolver object for pushing encrypted SMS to incoming
			// folder
			ContentResolver contentResolver = context.getContentResolver();
			
			for(int i = 0; i < smsExtra.length; ++i)
			{
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) smsExtra[i]);
				
//				String body = sms.getMessageBody().toString();
				String phone = sms.getOriginatingAddress();
				
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				
				String countryCode = CountryCodeMap.COUNTRIES.get(tm
						.getSimCountryIso());
				if(phone.startsWith(countryCode))
				{
					phone = "0" + phone.substring(countryCode.length());
				}
				
//				messages += "SMS from " + phone + " :\n";
//				messages += body + "\n";
				
				// Here you can add any your code to work with incoming SMS
				// I added encrypting of all received SMS
				
				putSmsToDatabase(context, contentResolver, sms);
			}
			
			// Display SMS message
//			Toast.makeText(context, "RAVEN: " + messages, Toast.LENGTH_SHORT)
//					.show();
		}
		// WARNING!!!
		// If you uncomment next line then received SMS will not be put to
		// incoming.
		// Be careful!
		// this.abortBroadcast();
	}
	
	@SuppressWarnings("deprecation")
	private void putSmsToDatabase(Context context,
			ContentResolver contentResolver, SmsMessage sms)
	{
		String body = sms.getMessageBody().toString();
		String phone = sms.getOriginatingAddress();
		
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		
		String countryCode = CountryCodeMap.COUNTRIES
				.get(tm.getSimCountryIso());
		if(phone.startsWith(countryCode))
		{
			phone = "0" + phone.substring(countryCode.length());
		}
		
		// if(phone.startsWith("0"))
		// {
		// phone = countryCode + phone.substring(1);
		// }
		
		RavenDAL dal = new RavenDAL(context);
		AppPreferences _appPrefs = dal.AppPreferences();
		String translated = body;
		if (_appPrefs.getBoolean(AppPreferences.TRANSLATE_IN)) {
			String to = _appPrefs.getString(AppPreferences.TRNASLATE_TO);
			if (to == "")
				to = "he";
			String from = "en";
			translated = translate(from, to, body);
		}
			
		dal.addMessage(body, translated, phone, Constants.RECEIVED,
				Constants.NOT_READ, Constants.NOT_SENT);
		Cursor c1 = dal.getAllLastMessagesCursor();
		Cursor c2 = dal.getChatWithContactCursor(phone);
		HistoryActivity.mca.changeCursor(c1);
		Chat.mca.changeCursor(c2);
		Chat.list.setSelection(Chat.list.getAdapter().getCount()-1);
				
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    // Get info from the currently active task
	    List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
	    String activityName = taskInfo.get(0).topActivity.getClassName();
	    if(!activityName.equals(HistoryActivity.class.getName()) && !activityName.equals(Chat.class.getName()))
	    {
			NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification mNotification = new Notification(R.drawable.ic_launcher,"Received SMS",System.currentTimeMillis());		
			int NOTIFICATION_ID = 1;
			mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
			mNotification.defaults |= Notification.DEFAULT_VIBRATE;
//			mNotification.defaults |= Notification.DEFAULT_SOUND;
			mNotification.sound = Uri.parse("android.resource://" + context.getPackageName() + R.raw.crowing);
			Intent mIntent = new Intent(context, Chat.class);
			mIntent.putExtra("phoneNum", phone);
			PendingIntent mPedingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_CANCEL_CURRENT);
			mNotification.setLatestEventInfo(context, "Received SMS", body, mPedingIntent);
			notificationManager.notify(NOTIFICATION_ID , mNotification);	
	    }
	    
		// Create SMS row
		// ContentValues values = new ContentValues();
		// values.put(ADDRESS, sms.getOriginatingAddress());
		// values.put(DATE, sms.getTimestampMillis());
		// values.put(READ, MESSAGE_IS_NOT_READ);
		// values.put(STATUS, sms.getStatus());
		// values.put(TYPE, MESSAGE_TYPE_INBOX);
		// values.put(SEEN, MESSAGE_IS_NOT_SEEN);
		// values.put(BODY, sms.getMessageBody().toString());
		//
		// try
		// {
		// String encryptedPassword = StringCryptor.encrypt( new
		// String(PASSWORD), sms.getMessageBody().toString() );
		// values.put( BODY, encryptedPassword );
		// }
		// catch ( Exception e )
		// {
		// e.printStackTrace();
		// }
		//
		// Push row into the SMS table
		// contentResolver.insert(Uri.parse(SMS_URI), values);
	}
	
	private String translate(String from, String to, String message) {
		Translator t = Raven.SetService(Raven.YANDEX);
		String translated = t.translate(from, to, message);
		String original = message;
		
		if (translated == "")
			message = original;
		else
			message = translated;
		
		Log.d("Tranlation:", message);
		
		return message;
	}
}
