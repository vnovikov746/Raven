package com.example.raven.Objects;

/*
 * This class represents a message with the contact name with whom the conversation was, the text and the time it was sent
 */
public class Message
{
	private int received;
	private String contactPhoneNum;
	private String messageTxt;
	private String messageTime;
	
	public Message(String messageTxt, String messageTime, int received,
			String contactPhoneNum)
	{
		this.messageTxt = messageTxt;
		this.messageTime = messageTime;
		this.received = received;
		this.contactPhoneNum = contactPhoneNum;
	}
	
	public String getMessageTxt()
	{
		return messageTxt;
	}
	
	public String getMessageTime()
	{
		return messageTime;
	}
	
	public int getReceived()
	{
		return received;
	}
	
	public String getContactPhoneNum()
	{
		return contactPhoneNum;
	}
}
