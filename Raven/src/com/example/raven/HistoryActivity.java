package com.example.raven;

import java.util.LinkedList;

import Objects.Message;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import db.RavenDAL;

public class HistoryActivity extends Activity
{
	private RavenDAL dal = new RavenDAL(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		
		LinkedList<Message> messages = dal.getAllLastMessages();
		
		TableLayout historyTable = (TableLayout)
		findViewById(R.id.historyTable);
		historyTable.setStretchAllColumns(true);
		historyTable.bringToFront();
		
		historyTable.removeAllViews();
		
		TableRow tr = new TableRow(this);
		historyTable.addView(tr);
		
		//////////////////////
		messages.add(new Message("a a", "Hello", "12:45", 0, "0545424151"));
		messages.add(new Message("a a", "Hi", "12:46", 1, "0545424151"));
		messages.add(new Message("a a", "Helllllllo", "12:47", 0, "0545424151"));
		messages.add(new Message("a a", "Hiiiii", "12:48", 1, "0545424151"));
		messages.add(new Message("a a", "Hahahaha", "12:49", 0, "0545424151"));
		//////////////////////
		
		for(int i = 0; i < messages.size(); i++)
		{
			tr = new TableRow(this);
			TextView tv1 = new TextView(this);
			tv1.setText("" + messages.get(i).getContactPhoneNum() + " "
			+ messages.get(i).getMessageTime() + " "
			+ messages.get(i).getMessageTxt());
			
		 	tr.addView(tv1);
			
			historyTable.addView(tr);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history, menu);
		return true;
	}
	
	public void onSendNew(View v)
	{
		Intent intent = new Intent(this, NewMessage.class);
		startActivity(intent);
	}
}
