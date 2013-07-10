package com.luciocossio.classclient.activities;

import java.io.File;
import java.util.List;

import com.luciocossio.classclient.R;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.activities.image.ImageGallery;
import com.luciocossio.classclient.http.RESTApacheClient;
import com.luciocossio.classclient.http.RESTJsonClient;
import com.luciocossio.classserviceclient.util.SystemUiHider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class PowerPointPresentationActivity extends Activity implements OnItemClickListener {
	
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
		
		ListView listView = (ListView)findViewById(R.id.files);
		listView.setOnItemClickListener(this);
		
		this.updateList();
	}
	
	public void updateList()
	{		
		final ListView list = (ListView) findViewById(R.id.files);
		final Context context = this;
		
		AsyncTaskList task = new AsyncTaskList(presentationClient, dialog, "Carregando lista de arquivos...")
		{			
			@Override
			protected void onPostExecute(List<String> files)
			{	
				super.onPostExecute(files);
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, 
			            android.R.layout.simple_list_item_1);
				adapter.addAll(files);
				list.setAdapter(adapter);
			}
			
			@Override
			protected List<String> ExecuteTask()
			{
				List<String> files = client.getPresentationFileNames();
				return files;
			}			
		};
		task.execute();
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
				final PowerPointPresentationActivity thisActivity = this;
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
		    				thisActivity.updateList();
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ListView list = (ListView) findViewById(R.id.files);
		String selectedFromList = (String) (list.getItemAtPosition(arg2));
		lastFilename = selectedFromList;		
		startPresentation(null);
	}
	
}
