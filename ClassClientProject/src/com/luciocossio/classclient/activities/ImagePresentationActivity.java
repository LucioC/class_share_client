package com.luciocossio.classclient.activities;

import java.io.File;
import java.util.List;

import com.luciocossio.classclient.R;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.activities.image.PresentationImageActivity;
import com.luciocossio.classclient.async.AsyncTaskList;
import com.luciocossio.classclient.async.PresentationAsyncTask;

import android.net.Uri;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ImagePresentationActivity extends PresentationListActivity implements OnItemClickListener {
	
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
				final ImagePresentationActivity thisActivity = this;
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
		setContentView(R.layout.activity_image_list);
	}	

	protected void setListListener() {
		ListView listView = (ListView)findViewById(R.id.image_list);
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
	
	public void startImagePresentation(View view)
	{
		final Activity thisPanel = this;
		PresentationAsyncTask task = new PresentationAsyncTask(presentationClient, dialog)
		{
			@Override
			protected ResultMessage executeTask()
			{
				return client.openImage(lastFilename);
			}
			
			@Override
			protected void onEndPostExecute(ResultMessage result)
			{
				if(result.getWasSuccessful())
				{
					Intent intent = new Intent(thisPanel, PresentationImageActivity.class);	
					intent.putExtra(CommonVariables.ServerAddress, serverUrl);
					startActivity(intent);
				}
			}
		};
		task.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ListView list = (ListView) findViewById(R.id.image_list);
		String selectedFromList = (String) (list.getItemAtPosition(arg2));
		lastFilename = selectedFromList;
		startImagePresentation(null);
	}
	
	public void updateList() {		
		final ListView list = (ListView) findViewById(R.id.image_list);
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
				List<String> files = client.getImageFileNames();
				return files;
			}			
		};
		task.execute();
	}
	
}
