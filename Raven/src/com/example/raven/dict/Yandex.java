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
			translatedText[0] = "Error";
		}

		return translatedText;
	}
	
	
	
//	error:
//	{"code":401,"message":"API key is invalid"}
	
	
//	ERR_KEY_INVALID	401	Invalid API key.
//	ERR_KEY_BLOCKED	402	This API key has been blocked.
//	https://translate.yandex.net/api/v1.5/tr.json/getLangs?key=APIkey&ui=en
//	Langs getLangs(string key, string ui);
//	{"dirs":["az-ru","be-bg","be-cs","be-de","be-en","be-es","be-fr","be-it","be-pl","be-ro","be-ru","be-sr","be-tr","bg-be","bg-ru","bg-uk","ca-en","ca-ru","cs-be","cs-en","cs-ru","cs-uk","da-en","da-ru","de-be","de-en","de-es","de-fr","de-it","de-ru","de-tr","de-uk","el-en","el-ru","en-be","en-ca","en-cs","en-da","en-de","en-el","en-es","en-et","en-fi","en-fr","en-hu","en-it","en-lt","en-lv","en-mk","en-nl","en-no","en-pt","en-ru","en-sk","en-sl","en-sq","en-sv","en-tr","en-uk","es-be","es-de","es-en","es-ru","es-uk","et-en","et-ru","fi-en","fi-ru","fr-be","fr-de","fr-en","fr-ru","fr-uk","hr-ru","hu-en","hu-ru","hy-ru","it-be","it-de","it-en","it-ru","it-uk","lt-en","lt-ru","lv-en","lv-ru","mk-en","mk-ru","nl-en","nl-ru","no-en","no-ru","pl-be","pl-ru","pl-uk","pt-en","pt-ru","ro-be","ro-ru","ro-uk","ru-az","ru-be","ru-bg","ru-ca","ru-cs","ru-da","ru-de","ru-el","ru-en","ru-es","ru-et","ru-fi","ru-fr","ru-hr","ru-hu","ru-hy","ru-it","ru-lt","ru-lv","ru-mk","ru-nl","ru-no","ru-pl","ru-pt","ru-ro","ru-sk","ru-sl","ru-sq","ru-sr","ru-sv","ru-tr","ru-uk","sk-en","sk-ru","sl-en","sl-ru","sq-en","sq-ru","sr-be","sr-ru","sr-uk","sv-en","sv-ru","tr-be","tr-de","tr-en","tr-ru","tr-uk","uk-bg","uk-cs","uk-de","uk-en","uk-es","uk-fr","uk-it","uk-pl","uk-ro","uk-ru","uk-sr","uk-tr"],"langs":{"ar":"Arabic","az":"Azerbaijani","be":"Belarusian","bg":"Bulgarian","bs":"Bosnian","ca":"Catalan","cs":"Czech","da":"Danish","de":"German","el":"Greek","en":"English","es":"Spanish","et":"Estonian","fi":"Finnish","fr":"French","he":"Hebrew","hr":"Croatian","hu":"Hungarian","hy":"Armenian","id":"Indonesian","is":"Icelandic","it":"Italian","ka":"Georgian","lt":"Lithuanian","lv":"Latvian","mk":"Macedonian","ms":"Malay","mt":"Maltese","nl":"Dutch","no":"Norwegian","pl":"Polish","pt":"Portuguese","ro":"Romanian","ru":"Russian","sk":"Slovak","sl":"Slovenian","sq":"Albanian","sr":"Serbian","sv":"Swedish","tr":"Turkish","uk":"Ukrainian","vi":"Vietnamese"}}
	
	
	
	
	
	
	
	
	
//	ERR_OK	200	Operation completed successfully.
//	ERR_KEY_INVALID	401	Invalid API key.
//	ERR_KEY_BLOCKED	402	This API key has been blocked.
//	ERR_DAILY_REQ_LIMIT_EXCEEDED	403	You have reached the daily limit for requests (including calls of the translate method).
//	ERR_DAILY_CHAR_LIMIT_EXCEEDED	404	You have reached the daily limit for the volume of translated text (including calls of the translate method).
//	https://translate.yandex.net/api/v1.5/tr.json/detect?key=APIkey&text=Hello+world
//	DetectedLang detect(string key, string text[], string format);
	
//	Optional
//	format	string	Text format.
//	Possible values:
//	plain - Text without markup (default value).
//	html - Text in HTML format.
	
//	{"code":200,"lang":"en"}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

