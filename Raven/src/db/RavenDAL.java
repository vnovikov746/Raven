package db;

import java.util.LinkedList;

import Objects.Message;
import android.content.Context;

public class RavenDAL
{
	RavenDB db;
	
	public RavenDAL(Context context)
	{
		db = new RavenDB(context);
	}
	
	/*
	 * Add Contact to contacts table
	 */
	public void addContact(String name, String surName, String phoneNum,
			String language, int transtale, String time)
	{
		db.addContact(name, surName, phoneNum, language, transtale, time);
	}
	
	/*
	 * Add message to messages table
	 */
	public void addMessage(String txt, String transTxt, int contact,
			int received, int read, int sent)
	{
		db.addMessage(txt, transTxt, contact, received, read, sent);
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
	public Message getLastMessage(int contactId)
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
}
