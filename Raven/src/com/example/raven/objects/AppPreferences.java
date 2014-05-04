package com.example.raven.objects;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {
	
	//keys
    public static final String FIRST_LAUNCH = "first_launch";
    public static final String TRANSLATE_IN = "translate_in";
    public static final String TRANSLATE_OUT = "translate_out";

    private static final String APP_SHARED_PREFS = AppPreferences.class.getSimpleName(); //  Name of the file -.xml //"com.example.app"
    private SharedPreferences _sharedPrefs;
    private Editor _prefsEditor;

    public AppPreferences(Context context) {
        this._sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this._prefsEditor = _sharedPrefs.edit();
    }

    //boolean
    public boolean getBoolean(String key) {
        return _sharedPrefs.getBoolean(key, false);
    }
    public void setBoolean(String key, boolean value) {
        _prefsEditor.putBoolean(key, value);
        _prefsEditor.commit();
    }

    //int
    public int getInt(String key) {
        return _sharedPrefs.getInt(key, 0);
    }
    public void setInt(String key, int value) {
        _prefsEditor.putInt(key, value);
        _prefsEditor.commit();
    }

    //String
    public String getString(String key) {
        return _sharedPrefs.getString(key, "");
    }
    public void setString(String key, String value) {
        _prefsEditor.putString(key, value);
        _prefsEditor.commit();
    }

    
}
