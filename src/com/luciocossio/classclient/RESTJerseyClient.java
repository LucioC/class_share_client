package com.luciocossio.classclient;

import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import android.util.Log;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * 
 * Class that handles Json to Json communication with a REST server, returning Json Strings back to client.
 * 
 * @author Lucio P. Cossio
 *
 */
public class RESTJerseyClient implements RESTJsonClient {
	
	private Client client;

	public RESTJerseyClient()
	{
		client = Client.create();
	}
	
	public String addQueryParametersToUrl(String url, Map<String, String> queryParameters)
	{		
		StringBuilder newUrl = new StringBuilder();
		newUrl.append(url);

		if (queryParameters != null && queryParameters.size() > 0)
		{
			newUrl.append("?");
			Set<String> keys = queryParameters.keySet();
			for(String key : keys)
			{
				String value = queryParameters.get(key);
				newUrl.append(key);
				newUrl.append("=");
				newUrl.append(value);				
				newUrl.append("&");
			}
			newUrl.deleteCharAt(newUrl.length()-1);
		}
		return newUrl.toString();		
	}

	@Override
	public RESTJsonResponse doGet(String url, Map<String, String> queryParameters)
	{
		String completeUrl = addQueryParametersToUrl(url, queryParameters);
		
		WebResource resource = client.resource(completeUrl);
		ClientResponse clientResponse = resource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);	
		
		//FIXME error in this line when running on android emulator (workaround on internet)
		RESTJsonResponse jsonResponse = new RESTJsonResponse(clientResponse.getStatus(), clientResponse.getEntity(String.class));
		
		return jsonResponse;
	}

	@Override
	public RESTJsonResponse doPost(String location, String jsonContent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RESTJsonResponse doDelete(String location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RESTJsonResponse doPut(String location) {
		// TODO Auto-generated method stub
		return null;
	}

}
