package com.luciocossio.classclient.model;



public class GeneralPresentationState {
	
	private ImagePresentationInfo imageInfo;
	private SlidePresentationInfo slidesInfo;
	
	public GeneralPresentationState(ImagePresentationInfo imageInfo, SlidePresentationInfo slidesInfo)
	{
		this.imageInfo = imageInfo;
		this.slidesInfo = slidesInfo;
	}
	
	public ImagePresentationInfo getImageInfo() {
		return imageInfo;
	}
	public void setImageInfo(ImagePresentationInfo imageInfo) {
		this.imageInfo = imageInfo;
	}
	public SlidePresentationInfo getSlidesInfo() {
		return slidesInfo;
	}
	public void setSlidesInfo(SlidePresentationInfo slidesInfo) {
		this.slidesInfo = slidesInfo;
	}

}
