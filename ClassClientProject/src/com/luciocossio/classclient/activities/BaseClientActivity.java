package com.luciocossio.classclient.activities;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.http.RESTApacheClient;
import com.luciocossio.classclient.http.RESTJsonClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class BaseClientActivity extends Activity {

	protected ProgressDialog dialog;
	protected PresentationClient presentationClient;
	protected String serverUrl;

	public BaseClientActivity() {
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		Intent intent = getIntent();		
		serverUrl = intent.getStringExtra(CommonVariables.ServerAddress);
		
		initializePresentationClient();
	
		dialog = new ProgressDialog(this);
	}

	protected void initializePresentationClient() {
		RESTJsonClient jsonClient = new RESTApacheClient();
		presentationClient = new PresentationClient(jsonClient, serverUrl);
	}

}