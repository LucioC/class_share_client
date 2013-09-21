package com.luciocossio.classclient.http.server;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.async.PresentationAsyncTask;

import android.content.Context;
import android.content.Intent;

public class RegisterForServerUpdates {

	public static void startServiceForImageUpdate(Context context, PresentationClient client)
	{
		Intent intent = new Intent(context, HTTPService.class);		
		context.startService(intent);
		
		PresentationAsyncTask task = new PresentationAsyncTask(client, null) {

			@Override
			protected ResultMessage executeTask() {
				return client.addListenerForImagePresentation();
			}
			
			@Override
			protected void onEndPostExecute(ResultMessage result) {
				
			}
			
		};
		task.execute();
	}	

	public static void startServiceForSlidesUpdate(Context context, PresentationClient client)
	{
		Intent intent = new Intent(context, HTTPService.class);		
		
		context.startService(intent);
		PresentationAsyncTask task = new PresentationAsyncTask(client, null) {

			@Override
			protected ResultMessage executeTask() {
				return client.addListenerForSlidesPresentation();
			}
			
			@Override
			protected void onEndPostExecute(ResultMessage result) {
				
			}
			
		};
		task.execute();
	}
	
	public static void stopService(Context context, PresentationClient client)
	{
		Intent intent = new Intent(context, HTTPService.class);
		//context.stopService(intent);
		PresentationAsyncTask task = new PresentationAsyncTask(client, null) {

			@Override
			protected ResultMessage executeTask() {
				return client.deleteListener();
			}
			
			@Override
			protected void onEndPostExecute(ResultMessage result) {
				
			}
			
		};
		task.execute();		
	}

}
