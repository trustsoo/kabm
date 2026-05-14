package com.kabm.svl;

import com.kabm.util.NetworkUtil;
import com.kabm.util.UserAgentUtil;
import jdf.framework.core.*;
import jdf.framework.core.data.InteractionBean;
import jdf.framework.core.data.ResourceException;
import jdf.framework.core.http.WebController;
import jdf.framework.core.io.StreamUtil;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.DateTime;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.core.util.Utility;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.io.FileDeleteStrategy;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FileUploaderSvl extends HttpServlet implements Servlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8662476226643944154L;


	private class FileUploadProgressListener implements ProgressListener
	{ 
		private String ip  = null;
		private long megaBytes = -1;
		public void setClientIP(String ip)
		{
			this.ip = ip;
		}
		
		
		
        public void update(long pBytesRead, long pContentLength, int pItems) {
            long mBytes = pBytesRead / 1000000;
            if (megaBytes == mBytes) {            	
                return;
            }
            megaBytes = mBytes;
            
            Logger.info.println(CLASS_NAME+ip+" reading item " + pItems);
            if (pContentLength == -1) {
                Logger.info.println(CLASS_NAME+ip+" " + pBytesRead + " bytes have been read.");
            } else {
				Logger.info.println(CLASS_NAME+ip+"("+pItems+"): "+ pBytesRead + " of " + pContentLength);
            	
            }
        }		
	}
	
	private class DeleteFilesOnEndUploadCleaningTracker extends FileCleaningTracker 
	{
		private List<String> filesToDelete = new ArrayList();

	     public void deleteTemporaryFiles() {
	         for (String file : filesToDelete) {
	        	 
	        	 Logger.debug.println("<DeleteFilesOnEndUploadCleaningTracker>"+file);
	        	 
	             new File(file).delete();
	         }
	         filesToDelete.clear();
	     }

	     @Override
	     public synchronized void exitWhenFinished() {
	         deleteTemporaryFiles();
	     }

	     @Override
	     public int getTrackCount() {
	         return filesToDelete.size();
	     }

	     @Override
	     public void track(File file, Object marker) {
	         filesToDelete.add(file.getAbsolutePath());
	     }

	     @Override
	     public void track(File file, Object marker, FileDeleteStrategy deleteStrategy) {
	         filesToDelete.add(file.getAbsolutePath());
	     }

	     @Override
	     public void track(String path, Object marker) {
	         filesToDelete.add(path);
	     }

	     @Override
	     public void track(String path, Object marker, FileDeleteStrategy deleteStrategy) {
	         filesToDelete.add(path);
	     }
	}
	
	
	private final String CLASS_NAME="<at:FileUploaderSvl> ";
	
	public FileUploaderSvl()
	{
		super();
	}
	
	public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

    }
	
	public void destroy()
    {
        super.destroy();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try{
			process( request, response);
		}catch(Throwable t){
			Logger.err.println("File Uploade request fail");
		}
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{	
		try{
			process( request, response);
		}catch(Throwable t){
			Logger.err.println("File Uploade request fail");
		}
	}
	
	protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String tempPath = null;
		int maxUploadFileKilloBytes = 0;
		String charEncoder = null;
		DiskFileItemFactory factory = null;
		Config conf = null;
		String clientIP = NetworkUtil.getRemoteAddr(request);
		String fileName = null;
		String orgfileName = null;
		String ext_name = "";
		String returnUrl = null;
		String publicDir = null;
		String dir = null;
		String publicUrl = null;
		String photoExtNames = null;
		String excludExtNames = null;
		String includExtNames = null;
		String excelRead = "N";
		String kmRead = "N";
		String xlsReadType = "";
		String data_id = "";
		File uploadedFile = null;
		String file_no = null;
		boolean isPublic = false;
		
		String CKEditor = request.getParameter("CKEditor");
		String office_attach_div_cd = null;
		String office_brd_mng_no = null;
		String office_title = null;
		String file_div_path = null;
		
		//설문대상 전용
		String survey_id ="";
		String cust_type ="";
		String survey_div_cd ="";
		
		String currMM = DateTime.getFormatString("yyyyMM");
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		File _dir = null;
		Logger.info.println("isMultipart1:"+isMultipart);
		
		int fileSize = 0;
		
		Message msbBox = new MessageBox();
		
		boolean isWriteDB = true;
		
		try
		{
			conf = Configuration.lookup("/MultipartAttributes");
			tempPath = conf.getString("tempPath");
			
			File tempPaths = new File(tempPath);
			if(!tempPaths.exists()) tempPaths.mkdirs();
			
			charEncoder = conf.getString("encoding");
			photoExtNames = conf.getString("photoExtNames");
			dir = conf.getString("dir");
			publicDir = conf.getString("publicDir");
			publicUrl = conf.getString("publicUrl")+"/"+currMM+"/";
			maxUploadFileKilloBytes = conf.getInt("maxUploadFileKilloBytes");
			excludExtNames = conf.getString("excludExtNames");
			includExtNames = conf.getString("includExtNames");
			file_div_path = conf.getString("file_div_path");
			
		} catch(ConfigurationException ce)
		{
			Logger.warn.println(CLASS_NAME+" config.xml read fail."+Utility.getStackTrace(ce));
		}
		
		
		
		if(isMultipart)
		{
			try
			{
				factory = new DiskFileItemFactory();                                   
				factory.setSizeThreshold(20 * 1024 * 1024);
				factory.setRepository(new File(tempPath));	        
				ServletFileUpload upload = new ServletFileUpload(factory);  
				upload.setHeaderEncoding(charEncoder);
				upload.setSizeMax(maxUploadFileKilloBytes * 1024);
				
				
				/*Upload progress monitor....*/
				FileUploadProgressListener listener = new FileUploadProgressListener();
				listener.setClientIP(clientIP);
				upload.setProgressListener(listener);
				
				DeleteFilesOnEndUploadCleaningTracker tracker = new DeleteFilesOnEndUploadCleaningTracker();
				factory.setFileCleaningTracker(tracker); 
				
				
				List<FileItem> items = upload.parseRequest(request);	
							
				Iterator<FileItem> iter = items.iterator();
				FileItem fileItem = null;
				String key = null;
				//String filePath = null;
				while(iter.hasNext())
				{
					fileItem = iter.next();
					key = fileItem.getFieldName();
					
					if(fileItem.isFormField())
					{
						
						String val = fileItem.getString(charEncoder);
						Logger.info.println(CLASS_NAME+key+":"+val);
						if(key.equals("CKEditorFuncNum"))
						{
							if( conf != null)
								returnUrl = conf.getString("photoUploadCallback");
							
							returnUrl = returnUrl + "?callback_func="+val;
						} else if(key.equals("isPublic"))
						{
							if(val != null && val.equals("Y"))
								isPublic = true;
						} 
						
					} else
					{					
				    	orgfileName = fileItem.getName();
				    	
				    	if(orgfileName == null || "".equals(orgfileName)) throw new NullPointerException("File Name is Null");
				    	
				    	if(orgfileName.lastIndexOf(".") !=-1 ) 
						      ext_name = orgfileName.substring(orgfileName.lastIndexOf(".")+1);
				    	
				    	if(ext_name != null && !"".equals(ext_name)) ext_name = ext_name.toLowerCase();
				    	
				    	fileName = System.currentTimeMillis()+"."+ext_name;

				    	if(!isAllowFile(excludExtNames, includExtNames, ext_name))
				    	{	
				    		tracker.deleteTemporaryFiles();
				    		
				    		
				    		throw new Exception(msbBox.getMessage(UserAgentUtil.getUserLanguage(request),"WARN", "W0003")+"("+ext_name+")");
				    	}
				    	
				    	if(CKEditor != null) isPublic = true;
				    	
				    	
						if(isPublic)
						{	
							Logger.info.println("key:" + key);
							if(key.equals("callback_func"))
							{
								if( conf != null)
									returnUrl = conf.getString("photoUploadCallback");
							}
							
							_dir = new File(publicDir+ File.separator+currMM);
							
							if(!_dir.exists()) _dir.mkdirs();	
							
							uploadedFile=new File(_dir.getAbsoluteFile(),fileName);
							
							
						} else
						{
							_dir = new File(dir+ File.separator+currMM);
							
							if(!_dir.exists()) _dir.mkdirs();
							
							uploadedFile=new File(_dir.getAbsoluteFile(),fileName);
							
							Logger.info.println(CLASS_NAME+uploadedFile.getAbsolutePath()+" upload complete.");
						}
						fileItem.write(uploadedFile);
				        fileItem.delete();
				        fileSize = (int)uploadedFile.length();
				        
				        try
						{
							//RunCommand.runChmod(_dir.getAbsoluteFile()+java.io.File.separator+fileName);
						}catch(Exception ie){
							Logger.err.println(CLASS_NAME+Utility.getStackTrace(ie));
						}
					}
				}	
				
				//String encrypt_key = SitePropertyManager.getString("COOKIE_USER_ID_KEY");
				//byte[] encryptValue = jdf.framework.core.util.encrypt.CipherUtil.encode(encrypt_key.getBytes(), fileName.getBytes());
				//String encFileName = jdf.framework.core.util.encrypt.CipherUtil.hexToString(encryptValue);

				//Logger.debug.println(">>>>>>orgFileName: " + orgfileName);
				try{
					orgfileName = orgfileName.replaceAll(",", "_");
				}catch(Exception ex){
					
				}
				
				
				if(CKEditor != null)
				{
					returnUrl = conf.getString("photoUploadCallback");
					
					returnUrl = returnUrl + "?callback_func="+request.getParameter("CKEditorFuncNum");
				}
				
				
				
				if(returnUrl != null)
				{
					response.sendRedirect(returnUrl+"&sFileURL="+publicUrl+fileName+"&sFileName="+fileName);
					return;
				}
				/*else if(kmRead.equals("Y"))
				{
					String oFilePath = _dir.getAbsoluteFile().toString();
					String oFileName = fileName;
					request.setAttribute("oFilePath", oFilePath);
					request.setAttribute("oFileName", oFileName);
					request.setAttribute("orgfileName", orgfileName);
					request.setAttribute("path", currMM);
					request.setAttribute("extname", ext_name);
					request.setAttribute("filesize", fileSize);
					request.setAttribute("office_attach_div_cd", office_attach_div_cd);
					request.setAttribute("encFileName", encFileName);
					request.setAttribute("office_brd_mng_no", office_brd_mng_no);
					request.setAttribute("office_title", office_title);
					request.getRequestDispatcher("/common/action/kmParser.jspx?cmd=readOffice").forward(request, response);
					
				}else if(excelRead.equals("Y"))
				{
					String xlsFilePath = _dir.getAbsoluteFile().toString();
					String xlsFileName = fileName;
					request.setAttribute("xlsFilePath", xlsFilePath);
					request.setAttribute("xlsFileName", xlsFileName);
					request.setAttribute("xlsReadType", xlsReadType);
					request.setAttribute("data_id", data_id);
					request.getRequestDispatcher("/common/action/fileParser.jspx?cmd=readExcel").forward(request, response);
					//xls파일 내용 일기.					
					//readExcel(request, response, xlsFilePath, xlsFileName, xlsReadType, data_id);
				}*/
				else
				{
					
					File absFile = null;
					if( _dir != null)
						absFile = _dir.getAbsoluteFile();
					StringBuffer data = new StringBuffer();
					data.append("<file>\n")
						.append("<filename>\n")
						.append(fileName).append("\n")
						.append("</filename>\n")
						.append("<path>\n")
						//.append(absFile).append("\n")
						.append(currMM).append("\n")
						.append("</path>\n")
						.append("<extname>\n")
						.append(ext_name).append("\n")
						.append("</extname>\n")
						.append("<filesize>\n")
						.append(fileSize).append("\n")
						.append("</filesize>\n")
						.append("<user_filename><![CDATA[").append(orgfileName).append("]]></user_filename>")
					    .append("</file>\n");
					printMessage(request, response, "200", "success", data);
				}
				
			 }catch(Exception ex)
			{
				if(returnUrl != null)
					returnUrl = returnUrl+"&errstr="+java.net.URLEncoder.encode(ex.getMessage(), "utf-8");
				else								
					printMessage(request, response, "500", ex.getMessage(), new StringBuffer());
			
				Logger.err.println(CLASS_NAME+Utility.getStackTrace(ex));
				
				isWriteDB = false;
			}
			
			
		} else
		{
			orgfileName = request.getHeader("file-name");
			String file_type =request.getHeader("file-type");
			String file_size = request.getHeader("file-size");
			String temp = request.getHeader("isPublic");
			
			if(temp != null && temp.equals("Y")) isPublic = true;
			
			if(orgfileName == null || "".equals(orgfileName)) throw new NullPointerException("File Name is Null");
	    	
	    	if(orgfileName.lastIndexOf(".") !=-1 ) 
			      ext_name = orgfileName.substring(orgfileName.lastIndexOf(".")+1);
	    	
	    	if(ext_name != null && !"".equals(ext_name)) ext_name = ext_name.toLowerCase();
	    	
	    	fileName = System.currentTimeMillis()+"."+ext_name;		    
						
			fileSize = Integer.parseInt(file_size);
			
			if(isPublic)
			{	
				_dir = new File(publicDir+ File.separator+currMM);
				
				if(!_dir.exists()) _dir.mkdirs();	
				
				uploadedFile=new File(_dir.getAbsoluteFile(),fileName);
				
				if( conf != null)
					returnUrl = conf.getString("photoUploadCallback")+"?a=1";
				
			} else
			{
				_dir = new File(dir+ File.separator+currMM);
				
				if(!_dir.exists()) _dir.mkdirs();
				
				uploadedFile=new File(_dir.getAbsoluteFile(),fileName);
				
				
			}			
			
			InputStream in = null;
			FileOutputStream fos = null;
			try
			{
				in = request.getInputStream();
				fos = new FileOutputStream(_dir.getAbsoluteFile()+ File.separator+fileName);
				StreamUtil.copy(in, fos);				
				Logger.info.println(CLASS_NAME+uploadedFile.getAbsolutePath()+" upload complete.");
				
			} catch(IOException iex)
			{
				isWriteDB = false;
				
				Logger.err.println(CLASS_NAME+Utility.getStackTrace(iex));
				response.sendError(response.SC_INTERNAL_SERVER_ERROR, iex.getMessage());
				
			} catch(Exception ex)
			{
				isWriteDB = false;
				
				Logger.err.println(CLASS_NAME+Utility.getStackTrace(ex));
				response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
			} finally
			{
				if(fos != null)try{fos.close();}catch(Exception ex){}
				if(in != null)try{in.close();}catch(Exception ex){}
			}
		}
		
		PrintWriter out = null;
		
		try
		{
			String absFile = "";
			if( _dir != null )
				absFile = _dir.getAbsoluteFile().toString();
			if(isWriteDB)
				file_no = doInsTmpl(request,response,orgfileName,fileSize, fileName, ext_name,publicUrl,absFile );
			else
				file_no = "-1";
			
			if( !isMultipart)
			{
				out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
				out.write("&bNewLine=true&sFileURL="+publicUrl+fileName+"&sFileName="+fileName+"&file_no="+file_no);
			}
		} catch(IOException iex)
		{
			Logger.err.println(CLASS_NAME+Utility.getStackTrace(iex));
			response.sendError(response.SC_INTERNAL_SERVER_ERROR, iex.getMessage());
		} catch(Exception ex)
		{
			Logger.err.println(CLASS_NAME+Utility.getStackTrace(ex));
			response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
		} finally
		{
			if(out != null) try{out.close();}catch(Exception ex){}
		}
		
	}
	
	private void printMessage(HttpServletRequest request, HttpServletResponse response, 
			String resultCD, String message, StringBuffer data)
	{
		PrintWriter out = null;
		
		String printUserMsg = null;
		
		if(resultCD != null && !"200".equals(resultCD))
		{
			if(message == null || "".equals(message))
				printUserMsg = "We are very sorry. Internal server Error.";
			else
				printUserMsg = message;
		}
		else
			printUserMsg = message;
		
		try
		{
			StringBuffer xml = new StringBuffer();
			xml.append("<?xml version=\'1.0\' encoding=\'utf-8\'?>")
			   .append("<result>")
			   .append("	<code>").append(resultCD).append("</code>")
			   .append("	<msg><![CDATA[").append(printUserMsg).append("]]></msg>")
			   .append("	<data>").append(data).append("</data>")			   
			   .append("</result>");
			response.setContentType("text/xml");
			out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
			out.write(xml.toString());			
			Logger.info.println(CLASS_NAME+xml.toString()+"(client:"+NetworkUtil.getRemoteAddr(request)+")");						
		} catch(IOException iex){
			Logger.warn.println(CLASS_NAME+iex.toString());
		} catch(Exception ex)
		{
			Logger.warn.println(CLASS_NAME+ex.toString());
		}finally
		{
			if(out != null) try{out.close();}catch(Exception ex){}
		}
	}
		
	private boolean isAllowFile(String excludExtNames, String includExtnames, String extName)
	{
		String[] excludExtNamesArray = SmartStringArray.split(",", excludExtNames);
		String[] includExtNamesArray = SmartStringArray.split(",", includExtnames);
		
	
		List<String> list = null;
		
		if(excludExtNamesArray.length > 0)
		{
			list = Arrays.asList(excludExtNamesArray);
			return !list.contains(extName);
		} else
		{
			list = Arrays.asList(includExtNamesArray);
			return list.contains(extName);
		}
	}
	
	
	private String doInsTmpl(HttpServletRequest request, HttpServletResponse response, 
				String orgFileName, int fileSize, String fileName, String ext_name, String publicUrl, String fileDir)
	{
		jdf.framework.core.data.DataSet input = new jdf.framework.core.data.DataSet();
		jdf.framework.core.data.DataSet output = new jdf.framework.core.data.DataSet();
		WebController.bind(request, input);
		InteractionBean interact = new InteractionBean();
		String val = null;
		
		try {
			input.put("cmd", "createTmpl");
			input.put("file_nm", fileName);
			input.put("file_path", fileDir);
			input.put("file_size", fileSize);
			input.put("ext_nm", ext_name);
			
			try{
				orgFileName = orgFileName.replaceAll(",", "_");
			}catch(Exception ex){
				
			}
			
			input.put("user_file_nm", orgFileName);
			
			output = interact.execute("/common/Attach", input);
			val = output.getText("file_no");
		} catch (ResourceException ex) {
			Logger.err.print(Utility.getStackTrace(ex));
		}
		Logger.info.println("file_no: " + val);
		return val;
	}
	
	
}