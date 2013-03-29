package com.luciocossio.classserviceclient.test.units;

import java.io.File;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.RESTJsonClient;
import com.luciocossio.classclient.RESTJsonResponse;
import com.luciocossio.classclient.ResultMessage;

import junit.framework.Assert;
import junit.framework.TestCase;
import static org.mockito.Mockito.*;

public class PresentationTest extends TestCase {
	
	PresentationClient presentationClient;
	RESTJsonClient jsonClient;
	
		
	@Override
	public void setUp()
	{
		jsonClient = mock(RESTJsonClient.class);
		presentationClient = new PresentationClient(jsonClient, "http://localhost.com/");	
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
				
		ResultMessage actual = client.startPresentation("presentation.pptx");
		
		ResultMessage expected = new ResultMessage("started", true);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
	}
	
	public void testWrongStartPresentation()
	{
		PresentationClient client = new PresentationClient(jsonClient, "http://localhost.com/");
		
		when(jsonClient.doPut(anyString(), anyString())).thenReturn(new RESTJsonResponse(404, "{\"message\":\"error\"}"));
				
		ResultMessage actual = client.startPresentation("presentation.pptx");
		
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
