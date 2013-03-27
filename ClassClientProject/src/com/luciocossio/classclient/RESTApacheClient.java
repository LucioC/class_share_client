package com.luciocossio.classclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
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
	public InputStream doGetFile(String location) throws ClientProtocolException, IOException {
		
		HttpGet httpGet = new HttpGet(location);
		HttpResponse httpResponse;
		RESTJsonResponse jsonResponse;
		
			httpResponse = client.execute(httpGet);
			
			InputStream stream = httpResponse.getEntity().getContent();
						
			return stream;
	}

	@Override
	public RESTJsonResponse doPost(String location, String jsonContent) {
		// TODO Auto-generated method stub
		
		
		return null;
	}
	
	@Override
	public RESTJsonResponse doPostFile(String location, File file, String fileMimetype) {
		
		FileEntity fileEntity = new FileEntity(file, fileMimetype);
		
		HttpPost httpPost = new HttpPost(location);
		httpPost.setHeader("Content-Type", fileMimetype);
		httpPost.setEntity(fileEntity);
		
		HttpResponse httpResponse;
		RESTJsonResponse jsonResponse;
		try {
			httpResponse = client.execute(httpPost);
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
	public RESTJsonResponse doPutFile(String location, File file, String fileMimetype) {
		
		FileEntity fileEntity = new FileEntity(file, fileMimetype);
		
		HttpPut httpPost = new HttpPut(location);
		httpPost.setHeader("Content-Type", fileMimetype);
		httpPost.setEntity(fileEntity);
		
		HttpResponse httpResponse;
		RESTJsonResponse jsonResponse;
		try {
			httpResponse = client.execute(httpPost);
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
	public RESTJsonResponse doDelete(String location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RESTJsonResponse doPut(String location, String jsonContent) {
		
		HttpPut httpPut = new HttpPut(location);
		httpPut.setHeader("Content-Type", "application/json");
		StringEntity jsonEntity;
		try {
			jsonEntity = new StringEntity(jsonContent);			
		} catch (UnsupportedEncodingException e1) {
			return new RESTJsonResponse(400,"{\"message\": \"Bad json entity\"}"); 
		}
		httpPut.setEntity(jsonEntity);
		
		HttpResponse httpResponse;
		RESTJsonResponse jsonResponse;
		try {
			httpResponse = client.execute(httpPut);
			jsonResponse = new RESTJsonResponse(httpResponse.getStatusLine().getStatusCode(), 
					EntityUtils.toString(httpResponse.getEntity() ));
						
			return jsonResponse;
		} catch (ClientProtocolException e) {
			return new RESTJsonResponse(500,"{\"message\": \"" + e.getMessage() + "\"}"); 
		} catch (IOException e) {
			return new RESTJsonResponse(500,"{\"message\": \"" + e.getMessage() + "\"}"); 
		}
	}

}
