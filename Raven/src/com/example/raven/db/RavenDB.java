package com.example.raven.db;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

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
				+ Constants.COLUMN_CONTACT_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ Constants.COLUMN_CONTACT_PHONE_NUM + " TEXT_TYPE, "
				+ Constants.COLUMN_CONTACT_LANGUAGE + " TEXT_TYPE, "
				+ Constants.COLUMN_CONTACT_TIME + " TEXT_TYPE, "
				+ Constants.COLUMN_CONTACT_TRANSLATE + " INTEGER);");
		
		// create messages table
		db.execSQL("CREATE TABLE " + Constants.TABLE_MESSAGES
				+ "("
				+ Constants.COLUMN_MESSAGE_ID
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
		db.execSQL("CREATE TABLE " + Constants.TABLE_COUNTRIES + "("
				+ Constants._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ Constants.COLUMN_COUNTRY_NAME + " TEXT_TYPE, "
				+ Constants.COLUMN_COUNTRY_LANGUAGE + " TEXT_TYPE);");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// Drop older tables if existed
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_CONTACTS);
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_MESSAGES);
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_COUNTRIES);
		
		// Create tables again
		onCreate(db);
	}
	
	/*
	 * Add Contact to contacts table
	 */
	public void addContact(String phoneNum, String language, int transtale,
			String time)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(Constants.COLUMN_CONTACT_PHONE_NUM, phoneNum);
		values.put(Constants.COLUMN_CONTACT_LANGUAGE, language);
		values.put(Constants.COLUMN_CONTACT_TRANSLATE, transtale);
		values.put(Constants.COLUMN_CONTACT_TIME, time);
		
		db.insert(Constants.TABLE_CONTACTS, null, values);
		db.close();
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
	
	/*
	 * Add country/language to countries table
	 */
	public void addCountry(String name, String language)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(Constants.COLUMN_COUNTRY_NAME, name);
		values.put(Constants.COLUMN_COUNTRY_LANGUAGE, language);
		
		db.insert(Constants.TABLE_COUNTRIES, null, values);
		db.close();
	}
	
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
		String selectQuery = "SELECT DISTINCT "
				+ Constants.COLUMN_MESSAGE_TO_CONTACT + " FROM "
				+ Constants.TABLE_MESSAGES + ";";
		
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.moveToFirst())
		{
			do
			{
				messages.add(getLastMessage(c.getString(0)));
			}
			while(c.moveToNext());
		}
		c.close();
		db.close();
		return messages;
	}
}
