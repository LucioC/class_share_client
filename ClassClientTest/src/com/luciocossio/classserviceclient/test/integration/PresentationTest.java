package com.luciocossio.classserviceclient.test.integration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.http.RESTApacheClient;
import com.luciocossio.classclient.http.RESTJsonClient;

import junit.framework.Assert;
import junit.framework.TestCase;

public class PresentationTest extends TestCase {
	
	public final String serverUrl = "http://10.1.1.7:8880/";
	public final String fileName = "C:/Users/lucioc/Dropbox/Public/Mestrado/Dissertacao/PEP/PEP_posM.pptx";
		
	public void testUploadStartPresentation()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		PresentationClient client = new PresentationClient(jsonClient, serverUrl);
		
		File file = new File(fileName);
		ResultMessage actual = client.uploadFile(file, "presentation.pptx");
		ResultMessage expected = new ResultMessage("File was uploaded to server", true);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
		
		actual = client.preparePresentation("presentation.pptx");
		actual = client.startPresentation();
		expected = new ResultMessage("Presentation has been started", true);
		Assert.assertEquals(expected, actual);
	}
	
	public void testUploadStartNextPreviousClosePresentation()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		PresentationClient client = new PresentationClient(jsonClient, serverUrl);
		
		File file = new File(fileName);
		ResultMessage actual = client.uploadFile(file, "presentation.pptx");
		ResultMessage expected = new ResultMessage("File was uploaded to server", true);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());

		actual = client.preparePresentation("presentation.pptx");
		actual = client.startPresentation();
		expected = new ResultMessage("Presentation has been started", true);
		Assert.assertEquals(expected, actual);
		
		actual = client.nextSlide();
		expected = new ResultMessage("Command Executed", true);
		Assert.assertEquals(expected, actual);
		
		actual = client.previousSlide();
		expected = new ResultMessage("Command Executed", true);
		Assert.assertEquals(expected, actual);
		
		actual = client.closePresentation();
		expected = new ResultMessage("Command Executed", true);
		Assert.assertEquals(expected, actual);		
	}
	
	public void testUploadFile()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		PresentationClient client = new PresentationClient(jsonClient, serverUrl);
		
		File file = new File(fileName);
		ResultMessage actual = client.uploadFile(file, "presentation.pptx");
		ResultMessage expected = new ResultMessage("File was uploaded to server", true);
		Assert.assertEquals(expected, actual);
	}
	
	public void testUploadAndGetFile()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		PresentationClient client = new PresentationClient(jsonClient, serverUrl);
		
		File file = new File(fileName);
		ResultMessage actual = client.uploadFile(file, "presentation.pptx");
		ResultMessage expected = new ResultMessage("File was uploaded to server", true);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
		
		InputStream fileReturned = null;
		try {
			fileReturned = client.getFile("presentation.pptx");
		} catch (Exception e) {
			fail("exception was throw");
		}

		// count bytes from inputstream 
		int read = 0;
		int total = 0;
		byte[] bytes = new byte[1024];	 
		try {
			while ((read = fileReturned.read(bytes)) != -1) {
				total += read;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	 
		
		Assert.assertEquals(file.length(), total);
	}
}
