package com.luciocossio.classserviceclient;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;

public class ChooseServiceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_choose_service);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_choose_service, menu);
		return true;
	}
	
	public void openPowerPointActivity(View view)
	{
		Intent intent = new Intent(this, PowerPointPresentationActivity.class);	
		startActivity(intent);
	}
	
	public void openImagePresentationActivity(View view)
	{
		Intent intent = new Intent(this, ImagePresentationActivity.class);	
		startActivity(intent);
	}

}
