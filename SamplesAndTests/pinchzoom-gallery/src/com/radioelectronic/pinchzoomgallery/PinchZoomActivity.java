package com.radioelectronic.pinchzoomgallery;

import com.radioelectronic.pinchzoomgallery.adapter.ImageAdapter;
import com.radioelectronic.pinchzoomgallery.widget.PinchZoomGallery;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author Artyom Kiriliyk
 */
public class PinchZoomActivity extends Activity
{
	private PinchZoomGallery gallery;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		gallery = (PinchZoomGallery) findViewById(R.id.gallery);
		gallery.setSpacing(15);
		gallery.setAdapter(new ImageAdapter(this));
		gallery.setSelection(1);
	}
}