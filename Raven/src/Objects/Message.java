package Objects;

/*
 * This class represents a message with the contact name with whom the conversation was, the text and the time it was sent
 */
public class Message
{
	private int received;
	private String contactName;
	private String contactPhoneNum;
	private String messageTxt;
	private String messageTime;
	
	public Message(String contactName, String messageTxt, String messageTime,
			int received, String contactPhoneNum)
	{
		this.contactName = contactName;
		this.messageTxt = messageTxt;
		this.messageTime = messageTime;
		this.received = received;
		this.contactPhoneNum = contactPhoneNum;
	}
	
	public String getContactName()
	{
		return contactName;
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
