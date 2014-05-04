package com.example.raven;

import com.example.raven.objects.AppPreferences;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class ChatSettings extends Activity
{
	private AppPreferences _appPrefs;
	private CheckBox translateIn;
	private CheckBox translateOut;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		_appPrefs = new AppPreferences(getApplicationContext());
		setContentView(R.layout.activity_chat_settings);
		
		//locators
		translateIn = (CheckBox) findViewById(R.id.translateIn);
		translateOut = (CheckBox) findViewById(R.id.translateOut);
		
		//init value
		if (_appPrefs.getBoolean(_appPrefs.TRANSLATE_IN))
			translateIn.setChecked(true);
		if (_appPrefs.getBoolean(_appPrefs.TRANSLATE_OUT))
			translateOut.setChecked(true);
		
		//set listeners
		translateIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				_appPrefs.setBoolean(_appPrefs.TRANSLATE_IN, isChecked);
			}
		});
		translateOut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				_appPrefs.setBoolean(_appPrefs.TRANSLATE_OUT, isChecked);
			}
		});

	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_settings, menu);
		return true;
	}
	
}
