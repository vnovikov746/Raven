package com.example.raven.db;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import android.content.Context;

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
	public void addCountry(String name, String language)
	{
		db.addCountry(name, language);
	}
	
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
	 * Get all Contacts
	 */
	public ArrayList<Map<String, String>> getAllContacts()
	{
		return db.getAllContacts();
	}
	
	// /*
	// * Add flag
	// */
	// public void addFlag(String flagKey, int flagValue)
	// {
	// db.addFlag(flagKey, flagValue);
	// }
	//
	// /*
	// * Update flag
	// */
	// public void updateFlag(String flagKey, int flagValue)
	// {
	// db.updateFlag(flagKey, flagValue);
	// }
	//
	// /*
	// * get falg
	// */
	// public int getFlagValue(String flagKey)
	// {
	// return db.getFlagValue(flagKey);
	// }
}
