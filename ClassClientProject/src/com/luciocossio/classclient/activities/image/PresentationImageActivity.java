package com.luciocossio.classclient.activities.image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.luciocossio.classclient.ImagePresentationInfo;
import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.SlidePresentationInfo;
import com.luciocossio.classclient.R;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.activities.BaseClientActivity;import com.luciocossio.classclient.activities.CommonVariables;
import com.luciocossio.classclient.activities.image.views.TouchImageView;
import com.luciocossio.classclient.async.AsyncTaskList;
import com.luciocossio.classclient.async.PresentationAsyncTask;
import com.luciocossio.classclient.http.server.RegisterForServerUpdates;
import com.luciocossio.classclient.http.server.HTTPService;
import com.luciocossio.classclient.listeners.impl.FlingTouchImageListener;
import com.luciocossio.classclient.listeners.impl.ImageMoveZoomPanConnector;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;

public class PresentationImageActivity extends BaseClientActivity {

	protected String serverUrl;
	ImageMoveZoomPanConnector listener = null;
	private GestureDetectorCompat simpleGesturesDetector; 
	
	protected String imageName = "";
	
	protected boolean hasStarted = false;
	
	FlingTouchImageListener flingListener;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_presentation);
		
		listener = new ImageMoveZoomPanConnector(this.presentationClient, 20, 150);
		TouchImageView imageView = getImageView();
		imageView.setListener(listener);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
		Intent intent = getIntent();		
		imageName = intent.getStringExtra(CommonVariables.FileName);		
		
		registerTouchListener();
		flingListener.setImageName(imageName);
		
		this.getImage();
		
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				getPresentationInfoFromServer();
			}
		};
		RegisterForServerUpdates.startServiceForImageUpdate(this, presentationClient);
	}
	
	public void getPresentationInfoFromServer()
	{			
		final PresentationImageActivity activity = this;
		final PresentationClient client = this.presentationClient;
		AsyncTask<String, Void, ImagePresentationInfo> task = new AsyncTask<String, Void, ImagePresentationInfo>()
		{
			ImagePresentationInfo result;

			@Override
			protected ImagePresentationInfo doInBackground(String... params) {						
				result = client.getImagePresentationInfo();
				return result;
			}
			
			@Override
			protected void onPostExecute(ImagePresentationInfo result) {
				activity.updatePresentationState(result);
			}
			
			@Override
			protected void onPreExecute() {
			}
		};
		task.execute();
	}
	
	public void updatePresentationState(ImagePresentationInfo presentationInfo)
	{
		Log.i("PresentationImageActivity", "should udpate image state");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		RegisterForServerUpdates.stopService(this, presentationClient);
	}
	
	private BroadcastReceiver receiver;
	@Override
	protected void onStart() {
	    super.onStart();
	    LocalBroadcastManager.getInstance(this).registerReceiver((receiver), new IntentFilter(HTTPService.NEW_REQUEST));
	}

	@Override
	protected void onStop() {
	    LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
	    super.onStop();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_control_image_presentation, menu);
	    return true;
	}

	protected void registerTouchListener() {
		final PresentationImageActivity thisPanel = this;
		flingListener = new FlingTouchImageListener(presentationClient, dialog, this, getImageView())
		{			
			@Override
			public void closed()
			{
				thisPanel.finish();
			}			
		};		
		final TouchImageView imageView = getImageView();
		simpleGesturesDetector = createSimpleGestureDetector(imageView);
		getImageView().registerListener(simpleGesturesDetector);
	}

	protected GestureDetectorCompat createSimpleGestureDetector(final TouchImageView imageView) {
		return new GestureDetectorCompat(this, flingListener)
		{
			PointF last = null;
			boolean flingStartedWithoutZoom = false;
			@Override
			public boolean onTouchEvent(MotionEvent event) {

				int pointerCount = event.getPointerCount();

				if(pointerCount == 1 && imageView.getImageScale() <= 1)
				{
					switch (event.getAction())
					{
					case MotionEvent.ACTION_DOWN:
						flingStartedWithoutZoom = true;
						last = new PointF(event.getX(), event.getY());
						break;

					case MotionEvent.ACTION_UP:
						if(flingStartedWithoutZoom)
						{
							flingListener.onFingerUpForFlingStartStop();
							flingStartedWithoutZoom = false;
						}
						break;

					case MotionEvent.ACTION_MOVE:						
						float curX = event.getX();
						float curY = event.getY();
						PointF current = new PointF(curX, curY);
						flingListener.updatePositionY(last, current);
						last = current;
						break;
					}
				}
				else
				{
					flingStartedWithoutZoom = false;
				}
				return super.onTouchEvent(event);
			}
		};
	}
	
	public TouchImageView getImageView()
	{
		TouchImageView imageView = (TouchImageView)findViewById(R.id.current_image);
		return imageView;
	}
	
	public void getImage()
	{
		final PresentationImageActivity activity = this;
		AsyncTaskList task = new AsyncTaskList(presentationClient, dialog, "Carregando imagem...")
		{			
			@Override
			protected void onPostExecute(List<String> images)
			{	
				super.onPostExecute(images);
				
				File file = new File(getFilesDir(), "image");
				
				String filePath = file.getAbsolutePath();
				TouchImageView imageView = activity.getImageView();
				
				Options options = new BitmapFactory.Options();
				
				options.inJustDecodeBounds = true;
			    BitmapFactory.decodeFile(filePath, options);
			    
			    options.inSampleSize = calculateInSampleSize(options, 1024, 768);
			    
			    options.inJustDecodeBounds = false;
				Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
				
				imageView.setImageBitmap(bitmap);
				imageView.setMaxZoom(15f);
			}

			@Override
			protected List<String> executeTask()
			{				
				List<String> filesPath = new ArrayList<String>();

				InputStream stream = null;
				try {
					stream = client.getFile(imageName);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				String filename = "image";

				File file = new File(getFilesDir(), filename);
				FileOutputStream outputStream;

				try {
					outputStream = new FileOutputStream(file);
					byte buf[]=new byte[40960];
					int len;
					while((len=stream.read(buf))>0)
					{
						outputStream.write(buf,0,len);
					}
					outputStream.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				filesPath.add(file.getAbsolutePath());

				return filesPath;
			}			
		};
		task.execute();
	}
	
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		closeImage();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_close_image:
	        	closeImage();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	protected void closeImage()
	{
		final Activity activity = this;
		PresentationAsyncTask task = new PresentationAsyncTask(presentationClient, dialog)
		{			
			@Override
			protected ResultMessage executeTask() {
				return client.closeImage();
			}
			
			@Override
			protected void onEndPostExecute(ResultMessage result) {
				super.onEndPostExecute(result);
				activity.finish();
			}
		};
		task.execute();
	}
	
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        // Calculate ratios of height and width to requested height and width
        final int heightRatio = Math.round((float) height / (float) reqHeight);
        final int widthRatio = Math.round((float) width / (float) reqWidth);

        // Choose the smallest ratio as inSampleSize value, this will guarantee
        // a final image with both dimensions larger than or equal to the
        // requested height and width.
        inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
    }

    return inSampleSize;
}
		

}