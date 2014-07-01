package com.example.raven.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.raven.objects.Message;

public class RavenDB extends SQLiteOpenHelper
{
	public RavenDB(Context context)
	{
		super(context, Constants.DATABASE_NAME, null,
				Constants.DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// create contacts table
		db.execSQL("CREATE TABLE " + Constants.TABLE_CONTACTS + "("
				+ Constants._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ Constants.COLUMN_CONTACT_NAME + " TEXT_TYPE, "
				+ Constants.COLUMN_CONTACT_TYPE + " TEXT_TYPE, "
				+ Constants.COLUMN_CONTACT_PHONE_NUM + " TEXT_TYPE, "
				+ Constants.COLUMN_CONTACT_LANGUAGE + " TEXT_TYPE, "
				+ Constants.COLUMN_CONTACT_TRANSLATE + " INTEGER);");
		
		// create messages table
		db.execSQL("CREATE TABLE " + Constants.TABLE_MESSAGES + "("
				+ Constants._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ Constants.COLUMN_MESSAGE_TO_CONTACT
				+ " TEXT_TYPE, " // contacts phone
				+ Constants.COLUMN_MESSAGE_RECEIVED_OR_SENT
				+ " INTEGER, " // received or sent
				+ Constants.COLUMN_MESSAGE_TXT + " TEXT_TYPE, "
				+ Constants.COLUMN_MESSAGE_TRANSTATED_TXT + " TEXT_TYPE, "
				+ Constants.COLUMN_MESSAGE_READ + " INTEGER, " // read or not
				+ Constants.COLUMN_MESSAGE_SENT + " INTEGER, " // sent or not
				+ Constants.COLUMN_MESSAGE_TIME + " TXT_TYPE);"); // time
																	// received
																	// or sent
		
		// create countries table
		// db.execSQL("CREATE TABLE " + Constants.TABLE_COUNTRIES + "("
		// + Constants._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		// + Constants.COLUMN_COUNTRY_NAME + " TEXT_TYPE, "
		// + Constants.COLUMN_COUNTRY_LANGUAGE + " TEXT_TYPE);");
		
		// create flags table
		db.execSQL("CREATE TABLE " + Constants.TABLE_FLAGS + "("
				+ Constants.COLUMN_FLAG_KEY + " TEXT_TYPE, "
				+ Constants.COLUMN_FLAG_VALUE + " INTEGER);");
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// Drop older tables if existed
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_CONTACTS);
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_MESSAGES);
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_COUNTRIES);
		// db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_RAVEN_FLAGS);
		
		// Create tables again
		onCreate(db);
	}
	
	/*
	 * add flag
	 */
	public void addFlag(String flagKey, int flagValue)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(Constants.COLUMN_FLAG_KEY, flagKey);
		values.put(Constants.COLUMN_FLAG_VALUE, flagValue);
		db.insert(Constants.TABLE_FLAGS, null, values);
		db.close();
	}
	
	/*
	 * Update flag
	 */
	public void updateFlag(String flagKey, int flagValue)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(Constants.COLUMN_FLAG_KEY, flagKey);
		values.put(Constants.COLUMN_FLAG_VALUE, flagValue);
		db.update(Constants.TABLE_FLAGS, values, null, null);
		db.close();
	}
	
	/*
	 * get flag value
	 */
	public int getFlagValue(String flagKey)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		int flagVlue = -1;
		
		String selectQuery = "SELECT " + Constants.COLUMN_FLAG_VALUE + " FROM "
				+ Constants.TABLE_FLAGS + " WHERE " + Constants.COLUMN_FLAG_KEY
				+ "='" + flagKey + "';";
		
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
		{
			c.moveToFirst();
			flagVlue = c.getInt(0);
		}
		c.close();
		db.close();
		return flagVlue;
	}
	
	/*
	 * Add Contact to contacts table
	 */
	public void addContact(String name, String phoneNum, String type,
			String language, int transtale)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(Constants.COLUMN_CONTACT_NAME, name);
		values.put(Constants.COLUMN_CONTACT_TYPE, type);
		values.put(Constants.COLUMN_CONTACT_PHONE_NUM, phoneNum);
		values.put(Constants.COLUMN_CONTACT_LANGUAGE, language);
		values.put(Constants.COLUMN_CONTACT_TRANSLATE, transtale);
		
		db.insert(Constants.TABLE_CONTACTS, null, values);
		db.close();
	}
	
	/*
	 * Get contact name
	 */
	public String getContactName(String phone)
	{
		String name;
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT DISTINCT " + Constants.COLUMN_CONTACT_NAME
				+ " FROM " + Constants.TABLE_CONTACTS + " WHERE "
				+ Constants.COLUMN_CONTACT_PHONE_NUM + "='" + phone + "';";
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() == 0)
		{
			return "";
		}
		c.moveToFirst();
		name = c.getString(0);
		return name;
	}
	
	/*
	 * check if contact exist in the db
	 */
	public boolean isContactExist(String contactPhone)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		String selectQuery = "SELECT * FROM " + Constants.TABLE_CONTACTS
				+ " WHERE " + Constants.COLUMN_CONTACT_PHONE_NUM + "='"
				+ contactPhone + "';";
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
		{
			return true;
		}
		return false;
	}
	
	/*
	 * Add message to messages table
	 */
	public void addMessage(String txt, String transTxt, String contactPhone,
			int received, int read, int sent)
	{
		String currentTime;
		
		SQLiteDatabase db = this.getWritableDatabase();
		currentTime = new SimpleDateFormat("dd-MM-yyyy HH:mm",
				Locale.getDefault()).format(new Date());
		
		ContentValues values = new ContentValues();
		values.put(Constants.COLUMN_MESSAGE_TXT, txt);
		values.put(Constants.COLUMN_MESSAGE_TRANSTATED_TXT, transTxt);
		values.put(Constants.COLUMN_MESSAGE_TO_CONTACT, contactPhone);
		values.put(Constants.COLUMN_MESSAGE_RECEIVED_OR_SENT, received);
		values.put(Constants.COLUMN_MESSAGE_READ, read);
		values.put(Constants.COLUMN_MESSAGE_SENT, sent);
		values.put(Constants.COLUMN_MESSAGE_TIME, currentTime);
		
		db.insert(Constants.TABLE_MESSAGES, null, values);
		db.close();
	}
	
	public ArrayList<Message> getChatWithContact(String contactPhone)
	{
		ArrayList<Message> messages = new ArrayList<Message>();
		SQLiteDatabase db = this.getWritableDatabase();
		
		String selectQuery = "SELECT " + Constants._ID + ","
				+ Constants.COLUMN_MESSAGE_TXT + ","
				+ Constants.COLUMN_MESSAGE_TRANSTATED_TXT + ","
				+ Constants.COLUMN_MESSAGE_TIME + ","
				+ Constants.COLUMN_MESSAGE_RECEIVED_OR_SENT + ","
				+ Constants.COLUMN_MESSAGE_READ + ","
				+ Constants.COLUMN_MESSAGE_SENT + " FROM "
				+ Constants.TABLE_MESSAGES + " WHERE "
				+ Constants.COLUMN_MESSAGE_TO_CONTACT + "='" + contactPhone
				+ "';";
		
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.moveToFirst())
		{
			do
			{
				messages.add(new Message(c.getString(0), c.getString(1), c
						.getString(2), contactPhone, c.getInt(3), c.getInt(4),
						c.getInt(5)));
			}
			while(c.moveToNext());
		}
		c.close();
		db.close();
		
		return messages;
	}
	
	/*
	 * get cursor for all the messages related to contact
	 */
	public Cursor getChatWithContactCursor(String contactPhone)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		String selectQuery = "SELECT " + Constants._ID + ","
				+ Constants.COLUMN_MESSAGE_TXT + ","
				+ Constants.COLUMN_MESSAGE_TRANSTATED_TXT + ","
				+ Constants.COLUMN_MESSAGE_TIME + ","
				+ Constants.COLUMN_MESSAGE_RECEIVED_OR_SENT + ","
				+ Constants.COLUMN_MESSAGE_READ + ","
				+ Constants.COLUMN_MESSAGE_SENT + " FROM "
				+ Constants.TABLE_MESSAGES + " WHERE "
				+ Constants.COLUMN_MESSAGE_TO_CONTACT + "='" + contactPhone
				+ "';";
		
		Cursor c = db.rawQuery(selectQuery, null);
		c.moveToFirst();
		return c;
	}
	
	/*
	 * Add country/language to countries table
	 */
	// public void addCountry(String name, String language)
	// {
	// SQLiteDatabase db = this.getWritableDatabase();
	//
	// ContentValues values = new ContentValues();
	// values.put(Constants.COLUMN_COUNTRY_NAME, name);
	// values.put(Constants.COLUMN_COUNTRY_LANGUAGE, language);
	//
	// db.insert(Constants.TABLE_COUNTRIES, null, values);
	// db.close();
	// }
	
	/*
	 * Get last message from a contact
	 */
	public Message getLastMessage(String contactPhone)
	{
		String messageTxt = "";
		String translatedTxt = "";
		String messageTime = "";
		int receivedOrSent = -1;
		int read = -1;
		int sent = -1;
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT " + Constants.COLUMN_MESSAGE_TXT + ","
				+ Constants.COLUMN_MESSAGE_TRANSTATED_TXT + ","
				+ Constants.COLUMN_MESSAGE_TIME + ","
				+ Constants.COLUMN_MESSAGE_RECEIVED_OR_SENT + ","
				+ Constants.COLUMN_MESSAGE_READ + ","
				+ Constants.COLUMN_MESSAGE_SENT + " FROM "
				+ Constants.TABLE_MESSAGES + " WHERE "
				+ Constants.COLUMN_MESSAGE_TO_CONTACT + "='" + contactPhone
				+ "';";
		
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.moveToLast())
		{
			messageTxt = c.getString(0);
			translatedTxt = c.getString(1);
			messageTime = c.getString(2);
			receivedOrSent = c.getInt(3);
			read = c.getInt(4);
			sent = c.getInt(5);
		}
		c.close();
		db.close();
		
		return new Message(messageTxt, translatedTxt, messageTime,
				contactPhone, receivedOrSent, read, sent);
	}
	
	/*
	 * Get last messages from all contacts
	 */
	public LinkedList<Message> getAllLastMessages()
	{
		LinkedList<Message> messages = new LinkedList<Message>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT " + Constants.COLUMN_MESSAGE_TXT + ","
				+ Constants.COLUMN_MESSAGE_TRANSTATED_TXT + ","
				+ Constants.COLUMN_MESSAGE_TIME + ","
				+ Constants.COLUMN_MESSAGE_TO_CONTACT + ","
				+ Constants.COLUMN_MESSAGE_RECEIVED_OR_SENT + ","
				+ Constants.COLUMN_MESSAGE_READ + ","
				+ Constants.COLUMN_MESSAGE_SENT + "," + "MAX(" + Constants._ID
				+ ") AS _id" + " FROM " + Constants.TABLE_MESSAGES
				+ " GROUP BY " + Constants.COLUMN_MESSAGE_TO_CONTACT
				+ " ORDER BY " + Constants._ID + ";";
		
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.moveToFirst())
		{
			do
			{
				messages.add(new Message(c.getString(0), c.getString(1), c
						.getString(2), c.getString(3), c.getInt(4),
						c.getInt(5), c.getInt(6)));
			}
			while(c.moveToNext());
		}
		c.close();
		db.close();
		return messages;
	}
	
	/*
	 * Get Cursor fot last messages from all contacts
	 */
	public Cursor getAllLastMessagesCursor()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT " + Constants.COLUMN_MESSAGE_TXT + ","
				+ Constants.COLUMN_MESSAGE_TRANSTATED_TXT + ","
				+ Constants.COLUMN_MESSAGE_TIME + ","
				+ Constants.COLUMN_MESSAGE_TO_CONTACT + ","
				+ Constants.COLUMN_MESSAGE_RECEIVED_OR_SENT + ","
				+ Constants.COLUMN_MESSAGE_READ + ","
				+ Constants.COLUMN_MESSAGE_SENT + "," + "MAX(" + Constants._ID
				+ ") AS _id" + " FROM " + Constants.TABLE_MESSAGES
				+ " GROUP BY " + Constants.COLUMN_MESSAGE_TO_CONTACT
				+ " ORDER BY " + Constants._ID + ";";
		
		Cursor c = db.rawQuery(selectQuery, null);
		return c;
	}
	
	/*
	 * Get all contacts
	 */
	public ArrayList<Map<String, String>> getAllContacts()
	{
		ArrayList<Map<String, String>> contacts = new ArrayList<Map<String, String>>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT " + Constants.COLUMN_CONTACT_NAME + ","
				+ Constants.COLUMN_CONTACT_PHONE_NUM + ","
				+ Constants.COLUMN_CONTACT_TYPE + " FROM "
				+ Constants.TABLE_CONTACTS + ";";
		
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.moveToFirst())
		{
			do
			{
				Map<String, String> NamePhoneType = new HashMap<String, String>();
				NamePhoneType.put("Name", c.getString(0));
				NamePhoneType.put("Phone", c.getString(1));
				NamePhoneType.put("Type", c.getString(2));
				contacts.add(NamePhoneType);
			}
			while(c.moveToNext());
		}
		c.close();
		db.close();
		return contacts;
	}
	
	/*
	 * Get all contacts cursor
	 */
	public Cursor getAllContactsCursor()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT " + Constants._ID + ","
				+ Constants.COLUMN_CONTACT_NAME + ","
				+ Constants.COLUMN_CONTACT_PHONE_NUM + ","
				+ Constants.COLUMN_CONTACT_TYPE + " FROM "
				+ Constants.TABLE_CONTACTS + ";";
		
		Cursor c = db.rawQuery(selectQuery, null);
		c.moveToFirst();
		return c;
	}
	
	/*
	 * Delete all contacts
	 */
	public void deleteAllContacts()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		String deleteQuery = "DELETE " + " FROM " + Constants.TABLE_CONTACTS
				+ ";";
		db.execSQL(deleteQuery);
		db.close();
	}
}
