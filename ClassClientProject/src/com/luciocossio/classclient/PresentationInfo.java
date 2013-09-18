package com.luciocossio.classclient;

public class PresentationInfo {
	
    private int slidesNumber;
    private int currentSlide;
    private String fileName;
    
	public int getSlidesNumber() {
		return slidesNumber;
	}
	public void setSlidesNumber(int slidesNumber) {
		this.slidesNumber = slidesNumber;
	}
	public int getCurrentSlide() {
		return currentSlide;
	}
	public void setCurrentSlide(int currentSlide) {
		this.currentSlide = currentSlide;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
