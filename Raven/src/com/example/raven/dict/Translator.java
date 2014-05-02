package com.example.raven.dict;

import java.util.Map;


//Factory method pattern
public abstract class Translator {

	
	public abstract String translate(String lang, String text);
	public abstract String[] translate(String lang, String[] text);
	public abstract Map<String, String> getLangs();
	public abstract String detect(String text);
	public abstract String detect(String[] text);
	
	@Override
	public String toString(){
		return "Translator";
	}
	
}
