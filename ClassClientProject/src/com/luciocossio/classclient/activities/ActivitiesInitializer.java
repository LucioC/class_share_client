package com.luciocossio.classclient.activities;

import android.app.Activity;
import android.content.Intent;

import com.luciocossio.classclient.activities.image.PresentationImageActivity;
import com.luciocossio.classclient.activities.image.PresentationSlidesActivity;

public class ActivitiesInitializer {

	public void startImagePresentationActivity(
			final Activity thisPanel, String serverUrl, String fileName, boolean started) {
		Intent intent = new Intent(thisPanel, PresentationImageActivity.class);	
		intent.putExtra(CommonVariables.ServerAddress, serverUrl);
		intent.putExtra(CommonVariables.FileName, fileName);
		intent.putExtra(CommonVariables.RunningState, started);
		thisPanel.startActivity(intent);
	}

	public void startPresentationSlidesActivity(final Activity thisPanel, String serverUrl, boolean started) {
		Intent intent = new Intent(thisPanel, PresentationSlidesActivity.class);	
		intent.putExtra(CommonVariables.ServerAddress, serverUrl);
		intent.putExtra(CommonVariables.RunningState, started);
		thisPanel.startActivity(intent);
	}
}
