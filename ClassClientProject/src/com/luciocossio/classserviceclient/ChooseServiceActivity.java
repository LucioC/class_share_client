package com.luciocossio.classserviceclient;

import java.io.Console;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

public class ChooseServiceActivity extends Activity implements OnSharedPreferenceChangeListener {
	
	SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Setup preferences
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_choose_service);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
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
	
	public void openPowerPointActivity(View view)
	{
		String serverUrl;
		serverUrl = "http://";
		serverUrl+= prefs.getString("server", "10.1.1.4");
		serverUrl+= ":";
		serverUrl+= prefs.getString("port", "8880");
		serverUrl+= "/";
		
		Intent intent = new Intent(this, PowerPointPresentationActivity.class);	
		Log.d("DEBUG", serverUrl);
		intent.putExtra(CommonVariables.ServerAddress, serverUrl);
		startActivity(intent);
	}
	
	public void openImagePresentationActivity(View view)
	{
		String serverUrl;
		serverUrl = "http://";
		serverUrl+= prefs.getString("server", "10.1.1.4");
		serverUrl+= ":";
		serverUrl+= prefs.getString("port", "8880");
		serverUrl+= "/";
		
		Log.d("DEBUG", serverUrl);
		
		Intent intent = new Intent(this, ImagePresentationActivity.class);	
		intent.putExtra(CommonVariables.ServerAddress, serverUrl);
		startActivity(intent);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

}
