package com.luciocossio.classserviceclient.test.units;

import com.luciocossio.gestures.FlingDirectionIdentifier;
import com.luciocossio.gestures.Gestures;

import android.view.MotionEvent;
import junit.framework.TestCase;

public class FlingDirectionTest extends TestCase {

	public void testOnFlingReturnRightDirection() {
		MotionEvent e1 = MotionEvent.obtain(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		MotionEvent e2 = MotionEvent.obtain(2, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1);
		
		FlingDirectionIdentifier flingDirection = new FlingDirectionIdentifier();
		
		String expected = Gestures.FLING_RIGHT;
		String actual = flingDirection.onFlingReturnDirection(e1, e2);
		
		assertEquals(expected, actual);
	}
	
	public void testOnFlingReturnLeftDirection() {
		MotionEvent e1 = MotionEvent.obtain(2, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1);
		MotionEvent e2 = MotionEvent.obtain(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		
		FlingDirectionIdentifier flingDirection = new FlingDirectionIdentifier();
		
		String expected = Gestures.FLING_LEFT;
		String actual = flingDirection.onFlingReturnDirection(e1, e2);
		
		assertEquals(expected, actual);
	}
	
	public void testOnFlingReturnDownDirection() {
		MotionEvent e1 = MotionEvent.obtain(2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		MotionEvent e2 = MotionEvent.obtain(1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1);
		
		FlingDirectionIdentifier flingDirection = new FlingDirectionIdentifier();
		
		String expected = Gestures.FLING_DOWN;
		String actual = flingDirection.onFlingReturnDirection(e1, e2);
		
		assertEquals(expected, actual);
	}
	
	public void testOnFlingReturnUpDirection() {
		MotionEvent e1 = MotionEvent.obtain(2, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1);
		MotionEvent e2 = MotionEvent.obtain(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		
		FlingDirectionIdentifier flingDirection = new FlingDirectionIdentifier();
		
		String expected = Gestures.FLING_UP;
		String actual = flingDirection.onFlingReturnDirection(e1, e2);
		
		assertEquals(expected, actual);
	}
}
