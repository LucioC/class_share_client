package com.luciocossio.classclient.activities;

import java.io.File;
import java.util.List;

import com.luciocossio.classclient.R;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.activities.image.PresentationSlidesActivity;
import com.luciocossio.classclient.async.AsyncTaskList;
import com.luciocossio.classclient.async.PresentationAsyncTask;
import com.luciocossio.classserviceclient.util.SystemUiHider;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
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
public class PowerPointPresentationActivity extends PresentationListActivity implements OnItemClickListener {
	
	static final int REQUEST_CODE = 1234;
	static final String CHOOSER_TITLE = "Select a file";
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
	    			protected ResultMessage executeTask()
	    			{
	    				return client.uploadFile(file, file.getName());
	    			}
	    			
	    			@Override
	    			protected void onEndPostExecute(ResultMessage result)
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
	
	protected void setContentView() {
		setContentView(R.layout.activity_powerpoint_list);
	}	

	protected void setListListener() {
		ListView listView = (ListView)findViewById(R.id.files);
		listView.setOnItemClickListener(this);
	}
	
	public void sendFile(View view) {		
		 Intent target = FileUtils.createGetContentIntent();
		 Intent intent = Intent.createChooser(target, CHOOSER_TITLE);
		    try {
		        startActivityForResult(intent, REQUEST_CODE);
		    } catch (ActivityNotFoundException e) {
		        // The reason for the existence of aFileChooser
		    }
	}
	
	public void startPresentation(View view)
	{
		final Activity thisPanel = this;
		PresentationAsyncTask task = new PresentationAsyncTask(presentationClient, dialog)
		{
			@Override
			protected ResultMessage executeTask()
			{
				return client.preparePresentation(lastFilename);
			}			
			
			@Override
			protected void onEndPostExecute(ResultMessage result)
			{
				if(result.getWasSuccessful())
				{
					Intent intent = new Intent(thisPanel, PresentationSlidesActivity.class);	
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
	
	public void updateList() {		
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
			protected List<String> executeTask()
			{
				List<String> files = client.getPresentationFileNames();
				return files;
			}			
		};
		task.execute();
	}	
}
