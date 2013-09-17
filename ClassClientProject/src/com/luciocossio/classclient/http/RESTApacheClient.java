package com.luciocossio.classclient.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class RESTApacheClient implements RESTJsonClient {
	
	HttpClient client;
	HTTPUtils httpUtils;
	
	public RESTApacheClient()
	{
		client = new DefaultHttpClient();
		httpUtils = new HTTPUtils();
	}	

	@Override
	public RESTJsonResponse doGet(String location,
			Map<String, String> queryParameters) {
		
		String newUrl = httpUtils.addQueryParametersToUrl(location, queryParameters);
		
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

		httpResponse = client.execute(httpGet);

		InputStream stream = httpResponse.getEntity().getContent();

		return stream;
	}

	@Override
	public RESTJsonResponse doPost(String location, String jsonContent) {
		HttpPost httpPost = new HttpPost(location);
		httpPost.setHeader("Content-Type", "application/json");
		StringEntity jsonEntity;
		try {
			jsonEntity = new StringEntity(jsonContent);			
		} catch (UnsupportedEncodingException e1) {
			return new RESTJsonResponse(400,"{\"message\": \"Bad json entity\"}"); 
		}
		httpPost.setEntity(jsonEntity);
		
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
		} catch (Exception e) {
			return new RESTJsonResponse(500,"{\"message\": \"" + e.getMessage() + "\"}"); 
		}
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
		HttpDelete httpDelete = new HttpDelete(location);
				
		HttpResponse httpResponse;
		RESTJsonResponse jsonResponse;
		try {
			httpResponse = client.execute(httpDelete);
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
