package com.luciocossio.classclient.async;

import java.util.List;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import com.luciocossio.classclient.PresentationClient;

public class AsyncTaskList extends AsyncTask<String, Void, List<String>> {
	
	protected PresentationClient client;
	protected ProgressDialog dialog;
	protected List<String> result = null;
	protected String message;
	
	public AsyncTaskList(PresentationClient client, ProgressDialog dialog, String message )
	{
		this.client = client;
		this.dialog = dialog;
		this.message = message;
	}
	
	@Override
	protected void onPreExecute() {

		final AsyncTaskList task = this;

		if(dialog != null)
		{
			dialog.setMessage(this.message);			
			dialog.setOnCancelListener(new OnCancelListener() {			
				@Override
				public void onCancel(DialogInterface dialog) {
					task.cancel(true);
				}
			});

			dialog.show();
		}
	}
	
	//Should override this for specific task execution
	protected List<String> executeTask()
	{
		return null;
	}
	
	@Override
	protected List<String> doInBackground(String... params) {
				
		result = executeTask();
		return result;
	}
	
	@Override
	protected void onPostExecute(List<String> result) {
		dialog.cancel();
	}
}
