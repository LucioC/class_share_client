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
	
	private void sleep(long mileseconds)
	{		
		try {
			Thread.sleep(mileseconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testOpenAndCloseImage()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		PresentationClient client = new PresentationClient(jsonClient, serverUrl);
		
		File file = new File(fileName);
		
		ResultMessage actual = client.uploadFile(file, "android.jpg");
		ResultMessage expected = new ResultMessage("File was uploaded to server", true);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
		
		actual = client.openImage("android.jpg");
		Assert.assertTrue("something went bad", actual.getWasSuccessful());		
		
		//FIXME Windows initialization takes some time, we need to wait before execute a close call. we could get a window status remotely and then close
		sleep(1000);
		
		actual = client.closeImage();
		expected = new ResultMessage("Command Executed", true);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
		Assert.assertTrue("something went bad", actual.getWasSuccessful());
	}
	
	public void testOpenTwoTimesAndCloseImage()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		PresentationClient client = new PresentationClient(jsonClient, serverUrl);
		
		File file = new File(fileName);
		
		ResultMessage actual = client.uploadFile(file, "android.jpg");
		ResultMessage expected = new ResultMessage("File was uploaded to server", true);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
		
		actual = client.openImage("android.jpg");
		Assert.assertTrue("something went bad", actual.getWasSuccessful());		
		
		sleep(2000);
		
		//Should open new image and close old one (a human should confirm it);
		actual = client.openImage("android.jpg");
		Assert.assertTrue("something went bad", actual.getWasSuccessful());		
		
		//FIXME Windows initialization takes some time, we need to wait before execute a close call. we could get a window status remotely and then close
		sleep(1000);
		
		actual = client.closeImage();
		expected = new ResultMessage("Command Executed", true);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
		Assert.assertTrue("something went bad", actual.getWasSuccessful());
	}
	
	public void testZoomInImage()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		PresentationClient client = new PresentationClient(jsonClient, serverUrl);
		
		File file = new File(fileName);
		
		ResultMessage actual = client.uploadFile(file, "android.jpg");
		ResultMessage expected = new ResultMessage("File was uploaded to server", true);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
		
		actual = client.openImage("android.jpg");
		Assert.assertTrue("something went bad", actual.getWasSuccessful());		
		sleep(2000);		
				
		//zoom in two times
		actual = client.zoomInImage();
		Assert.assertTrue("something went bad " + actual.getMessage(), actual.getWasSuccessful());		
		actual = client.zoomInImage();
		Assert.assertTrue("something went bad " + actual.getMessage(), actual.getWasSuccessful());	
		

		sleep(2000);		
		
	}
	
	public void testMoveRightImage()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		PresentationClient client = new PresentationClient(jsonClient, serverUrl);
		
		File file = new File(fileName);
		
		ResultMessage actual = client.uploadFile(file, "android.jpg");
		ResultMessage expected = new ResultMessage("File was uploaded to server", true);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
		
		actual = client.openImage("android.jpg");
		Assert.assertTrue("something went bad", actual.getWasSuccessful());		
		
		sleep(3000);
		//move right two times
		actual = client.moveImageRight();
		Assert.assertTrue("something went bad " + actual.getMessage(), actual.getWasSuccessful());	
		actual = client.moveImageRight();
		Assert.assertTrue("something went bad " + actual.getMessage(), actual.getWasSuccessful());	
		sleep(2000);		
		
	}
	
	public void testMoveUpImage()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		PresentationClient client = new PresentationClient(jsonClient, serverUrl);
		
		File file = new File(fileName);
		
		ResultMessage actual = client.uploadFile(file, "android.jpg");
		ResultMessage expected = new ResultMessage("File was uploaded to server", true);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
		
		actual = client.openImage("android.jpg");
		Assert.assertTrue("something went bad", actual.getWasSuccessful());		
		
		sleep(3000);
		//move up two times
		actual = client.moveImageUp();
		Assert.assertTrue("something went bad " + actual.getMessage(), actual.getWasSuccessful());	
		actual = client.moveImageUp();
		Assert.assertTrue("something went bad " + actual.getMessage(), actual.getWasSuccessful());	
		sleep(2000);		
		
	}
	
	public void testMoveLeftImage()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		PresentationClient client = new PresentationClient(jsonClient, serverUrl);
		
		File file = new File(fileName);
		
		ResultMessage actual = client.uploadFile(file, "android.jpg");
		ResultMessage expected = new ResultMessage("File was uploaded to server", true);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
		
		actual = client.openImage("android.jpg");
		Assert.assertTrue("something went bad", actual.getWasSuccessful());		
		
		sleep(3000);
		//move left two times
		actual = client.moveImageLeft();
		Assert.assertTrue("something went bad " + actual.getMessage(), actual.getWasSuccessful());	
		actual = client.moveImageLeft();
		Assert.assertTrue("something went bad " + actual.getMessage(), actual.getWasSuccessful());	

		sleep(2000);		
	}
	
	public void testMoveDownImage()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		PresentationClient client = new PresentationClient(jsonClient, serverUrl);
		
		File file = new File(fileName);
		
		ResultMessage actual = client.uploadFile(file, "android.jpg");
		ResultMessage expected = new ResultMessage("File was uploaded to server", true);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
		
		actual = client.openImage("android.jpg");
		Assert.assertTrue("something went bad", actual.getWasSuccessful());		
		
		sleep(3000);
		//move down two times
		actual = client.moveImageDown();
		Assert.assertTrue("something went bad " + actual.getMessage(), actual.getWasSuccessful());	
		actual = client.moveImageDown();
		Assert.assertTrue("something went bad " + actual.getMessage(), actual.getWasSuccessful());	
		sleep(2000);		
		
	}
	
	public void testZoomOutImage()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		PresentationClient client = new PresentationClient(jsonClient, serverUrl);
		
		File file = new File(fileName);
		
		ResultMessage actual = client.uploadFile(file, "android.jpg");
		ResultMessage expected = new ResultMessage("File was uploaded to server", true);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
		
		actual = client.openImage("android.jpg");
		Assert.assertTrue("something went bad", actual.getWasSuccessful());		
		
		sleep(3000);		
		//zoom out two times
		actual = client.zoomOutImage();
		Assert.assertTrue("something went bad " + actual.getMessage(), actual.getWasSuccessful());		
		actual = client.zoomOutImage();
		Assert.assertTrue("something went bad " + actual.getMessage(), actual.getWasSuccessful());	
		sleep(2000);		
	}
	
	public void testRotateLeftImage()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		PresentationClient client = new PresentationClient(jsonClient, serverUrl);
		
		File file = new File(fileName);
		
		ResultMessage actual = client.uploadFile(file, "android.jpg");
		ResultMessage expected = new ResultMessage("File was uploaded to server", true);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
		
		actual = client.openImage("android.jpg");
		Assert.assertTrue("something went bad", actual.getWasSuccessful());		

		sleep(2000);		
		//Rotate left
		actual = client.rotateImageLeft();
		Assert.assertTrue("something went bad " + actual.getMessage(), actual.getWasSuccessful());		
		sleep(2000);
	}
	
	public void testRotateRightImage()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		PresentationClient client = new PresentationClient(jsonClient, serverUrl);
		
		File file = new File(fileName);
		
		ResultMessage actual = client.uploadFile(file, "android.jpg");
		ResultMessage expected = new ResultMessage("File was uploaded to server", true);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
		
		actual = client.openImage("android.jpg");
		Assert.assertTrue("something went bad", actual.getWasSuccessful());
		
		sleep(2000);		
		//rotate one time right
		actual = client.rotateImageRight();
		Assert.assertTrue("something went bad " + actual.getMessage(), actual.getWasSuccessful());		
		
		sleep(2000);	
		
		actual = client.closeImage();
		expected = new ResultMessage("Command Executed", true);
		Assert.assertEquals(expected.getMessage(), actual.getMessage());
		Assert.assertTrue("something went bad", actual.getWasSuccessful());
	}

}
