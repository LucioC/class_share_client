package com.luciocossio.classserviceclient.test.integration;

import java.io.File;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.RESTApacheClient;
import com.luciocossio.classclient.RESTJsonClient;
import com.luciocossio.classclient.ResultMessage;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ImagePresentationTest extends TestCase {
	
	public final String serverUrl = "http://10.1.1.4:8880/";
	public final String fileName = "C:/Users/lucioc/Dropbox/Public/android.jpg";

	public void testUploadFile()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		PresentationClient client = new PresentationClient(jsonClient, serverUrl);
		
		File file = new File(fileName);
		ResultMessage actual = client.uploadFile(file, "android.jpg");
		ResultMessage expected = new ResultMessage("File was uploaded to server", true);
		Assert.assertEquals(expected, actual);
	}
	
	public void testOpenImage()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		PresentationClient client = new PresentationClient(jsonClient, serverUrl);
		
		File file = new File(fileName);
		
		ResultMessage actual = client.uploadFile(file, "android.jpg");
		ResultMessage expected = new ResultMessage("File was uploaded to server", true);
		Assert.assertEquals(expected, actual);
		
		actual = client.openImage("android.jpg");
		expected = new ResultMessage("File was uploaded to server", true);
		Assert.assertTrue("something went bad", actual.getWasSuccessful());
	}

}
