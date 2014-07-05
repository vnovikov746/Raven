package com.example.raven;

import com.example.raven.objects.AppPreferences;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class GlobalSettings extends Activity
{
	
	private AppPreferences _appPrefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		_appPrefs = new AppPreferences(GlobalSettings.this);
		setContentView(R.layout.activity_global_settings);
		
		//init first settings
		if (!_appPrefs.getBoolean(_appPrefs.FIRST_LAUNCH)) {
			_appPrefs.setBoolean(_appPrefs.FIRST_LAUNCH, true);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.global_settings, menu);
		return true;
	}
	
}
