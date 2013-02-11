package com.luciocossio.classclient;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class RESTApacheClient implements RESTJsonClient {
	
	HttpClient client;
	
	public RESTApacheClient()
	{
		client = new DefaultHttpClient();
	}
	
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

	@Override
	public RESTJsonResponse doGet(String location,
			Map<String, String> queryParameters) {
		
		String newUrl = addQueryParametersToUrl(location, queryParameters);
		
		HttpGet httpGet = new HttpGet(newUrl);
		HttpResponse httpResponse;
		RESTJsonResponse jsonResponse;
		try {
			httpResponse = client.execute(httpGet);
			jsonResponse = new RESTJsonResponse(httpResponse.getStatusLine().getStatusCode(), 
					EntityUtils.toString(httpResponse.getEntity() ));
						
			return jsonResponse;
		} catch (ClientProtocolException e) {
			return new RESTJsonResponse(500,"{\"message\": \"" + e.getMessage() + "\"}"); 
		} catch (IOException e) {
			return new RESTJsonResponse(500,"{\"message\": \"" + e.getMessage() + "\"}"); 
		}
	}

	@Override
	public RESTJsonResponse doPost(String location, String jsonContent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RESTJsonResponse doDelete(String location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RESTJsonResponse doPut(String location) {
		// TODO Auto-generated method stub
		return null;
	}

}
