package com.luciocossio.classclient.http;

import java.util.Map;
import java.util.Set;

public class HTTPUtils {

	public String addQueryParametersToUrl(String url, Map<String, String> queryParameters)
	{		
		StringBuilder newUrl = new StringBuilder();
		newUrl.append(url);

		if (queryParameters != null && queryParameters.size() > 0)
		{
			newUrl.append("?");
			Set<String> keys = queryParameters.keySet();
			for(String key : keys)
			{
				String value = queryParameters.get(key);
				newUrl.append(key);
				newUrl.append("=");
				newUrl.append(value);				
				newUrl.append("&");
			}
			newUrl.deleteCharAt(newUrl.length()-1);
		}
		return newUrl.toString();		
	}
	
}
