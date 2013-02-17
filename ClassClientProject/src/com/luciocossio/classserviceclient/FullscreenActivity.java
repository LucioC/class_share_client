package com.luciocossio.classserviceclient;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.RESTApacheClient;
import com.luciocossio.classclient.RESTJsonClient;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classserviceclient.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;
	
	private ProgressDialog dialog;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	
	private PresentationAsyncTask presentationTask;
	
	private final String START_PRESENTATION = "START";
	private final String NEXT_SLIDE = "NEXT";
	private final String PREVIOUS_SLIDE = "PREVIOUS";
	private final String CLOSE_PRESENTATION = "CLOSE";
	private final String SEND_FILE = "SENDFILE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fullscreen);

		//final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.content_layout);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
						} else {
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});
		
		//FIXME hardcoded for test purpose
		serverUrl = "http://10.1.1.5:8880/";
		presentationFilename = "C:/Users/lucioc/Dropbox/Public/Mestrado/Dissertacao/PEP/PEP_posM.pptx";
		InitializePresentationClient();		

		dialog = new ProgressDialog(this);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
	
	private PresentationClient presentationClient;
	private String serverUrl;
	private String presentationFilename;
	
	private void InitializePresentationClient()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		presentationClient = new PresentationClient(jsonClient, serverUrl);
	}
	

	public void sendFile(View view)
	{
		PresentationAsyncTask task = new PresentationAsyncTask();
		task.execute(SEND_FILE, "sdcard/DCIM/MISC/presentation.pptx");
	}

	
	public void startPresentation(View view)
	{
		PresentationAsyncTask task = new PresentationAsyncTask();
		task.execute(START_PRESENTATION, presentationFilename);
	}

	public void goToNextSlide(View view)
	{
		PresentationAsyncTask task = new PresentationAsyncTask();
		task.execute(NEXT_SLIDE);
	}
	
	public void goToPreviosuSlide(View view)
	{
		PresentationAsyncTask task = new PresentationAsyncTask();
		task.execute(PREVIOUS_SLIDE);
	}
	
	public void closePresentation(View view)
	{
		PresentationAsyncTask task = new PresentationAsyncTask();
		task.execute(CLOSE_PRESENTATION);
	}
	
	private class PresentationAsyncTask extends AsyncTask<String, Void, ResultMessage> {
		
		@Override
		protected void onPreExecute() {
			
			dialog.setMessage("Enviando requisição para o servidor...");			
			dialog.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					if (presentationTask != null) {
						presentationTask.cancel(true);
					}
				}
			});
			
			dialog.show();
		}
		
		@Override
		protected ResultMessage doInBackground(String... params) {
			ResultMessage resultMessage = null;

			Log.i("BackGroundCall",params[0]);

			if (params[0].equals(START_PRESENTATION))
			{
				resultMessage = presentationClient.startPresentation(params[1]);

				if(resultMessage == null) 
				{
					Log.e("BackGroundCall","returning a null result");
				}
				else
				{
					Log.i("BackGroundCall","result returned ok from function");
				}
			}
			if (params[0].equals(NEXT_SLIDE))
			{
				resultMessage = presentationClient.nextSlide();
			}
			if (params[0].equals(PREVIOUS_SLIDE))
			{
				resultMessage = presentationClient.previousSlide();
			}
			if (params[0].equals(CLOSE_PRESENTATION))
			{
				resultMessage = presentationClient.closePresentation();
			}
			if (params[0].equals(SEND_FILE))
			{
				resultMessage = presentationClient.uploadFile(params[1], "presentation.pptx");
			}

			return resultMessage;
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
			dialog.dismiss();
		}
	}
	
}
