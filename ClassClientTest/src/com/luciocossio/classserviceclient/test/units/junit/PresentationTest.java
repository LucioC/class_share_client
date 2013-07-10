package com.luciocossio.classserviceclient.test.units.junit;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.http.RESTJsonClient;
import com.luciocossio.classclient.http.RESTJsonResponse;

import junit.framework.Assert;
import junit.framework.TestCase;
import static org.mockito.Mockito.*;

public class PresentationTest extends TestCase {
	
	RESTJsonClient jsonClient;	
		
	@Override
	public void setUp()
	{
		jsonClient = mock(RESTJsonClient.class);
	}
	
	@Override
	public void tearDown()
	{
			
	}
	
	public void testUploadFilePresentation()
	{
		PresentationClient client = new PresentationClient(jsonClient, "http://localhost.com/");
		
		when(jsonClient.doPutFile(anyString(), any(File.class), anyString())).thenReturn(new RESTJsonResponse(200, "{\"message\":\"hello\"}"));
		
		File file = mock(File.class);
		
		ResultMessage actual = client.uploadFile(file, "presentation.pptx");
		
		ResultMessage expected = new ResultMessage("hello", true);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
	}
	
	public void testGetPresentationImageNames()
	{
		PresentationClient client = new PresentationClient(jsonClient, "http://localhost.com/");
		
		when(jsonClient.doGet(anyString(), anyMap())).thenReturn(new RESTJsonResponse(200, "{\"images\":[\"1\",\"2\"]}"));
				
		List<String> actual = client.getCurrentPresentationImageNames();
		
		List<String> expected = new ArrayList<String>();
		expected.add("1");
		expected.add("2");
		
		Assert.assertEquals(expected, actual);
	}
	
	public void testWrongUploadFilePresentation()
	{
		PresentationClient client = new PresentationClient(jsonClient, "http://localhost.com/");
		
		when(jsonClient.doPutFile(anyString(), any(File.class), anyString())).thenReturn(new RESTJsonResponse(400, "{\"message\":\"error\"}"));
		
		File file = mock(File.class);
		
		ResultMessage actual = client.uploadFile(file, "presentation.pptx");
		
		ResultMessage expected = new ResultMessage("error", false);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
	}
	
	public void testStartPresentation()
	{
		PresentationClient client = new PresentationClient(jsonClient, "http://localhost.com/");
		
		when(jsonClient.doPut(anyString(), anyString())).thenReturn(new RESTJsonResponse(200, "{\"message\":\"started\"}"));

		ResultMessage actual = client.startPresentation();
		
		ResultMessage expected = new ResultMessage("started", true);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
	}
	
	public void testWrongStartPresentation()
	{
		PresentationClient client = new PresentationClient(jsonClient, "http://localhost.com/");
		
		when(jsonClient.doPut(anyString(), anyString())).thenReturn(new RESTJsonResponse(404, "{\"message\":\"error\"}"));
				
		ResultMessage actual = client.preparePresentation("presentation.pptx");
		
		ResultMessage expected = new ResultMessage("error", false);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
	}
	
	public void testNextSlidePresentation()
	{
		PresentationClient client = new PresentationClient(jsonClient, "http://localhost.com/");
		
		when(jsonClient.doPut(anyString(), eq("{\"command\":\"next\"}"))).thenReturn(new RESTJsonResponse(200, "{\"message\":\"next\"}"));
				
		ResultMessage actual = client.nextSlide();
		
		ResultMessage expected = new ResultMessage("next", true);
		verify(jsonClient).doPut(anyString(), eq("{\"command\":\"next\"}"));
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
	}
	
	public void testPreviousSlidePresentation()
	{
		PresentationClient client = new PresentationClient(jsonClient, "http://localhost.com/");
		
		when(jsonClient.doPut(anyString(), eq("{\"command\":\"previous\"}"))).thenReturn(new RESTJsonResponse(200, "{\"message\":\"next\"}"));
				
		ResultMessage actual = client.previousSlide();
		
		ResultMessage expected = new ResultMessage("next", true);
		verify(jsonClient).doPut(anyString(), eq("{\"command\":\"previous\"}"));
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
	}
	
	public void testListFiles()
	{
		PresentationClient client = new PresentationClient(jsonClient, "http://localhost.com/");
		
		when(jsonClient.doGet(anyString(),anyMap())).thenReturn(new RESTJsonResponse(200, "{\"files\":[\"android.jpg\",\"AspectosCulturais.pptx\",\"presentation.pptx\",\"waldo3.jpg\",\"wally1.jpg\"]}"));
		
		List<String> actual = client.getFileNames(null);
		
		List<String> expected = new ArrayList<String>();
		expected.add("android.jpg");
		expected.add("AspectosCulturais.pptx");
		expected.add("presentation.pptx");
		expected.add("waldo3.jpg");
		expected.add("wally1.jpg");		
		
		Assert.assertEquals(expected, actual);
	}
	
	public void testListPresentationFiles()
	{
		PresentationClient client = new PresentationClient(jsonClient, "http://localhost.com/");

		when(jsonClient.doGet(anyString(),anyMap())).thenReturn(new RESTJsonResponse(200, "{\"files\":[\"AspectosCulturais.pptx\",\"presentation.pptx\"]}"));
		
		List<String> actual = client.getPresentationFileNames();
		
		List<String> expected = new ArrayList<String>();
		expected.add("AspectosCulturais.pptx");
		expected.add("presentation.pptx");

		Map<String, String> queryParameters = new HashMap<String, String>();
		queryParameters.put("type", "presentation");
		verify(jsonClient).doGet(anyString(), eq(queryParameters));
		
		Assert.assertEquals(expected, actual);
	}
	
	public void testListImageFiles()
	{
		PresentationClient client = new PresentationClient(jsonClient, "http://localhost.com/");
		
		when(jsonClient.doGet(anyString(),anyMap())).thenReturn(new RESTJsonResponse(200, "{\"files\":[\"android.jpg\",\"waldo3.jpg\"]}"));
		
		List<String> actual = client.getImageFileNames();
		
		List<String> expected = new ArrayList<String>();
		expected.add("android.jpg");
		expected.add("waldo3.jpg");		

		Map<String, String> queryParameters = new HashMap<String, String>();
		queryParameters.put("type", "image");
		verify(jsonClient).doGet(anyString(), eq(queryParameters));
		
		Assert.assertEquals(expected, actual);
	}
	
	public void testClosePresentation()
	{
		PresentationClient client = new PresentationClient(jsonClient, "http://localhost.com/");
		
		when(jsonClient.doPut(anyString(), eq("{\"command\":\"close\"}"))).thenReturn(new RESTJsonResponse(200, "{\"message\":\"next\"}"));
				
		ResultMessage actual = client.closePresentation();
		
		ResultMessage expected = new ResultMessage("next", true);
		verify(jsonClient).doPut(anyString(), eq("{\"command\":\"close\"}"));
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
	}

}
