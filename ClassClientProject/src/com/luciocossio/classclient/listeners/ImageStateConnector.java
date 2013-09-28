package com.luciocossio.classclient.listeners;

public interface ImageStateConnector {

	void updateVisiblePart(int left, int top, int right, int bottom, int imageHeight, int imageWidth);
	void setImageAngle(int imageAngle);
	
}
