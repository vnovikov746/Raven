package com.example.raven.db;

import android.provider.BaseColumns;

public class Constants implements BaseColumns
{
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "RAVENDBFile.db";
	
	public static final String _ID = "_id";
	
	public static final String TABLE_CONTACTS = "CONTACTS";
	public static final String TABLE_MESSAGES = "MESSAGES";
	public static final String TABLE_COUNTRIES = "COUNTRIES";
	public static final String TABLE_FLAGS = "FLAGS";
	
	public static final String COLUMN_CONTACT_NAME = "CONTACT_NAME";
	public static final String COLUMN_CONTACT_TYPE = "CONTACT_TYPE";
	public static final String COLUMN_CONTACT_PHONE_NUM = "PHONE_NUM";
	public static final String COLUMN_CONTACT_LANGUAGE = "CONTACT_LANGUAGE";
	public static final String COLUMN_CONTACT_TRANSLATE = "CONTACT_TRANSLATE";
	
	public static final String COLUMN_MESSAGE_TO_CONTACT = "MESSAGE_TO_CONTACT";
	public static final String COLUMN_MESSAGE_RECEIVED_OR_SENT = "MESSAGE_RECEIVED_OR_SENT";
	public static final String COLUMN_MESSAGE_TXT = "MESSAGE_TXT";
	public static final String COLUMN_MESSAGE_TRANSTATED_TXT = "MESSAGE_TRANSTATED_TXT";
	public static final String COLUMN_MESSAGE_READ = "MESSAGE_READ";
	public static final String COLUMN_MESSAGE_SENT = "MESSAGE_SENT";
	public static final String COLUMN_MESSAGE_TIME = "MESSAGE_TIME";
	
	// public static final String COLUMN_COUNTRY_NAME = "COUNTRY_NAME";
	// public static final String COLUMN_COUNTRY_LANGUAGE = "COUNTRY_LANGUAGE";
	
	public static final String COLUMN_FLAG_KEY = "FLAG_KEY";
	public static final String COLUMN_FLAG_VALUE = "FLAG_VALUE";
	
	public static final String COLUMN_FLAG_SERVICE_INSTANCE = "SERVICE_INSTANCE";
	public static final String COLUMN_FLAG_UPDATE_CONTACTS = "UPDATE_CONTACTS";
	
	public static final int RECEIVED = 1;
	public static final int SENT_BY_ME = 0;
	public static final int READ = 1;
	public static final int NOT_READ = 0;
	public static final int SENT = 1;
	public static final int NOT_SENT = 0;
	public static final int UPDATE_CONTACTS = 1;
	public static final int DONT_UPDATE_CONTACTS = 0;
	public static final int CREATE_SERVICE_INSTANCE = 1;
	public static final int DONT_CREATE_SERVICE_INSTANCE = 0;
}
