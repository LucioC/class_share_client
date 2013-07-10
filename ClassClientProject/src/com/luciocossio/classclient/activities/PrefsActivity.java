package com.luciocossio.classclient.activities;

import android.os.Bundle;
import com.luciocossio.classclient.R;
import android.preference.PreferenceActivity;

public class PrefsActivity extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
	}
}
