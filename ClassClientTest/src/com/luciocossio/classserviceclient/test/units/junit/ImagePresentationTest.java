package com.luciocossio.classserviceclient.test.units.junit;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import com.luciocossio.classclient.ClassClientHTTPCommon;
import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.http.RESTJsonClient;
import com.luciocossio.classclient.http.RESTJsonResponse;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ImagePresentationTest extends TestCase {
	ClassClientHTTPCommon presentationClient;
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
	
	public void testOpenImage()
	{
		PresentationClient client = new PresentationClient(jsonClient, "http://localhost.com/");
		
		when(jsonClient.doPut(anyString(), anyString())).thenReturn(new RESTJsonResponse(200, "{\"message\":\"started\"}"));
				
		ResultMessage actual = client.openImage("image.jpg");
		
		ResultMessage expected = new ResultMessage("started", true);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
	}
	
	public void testWrongOpenImage()
	{
		PresentationClient client = new PresentationClient(jsonClient, "http://localhost.com/");
		
		when(jsonClient.doPut(anyString(), anyString())).thenReturn(new RESTJsonResponse(404, "{\"message\":\"error\"}"));
				
		ResultMessage actual = client.openImage("image.jpg");
		
		ResultMessage expected = new ResultMessage("error", false);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
	}
	
	public void testMoveRightImage()
	{
		PresentationClient client = new PresentationClient(jsonClient, "http://localhost.com/");
		
		when(jsonClient.doPut(anyString(), eq("{\"command\":\"moveright\"}"))).thenReturn(new RESTJsonResponse(200, "{\"message\":\"next\"}"));
				
		ResultMessage actual = client.moveImageRight();
		
		ResultMessage expected = new ResultMessage("next", true);
		verify(jsonClient).doPut(anyString(), eq("{\"command\":\"moveright\"}"));
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
	}
	
	public void testRotateLeftImage()
	{
		PresentationClient client = new PresentationClient(jsonClient, "http://localhost.com/");
		
		when(jsonClient.doPut(anyString(), eq("{\"command\":\"rotateleft\"}"))).thenReturn(new RESTJsonResponse(200, "{\"message\":\"next\"}"));
				
		ResultMessage actual = client.rotateImageLeft();
		
		ResultMessage expected = new ResultMessage("next", true);
		verify(jsonClient).doPut(anyString(), eq("{\"command\":\"rotateleft\"}"));
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
	}
	
	public void testClosePresentation()
	{
		PresentationClient client = new PresentationClient(jsonClient, "http://localhost.com/");
		
		when(jsonClient.doPut(anyString(), eq("{\"command\":\"close\"}"))).thenReturn(new RESTJsonResponse(200, "{\"message\":\"next\"}"));
				
		ResultMessage actual = client.closeImage();
		
		ResultMessage expected = new ResultMessage("next", true);
		verify(jsonClient).doPut(anyString(), eq("{\"command\":\"close\"}"));
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
	}

}
