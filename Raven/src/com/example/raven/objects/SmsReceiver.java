package com.example.raven.objects;

import com.example.raven.R;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.example.raven.Chat;
import com.example.raven.HistoryActivity;
import com.example.raven.db.Constants;
import com.example.raven.db.RavenDAL;

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
		
		String messages = "";
		
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
				
				String body = sms.getMessageBody().toString();
				String phone = sms.getOriginatingAddress();
				
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				
				String countryCode = CountryCodeMap.COUNTRIES.get(tm
						.getSimCountryIso());
				if(phone.startsWith(countryCode))
				{
					phone = "0" + phone.substring(countryCode.length());
				}
				
				messages += "SMS from " + phone + " :\n";
				messages += body + "\n";
				
				// Here you can add any your code to work with incoming SMS
				// I added encrypting of all received SMS
				
				putSmsToDatabase(context, contentResolver, sms);
			}
			
			// Display SMS message
			Toast.makeText(context, "RAVEN: " + messages, Toast.LENGTH_SHORT)
					.show();
		}
		// WARNING!!!
		// If you uncomment next line then received SMS will not be put to
		// incoming.
		// Be careful!
		// this.abortBroadcast();
	}
	
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
		dal.addMessage(body, null, phone, Constants.RECEIVED,
				Constants.NOT_READ, Constants.NOT_SENT);
		Cursor c1 = dal.getAllLastMessagesCursor();
		Cursor c2 = dal.getChatWithContactCursor(phone);
		HistoryActivity.mca.changeCursor(c1);
		Chat.mca.changeCursor(c2);
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_launcher).setContentTitle("Received SMS").setContentText(body);
		Intent resultIntent = new Intent(context, Chat.class);
		PendingIntent resultPedingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPedingIntent);
		
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
}
