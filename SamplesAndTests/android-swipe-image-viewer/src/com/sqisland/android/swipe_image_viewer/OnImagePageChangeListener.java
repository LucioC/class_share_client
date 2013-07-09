package com.sqisland.android.swipe_image_viewer;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

public class OnImagePageChangeListener implements OnPageChangeListener {

	@Override
	public void onPageScrollStateChanged(int arg0) {
		Log.i("ChangeListener", "ScrollStateChanged " + arg0);

		switch(arg0)
		{
			case ViewPager.SCROLL_STATE_DRAGGING:
				Log.i("ChangeListener", "ScrollStateChanged dragging");
				break;
			case ViewPager.SCROLL_STATE_IDLE:
				Log.i("ChangeListener", "ScrollStateChanged idle");
				break;
			case ViewPager.SCROLL_STATE_SETTLING:
				Log.i("ChangeListener", "ScrollStateChanged settling");
				break;
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		//Log.i("ChangeListener", "onPageScrolled offset" + arg1);
	}

	@Override
	public void onPageSelected(int arg0) {
		Log.i("ChangeListener", "onPageSelected " + arg0);
	}

}
