package com.luciocossio.classclient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.Gson;
import com.luciocossio.classclient.http.RESTJsonClient;
import com.luciocossio.classclient.http.RESTJsonResponse;

public class ClassClientHTTPCommon {

	public ClassClientHTTPCommon() {
		super();
	}	

	protected RESTJsonClient restClient;

	protected ResultMessage doGet(Map<String, String> queryParameters, String url) {
		RESTJsonResponse response = restClient.doGet(url, queryParameters);
		ResultMessage message = new ResultMessage("", false);
	
		try
		{
			message.setMessage(response.getJsonContent());
	
			if (response.getHttpStatus() >= 200 && response.getHttpStatus() < 400)
			{
				message.setWasSuccessful(true);
			}
			else
			{
				message.setWasSuccessful(false);
			}
		}
		catch(Exception e)
		{
			return new ResultMessage(e.fillInStackTrace().toString(), false);
		}
	
		return message;
	}

	protected InputStream doGetFile(String url) throws ClientProtocolException,
			IOException {
				InputStream response = restClient.doGetFile(url);
			
				return response;
			}

	protected ResultMessage doPostFile(String url, File file, String fileMimetype) {
		
		RESTJsonResponse response = restClient.doPostFile(url, file, fileMimetype);		
		ResultMessage message;
	
		try
		{
			Gson gson = new Gson();
			message = gson.fromJson(response.getJsonContent(), ResultMessage.class);
	
			if (response.getHttpStatus() >= 200 && response.getHttpStatus() < 400)
			{
				message.setWasSuccessful(true);
			}
			else
			{
				message.setWasSuccessful(false);
			}
		}
		catch(Exception e)
		{
			return new ResultMessage(e.fillInStackTrace().toString(), false);
		}
	
		return message;
	}

	protected ResultMessage doPutFile(String url, File file, String fileMimetype) {
		
		RESTJsonResponse response = restClient.doPutFile(url, file, fileMimetype);		
		ResultMessage message;
	
		try
		{
			Gson gson = new Gson();
			message = gson.fromJson(response.getJsonContent(), ResultMessage.class);
	
			if (response.getHttpStatus() >= 200 && response.getHttpStatus() < 400)
			{
				message.setWasSuccessful(true);
			}
			else
			{
				message.setWasSuccessful(false);
			}
		}
		catch(Exception e)
		{
			return new ResultMessage(e.fillInStackTrace().toString(), false);
		}
	
		return message;
	}

	protected ResultMessage doPut(String url, String content) {
		RESTJsonResponse response = restClient.doPut(url, content);
		ResultMessage message;
	
		try
		{
			Gson gson = new Gson();
			message = gson.fromJson(response.getJsonContent(), ResultMessage.class);
	
			if (response.getHttpStatus() >= 200 && response.getHttpStatus() < 400)
			{
				message.setWasSuccessful(true);
			}
			else
			{
				message.setWasSuccessful(false);
			}
		}
		catch(Exception e)
		{
			return new ResultMessage(e.fillInStackTrace().toString(), false);
		}
	
		return message;
	}
		
	public ResultMessage doPost(String url, String content) {
		RESTJsonResponse response = restClient.doPost(url, content);
		ResultMessage message;
	
		try
		{
			Gson gson = new Gson();
			message = gson.fromJson(response.getJsonContent(), ResultMessage.class);
	
			if (response.getHttpStatus() >= 200 && response.getHttpStatus() < 400)
			{
				message.setWasSuccessful(true);
			}
			else
			{
				message.setWasSuccessful(false);
			}
		}
		catch(Exception e)
		{
			return new ResultMessage(e.fillInStackTrace().toString(), false);
		}
	
		return message;
	}
	
	public ResultMessage doDelete(String url) {
		RESTJsonResponse response = restClient.doDelete(url);
		ResultMessage message;
	
		try
		{
			Gson gson = new Gson();
			message = gson.fromJson(response.getJsonContent(), ResultMessage.class);
	
			if (response.getHttpStatus() >= 200 && response.getHttpStatus() < 400)
			{
				message.setWasSuccessful(true);
			}
			else
			{
				message.setWasSuccessful(false);
			}
		}
		catch(Exception e)
		{
			return new ResultMessage(e.fillInStackTrace().toString(), false);
		}
	
		return message;
	}
}