package com.kabm.util;

import java.io.File;
import java.io.FilenameFilter;

public class FileListFilter implements FilenameFilter{
	private String extension;
	
	public FileListFilter( String extension)
	{
		this.extension = extension;
	}
	
	public boolean accept( File directory ,  String filename)
	{
		boolean fileOK = true;
		
		if(extension != null){
			fileOK &= filename.endsWith('.' + extension);
		}
		return fileOK;
	}
}