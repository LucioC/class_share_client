package com.luciocossio.classclient.activities.image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.R;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.activities.CommonVariables;
import com.luciocossio.classclient.activities.listeners.FlingAndTouchPresentationListener;
import com.luciocossio.classclient.async.AsyncTaskList;
import com.luciocossio.classclient.async.PresentationAsyncTask;
import com.luciocossio.classclient.http.RESTApacheClient;
import com.luciocossio.classclient.http.RESTJsonClient;

public class ImageGallery extends Activity {

	protected PresentationClient client;	
	protected ProgressDialog dialog;
	protected String serverUrl;
	
	ImagePagerAdapter adapter;
	OnPresentationImagePageChangeListener pageListener;
	FlingAndTouchPresentationListener flingListener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_image_gallery);

		List<String> list = new ArrayList<String>();
		
		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
		adapter = new ImagePagerAdapter(list);
		viewPager.setAdapter(adapter);	
		
		Intent intent = getIntent();		
		serverUrl = intent.getStringExtra(CommonVariables.ServerAddress);	
		dialog = new ProgressDialog(this);
		initializePresentationClient();

		pageListener = new OnPresentationImagePageChangeListener(client, dialog);
		viewPager.setOnPageChangeListener(pageListener);

		final Activity thisPanel = this;
		flingListener = new FlingAndTouchPresentationListener(client, dialog, this)
		{
			@Override
			public void closed()
			{
				thisPanel.finish();
			}				

			@Override
			public void started()
			{
				pageListener.setCallServer(true);
			}
		};

		this.getImages();
	}

	private void initializePresentationClient()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		client = new PresentationClient(jsonClient, serverUrl);
	}	
	
	public void setImageUrl(List<String> imageUrls)
	{
		adapter.setImages(imageUrls);
		adapter.notifyDataSetChanged();
	}
	
	public void getImages()
	{
		final ImageGallery gallery = this;
		AsyncTaskList task = new AsyncTaskList(client, dialog, "Carregando slides da apresentação...")
		{
			
			@Override
			protected void onPostExecute(List<String> images)
			{	
				super.onPostExecute(images);
				gallery.setImageUrl(images);
			}
			
			@Override
			protected List<String> executeTask()
			{				
				List<String> imageNames = client.getCurrentPresentationImageNames();
				List<String> filesPath = new ArrayList<String>();
				
				for(String imageName : imageNames)
				{
					InputStream stream = null;
					try {
						stream = client.getImage(imageName);
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

					String filename = imageName;
					
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
				}

				return filesPath;
			}			
		};
		task.execute();
	}
	
	private class ImagePagerAdapter extends PagerAdapter {
		
		private Uri[] mImages = new Uri[]{};
				
		public ImagePagerAdapter(List<String> images)
		{
			this.setImages(images);
		}
		
		public void setImages(List<String> images)
		{
			mImages = new Uri[images.size()];
			for(int i=0; i<images.size();i++)
			{
				mImages[i] = Uri.parse(images.get(i));
			}
		}
		
		@Override
		public int getCount() {
			return mImages.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((ImageView) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {		
			Context context = ImageGallery.this;
			PresentationImageView imageView = new PresentationImageView(context, flingListener);
			int padding = context.getResources().getDimensionPixelSize(
					R.dimen.padding_medium);
			imageView.setPadding(padding, padding, padding, padding);
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			imageView.setImageURI(mImages[position]);
			((ViewPager) container).addView(imageView, 0);
			
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}
	}
}