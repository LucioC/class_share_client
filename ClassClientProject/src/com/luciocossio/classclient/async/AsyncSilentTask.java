package com.luciocossio.classclient.async;

import android.os.AsyncTask;
import android.util.Log;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.ResultMessage;

public class AsyncSilentTask extends AsyncTask<String, Void, ResultMessage> {
	
	protected PresentationClient client;
	protected ResultMessage result = null;
	
	public AsyncSilentTask(PresentationClient client )
	{
		this.client = client;
	}
		
	//Should override this for specific task execution
	protected ResultMessage executeTask()
	{
		return null;
	}
	
	@Override
	protected ResultMessage doInBackground(String... params) {
				
		result = executeTask();
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
		onEndPostExecute(result);
	}
	
	//Override this to aditional calls
	protected void onEndPostExecute(ResultMessage result)
	{
		
	}
}
