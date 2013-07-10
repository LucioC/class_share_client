package com.luciocossio.classserviceclient.test.units.junit;

import java.util.LinkedHashMap;

import com.luciocossio.classclient.http.HTTPUtils;

import junit.framework.Assert;
import junit.framework.TestCase;

public class HTTPUtilsTest extends TestCase {
	
	public void testQueryParameterToUrl()
	{
		HTTPUtils httpUtils = new HTTPUtils();
		
		String desiredUrl = "http://example.com/test?param1=value1&param2=value2";
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("param1", "value1");
		map.put("param2", "value2");
	
		String url = httpUtils.addQueryParametersToUrl("http://example.com/test", map);
		
		Assert.assertEquals(desiredUrl, url);
	}

}
