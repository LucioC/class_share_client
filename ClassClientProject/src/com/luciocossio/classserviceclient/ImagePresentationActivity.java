package com.luciocossio.classserviceclient;

import java.io.File;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.RESTApacheClient;
import com.luciocossio.classclient.RESTJsonClient;
import com.luciocossio.classclient.ResultMessage;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.view.View;
import android.view.Window;

public class ImagePresentationActivity extends Activity {

	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_image_presentation);
		
		//FIXME hardcoded for test purpose
		serverUrl = "http://10.1.1.4:8880/";
		imageFilename = "presentation.pptx";
		
		initializePresentationClient();		

		dialog = new ProgressDialog(this);
	}
	
	private PresentationClient presentationClient;
	private String serverUrl;
	private String imageFilename;
	
	private void initializePresentationClient()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		presentationClient = new PresentationClient(jsonClient, serverUrl);
	}	

	private static final int REQUEST_CODE = 1234;
	private static final String CHOOSER_TITLE = "Select a file";
	public void sendFile(View view)
	{		
		 Intent target = FileUtils.createGetContentIntent();
		 Intent intent = Intent.createChooser(target, CHOOSER_TITLE);
		    try {
		        startActivityForResult(intent, REQUEST_CODE);
		    } catch (ActivityNotFoundException e) {
		        // The reason for the existence of aFileChooser
		    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    switch (requestCode) {
	    case REQUEST_CODE:  
	        if (resultCode == RESULT_OK) {
	            // The URI of the selected file 
	            final Uri uri = data.getData();

	    		PresentationAsyncTask task = new PresentationAsyncTask(presentationClient, dialog) {
	    			@Override
	    			protected ResultMessage ExecuteTask()
	    			{
	    				File file = FileUtils.getFile(uri);
	    				imageFilename = file.getName();
	    				return client.uploadFile(file, file.getName());
	    			}
	    		};
	    		
	    		task.execute();
	        }
	    }
	}
	
	public void openImage(View view)
	{
		final Activity thisPanel = this;
		PresentationAsyncTask task = new PresentationAsyncTask(presentationClient, dialog)
		{
			@Override
			protected ResultMessage ExecuteTask()
			{
				return client.openImage(imageFilename);
				//return null;
			}
			
			@Override
			protected void OnEndPostExecute(ResultMessage result)
			{
				if(result.getWasSuccessful())
				{
					Intent intent = new Intent(thisPanel, ControlImagePresentationActivity.class);	
					intent.putExtra(CommonVariables.ServerAddress, serverUrl);
					startActivity(intent);
				}
			}
		};
		task.execute();
	}

}