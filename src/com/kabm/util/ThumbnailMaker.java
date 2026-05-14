package com.kabm.util;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.log.Logger;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ThumbnailMaker 
{
	public static final String thumbPrefixName = "";
	private static final String LOG_ID="<at:ThumbnailMaker> ";
	
	private static String convertCommand = "C:\\Program Files\\ImageMagick-7.0.6-Q16\\convert.exe";
	/*
	 * convert image_org.jpg -resize 25%x25% -quality 100 image_out.jpg
	  image_org.jpg  이미지를 1/4 축소한 image_out.jpg로 바꾼다.
 
	 convert image_org.jpg -resize 800x600 -quality 100 image_out.jpg
     [설명] image_org.jpg  이미지를 800x600픽셀로 리사이즈 하지만 비율을 유지하며 큰사이즈 비율 기준으로 image_out.jpg를 생성한다.
	 * 
	 * */
	
	static
	{
		try
		{
			Config conf = Configuration.lookup("/MultipartAttributes");
			convertCommand = conf.getString("imageMagic");
		}catch(Exception e){
			
		}
	}
	
	public static boolean doMakeScalrThumbnail(File file, String destDir, int dw, int dh) throws Exception
	{
		BufferedImage srcImg = null;
		BufferedImage cropImg = null;
		BufferedImage destImg = null;
		try
		{
			srcImg = ImageIO.read(file);
			
			int ow = srcImg.getWidth(); 
			int oh = srcImg.getHeight();
			int nw = ow; 
			int nh = (ow * dh) / dw;
			if(nh > oh) { nw = (oh * dw) / dh; nh = oh; }
			cropImg = Scalr.crop(srcImg, (ow-nw)/2, (oh-nh)/2, nw, nh);
			destImg = Scalr.resize(cropImg, dw, dh);
		}catch(Exception e) {
			throw new Exception(file.getAbsolutePath()+" is invalid image format.");
		}		
		
		String filePath = null;
		String fileFullName = null;
		String fileAssociationName = null;
		String filePrefixName = null;
		
		filePath = file.getParent();
		fileFullName = file.getName();
		Logger.debug.println("filePath:"+filePath+", fileFullName:"+fileFullName);
		try
		{
			fileAssociationName = fileFullName.substring(fileFullName.lastIndexOf(".")).replace(".", "");
			fileAssociationName = fileAssociationName.toLowerCase();
			filePrefixName = fileFullName.substring(0, fileFullName.lastIndexOf("."));
		} catch(Exception ex)
		{
			throw new Exception(file.getAbsolutePath()+" is invalid file format.");
		}
		
		if("png".equals(fileAssociationName) || "jpg".equals(fileAssociationName)  || "jpeg".equals(fileAssociationName) || "bmp".equals(fileAssociationName) || "gif".equals(fileAssociationName))
		{				
			if("jpeg".equals(fileAssociationName)) fileAssociationName = "jpg";
		} else
		{
			return false;
		}
		try
		{
			File dir = new File(destDir);			
			if(!dir.exists()) dir.mkdirs();
			
			String thumbName = destDir+ File.separator+thumbPrefixName+filePrefixName+"."+fileAssociationName;
			Logger.debug.println("thumbName:"+thumbName+", fileAssociationName:"+fileAssociationName);
			File thumbFile = new File(thumbName); 
			ImageIO.write(destImg, fileAssociationName.toUpperCase(), thumbFile);
		
		} catch(Exception ex)
		{
			Logger.warn.println(LOG_ID+ex.getMessage());
			return false;
		}

		return true;
	}
	
	public static boolean doMakeThumbnail(File file, String destDir, String attachDiv) throws Exception
	{
				
		File[] files = {file};
		String[] destDirs = {destDir};
		
		return doMakeThumbnails(files, destDirs, attachDiv);
	}
	public static boolean doMakeThumbnails(File[] files, String[] destDirs, String attachDiv) throws Exception
	{
		String sizeOption = "";
		
		if( "attach_div_01".equals(attachDiv) )
		{
			//배너
			sizeOption = "192X70";
		}else if( "attach_div_02".equals(attachDiv) )
		{
			//팝업
			sizeOption = "";
		}else if( "attach_div_03".equals(attachDiv) )
		{
			//게시판
			sizeOption = "332X219";
		}else if( "attach_div_04".equals(attachDiv) )
		{
			//임원정보
			sizeOption = "68X75";
		}else if( "attach_div_05".equals(attachDiv) )
		{
			//지회정보
			sizeOption = "107X130";
		}
		
		return doMake(files, destDirs, sizeOption);
	}
	public static boolean doMake(File[] files, String[] destDir, String sizeOption) throws Exception
	{
		String filePath = null;
		String fileFullName = null;
		String fileAssociationName = null;
		String filePrefixName = null;
		
		List<String> command = null;
		
		File dir = null;
		//for(File f : files)
		for(int idx=0; idx<files.length; idx++)
		{
			try
			{
				filePath = files[idx].getParent();
				fileFullName = files[idx].getName();
				Logger.debug.println("filePath:"+filePath+", fileFullName:"+fileFullName);
				try
				{
					fileAssociationName = fileFullName.substring(fileFullName.lastIndexOf("."));
					fileAssociationName = fileAssociationName.toLowerCase();
					filePrefixName = fileFullName.substring(0, fileFullName.lastIndexOf("."));
				} catch(Exception ex)
				{
					throw new Exception(files[idx].getAbsolutePath()+" is invalid file format.");
				}
				
				if(".png".equals(fileAssociationName) || ".jpg".equals(fileAssociationName)  || ".jpeg".equals(fileAssociationName) || ".bmp".equals(fileAssociationName) || ".gif".equals(fileAssociationName))
				{				
				} else
				{
					return false;
				}
				
				dir = new File(destDir[idx]);			
				if(!dir.exists()) dir.mkdirs();
				
				
				command = new ArrayList<String>();					
				command.add(convertCommand);
				command.add("-strip");
				if(sizeOption != null && !"".equals(sizeOption)){
					command.add("-scale");
					command.add(sizeOption);
				}
				command.add("-quality");
				command.add("100");
				command.add("-depth");
				command.add("8");					
				command.add(files[idx].getAbsolutePath());
				command.add(destDir[idx]+ File.separator+thumbPrefixName+filePrefixName+fileAssociationName);
				
				
				for(int kdx=0; kdx<command.size(); kdx++)
				{
					Logger.debug.print(LOG_ID+command.get(kdx)+" ");
				}
				Logger.debug.println("");
				
				runCommand(((String[])command.toArray(new String[1])));
				
				Thread.sleep(1*200);
				
			} catch(Exception ex)
			{
				Logger.warn.println(LOG_ID+ex.getMessage());
			}
			
		}
		
		return true;
	}
	
	
	
	private static void runCommand(String[] command) throws Exception
	{
		Runtime rt = null;
		Process proc = null;
		InputStreamReader is = null;
		BufferedReader br = null;
		
		try
		{
			rt = Runtime.getRuntime();
			proc = rt.exec(command);			
			br = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			String line = null;
			Logger.debug.println(LOG_ID+"<INFO>");
			StringBuffer sb = new StringBuffer();
			while ( (line = br.readLine()) != null)
			{
				sb.append(line);
			}
			Logger.debug.println(LOG_ID+sb.toString());
			Logger.debug.println(LOG_ID+"</INFO>");
			int exitVal = proc.waitFor();
		} catch(Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		} finally
		{
			if(br != null) try{br.close();}catch(Exception ex){}
		}
	}
	
	
	public static void main(String[] args) throws Exception
	{
		File f = new File("C:/Users/UBICUS-141/Pictures/그림3.png");
		ThumbnailMaker.doMakeThumbnail(f, "D:\\TEST", "attach_div_04");
	}
	
	
}
