package com.luciocossio.classclient.activities.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.luciocossio.classclient.R;
import com.luciocossio.classclient.activities.BaseClientActivity;import com.luciocossio.classclient.activities.image.views.TouchImageView;
import com.luciocossio.classclient.async.AsyncTaskList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class PresentationImageActivity extends BaseClientActivity {

	protected String serverUrl;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_presentation);
		this.getImage();
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
				InputStream inputStream = null;
				try {
					inputStream = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				TouchImageView imageView = activity.getImageView();
				
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				imageView.setImageBitmap(bitmap);
				imageView.setMaxZoom(4f);			
				
				//imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				//imageView.setImageURI(Uri.parse(images.get(0)));
			}

			@Override
			protected List<String> executeTask()
			{				
				List<String> filesPath = new ArrayList<String>();

				InputStream stream = null;
				try {
					stream = client.getCurrentImage();
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
		

}