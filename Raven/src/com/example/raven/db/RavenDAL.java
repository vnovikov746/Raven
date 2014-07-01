package com.example.raven.db;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;

import com.example.raven.objects.Message;

public class RavenDAL
{
	private RavenDB db;
	
	public RavenDAL(Context context)
	{
		db = new RavenDB(context);
	}
	
	/*
	 * Add Contact to contacts table
	 */
	public void addContact(String name, String phoneNum, String type,
			String language, int transtale)
	{
		db.addContact(name, phoneNum, type, language, transtale);
	}
	
	/*
	 * Add all contacts to contacts table
	 */
	public void addAllConacts(ArrayList<Map<String, String>> contacts)
	{
		for(Map<String, String> map : contacts)
		{
			db.addContact(map.get("Name"), map.get("Phone"), map.get("Type"),
					null, 0);
		}
	}
	
	/*
	 * check if contact exist in the db
	 */
	public boolean isContactExist(String contactPhone)
	{
		return db.isContactExist(contactPhone);
	}
	
	/*
	 * get all the messages related to contact
	 */
	public ArrayList<Message> getChatWithContact(String contactPhone)
	{
		return db.getChatWithContact(contactPhone);
	}
	
	/*
	 * get cursor for all the messages related to contact
	 */
	public Cursor getChatWithContactCursor(String contactPhone)
	{
		return db.getChatWithContactCursor(contactPhone);
	}
	
	/*
	 * Delete all Contacts
	 */
	public void deleteAllContacts()
	{
		db.deleteAllContacts();
	}
	
	/*
	 * Add message to messages table
	 */
	public void addMessage(String txt, String transTxt, String contactPhone,
			int received, int read, int sent)
	{
		db.addMessage(txt, transTxt, contactPhone, received, read, sent);
	}
	
	/*
	 * Add country/language to countries table
	 */
	// public void addCountry(String name, String language)
	// {
	// db.addCountry(name, language);
	// }
	
	/*
	 * Get last message from a contact
	 */
	public Message getLastMessage(String contactId)
	{
		return db.getLastMessage(contactId);
	}
	
	/*
	 * Get last messages from all contacts
	 */
	public LinkedList<Message> getAllLastMessages()
	{
		return db.getAllLastMessages();
	}
	
	/*
	 * Get Cursor fot last messages from all contacts
	 */
	public Cursor getAllLastMessagesCursor()
	{
		return db.getAllLastMessagesCursor();
	}
	
	/*
	 * Get all Contacts
	 */
	public ArrayList<Map<String, String>> getAllContacts()
	{
		return db.getAllContacts();
	}
	
	/*
	 * Get all contacts cursor
	 */
	public Cursor getAllContactsCursor()
	{
		return db.getAllContactsCursor();
	}
	
	/*
	 * Add flag
	 */
	public void addFlag(String flagKey, int flagValue)
	{
		if(db.getFlagValue(flagKey) == -1)
		{
			db.addFlag(flagKey, flagValue);
		}
	}
	
	/*
	 * Get contact name
	 */
	public String getContactName(String phone)
	{
		return db.getContactName(phone);
	}
	
	/*
	 * Update flag
	 */
	public void updateFlag(String flagKey, int flagValue)
	{
		db.updateFlag(flagKey, flagValue);
	}
	
	/*
	 * get falg
	 */
	public int getFlagValue(String flagKey)
	{
		return db.getFlagValue(flagKey);
	}
}
