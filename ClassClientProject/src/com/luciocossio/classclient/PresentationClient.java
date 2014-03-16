package com.luciocossio.classclient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.Gson;
import com.luciocossio.classclient.http.RESTJsonClient;
import com.luciocossio.classclient.model.ImagePresentationInfo;
import com.luciocossio.classclient.model.SlidePresentationInfo;

public class PresentationClient extends ClassClientHTTPCommon {
				
	private final String START_PRESENTATION_PATH = "presentation";
	private final String PREPARE_PRESENTATION_PATH = "presentation/prepare";
	private final String PRESENTATION_SLIDES_PATH = "presentation/slides";
	private final String IMAGE_COMMAND = "image/action";
	private final String OPEN_IMAGE_PATH = "image";
	private final String GET_IMAGE_PATH = "image";
	private final String NEXT_SLIDE_PATH = "presentation/action";
	private final String PREVIOUS_SLIDE_PATH = "presentation/action";
	private final String GOTOSLIDE_PATH = "presentation/action";
	private final String PRESENTATION_INFO = "presentation/info";
	private final String IMAGE_INFO = "image/info";
	private final String CLOSE_PRESENTATION_PATH = "presentation/action";
	private final String FILE_PATH = "files";
	private final String LISTENERS_PATH = "listeners";
	
	private String serverUrl;
	
	private final String PPTX_MIMETYPE = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
	
	public PresentationClient(RESTJsonClient restClient, String serverUrl)
	{
		this.restClient = restClient;
		this.serverUrl = serverUrl;
	}
	
	public ResultMessage uploadFile(File file, String fileName)
	{
		String url = serverUrl + FILE_PATH + "/" + fileName;
		
		return doPutFile(url, file, PPTX_MIMETYPE);		
	}
	
	public ResultMessage startPresentation()
	{
		String url = serverUrl + START_PRESENTATION_PATH;		
		return doPut(url, "");
	}
	
	public ResultMessage preparePresentation(String fileName)
	{
		String url = serverUrl + PREPARE_PRESENTATION_PATH;
		
		return doPut(url, "{\"fileName\":\""+ fileName +"\"}");
	}

	public ResultMessage nextSlide()
	{
		String url = serverUrl + NEXT_SLIDE_PATH;
		
		return doPut(url,"{\"command\":\"next\"}");
	}

	public ResultMessage previousSlide()
	{		
		String url = serverUrl + PREVIOUS_SLIDE_PATH;

		return doPut(url,"{\"command\":\"previous\"}");
	}
	
	public ResultMessage goToSlideNumber(String slideNumber)
	{
		String url = serverUrl + GOTOSLIDE_PATH;
		
		return doPut(url,"{\"command\":\"gotoslide\", \"arg\":\"" + slideNumber + "\"}");
	}

	public ResultMessage closePresentation()
	{
		String url = serverUrl + CLOSE_PRESENTATION_PATH;

		return doPut(url,"{\"command\":\"close\"}");
	}	
	
	public InputStream getFile(String fileName) throws ClientProtocolException, IOException
	{
		String url = serverUrl + FILE_PATH + "/" + fileName;

		InputStream stream = doGetFile(url);
		return stream;
	}	
	
	public InputStream getSlideImage(String imageName) throws ClientProtocolException, IOException
	{
		String url = serverUrl + PRESENTATION_SLIDES_PATH + "/" + imageName;
		InputStream stream = doGetFile(url);
		return stream;
	}
	
	public InputStream getCurrentImage() throws ClientProtocolException, IOException
	{
		String url = serverUrl + GET_IMAGE_PATH;
		InputStream stream = doGetFile(url);
		return stream;
	}
	
	public ResultMessage openImage(String fileName)
	{		
		String url = serverUrl + OPEN_IMAGE_PATH;
		
		return doPut(url, "{\"fileName\":\""+ fileName +"\"}");
	}
	
	public ResultMessage addListenerForSlidesPresentation()
	{		
		String url = serverUrl + LISTENERS_PATH;
		
		return doPost(url, "{\"mode\":\"slides\"}");
	}
	
	public ResultMessage addListenerForImagePresentation()
	{		
		String url = serverUrl + LISTENERS_PATH;
		
		return doPost(url, "{\"mode\":\"image\"}");
	}
	
	public ResultMessage deleteListener()
	{		
		String url = serverUrl + LISTENERS_PATH;
		
		return doDelete(url);
	}
	
	public ResultMessage closeImage()
	{
		String url = serverUrl + IMAGE_COMMAND;

		return doPut(url,"{\"command\":\"closeimage\"}");
	}	
	
