package com.luciocossio.classclient.listeners;

public interface ImageMoveZoomPanListener {

	void updateVisiblePart(int left, int top, int right, int bottom, int imageHeight, int imageWidth);
	void rotate(float angle);
}
