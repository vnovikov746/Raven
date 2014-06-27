package com.example.raven.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class ContactObserverService extends Service
{
	public static ArrayList<Map<String, String>> mPeopleList;
	protected static SharedPreferences globalSettings;
	protected static SharedPreferences.Editor editor;
	
	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		mPeopleList = new ArrayList<Map<String, String>>();
		
		// globalSettings = getSharedPreferences("GlobalSettings",
		// MODE_PRIVATE);
		// String importContacts = globalSettings.getString("Import Contacts",
		// "true");
		// if(importContacts.trim().equals("true"))
		// {
		PopulatePeopleList();
		// RavenDAL dal = new RavenDAL(this);
		// dal.addAllConacts(mPeopleList);
		// editor = globalSettings.edit();
		// editor.putString("Import Contacts", "false");
		// editor.commit();
		// }
		
		this.getContentResolver().registerContentObserver(
				ContactsContract.Contacts.CONTENT_URI, true, mObserver);
	}
	
	private ContentObserver mObserver = new ContentObserver(new Handler())
	{
		@Override
		public void onChange(boolean selfChange)
		{
			super.onChange(selfChange);
			
			PopulatePeopleList();
		}
	};
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startID)
	{
		return START_STICKY;
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		getContentResolver().unregisterContentObserver(mObserver);
	}
	
	public void PopulatePeopleList()
	{
		mPeopleList.clear();
		Cursor people = getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null,
				Phone.DISPLAY_NAME + " ASC");
		while(people.moveToNext())
		{
			String contactName = people.getString(people
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			
			String contactId = people.getString(people
					.getColumnIndex(ContactsContract.Contacts._ID));
			String hasPhone = people
					.getString(people
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			
			if((Integer.parseInt(hasPhone) > 0))
			{
				// You know have the number so now query it like this
				Cursor phones = getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				while(phones.moveToNext())
				{
					// store numbers and display a dialog letting the user
					// select which.
					String phoneNumber = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					
					String numberType = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
					
					Map<String, String> NamePhoneType = new HashMap<String, String>();
					
					NamePhoneType.put("Name", contactName);
					NamePhoneType.put("Phone", phoneNumber);
					
					if(numberType.equals("0"))
					{
						NamePhoneType.put("Type", "Work");
					}
					else if(numberType.equals("1"))
					{
						NamePhoneType.put("Type", "Home");
					}
					else if(numberType.equals("2"))
					{
						NamePhoneType.put("Type", "Mobile");
					}
					else
					{
						NamePhoneType.put("Type", "Other");
					}
					
					// Then add this map to the list.
					mPeopleList.add(NamePhoneType);
				}
				phones.close();
			}
		}
		people.close();
	}
}
