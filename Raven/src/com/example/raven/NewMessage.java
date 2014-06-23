package com.example.raven;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import db.RavenDAL;

public class NewMessage extends Activity
{
	private RavenDAL dal = new RavenDAL(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_message);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_message, menu);
		return true;
	}
	
	public void onSendClick(View v)
	{
		EditText phoneTxt = (EditText) findViewById(R.id.PhoneNum);
		String phoneNum = phoneTxt.getText().toString();
		MultiAutoCompleteTextView smsTxt = (MultiAutoCompleteTextView) findViewById(R.id.SmsTxt);
		String messageTxt = smsTxt.getText().toString();
		dal.addMessage(messageTxt, null, 1, 0, 1, 1);
	}
}
