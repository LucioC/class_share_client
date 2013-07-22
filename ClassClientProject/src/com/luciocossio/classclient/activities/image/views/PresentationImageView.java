package com.luciocossio.classclient.activities.image.views;

import android.content.Context;
import android.widget.ImageView;

public class PresentationImageView extends ImageView {

	
	public PresentationImageView(Context context) {
		super(context);
	}
		
	public void setPositionY(float y)
	{
		this.setY(y);
		invalidate();
	}	

}
