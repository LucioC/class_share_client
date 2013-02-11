package com.luciocossio.classclient;

import java.util.LinkedHashMap;
import java.util.Map;

import android.util.Log;

import com.google.gson.Gson;


public class PresentationClient {
				
	private final String START_PRESENTATION_PATH = "presentation/open";
	private final String NEXT_SLIDE_PATH = "presentation/next";
	private final String PREVIOUS_SLIDE_PATH = "presentation/previous";
	private final String CLOSE_PRESENTATION_PATH = "presentation/close";
	
	private RESTJsonClient restClient;
	private String serverUrl;
	
	public PresentationClient(RESTJsonClient restClient, String serverUrl)
	{
		this.restClient = restClient;
		this.serverUrl = serverUrl;
	}
	
	public ResultMessage startPresentation(String fileName)
	{
		Map<String, String> queryParameters = new LinkedHashMap<String, String>();
		queryParameters.put("fileName", fileName);
		
		String url = serverUrl + START_PRESENTATION_PATH;
		
		return doGetAndReturnResult(queryParameters, url);
	}

	public ResultMessage nextSlide()
	{
		String url = serverUrl + NEXT_SLIDE_PATH;
		
		return doGetAndReturnResult(null, url);
	}

	public ResultMessage previousSlide()
	{		
		String url = serverUrl + PREVIOUS_SLIDE_PATH;
		
		return doGetAndReturnResult(null, url);
	}

	public ResultMessage closePresentation()
	{
		String url = serverUrl + CLOSE_PRESENTATION_PATH;
		
		return doGetAndReturnResult(null, url);
	}	

	private ResultMessage doGetAndReturnResult(Map<String, String> queryParameters, String url) {
		RESTJsonResponse response = restClient.doGet(url, queryParameters);
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
