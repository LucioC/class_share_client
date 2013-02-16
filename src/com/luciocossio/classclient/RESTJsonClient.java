package com.luciocossio.classclient;

import java.io.File;
import java.util.Map;

public interface RESTJsonClient {
	
	public RESTJsonResponse doGet(String location, Map<String, String> queryParameters);
	
	public RESTJsonResponse doPost(String location, String jsonContent);
	
	public RESTJsonResponse doDelete(String location);
	
	public RESTJsonResponse doPut(String location, String jsonContent);

	public RESTJsonResponse doPostFile(String location, File file, String fileMimetype);

}