	public ResultMessage rotateImageRight()
	{
		String url = serverUrl + IMAGE_COMMAND;
		
		return doPut(url,"{\"command\":\"rotateright\"}");
	}
	
	public ResultMessage moveImage(int dx, int dy)
	{
		String url = serverUrl + IMAGE_COMMAND;
		
		return doPut(url,"{\"command\":\"move\", \"param\":\""+ dx + ":" + dy + "\"}");
	}
	
	public ResultMessage zoomImage(float zoom, int x, int y)
	{
		String url = serverUrl + IMAGE_COMMAND;
		
		return doPut(url,"{\"command\":\"zoom\", \"param\":\""+ zoom + ":" + x + ":" + y + "\"}");
	}
		
	public ResultMessage updateImageVisiblePart(int left, int top, int right, int bottom, int imageHeight, int imageWidth, int angle)
	{
		String url = serverUrl + IMAGE_COMMAND;
		
		return doPut(url,"{\"command\":\"visiblepart\", \"param\":\""+ left + ":" + top + ":" + right + ":" + bottom + ":" + imageHeight + ":" + imageWidth + ":" + angle + "\"}");
	}
	
	public ResultMessage zoomImage(float zoom)
	{
		String url = serverUrl + IMAGE_COMMAND;
		
		return doPut(url,"{\"command\":\"zoom\", \"param\":\""+ zoom + "\"}");
	}
	
	public ResultMessage rotateImageLeft()
	{
		String url = serverUrl + IMAGE_COMMAND;
		
		return doPut(url,"{\"command\":\"rotateleft\"}");
	}

	public ResultMessage moveImageRight()
	{
		String url = serverUrl + IMAGE_COMMAND;
		
		return doPut(url,"{\"command\":\"moveright\"}");
	}
	
	public ResultMessage moveImageLeft()
	{
		String url = serverUrl + IMAGE_COMMAND;
		
		return doPut(url,"{\"command\":\"moveleft\"}");
	}
	
	public ResultMessage moveImageUp()
	{
		String url = serverUrl + IMAGE_COMMAND;
		
		return doPut(url,"{\"command\":\"moveup\"}");
	}
	
	public ResultMessage moveImageDown()
	{
		String url = serverUrl + IMAGE_COMMAND;
		
		return doPut(url,"{\"command\":\"movedown\"}");
	}

	public ResultMessage zoomInImage()
	{
		String url = serverUrl + IMAGE_COMMAND;
		
		return doPut(url,"{\"command\":\"zoomin\"}");
	}
	
	public ResultMessage zoomOutImage()
	{
		String url = serverUrl + IMAGE_COMMAND;
		
		return doPut(url,"{\"command\":\"zoomout\"}");
	}

	public List<String> getCurrentPresentationImageNames() {

		String url = serverUrl + PRESENTATION_SLIDES_PATH;
		
		ResultMessage message = this.doGet(null, url);
		
		Gson gson = new Gson();
		ListOfImages list = gson.fromJson(message.getMessage(), ListOfImages.class);
		return list.getImages();
	}
	
	public List<String> getFileNames(Map<String,String> queryParameters) {
		String url = serverUrl + FILE_PATH;
		
		ResultMessage message = this.doGet(queryParameters, url);
		
		Gson gson = new Gson();
		ListOfFiles list = gson.fromJson(message.getMessage(), ListOfFiles.class);
		return list.getFiles();
	}
	
	public SlidePresentationInfo getSlidePresentationInfo() {
		String url = serverUrl + PRESENTATION_INFO;
		
		ResultMessage message = this.doGet(null, url);
		
		Gson gson = new Gson();
		SlidePresentationInfo presentation = gson.fromJson(message.getMessage(), SlidePresentationInfo.class);
		return presentation;
	}
	
	public ImagePresentationInfo getImagePresentationInfo() {
		String url = serverUrl + IMAGE_INFO;
		
		ResultMessage message = this.doGet(null, url);
		
		Gson gson = new Gson();
		ImagePresentationInfo presentation = gson.fromJson(message.getMessage(), ImagePresentationInfo.class);
		return presentation;
	}
	
	public List<String> getImageFileNames() {
		
		Map<String,String> queryParameters = new HashMap<String, String>();
		queryParameters.put("type", "image");

		return this.getFileNames(queryParameters);
	}
	
	public List<String> getPresentationFileNames() {
		
		Map<String,String> queryParameters = new HashMap<String, String>();
		queryParameters.put("type", "presentation");

		return this.getFileNames(queryParameters);
	}

}
