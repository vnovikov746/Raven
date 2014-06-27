package com.example.raven.objects;

import com.example.raven.dict.Bing;
import com.example.raven.dict.Google;
import com.example.raven.dict.Translator;
import com.example.raven.dict.Yandex;

//TranslatorFactory
public class Raven {
	
	//Supported services list
	public static final int YANDEX = 0;
	public static final int GOOGLE = 1;
	public static final int BING = 2;

	public static Translator SetService(int service) {
		switch (service) {
		case YANDEX:	return new Yandex();
		case BING:		return new Bing();
		case GOOGLE:	return new Google();
		default:		return new Yandex();
		}
	}
}
