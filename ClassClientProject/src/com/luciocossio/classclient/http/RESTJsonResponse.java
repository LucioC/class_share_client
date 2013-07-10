package com.luciocossio.classclient.http;

public class RESTJsonResponse {
	
	private int httpStatus;
	private String jsonContent;
	
	public RESTJsonResponse(int httpStatus, String jsonContent)
	{
		this.httpStatus = httpStatus;
		this.jsonContent = jsonContent;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public String getJsonContent() {
		return jsonContent;
	}

}
