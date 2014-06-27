package com.example.raven.dict;


//Factory method pattern
public abstract class Translator {

	
	public abstract String translate(String lang, String text);
	public abstract String[] translate(String lang, String[] text);
	
	@Override
	public String toString(){
		return "Translator";
	}
	
}
