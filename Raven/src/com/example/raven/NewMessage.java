package com.example.raven;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SimpleAdapter;
import db.RavenDAL;

public class NewMessage extends Activity
{
	private RavenDAL dal = new RavenDAL(this);
	private SimpleAdapter mAdapter;
	private AutoCompleteTextView mTxtPhoneNo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_message);
		
		mTxtPhoneNo = (AutoCompleteTextView) findViewById(R.id.mmWhoNo);
		mTxtPhoneNo.setOnItemClickListener(new OnItemClickListener()
		{
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> av, View arg1, int index,
					long arg3)
			{
				Map<String, String> map = (Map<String, String>) av
						.getItemAtPosition(index);
				String number = map.get("Phone");
				mTxtPhoneNo.setText("" + number);
			}
		});
		
		mAdapter = new SimpleAdapter(this, HistoryActivity.mPeopleList,
				R.layout.custcontview,
				new String[] { "Name", "Phone", "Type" }, new int[] {
						R.id.ccontName, R.id.ccontNo, R.id.ccontType });
		
		mTxtPhoneNo.setAdapter(mAdapter);
	}
	
	public void onContactClick(View v)
	{
		String[] contactTokens = mTxtPhoneNo.getText().toString().split("=");
		String phoneNum = contactTokens[contactTokens.length - 1];
		mTxtPhoneNo.setText(phoneNum);
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
		EditText phoneTxt = (EditText) findViewById(R.id.mmWhoNo);
		String phoneNum = phoneTxt.getText().toString().trim();
		MultiAutoCompleteTextView smsTxt = (MultiAutoCompleteTextView) findViewById(R.id.SmsTxt);
		String messageTxt = smsTxt.getText().toString().trim();
		dal.addMessage(messageTxt, null, phoneNum, 0, 1, 1);
	}
	
	public void onContactsClick(View v)
	{
		Intent intent = new Intent(this, Contacts.class);
		startActivity(intent);
	}
}
