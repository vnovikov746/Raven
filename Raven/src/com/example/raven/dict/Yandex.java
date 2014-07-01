package com.example.raven.dict;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.raven.services.ServiceHandler;


public class Yandex extends Translator {

	// api settings
	private static final String API_URL = "https://translate.yandex.net/api/v1.5/tr.json/";
	private static final String API_KEY = "trnsl.1.1.20140626T212730Z.083a74f0c0c1b811.bf25adf388a47a5adaf777b21f1da3efc9f6353a";
	
	
    // JSON Node names
    private static final String TAG_CODE = "code";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_LANG = "lang";
    private static final String TAG_TEXT = "text";
    private static final String TAG_DIRS = "dirs";
    private static final String TAG_LANGS = "langs";


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
	private String[] translate(String lang, String[] text, String format, String options, int method) {
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
			translatedText[0] = "Error";
		}

		return translatedText;
	}


	@Override
	public String translate(String from, String to, String text) {
		String[] one_text = {text};
		return translate(from+"-"+to, one_text, "plain", "1", ServiceHandler.POST)[0];
	}

	@Override
	public String[] translate(String from, String to, String[] text) {
		return translate(from+"-"+to, text, "plain", "1", ServiceHandler.POST);
	}
	
	
	
	
	
	/**
	 * Translate
	 * @ui - 	en - in English
	 * 			ru - in Russian
	 * 			tr - in Turkish
	 * 			uk - in Ukrainian
	 * 
	 * api: http://api.yandex.com/translate/doc/dg/reference/getLangs.xml
	 * 
	 * ERR_KEY_INVALID	401	Invalid API key.
	 * ERR_KEY_BLOCKED	402	This API key has been blocked.
	 * */
	private Map<String, String> getLangs(String ui) {
		Map<String, String> langs = new HashMap<String, String>();
		
		//build the api call url
		String url = API_URL + "getLangs"
				+ "?key=" + API_KEY
				+ "&ui=" + ui;
		
		// Making a request to url and getting response
		ServiceHandler sh = new ServiceHandler();
		String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
		Log.d("Response: ", "> " + jsonStr);

        // Pares json response
        if (jsonStr != null) {
        	try {
				JSONObject jsonObj = new JSONObject(jsonStr);
				
				//get code error
				try {
					int r_code = jsonObj.getInt(TAG_CODE);
					return null;
				} catch (JSONException e) {}

				JSONArray r_dirs = jsonObj.getJSONArray(TAG_DIRS);
				String[] dirs = new String[r_dirs.length()];
				for (int i=0; i<r_dirs.length(); i++)
					dirs[i] = r_dirs.getString(i);

				JSONObject r_langs = jsonObj.getJSONObject(TAG_LANGS);
				Iterator keys = r_langs.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = (String) r_langs.get(key);
					langs.put(key, value);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}

		return langs;
	}


	@Override
	public Map<String, String> getLangs() {
		return getLangs("en");
	}
	
	
	
	
	
	
	
	/**
	 * detect
	 * @text - ....
	 * 
	 * api: http://api.yandex.com/translate/doc/dg/reference/detect.xml
	 * 
	 * ERR_OK	200	Operation completed successfully.
	 * ERR_KEY_INVALID	401	Invalid API key.
	 * ERR_KEY_BLOCKED	402	This API key has been blocked.
	 * ERR_DAILY_REQ_LIMIT_EXCEEDED	403	You have reached the daily limit for requests (including calls of the detect method).
	 * ERR_DAILY_CHAR_LIMIT_EXCEEDED	404	You have reached the daily limit for the volume of translated text (including calls of the detect method).
	 * */
	private String detect(String[] text, String format, int method) {
		String r_lang = "";
		
		//build the api call url
		String url = API_URL + "detect"
				+ "?key=" + API_KEY
				+ "&format=" + format;
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
				
				r_lang = jsonObj.getString(TAG_LANG);
			
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
			r_lang = "Error";
		}

		return r_lang;
	}

	@Override
	public String detect(String text) {
		String[] one_text = {text};
		return detect(one_text, "plain", ServiceHandler.POST);
	}
	
	@Override
	public String detect(String[] text) {
		return detect(text, "plain", ServiceHandler.POST);
	}
	
	
	
}

