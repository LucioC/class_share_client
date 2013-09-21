package com.luciocossio.classclient.helpers;

public class FileExtensionHelper {
	
	public boolean isImageExtension(String extension)
	{
		boolean isImage = false;
		
		if(extension.equals("jpg") || extension.equals("png") || extension.equals("jpeg"))
		{
			return true;
		}
		
		return isImage;	
	}	

	public boolean isPowerPointExtension(String extension)
	{
		boolean isPowerPoint = false;
		
		if(extension.equals("ppt") || extension.equals("pptx"))
		{
			return true;			
		}
		
		return isPowerPoint;	
	}
	
	public String getFileExt(String FileName)
    {       
         String ext = FileName.substring((FileName.lastIndexOf(".") + 1), FileName.length());
         return ext;
    }

}
