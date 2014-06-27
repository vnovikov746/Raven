package com.example.raven.dict;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.raven.objects.ServiceHandler;


public class Yandex extends Translator {

	// api settings
	private static final String API_URL = "https://translate.yandex.net/api/v1.5/tr.json/";
	private static final String API_KEY = "trnsl.1.1.20140626T212730Z.083a74f0c0c1b811.bf25adf388a47a5adaf777b21f1da3efc9f6353a";
	
	
    // JSON Node names
    private static final String TAG_CODE = "code";
    private static final String TAG_LANG = "lang";
    private static final String TAG_TEXT = "text";
	

	@Override
	public String translate(String lang, String text) {
		String[] one_text = {text};
		return translate(lang, one_text, "plain", "1", ServiceHandler.POST)[0];
	}

	@Override
	public String[] translate(String lang, String[] text) {
		return translate(lang, text, "plain", "1", ServiceHandler.POST);
	}

	/**
	 * Translate
	 * @lang - "from-to"
	 * @text - ....
	 * 
	 * api: http://api.yandex.com/translate/doc/dg/reference/translate.xml
	 * 
	 * ERR_OK	200	Operation completed successfully.
	 * ERR_KEY_INVALID	401	Invalid API key.
	 * ERR_KEY_BLOCKED	402	This API key has been blocked.
	 * ERR_DAILY_REQ_LIMIT_EXCEEDED	403	You have reached the daily limit for requests (including calls of the detect method).
	 * ERR_DAILY_CHAR_LIMIT_EXCEEDED	404	You have reached the daily limit for the volume of translated text (including calls of the detect method).
	 * ERR_TEXT_TOO_LONG	413	The text size exceeds the maximum.
	 * ERR_UNPROCESSABLE_TEXT	422	The text could not be translated.
	 * ERR_LANG_NOT_SUPPORTED	501	The specified translation direction is not supported.
	 * */
	public String[] translate(String lang, String[] text, String format, String options, int method) {
		
		String[] translatedText = new String[text.length];
		
		//build the api call url
		String url = API_URL + "translate"
				+ "?key=" + API_KEY
				+ "&lang=" + lang
				+ "&format=" + format
				+ "&options=" + options;
		for (String phrase : text)
			url += "&text=" + phrase;
		
		// Making a request to url and getting response
		ServiceHandler sh = new ServiceHandler();
		String jsonStr = sh.makeServiceCall(url, method);
		Log.d("Response: ", "> " + jsonStr);

        // Pares json response
        if (jsonStr != null) {
        	try {
				JSONObject jsonObj = new JSONObject(jsonStr);
				int r_code = jsonObj.getInt(TAG_CODE);
				if (r_code != 200) return null;
				
				String r_lang = jsonObj.getString(TAG_LANG);
				JSONArray r_text = jsonObj.getJSONArray(TAG_TEXT);
				
				for (int i=0; i<r_text.length(); i++)
				translatedText[i] = r_text.getString(i);
			
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}

		return translatedText;
	}
}
