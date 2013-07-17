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
import android.graphics.BitmapFactory.Options;
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
				
				String filePath = file.getAbsolutePath();
				TouchImageView imageView = activity.getImageView();
				
				Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
			    BitmapFactory.decodeFile(filePath, options);
			    
			    options.inSampleSize = calculateInSampleSize(options, 1024, 768);

			    options.inJustDecodeBounds = false;			    
				Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
				
				imageView.setImageBitmap(bitmap);
				imageView.setMaxZoom(6f);		
				
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
        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
    }

    return inSampleSize;
}
		

}