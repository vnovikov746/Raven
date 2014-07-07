package com.example.raven;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.raven.objects.AppPreferences;

public class GlobalSettings extends Activity
{
	
	private AppPreferences _appPrefs;
	private Spinner translateTo;
	Map<String, String> langsMap, langsMapReverse;
	
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
		setContentView(R.layout.activity_global_settings);
		
		
		langsMap = new HashMap<String, String>();
		langsMapReverse = new HashMap<String, String>();
		getLangs(langsMap, langsMapReverse);
		
		
		translateTo = (Spinner) findViewById(R.id.translateTo);
		List<String> langArr = new ArrayList<String>();
		langArr.addAll(langsMap.values());
		Collections.sort(langArr);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,langArr);
        int spinnerPosition = dataAdapter.getPosition( langsMap.get(_appPrefs.getString(AppPreferences.TRNASLATE_TO)) );
		dataAdapter.setDropDownViewResource
		        (android.R.layout.simple_spinner_dropdown_item);
		         
		translateTo.setAdapter(dataAdapter);
		translateTo.setSelection(spinnerPosition);
		
		translateTo.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				_appPrefs.setString(_appPrefs.TRNASLATE_TO, langsMapReverse.get(translateTo.getSelectedItem().toString()));
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}

		});
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.global_settings, menu);
		return true;
	}
	

	
	private void getLangs(Map<String, String> list, Map<String, String> listReverse ) {
		
		String langsJSON = "{\"ar\":\"Arabic\",\"az\":\"Azerbaijani\",\"be\":\"Belarusian\",\"bg\":\"Bulgarian\",\"bs\":\"Bosnian\",\"ca\":\"Catalan\",\"cs\":\"Czech\",\"da\":\"Danish\",\"de\":\"German\",\"el\":\"Greek\",\"en\":\"English\",\"es\":\"Spanish\",\"et\":\"Estonian\",\"fi\":\"Finnish\",\"fr\":\"French\",\"he\":\"Hebrew\",\"hr\":\"Croatian\",\"hu\":\"Hungarian\",\"hy\":\"Armenian\",\"id\":\"Indonesian\",\"is\":\"Icelandic\",\"it\":\"Italian\",\"ka\":\"Georgian\",\"lt\":\"Lithuanian\",\"lv\":\"Latvian\",\"mk\":\"Macedonian\",\"ms\":\"Malay\",\"mt\":\"Maltese\",\"nl\":\"Dutch\",\"no\":\"Norwegian\",\"pl\":\"Polish\",\"pt\":\"Portuguese\",\"ro\":\"Romanian\",\"ru\":\"Russian\",\"sk\":\"Slovak\",\"sl\":\"Slovenian\",\"sq\":\"Albanian\",\"sr\":\"Serbian\",\"sv\":\"Swedish\",\"tr\":\"Turkish\",\"uk\":\"Ukrainian\",\"vi\":\"Vietnamese\"}}";
		JSONObject jsonObj = null;
		
		try {
			jsonObj = new JSONObject(langsJSON);
			Iterator keys = jsonObj.keys();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				String value = (String) jsonObj.get(key);
				list.put(key, value);
				listReverse.put(value, key);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
