package com.luciocossio.classclient.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.luciocossio.classclient.R;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.activities.image.PresentationImageActivity;
import com.luciocossio.classclient.activities.image.PresentationSlidesActivity;
import com.luciocossio.classclient.async.AsyncTaskList;
import com.luciocossio.classclient.async.PresentationAsyncTask;

import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ChooseFileActivity extends PresentationListActivity implements OnSharedPreferenceChangeListener, OnItemClickListener {
	
	SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Setup preferences
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		serverUrl = getServerAddressFromPreferences();
		if(serverUrl != null)
		{
			initializePresentationClient();
			this.updateList();
		}
	}
	
	@Override
	public void onAttachedToWindow() {
	    //openOptionsMenu(); 
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// TODO Auto-generated method stub
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
					startPresentationSlidesActivity(thisPanel);
				}
			}

			protected void startPresentationSlidesActivity(final Activity thisPanel) {
				Intent intent = new Intent(thisPanel, PresentationSlidesActivity.class);	
				intent.putExtra(CommonVariables.ServerAddress, serverUrl);
				startActivity(intent);
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
				//return client.openImage(lastFilename);
				return new ResultMessage("", true);
			}
			
			@Override
			protected void onEndPostExecute(ResultMessage result)
			{
				if(result.getWasSuccessful())
				{
					Intent intent = new Intent(thisPanel, PresentationImageActivity.class);	
					intent.putExtra(CommonVariables.ServerAddress, serverUrl);
					intent.putExtra(CommonVariables.FileName, lastFilename);
					startActivity(intent);
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
		
		String fileExtension = GetFileExt(lastFilename);
		
		if(isPowerPointExtension(fileExtension))			
		{
			startSlidesPresentation(null);
		}
		else if(isImageExtension(fileExtension))
		{
			startImagePresentation(null);
		}
	}
	
	public boolean isImageExtension(String extension)
	{
		boolean isImage = false;
		
		if(extension.equals("jpg") || extension.equals("png") || extension.equals("jpeg"))
		{
			return true;
		}
		
		return isImage;	
	}	

	public boolean isPowerPointExtension(String extension)
	{
		boolean isPowerPoint = false;
		
		if(extension.equals("ppt") || extension.equals("pptx"))
		{
			return true;			
		}
		
		return isPowerPoint;	
	}
	
	public String GetFileExt(String FileName)
    {       
         String ext = FileName.substring((FileName.lastIndexOf(".") + 1), FileName.length());
         return ext;
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
