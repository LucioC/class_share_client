package com.luciocossio.classclient.activities.image;

public interface ImageMoveZoomPanListener {

	void updateVisiblePart(int left, int top, int right, int bottom);
	void rotate(float angle);
}
