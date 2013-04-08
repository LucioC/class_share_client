package com.luciocossio.classserviceclient;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;
import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.ResultMessage;

public class PresentationAsyncTask extends AsyncTask<String, Void, ResultMessage> {
	
	protected PresentationClient client;
	protected ProgressDialog dialog;
	protected ResultMessage result = null;
	
	public PresentationAsyncTask(PresentationClient client, ProgressDialog dialog )
	{
		this.client = client;
		this.dialog = dialog;
	}
	
	@Override
	protected void onPreExecute() {

		final PresentationAsyncTask task = this;

		if(dialog != null)
		{
			dialog.setMessage("Enviando requisição para o servidor...");			
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
	protected ResultMessage ExecuteTask()
	{
		return null;
	}
	
	@Override
	protected ResultMessage doInBackground(String... params) {
				
		result = ExecuteTask();
		return result;
	}
	
	@Override
	protected void onPostExecute(ResultMessage result) {
		if(result == null) 
		{
			Log.e("PostExecute","result was null");
		}
		else
		{
			Log.i("PostExecute",result.getMessage());
		}
		if(dialog != null)
		{
			dialog.dismiss();
		}
		OnEndPostExecute(result);
	}
	
	//Override this to aditional calls
	protected void OnEndPostExecute(ResultMessage result)
	{
		
	}
}
