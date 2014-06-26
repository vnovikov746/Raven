package com.example.raven.objects;

/*
 * This class represents a message with the contact name with whom the conversation was, the text and the time it was sent
 */
public class Message
{
	private String messageTxt; // message on original language
	private String translatedTxt; // translated message (can be null)
	private String messageTime;
	private String contactPhoneNum;
	private int receivedOrSent; // in or out sms
	private int read; // 0 if not read 1 if already read
	private int sent; // 1 if the sms was successfully sent, else -- 0
	
	public Message(String messageTxt, String translatedTxt, String messageTime,
			String contactPhoneNum, int receivedOrSent, int read, int sent)
	{
		this.messageTxt = messageTxt;
		this.translatedTxt = translatedTxt;
		this.messageTime = messageTime;
		this.contactPhoneNum = contactPhoneNum;
		this.receivedOrSent = receivedOrSent;
		this.read = read;
		this.sent = sent;
	}
	
	public String getMessageTxt()
	{
		return messageTxt;
	}
	
	public String getTranslatedTxt()
	{
		return translatedTxt;
	}
	
	public void setTranslatedTxt(String trnslatedTxt)
	{
		this.translatedTxt = trnslatedTxt;
	}
	
	public String getMessageTime()
	{
		return messageTime;
	}
	
	public String getContactPhoneNum()
	{
		return contactPhoneNum;
	}
	
	public int getReceivedOrSent()
	{
		return receivedOrSent;
	}
	
	public int getRead()
	{
		return read;
	}
	
	public void setRead(int read)
	{
		this.read = read;
	}
	
	public int getSent()
	{
		return sent;
	}
	
	public void setSent(int sent)
	{
		this.sent = sent;
	}
}
