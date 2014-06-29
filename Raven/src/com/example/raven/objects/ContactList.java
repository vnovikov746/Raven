package com.example.raven.objects;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.widget.Toast;

import com.example.raven.db.Constants;
import com.example.raven.db.RavenDAL;

public class ContactList
{
	public static ArrayList<Map<String, String>> mPeopleList;
	private static RavenDAL dal;
		
	public static void updateList(Context context)
	{
		dal = new RavenDAL(context);
		int update = dal.getFlagValue(Constants.COLUMN_FLAG_UPDATE_CONTACTS);
		if(update == Constants.DONT_UPDATE_CONTACTS)
		{
			return;
		}
		else
		{
			while(update != Constants.UPDATE_CONTACTS)
			{
				update = dal.getFlagValue(Constants.COLUMN_FLAG_UPDATE_CONTACTS);
			}
			mPeopleList = dal.getAllContacts();
			Toast.makeText(context, "Contact List Updated",
			Toast.LENGTH_SHORT).show();
			dal.updateFlag(Constants.COLUMN_FLAG_UPDATE_CONTACTS,
			Constants.DONT_UPDATE_CONTACTS);			
		}
	}
}
