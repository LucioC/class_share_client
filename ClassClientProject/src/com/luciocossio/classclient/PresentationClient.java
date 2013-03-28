package com.luciocossio.classclient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.Gson;

public class PresentationClient {
				
	private final String START_PRESENTATION_PATH = "presentation";
	private final String IMAGE_COMMAND = "image/action";
	private final String OPEN_IMAGE_PATH = "image";
	private final String NEXT_SLIDE_PATH = "presentation/action";
	private final String PREVIOUS_SLIDE_PATH = "presentation/action";
	private final String CLOSE_PRESENTATION_PATH = "presentation/action";
	private final String FILE_PATH = "files/";
	
	private RESTJsonClient restClient;
	private String serverUrl;
	
	private final String PPTX_MIMETYPE = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
	
	public PresentationClient(RESTJsonClient restClient, String serverUrl)
	{
		this.restClient = restClient;
		this.serverUrl = serverUrl;
	}
	
	public ResultMessage uploadFile(File file, String fileName)
	{
		String url = serverUrl + FILE_PATH + fileName;
		
		return doPutFile(url, file, PPTX_MIMETYPE);		
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
	
	public InputStream getFile(String fileName) throws ClientProtocolException, IOException
	{
		String url = serverUrl + FILE_PATH + fileName;

		InputStream stream = doGetFile(url);
		return stream;
	}	
	
	public ResultMessage openImage(String fileName)
	{		
		String url = serverUrl + OPEN_IMAGE_PATH;
		
		return doPut(url, "{\"fileName\":\""+ fileName +"\"}");
	}
	
	public ResultMessage closeImage()
	{
		String url = serverUrl + IMAGE_COMMAND;

		return doPut(url,"{\"command\":\"close\"}");
	}	
	
	public ResultMessage rotateImageRight()
	{
		String url = serverUrl + IMAGE_COMMAND;
		
		return doPut(url,"{\"command\":\"rotateright\"}");
	}
	
	public ResultMessage rotateImageLeft()
	{
		String url = serverUrl + IMAGE_COMMAND;
		
		return doPut(url,"{\"command\":\"rotateleft\"}");
	}

	public ResultMessage moveImageRight()
	{
		String url = serverUrl + IMAGE_COMMAND;
		
		return doPut(url,"{\"command\":\"moveright\"}");
	}
	
	public ResultMessage moveImageLeft()
	{
		String url = serverUrl + IMAGE_COMMAND;
		
		return doPut(url,"{\"command\":\"moveleft\"}");
	}
	
	public ResultMessage moveImageUp()
	{
		String url = serverUrl + IMAGE_COMMAND;
		
		return doPut(url,"{\"command\":\"moveup\"}");
	}
	
	public ResultMessage moveImageDown()
	{
		String url = serverUrl + IMAGE_COMMAND;
		
		return doPut(url,"{\"command\":\"movedown\"}");
	}

	public ResultMessage zoomInImage()
	{
		String url = serverUrl + IMAGE_COMMAND;
		
		return doPut(url,"{\"command\":\"zoomin\"}");
	}
	
	public ResultMessage zoomOutImage()
	{
		String url = serverUrl + IMAGE_COMMAND;
		
		return doPut(url,"{\"command\":\"zoomout\"}");
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
	
	private InputStream doGetFile(String url) throws ClientProtocolException, IOException {
		InputStream response = restClient.doGetFile(url);

		return response;
	}
	
	private ResultMessage doPostFile(String url, File file, String fileMimetype) {
		
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
	
	private ResultMessage doPutFile(String url, File file, String fileMimetype) {
		
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
