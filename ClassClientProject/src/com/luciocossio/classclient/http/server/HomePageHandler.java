package com.luciocossio.classclient.http.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class HomePageHandler implements HttpRequestHandler {
	private Context context = null;

	private LocalBroadcastManager broadcast = null;
	public HomePageHandler(Context context){
		this.context = context;
		broadcast = LocalBroadcastManager.getInstance(context);
	}

	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext httpContext) throws HttpException, IOException {
		String contentType = "text/html";
		HttpEntity entity = new StringEntity("{\"message\":\"home\"}");
		
		((StringEntity)entity).setContentType(contentType);
		
		response.setEntity(entity);
		
		sendResult("");
	}
	
	
	public void sendResult(String message) {
	    Intent intent = new Intent(HTTPService.NEW_REQUEST);
	    if(message != null)
	        intent.putExtra(HTTPService.NEW_REQUEST, message);
	    broadcast.sendBroadcast(intent);
	}

}
