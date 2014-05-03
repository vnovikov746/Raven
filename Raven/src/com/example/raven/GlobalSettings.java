package com.example.raven;

import com.example.raven.objects.AppPreferences;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class GlobalSettings extends Activity
{
	
	private AppPreferences _appPrefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_global_settings);
		
		
		_appPrefs = new AppPreferences(getApplicationContext());
		String someString = _appPrefs.getSmsBody();
		_appPrefs.saveSmsBody(someString);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.global_settings, menu);
		return true;
	}
	
}
