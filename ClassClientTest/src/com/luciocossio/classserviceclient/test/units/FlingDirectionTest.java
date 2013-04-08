package com.luciocossio.classserviceclient.test.units;

import com.luciocossio.gestures.FlingDirectionIdentifier;

import android.view.MotionEvent;
import junit.framework.TestCase;

public class FlingDirectionTest extends TestCase {

	public void testOnFlingReturnRightDirection() {
		MotionEvent e1 = MotionEvent.obtain(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		MotionEvent e2 = MotionEvent.obtain(2, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1);
		
		FlingDirectionIdentifier flingDirection = new FlingDirectionIdentifier();
		
		String expected = flingDirection.RIGHT;
		String actual = flingDirection.onFlingReturnDirection(e1, e2);
		
		assertEquals(expected, actual);
	}
	
	public void testOnFlingReturnLeftDirection() {
		MotionEvent e1 = MotionEvent.obtain(2, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1);
		MotionEvent e2 = MotionEvent.obtain(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		
		FlingDirectionIdentifier flingDirection = new FlingDirectionIdentifier();
		
		String expected = flingDirection.LEFT;
		String actual = flingDirection.onFlingReturnDirection(e1, e2);
		
		assertEquals(expected, actual);
	}
	
	public void testOnFlingReturnDownDirection() {
		MotionEvent e1 = MotionEvent.obtain(2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		MotionEvent e2 = MotionEvent.obtain(1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1);
		
		FlingDirectionIdentifier flingDirection = new FlingDirectionIdentifier();
		
		String expected = flingDirection.DOWN;
		String actual = flingDirection.onFlingReturnDirection(e1, e2);
		
		assertEquals(expected, actual);
	}
	
	public void testOnFlingReturnUpDirection() {
		MotionEvent e1 = MotionEvent.obtain(2, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1);
		MotionEvent e2 = MotionEvent.obtain(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		
		FlingDirectionIdentifier flingDirection = new FlingDirectionIdentifier();
		
		String expected = flingDirection.UP;
		String actual = flingDirection.onFlingReturnDirection(e1, e2);
		
		assertEquals(expected, actual);
	}
}
