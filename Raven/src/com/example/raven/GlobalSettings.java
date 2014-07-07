package com.example.raven;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.raven.objects.AppPreferences;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Spinner;

public class GlobalSettings extends Activity
{
	
	private AppPreferences _appPrefs;
	private Spinner translateTo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		_appPrefs = new AppPreferences(GlobalSettings.this);
		
		//init first settings
		if (!_appPrefs.getBoolean(_appPrefs.FIRST_LAUNCH)) {
			_appPrefs.setBoolean(_appPrefs.FIRST_LAUNCH, true);
			_appPrefs.setString(_appPrefs.TRNASLATE_TO, "he");
		}
		
		Map<String, String> list =  getLangs();
		translateTo = (Spinner) findViewById(R.id.translateTo);
		
		
		setContentView(R.layout.activity_global_settings);
		

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.global_settings, menu);
		return true;
	}
	
	private Map<String, String> getLangs() {
		
		String langsJSON = "{\"ar\":\"Arabic\",\"az\":\"Azerbaijani\",\"be\":\"Belarusian\",\"bg\":\"Bulgarian\",\"bs\":\"Bosnian\",\"ca\":\"Catalan\",\"cs\":\"Czech\",\"da\":\"Danish\",\"de\":\"German\",\"el\":\"Greek\",\"en\":\"English\",\"es\":\"Spanish\",\"et\":\"Estonian\",\"fi\":\"Finnish\",\"fr\":\"French\",\"he\":\"Hebrew\",\"hr\":\"Croatian\",\"hu\":\"Hungarian\",\"hy\":\"Armenian\",\"id\":\"Indonesian\",\"is\":\"Icelandic\",\"it\":\"Italian\",\"ka\":\"Georgian\",\"lt\":\"Lithuanian\",\"lv\":\"Latvian\",\"mk\":\"Macedonian\",\"ms\":\"Malay\",\"mt\":\"Maltese\",\"nl\":\"Dutch\",\"no\":\"Norwegian\",\"pl\":\"Polish\",\"pt\":\"Portuguese\",\"ro\":\"Romanian\",\"ru\":\"Russian\",\"sk\":\"Slovak\",\"sl\":\"Slovenian\",\"sq\":\"Albanian\",\"sr\":\"Serbian\",\"sv\":\"Swedish\",\"tr\":\"Turkish\",\"uk\":\"Ukrainian\",\"vi\":\"Vietnamese\"}}";
		JSONObject jsonObj = null;
		Map<String, String> langsList = new HashMap<String, String>();
		
		try {
			jsonObj = new JSONObject(langsJSON);
			Iterator keys = jsonObj.keys();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				String value = (String) jsonObj.get(key);
				langsList.put(key, value);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return langsList;
	}
	
}
