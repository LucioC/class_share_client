package com.luciocossio.classclient.activities;

import java.io.File;
import com.luciocossio.classclient.R;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.RESTApacheClient;
import com.luciocossio.classclient.RESTJsonClient;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.activities.image.ImageGallery;
import com.luciocossio.classserviceclient.util.SystemUiHider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class PowerPointPresentationActivity extends Activity {
	
	private ProgressDialog dialog;	
	private PresentationClient presentationClient;
	private String serverUrl;
	private String lastFilename;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_powerpoint_presentation);

		Intent intent = getIntent();		
		serverUrl = intent.getStringExtra(CommonVariables.ServerAddress);
		
		lastFilename = "presentation.pptx";
		
		initializePresentationClient();		

		dialog = new ProgressDialog(this);
	}
	
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
				final File file = FileUtils.getFile(uri);

	    		PresentationAsyncTask task = new PresentationAsyncTask(presentationClient, dialog) {
	    			@Override
	    			protected ResultMessage ExecuteTask()
	    			{
	    				return client.uploadFile(file, file.getName());
	    			}
	    			
	    			@Override
	    			protected void OnEndPostExecute(ResultMessage result)
	    			{
	    				if(result.getWasSuccessful())
	    				{
		    				lastFilename = file.getName();	    					
	    				}
	    			}
	    		};
	    		
	    		task.execute();           
	        }
	    }
	}
	
	public void startPresentation(View view)
	{
		final Activity thisPanel = this;
		PresentationAsyncTask task = new PresentationAsyncTask(presentationClient, dialog)
		{
			@Override
			protected ResultMessage ExecuteTask()
			{
				client.preparePresentation(lastFilename);
				return client.startPresentation();
				//return null;
			}			
			
			@Override
			protected void OnEndPostExecute(ResultMessage result)
			{
				if(result.getWasSuccessful())
				{
					Intent intent = new Intent(thisPanel, ControlPowerPointActivity.class);	
					intent.putExtra(CommonVariables.ServerAddress, serverUrl);
					startActivity(intent);
				}
			}
		};
		task.execute();
	}
	
	public void startPresentation2(View view)
	{
		final Activity thisPanel = this;
		PresentationAsyncTask task = new PresentationAsyncTask(presentationClient, dialog)
		{
			@Override
			protected ResultMessage ExecuteTask()
			{
				return client.preparePresentation(lastFilename);
			}			
			
			@Override
			protected void OnEndPostExecute(ResultMessage result)
			{
				if(result.getWasSuccessful())
				{
					Intent intent = new Intent(thisPanel, ImageGallery.class);	
					intent.putExtra(CommonVariables.ServerAddress, serverUrl);
					startActivity(intent);
				}
			}
		};
		task.execute();
	}
	
}
