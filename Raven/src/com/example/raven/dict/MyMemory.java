package com.example.raven.dict;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.raven.services.ServiceHandler;

public class MyMemory extends Translator 
{

	// api settings
	//http://mymemory.translated.net/doc/spec.php
	private static final String API_URL = "http://api.mymemory.translated.net/get?";

    // JSON Node names
    private static final String TAG_RESPONSE_DATA = "responseData";
    private static final String TAG_TRANSLATED_TEXT = "translatedText";
    private static final String TAG_RESPONSE_STATUS = "responseStatus";
    private static final String TAG_MATCHES = "matches";
    private static final String TAG_TRANSLATION = "translation";
	

	/*
	 * q	The sentence you want to translate. Use UTF-8. Max 500 bytes	Mandatory	Hello World!
	 * langpair	Source and language pair, separated by the | symbol. Use ISO standard names or RFC3066	Mandatory	en|it of	Output format	Optional	json (default), tmx, serialized php array 
	 * mt	Enables Machine Translation in results. You can turn it off if you want just human segments	Optional	1 (default), 0
	 * key	Authenticates the request; matches from your private TM are returned too. Get your key here or use the keygen API	Optional	
	 * onlyprivate	If your request is authenticated, returns only matches from your private TM	Optional	0 (default), 1
	 * ip	The IP of the end user generating the request. Recommended for CAT tools and high volume usage Originating IP is always overridden by X-Forwarded-For header, if the latter is set.	Optional	93.81.217.71
	 * de	A valid email where we can reach you in case of troubles.Recommended for CAT tools and high volume usage	Optional	user@yourdomain.com
	 * user	Authenticates the request; matches from your private TM are returned too.	Optional, but needs the key parameter. Kept for backward compatibility only: now the key parameter alone is sufficient	
	 * 
	 * http://api.mymemory.translated.net/get?q=%D7%A9%D7%9C%D7%95%D7%9D&langpair=he|en
	 */
	private String[] translate(String lang, String text, int method) 
	{

		String[] translatedText = {""};
		
		//build the api call url
		String url = "";
		try 
		{
			url = API_URL + "?"
					+ "q=" + URLEncoder.encode(text, "UTF-8")	//Max 500 bytes
					+ "&langpair=" + lang
					+ "&de=raventranslator@gmail.com";
		} 
		catch (UnsupportedEncodingException e1) 
		{
			e1.printStackTrace();
		}
		
		// Making a request to url and getting response
		ServiceHandler sh = new ServiceHandler();
		String jsonStr = sh.makeServiceCall(url, method);
		Log.d("Response: ", "> " + jsonStr);

        // Pares json response
        if (jsonStr != null) 
        {
        	try {
				JSONObject jsonObj = new JSONObject(jsonStr);
				int r_code = jsonObj.getInt(TAG_RESPONSE_STATUS);
				if (r_code != 200) return null;
				
//				String r_lang = jsonObj.getJSONObject(TAG_RESPONSE_DATA).getString(TAG_TRANSLATED_TEXT);
				
				JSONArray r_text = jsonObj.getJSONArray(TAG_MATCHES);
				translatedText = new String[r_text.length()];
				for (int i=0; i<r_text.length(); i++)
					translatedText[i] = r_text.getJSONObject(i).getString(TAG_TRANSLATION);
			
			} 
        	catch (JSONException e) 
        	{
				e.printStackTrace();
			}
		} 
        else 
        {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
			translatedText[0] = "Error";
		}
		return translatedText;		
	}

	
	@Override
	public String translate(String from, String to, String text) 
	{
		return translate(from + "|" + to, text, ServiceHandler.GET)[0];
	}

	@Override
	public String[] translate(String from, String to, String[] text) 
	{
		// TODO: implement this method
		return null;
	}

	@Override
	public Map<String, String> getLangs() 
	{
		// TODO: implement this method
		return null;
	}

	@Override
	public String detect(String text) 
	{
		// TODO: implement this method
		return null;
	}

	@Override
	public String detect(String[] text) 
	{
		// TODO: implement this method
		return null;
	}
}
