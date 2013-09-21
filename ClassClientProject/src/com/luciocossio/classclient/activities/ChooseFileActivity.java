package com.luciocossio.classclient.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.R;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.activities.image.PresentationImageActivity;
import com.luciocossio.classclient.activities.image.PresentationSlidesActivity;
import com.luciocossio.classclient.async.AsyncTaskList;
import com.luciocossio.classclient.async.PresentationAsyncTask;
import com.luciocossio.classclient.helpers.FileExtensionHelper;
import com.luciocossio.classclient.model.GeneralPresentationState;
import com.luciocossio.classclient.model.ImagePresentationInfo;
import com.luciocossio.classclient.model.SlidePresentationInfo;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ChooseFileActivity extends PresentationListActivity implements OnSharedPreferenceChangeListener, OnItemClickListener {
	
	SharedPreferences prefs;
	FileExtensionHelper fileHelper = new FileExtensionHelper();
	ActivitiesInitializer initializer = new ActivitiesInitializer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Setup preferences
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);     
		
		serverUrl = getServerAddressFromPreferences();
		if(serverUrl != null)
		{
			initializePresentationClient();
			this.updateList();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public void onOptionsMenuClosed(Menu menu) {
		super.onOptionsMenuClosed(menu);		
		this.updateList();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch ( item.getItemId() ) {
			case R.id.itemPrefs:
				Intent intent = new Intent(this, PrefsActivity.class);
				startActivity(intent);
				break;
		}
		
		return true;
	}

	protected String getServerAddressFromPreferences() {	
		if(prefs.getString("server", null) == null) return null;
		
		String serverUrl;
		serverUrl = "http://";
		serverUrl+= prefs.getString("server", "10.1.1.4");
		serverUrl+= ":";
		serverUrl+= prefs.getString("port", "8880");
		serverUrl+= "/";
		return serverUrl;
	}
	
	public void startSlidesPresentation(View view)
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
					initializer.startPresentationSlidesActivity(thisPanel, serverUrl, false);
				}
			}
		};
		task.execute();
	}
	
	public void startImagePresentation(View view)
	{
		final Activity thisPanel = this;
		PresentationAsyncTask task = new PresentationAsyncTask(presentationClient, dialog)
		{
			@Override
			protected ResultMessage executeTask()
			{
				return new ResultMessage("", true);
			}
			
			@Override
			protected void onEndPostExecute(ResultMessage result)
			{
				if(result.getWasSuccessful())
				{
					initializer.startImagePresentationActivity(thisPanel, serverUrl, lastFilename, false);
				}
			}
		};
		task.execute();
	}


	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		serverUrl = getServerAddressFromPreferences();
		initializePresentationClient();
	}

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_choose_files);
	}

	@Override
	protected void setListListener() {
		ListView listView = (ListView)findViewById(R.id.files);
		listView.setOnItemClickListener(this);
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
				
				if(files == null || files.size() == 0)
				{
					files = new ArrayList<String>();
					files.add("Server not reachable");
				}
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, 
			            android.R.layout.simple_list_item_1);
				adapter.addAll(files);
				list.setAdapter(adapter);
			}
			
			@Override
			protected List<String> executeTask()
			{
				List<String> files = new ArrayList<String>();
				try
				{
					files = client.getFileNames(null);
				}
				catch(Exception e)
				{
					Log.e("UpdateList", e.getMessage());
				}
				return files;
			}			
		};
		task.execute();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ListView list = (ListView) findViewById(R.id.files);
		String selectedFromList = (String) (list.getItemAtPosition(arg2));
		lastFilename = selectedFromList;
		
		String fileExtension = fileHelper.getFileExt(lastFilename);
		
		if(fileHelper.isPowerPointExtension(fileExtension))			
		{
			startSlidesPresentation(null);
		}
		else if(fileHelper.isImageExtension(fileExtension))
		{
			startImagePresentation(null);
		}
	}
	
	static final int REQUEST_CODE = 1234;
	static final String CHOOSER_TITLE = "Select a file";
	public void sendFile(View view) {		
		 Intent target = FileUtils.createGetContentIntent();
		 Intent intent = Intent.createChooser(target, CHOOSER_TITLE);
		    try {
		        startActivityForResult(intent, REQUEST_CODE);
		    } catch (ActivityNotFoundException e) {
		        // The reason for the existence of aFileChooser
		    }
	}
	
	public void updateState(View view)
	{
		this.updateList();
		this.verifyIfHasPresentationRunning();	
	}
	
	public void verifyIfHasPresentationRunning()
	{
		final PresentationClient client = this.presentationClient;
		final ChooseFileActivity activity = this;
		AsyncTask<String, Void, GeneralPresentationState> task = new AsyncTask<String, Void, GeneralPresentationState>()
				{
			ImagePresentationInfo imageInfo;
			SlidePresentationInfo slidesInfo;

			@Override
			protected GeneralPresentationState doInBackground(String... params) {						
				imageInfo = client.getImagePresentationInfo();
				slidesInfo = client.getSlidePresentationInfo();
				return new GeneralPresentationState(imageInfo, slidesInfo);
			}

			@Override
			protected void onPostExecute(GeneralPresentationState presentationState) {
				
				if(presentationState.getImageInfo() == null || presentationState.getSlidesInfo() == null) return;
				
				//If returns an file name has an image running
				if(presentationState.getImageInfo().getFileName() != null && !presentationState.getImageInfo().getFileName().equals(""))
				{
					initializer.startImagePresentationActivity(activity, serverUrl, presentationState.getImageInfo().getFileName(), true);
				}
				else if(presentationState.getSlidesInfo().getFileName() != null && !presentationState.getSlidesInfo().getFileName().equals(""))
				{
					initializer.startPresentationSlidesActivity(activity, serverUrl, true);
				}
			}

			@Override
			protected void onPreExecute() {
			}
		};
		task.execute();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    switch (requestCode) {
	    case REQUEST_CODE:  
	        if (resultCode == RESULT_OK) {  
	            // The URI of the selected file 
	            final Uri uri = data.getData();
				final File file = FileUtils.getFile(uri);
				final ChooseFileActivity thisActivity = this;
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
}
