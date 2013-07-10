package com.luciocossio.classclient.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

public interface RESTJsonClient {
	
	public RESTJsonResponse doGet(String location, Map<String, String> queryParameters);
	
	public InputStream doGetFile(String location) throws ClientProtocolException, IOException;
	
	public RESTJsonResponse doPost(String location, String jsonContent);
	
	public RESTJsonResponse doDelete(String location);
	
	public RESTJsonResponse doPut(String location, String jsonContent);

	public RESTJsonResponse doPostFile(String location, File file, String fileMimetype);
	public RESTJsonResponse doPutFile(String location, File file, String fileMimetype);

}

