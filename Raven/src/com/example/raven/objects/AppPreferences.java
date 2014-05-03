package com.example.raven.objects;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {
    public static final String KEY_PREFS_SMS_BODY = "sms_body";
    private static final String APP_SHARED_PREFS = AppPreferences.class.getSimpleName(); //  Name of the file -.xml //"com.example.app"
    private SharedPreferences _sharedPrefs;
    private Editor _prefsEditor;

    public AppPreferences(Context context) {
        this._sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this._prefsEditor = _sharedPrefs.edit();
    }

    public String getSmsBody() {
//    	int selectionStart = prefs.getInt("selection-start", -1);
        return _sharedPrefs.getString(KEY_PREFS_SMS_BODY, "");
    }

    public void saveSmsBody(String text) {
//    	editor.putInt("selection-end", mSaved.getSelectionEnd());
        _prefsEditor.putString(KEY_PREFS_SMS_BODY, text);
        _prefsEditor.commit();
    }
}
