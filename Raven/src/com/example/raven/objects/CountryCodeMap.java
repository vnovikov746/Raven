package com.example.raven.objects;

import java.util.HashMap;
import java.util.Map;

public class CountryCodeMap
{
	public static final Map<String, String> COUNTRIES = new HashMap<String, String>()
	{
		private static final long serialVersionUID = 1L;
		{
			put("il", "+972");
		}
	};
}
