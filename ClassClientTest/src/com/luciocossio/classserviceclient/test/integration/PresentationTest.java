package com.luciocossio.classserviceclient.test.integration;

import java.io.File;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.RESTApacheClient;
import com.luciocossio.classclient.RESTJsonClient;
import com.luciocossio.classclient.ResultMessage;

import junit.framework.Assert;
import junit.framework.TestCase;

public class PresentationTest extends TestCase {
	
	public final String serverUrl = "http://10.1.1.5:8880/";
	public final String fileName = "C:/Users/lucioc/Dropbox/Public/Mestrado/Dissertacao/PEP/PEP_posM.pptx";
	
	public void testUploadStartNextPreviousClosePresentation()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		PresentationClient client = new PresentationClient(jsonClient, serverUrl);
		
		File file = new File(fileName);
		ResultMessage actual = client.uploadFile(file, "presentation.pptx");
		ResultMessage expected = new ResultMessage("File was uploaded to server", true);
		Assert.assertEquals(expected, actual);
		
		actual = client.startPresentation("presentation.pptx");
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
}
