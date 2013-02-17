package com.luciocossio.classclient;

import java.io.File;
import java.util.Map;
import com.google.gson.Gson;

public class PresentationClient {
				
	private final String START_PRESENTATION_PATH = "presentation";
	private final String NEXT_SLIDE_PATH = "presentation/action";
	private final String PREVIOUS_SLIDE_PATH = "presentation/action";
	private final String CLOSE_PRESENTATION_PATH = "presentation/action";
	private final String ADD_FILE_PATH = "files/";
	
	private RESTJsonClient restClient;
	private String serverUrl;
	
	private final String PPTX_MIMETYPE = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
	
	public PresentationClient(RESTJsonClient restClient, String serverUrl)
	{
		this.restClient = restClient;
		this.serverUrl = serverUrl;
	}
	
	public ResultMessage uploadFile(String localFileName, String fileName)
	{
		String url = serverUrl + ADD_FILE_PATH + fileName;
		
		return doPostFile(url, localFileName, PPTX_MIMETYPE);		
	}
	
	public ResultMessage startPresentation(String fileName)
	{		
		String url = serverUrl + START_PRESENTATION_PATH;
		
		return doPut(url, "{\"fileName\":\""+ fileName +"\"}");
	}

	public ResultMessage nextSlide()
	{
		String url = serverUrl + NEXT_SLIDE_PATH;
		
		return doPut(url,"{\"command\":\"next\"}");
	}

	public ResultMessage previousSlide()
	{		
		String url = serverUrl + PREVIOUS_SLIDE_PATH;

		return doPut(url,"{\"command\":\"previous\"}");
	}

	public ResultMessage closePresentation()
	{
		String url = serverUrl + CLOSE_PRESENTATION_PATH;

		return doPut(url,"{\"command\":\"close\"}");
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
	
	private ResultMessage doPostFile(String url, String localFileName, String fileMimetype) {
		
		File file = new File(localFileName);
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
	
	private ResultMessage doPut(String url, String content) {
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

}
