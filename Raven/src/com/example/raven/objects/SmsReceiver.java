package com.example.raven.objects;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

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

		String messages = "";
		String body = "";
		String address = "";

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

				body = sms.getMessageBody().toString();
				address = sms.getOriginatingAddress();

				messages += "SMS from " + address + " :\n";
				messages += body + "\n";

				// Here you can add any your code to work with incoming SMS
				// I added encrypting of all received SMS

				putSmsToDatabase(context, contentResolver, sms);
			}

//          translated 
    		Translator t = Raven.SetService(Raven.YANDEX);
    		Log.d("translate", "RAVEN: "+t.translate("en-he", body));
			
			
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
		String address = sms.getOriginatingAddress();

		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String countryCode = tm.getSimCountryIso();

		if(address.startsWith("0"))
		{
			address = countryCode + address.substring(1);
		}

		RavenDAL dal = new RavenDAL(context);
		dal.addMessage(body, null, address, Constants.RECEIVED,
				Constants.NOT_READ, Constants.NOT_SENT);

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