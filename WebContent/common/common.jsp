<%@ page import="java.util.Arrays"%>
<%@ page import="jdf.framework.core.log.Logger"%>
<%@ page import="jdf.framework.view.auth.User"%>
<%@ page import="jdf.framework.core.http.SessionAttributes"%>
<%@ page import="jdf.framework.core.Config"%>
<%@ page import="jdf.framework.core.Configuration"%>
<%@ page import="jdf.framework.core.util.*"%>
<%@ page import="jdf.framework.core.data.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>

<%!
	public final String MEM_DIV_NEW = "0";
	public final String MEM_DIV_REP = "1";
	public final String MEM_DIV_PESN = "3";
	public final String MEM_DIV_CORP = "4";
	public final String SMS_SITE_CODE = "BA681";
	public final String SMS_SITE_PASSWORD = "OSirHh2U3wbl";
	
	public final String OFFLINE_SESSION_KEY = "OFFLINE-USER";
	public final String OFFLINE_SESSION_USER_NM = "req_user_nm";
	public final String OFFLINE_SESSION_TEL_NO = "req_tel_no";
	public final String OFFLINE_SEN_REDIRECT = "/edu/lecture/offlineCtrl.jspx?cmd=offlineSelect";
	public final String OFFLINE_SEN_END = "/edu/lecture/offlineCtrl.jspx?cmd=offlineEduStep7";
	public final String OFFLINE_SEN_CONFIRM = "/edu/lecture/offlineCtrl.jspx?cmd=offlineUserConfirm";
	
	//attribute에 from ~ to  Date 설정
	public void setLimitCalendar(String minAttrName, String maxAttrName, int addMonth, HttpServletRequest req){
		setLimitCalendar(minAttrName, maxAttrName, addMonth,  req, "yyyy-MM-dd");
	}

	public void setLimitCalendar(String minAttrName, String maxAttrName, int addMonth, HttpServletRequest req, String dateFormat){
		java.text.SimpleDateFormat  formatter = new java.text.SimpleDateFormat(dateFormat);
		
		Date date = new Date();
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH,addMonth); 

		if(addMonth < 0) //-이면 오늘 날짜가 Max, +이면 오늘날짜가 min
		{
			req.setAttribute(minAttrName,formatter.format(cal.getTime()));
			req.setAttribute(maxAttrName,formatter.format(new Date()));
		}else{
			req.setAttribute(maxAttrName,formatter.format(cal.getTime()));
			req.setAttribute(minAttrName,formatter.format(new Date()));
		}
	}

	public jdf.framework.core.data.DataSet translateInjectField(jdf.framework.core.data.DataSet input) {
		java.util.Iterator iterator = input.keySet().iterator();// Iterate on keys

		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			String value = String.valueOf( input.get(key) );

			if (!"lc_body".equals(key)) {  //게시판 본문의 필드는 치환하지 않는다.
				value = value.replaceAll("--", "");
				value = value.replaceAll("'", "");
				// value = value.replaceAll("%", ""); //URL 패턴으로 나올 수 있을것
				value = value.replaceAll(";", "");
				value = value.replaceAll("<", "");
				value = value.replaceAll(">", "");
				value = value.replaceAll("\"", "");
				value = value.replaceAll("\\+", "");
			}

			input.put(key, value);
			// Logger.debug.println( key + "\t\t: \t" + value );
		}

		return input;
	}

	public String getConfigValue(String prefix, String attr)
	{
		String rtnValue = null;
		try
		{
			Config conf = Configuration.lookup(prefix);
			rtnValue = conf.getString(attr);
		} catch(Exception ex)
		{
			rtnValue = "";
		}
		
		return rtnValue;
	}
	
	public User getUserObject(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res)
	{
		SessionAttributes sessionAttribute = null;
		User session_user = null;
		try
		{    		
			sessionAttribute = new SessionAttributes(req); 
			session_user = (User)(sessionAttribute).getAttribute(User.SESSION_KEY);
		
			//session_user.toString();
			return session_user;	
		} catch(Exception ex)
		{
		}
		return null;	
	}	
	
	public jdf.framework.core.data.DataSet getUserSeesionInfo(HttpServletRequest req, HttpServletResponse res)
	{
		jdf.framework.core.data.DataSet userInfo = new jdf.framework.core.data.DataSet();
		User user = getUserObject(req, res);
		if(user != null) userInfo = user.getDataSet();		
		return userInfo;		
	}
	
	/*
		사용자의 session에 특정 grp_cd가 포함되어있는지 확인한다.
	*/
	public boolean isContainUserGroup(HttpServletRequest req, HttpServletResponse res, String grp_cd)
	{
		try 
		{
	        String grpcd = getUserObject(req, res).getGrpCd();
	        if( grpcd != null )
	        {
	            String[] arr = grpcd.split(",");
	            java.util.List<String> al = Arrays.asList(arr);
	            //ArrayList<String> al = new ArrayList<String>(Arrays.asList(arr));                    
	            if( al.contains(grp_cd) )
	                return true;
	        }
        } catch(Exception ex){}        
        return false;
	}
	
	
	protected String getBrowser(HttpServletRequest request) {
		
        String header = request.getHeader("User-Agent");
        log("header: " + header);
        if ( header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1) {
            return "MSIE";
        } else if ( header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if ( header.indexOf("Opera") > -1) {
            return "Opera";
        } else if ( header.indexOf("Safari") > -1) {
        	return "Safari";
        }
        
        return "Firefox";
	}	
	
	public String getDownFileNames(HttpServletRequest request,String fileName) 
	{
		String browser = getBrowser(request);
		
		if ( fileName == null || fileName.equals("") ) {
			fileName = "UnKnownFileName";
		}

		String resultName = "";
	    log("browser: " + browser);
		try{
			// Explorer
			if ( browser.indexOf("MSIE") != -1 ) { 
// 				resultName = new String( fileName.getBytes("EUC-KR"), "ISO-8859-1").replaceAll(" ","%20");
				resultName = new String( fileName.getBytes("EUC-KR"), "ISO-8859-1").replaceAll(" ","%20");
			}
			// Opera
			else if ( browser.indexOf("Opera") != -1 ) {
				resultName = new String( fileName.getBytes("UTF-8"), "ISO-8859-1");
			}
			// Chrome
			else if ( browser.indexOf("Chrome") != -1 ) {
				resultName = new String( fileName.getBytes("EUC-KR"), "ISO-8859-1");
			}
			// Safari
			else if ( browser.indexOf("Safari") != -1 ) {
				resultName = new String( fileName.getBytes("UTF-8"), "ISO-8859-1");
			}
			// FireFox
			else if ( browser.indexOf("Firefox") != -1 ) {
				resultName = new String( fileName.getBytes("UTF-8"), "ISO-8859-1");
			}
			// Other
			else{
				
				resultName = new String( fileName.getBytes("EUC-KR"), "ISO-8859-1");
			}
		} catch (Exception ex) {
			resultName = fileName; 
		}
		
		return resultName;										
	}
	
	
	public int getMaxAttachSizeValue()
	{
		return Integer.parseInt(getConfigValue("/MultipartAttributes", "maxUploadFileKilloBytes"));
	}
	
	//사진 업로드 다이얼로그에서 사용할때 지원할 확장자 형식 
	public String getPhotoExtnamesToDialog()
	{
		String rtnData = "";
		String photoExtNames = getConfigValue("/MultipartAttributes", "photoExtNames");
		String[] extnms = jdf.framework.core.util.SmartStringArray.split(",", photoExtNames);
		for(String extnm: extnms){
			if("".equals(rtnData)) 
				rtnData += "*." + extnm;
			else
				rtnData += ";*." + extnm;
		}
		return rtnData;
	}

	
	
	protected void sendFile(String filename, int fileSize, java.io.InputStream in, HttpServletResponse resp, HttpServletRequest req)
		throws Exception
	{
		java.io.OutputStream outs = null;
		
		try
		{		    
		    System.out.println("==========================");
		    System.out.println(filename);
		    System.out.println("==========================");
		    String contentType = null;
		    try
		    {
		        contentType = (new javax.activation.MimetypesFileTypeMap()).getContentType(filename);
		    } catch(Exception e)
		    {	
		    	
		    } 
		    Logger.debug.println("contentType =============== "+contentType);
		    resp.reset();
		    if(contentType == null || contentType.equals(""))
		        contentType = "application/octet-stream";		    
		    resp.setContentLength(fileSize);
		    
		    filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
// 		    resp.addHeader("Content-Disposition", "attachment; filename=\"" + getDownFileNames(req, filename) + "\"");
			resp.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		    resp.setContentType(contentType);		    
		    resp.setCharacterEncoding("UTF-8");
		    resp.setHeader("Content-Transfer-Encoding", "binary");
		    outs = resp.getOutputStream();		    
		    
		    	    
		    byte dd[] = new byte[4096];
		    for(int leng = 0; (leng = in.read(dd)) > 0;)
		    	outs.write(dd, 0, leng);
		
		    outs.flush();		    
		} catch(Exception ex)
		{
			ex.printStackTrace();
		}finally
		{
		    if(in != null) try{in.close();}catch(Exception exception1) { }
		    if(outs != null) try{outs.close();}catch(Exception exception2) { }
		}
		return;
	}
	
	
	protected void sendFileStream(String filename, int fileSize, java.io.File f, HttpServletResponse resp, HttpServletRequest req)
			throws Exception
	{
		java.io.BufferedInputStream in = null;
		java.io.BufferedOutputStream outs = null;
		
		try
		{		    
			in = new java.io.BufferedInputStream(new java.io.FileInputStream(f));
			
		    String contentType = null;
		    try
		    {
		        contentType = (new javax.activation.MimetypesFileTypeMap()).getContentType(filename);
		    } catch(Exception e)
		    {	
		    	contentType = "";
		    } 
		    
		    resp.reset();
		    if(contentType == null || contentType.equals(""))
		        contentType = "application/octet-stream";		    
		    resp.setContentLength(fileSize);
		    resp.addHeader("Content-Disposition", "attachment; filename=\"" + getDownFileNames(req, filename) + "\"");
		    resp.setContentType(contentType);		    
		    resp.setCharacterEncoding("UTF-8");
		    resp.setHeader("Content-Transfer-Encoding", "binary");
		    outs = new java.io.BufferedOutputStream( resp.getOutputStream());
		    
		    byte buffer[] = new byte[1024*1024*1];
		    int n=-1;
		    while((n = in.read(buffer))>-1)
		    {		    	
		    	outs.write(buffer,0,n);
		    	outs.flush();
		    }
		    
		    if( outs != null )
		    {
		    	buffer = null;
		    	outs.flush();
		    	outs.close();
		    }
		} catch(java.io.IOException iex)
		{
			Logger.warn.println(iex.toString());
		} catch(Exception ex)
		{
			Logger.warn.println(ex.toString());
		}finally
		{
		    if(in != null) try{in.close();}catch(Exception exception1) { }
		    if(outs != null) try{outs.close();}catch(Exception exception2) { }
		}
		return;
	}
	
	
	protected void sendBase64Stream(String filename, int fileSize, byte[] bytes, HttpServletResponse resp, HttpServletRequest req)
			throws Exception
	{
		java.io.InputStream in = null;
		java.io.OutputStream outs = null;
		
		try
		{	
			in = new java.io.ByteArrayInputStream(bytes);
		    String contentType = "application/octet-stream";
		    
		    resp.reset();		      
		    resp.setContentLength(fileSize);
		    resp.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		    resp.setContentType(contentType);		    
		    //resp.setCharacterEncoding("UTF-8");
		    resp.setHeader("Content-Transfer-Encoding", "binary");
		    outs = resp.getOutputStream();
		    
		    byte dd[] = new byte[4096];
		    for(int leng = 0; (leng = in.read(dd)) > 0;)
		    	outs.write(dd, 0, leng);
		
		    outs.flush();		    
		} catch(java.io.IOException iex)
		{
			Logger.warn.println(iex.toString());
		} catch(Exception ex)
		{
			Logger.warn.println(ex.toString());
		}finally
		{
		    if(in != null) try{in.close();}catch(Exception exception1) { }
		    if(outs != null) try{outs.close();}catch(Exception exception2) { }
		}
		return;
	}
	
			
	public String getPageString(int curpage, int total, int rows_per_page, String scriptFunc)
	{
		
		StringBuffer sb = new StringBuffer();
		int cntSize = 0 ;
		
		int totalPage  = 0;
		int startPage  = 0;
		int endPage  = 0;
		
		cntSize = 10;
		
		//System.out.println((((double)curpage-1)/(double)cntSize)*10);
		
		if ( (total%rows_per_page)==0) 
			totalPage = total/rows_per_page;
		else  
			totalPage = (total/rows_per_page)+1;
		
		startPage = (((curpage-1)/cntSize)*cntSize) ;
		
		//System.out.println(curpage+", "+startPage);
		
		endPage   = startPage +cntSize;
		
		if (endPage>=totalPage) endPage= totalPage;
		
		startPage++;
		
		System.out.println("curpage:"+curpage+", startPage:"+startPage+", endPage:"+endPage+", totalPage:"+totalPage);	
			
		
		if (  curpage > cntSize )
		{
			sb.append("<li>").append("<a href=\"javascript:"+scriptFunc+"('1');\" onMouseMove=\"window.status='Go first page.';\">First</a>").append("</li>");
			
		}
		else
			sb.append ("<li><a href='#'>First</a></li>") ;
	
	
		if ( startPage > cntSize)
		{
			sb.append("<li>").append("<a href=\"javascript:"+scriptFunc+"('"+String.valueOf(startPage-1)+"'); \" onMouseMove=\"window.status='Go previous page.';\">Prev</a>").append("</li>");
			
		}
		else
			sb.append ( "<li><a href='#'>Prev</a></li>");
		
		
		
		if (total ==0) 
		{
			sb.append ("<li><a href='#'>1</a></li>");
		}
		else
		{
		  for (int i=startPage ; i<= endPage ; i++)
		  {
		  	
			  if (i==curpage)
			  	sb.append("<li>").append("<a href=\"javascript:"+scriptFunc+"('"+i+"'); \" onMouseMove=\"window.status='Go "+i+" page.';\"><b>"+i+"</b></a>").append("</li>");
			  else if( i<=totalPage)
				sb.append("<li>").append("<a href=\"javascript:"+scriptFunc+"('"+i+"'); \" onMouseMove=\"window.status='Go "+i+" page.';\">"+i+"</a>").append("</li>");
		  	
		  }
		}
		
		
		
		if ( totalPage > endPage)
		{
			sb.append("<li>").append("<a href=\"javascript:"+scriptFunc+"('"+String.valueOf(endPage+1)+"'); \" onMouseMove=\"window.status='Go next page.';\">Next</a>").append("</li>");		   	
		}
		else
			sb.append ("<li><a href='#'>Next</a></li>");
			
		if ( totalPage > curpage && totalPage > cntSize )
		{
			sb.append("<li>").append("<a href=\"javascript:"+scriptFunc+"('"+String.valueOf(totalPage)+"'); \" onMouseMove=\"window.status='Go last page.';\">Last</a>").append("</li>");			
		}
		else
			sb.append ("<li><a href='#'>Last</a></li>") ;
		
		return sb.toString();
    
	}
	
	public void makeScalrThumbNail(DataSet input, int dw , int dh)
	{	 
		 if(input != null && input.getCount("file_nm") > 0 )
		{
			String thumb_path = "";
			String org_path = "";
			try
			{
				Config conf = Configuration.lookup("/file/thumb");
				thumb_path = conf.getString("path");
				
				conf = Configuration.lookup("/MultipartAttributes");
				org_path = conf.getString("dir");
			}catch(Exception e){}
			
			for(int idx=0; idx< input.getCount("file_nm"); idx++)
			{	
				try
				{
					String file_path = input.getText("file_path" ,idx);
					String file_nm = input.getText("file_nm" ,idx);
					
					File f = new File(org_path + java.io.File.separator + file_path + java.io.File.separator + file_nm);
					com.kabm.util.ThumbnailMaker.doMakeScalrThumbnail(f, thumb_path + java.io.File.separator + file_path , dw, dh);
				}catch(Exception e){
					
				}
			}
		}
	}
	
	public void makeThumbNail(DataSet input)
	{
		
		//첨부파일.
		 String file_nm = input.getText("file_nms");
		 String file_path = input.getText("file_paths");
		 
		 String[] file_nms = SmartStringArray.split(",", file_nm);
		 String[] file_paths = SmartStringArray.split(",", file_path);
		 	 
		 if(file_nms != null && file_nms.length > 0 )
		{
			String thumb_path = "";
			String org_path = "";
			try
			{
				Config conf = Configuration.lookup("/file/thumb");
				thumb_path = conf.getString("path");
				
				conf = Configuration.lookup("/MultipartAttributes");
				org_path = conf.getString("dir");
			}catch(Exception e){}
			
			for(int idx=0; idx<file_nms.length; idx++)
			{	
				try
				{
				File f = new File(org_path + java.io.File.separator + file_paths[idx] + java.io.File.separator + file_nms[idx]);
				com.kabm.util.ThumbnailMaker.doMakeThumbnail(f, thumb_path + java.io.File.separator + file_paths[idx] , input.getText("attach_div_cd"));
				}catch(Exception e){
					
				}
			}					
			
		}
	}
	
	public void sendSMS(String subject, String sms_msg, String user_nm, String tel_no  ) throws Exception
	{
		InteractionBean interact = new InteractionBean();
		
		DataSet input = new DataSet();
		input.put("subject", subject);
		input.put("sms_msg", sms_msg);
		input.put("send_date", DateTime.getTimestampString() );
		input.put("callback", "024655900");
		input.put("dest_info", user_nm + "^" + tel_no);
		input.put("dest_count", "1");
		input.put("cmd", "sendSMS");			
		interact.execute( "/common/SendSms" , input);
	}
	
	public void sendMMS(String subject, String sms_msg, String user_nm, String tel_no  ) throws Exception
	{
		InteractionBean interact = new InteractionBean();
		
		DataSet input = new DataSet();
		input.put("subject", subject);
		input.put("sms_msg", sms_msg);
		input.put("send_date", DateTime.getTimestampString() );
		input.put("callback", "024655900");
		input.put("dest_info", user_nm + "^" + tel_no);
		input.put("dest_count", "1");
		input.put("cmd", "sendMMS");			
		interact.execute( "/common/SendSms" , input);
	}
	
	public void removeOfflineSession(javax.servlet.http.HttpServletRequest req)
	{
		try
		{    		
			req.getSession().removeAttribute(OFFLINE_SESSION_KEY);
			
		} catch(Exception ex)
		{
		}
	}
	
	public void setOfflineSession(javax.servlet.http.HttpServletRequest req, DataSet user)
	{
		try
		{    		
			SessionAttributes sessionAttribute = new SessionAttributes(req); 
			sessionAttribute.setAttribute(OFFLINE_SESSION_KEY, user);
		
		} catch(Exception ex)
		{
		}
	}
	
	public jdf.framework.core.data.DataSet getOfflineSession(javax.servlet.http.HttpServletRequest req)
	{
		SessionAttributes sessionAttribute = null;
		jdf.framework.core.data.DataSet session_user = null;
		try
		{    		
			sessionAttribute = new SessionAttributes(req); 
			session_user = (jdf.framework.core.data.DataSet)(sessionAttribute).getAttribute(OFFLINE_SESSION_KEY);
		
			return session_user;	
		} catch(Exception ex)
		{
		}
		return null;	
	}	
	
	public String getOfflineSeesionInfo(HttpServletRequest req, String key)
	{		
		jdf.framework.core.data.DataSet user = getOfflineSession(req);
		String sessionInfo = "";
		if(user != null) sessionInfo = user.getText(key);		
		return sessionInfo;		
	}
	
	public static final String IS_MOBILE = "MOBILE";
	private static final String IS_PHONE = "PHONE";
	public static final String IS_TABLET = "TABLET";
	public static final String IS_PC = "PC";
	public static final String IS_ANDROID = "ANDROID";
	
	public boolean isAndroid(HttpServletRequest req)
	{
		String userAgent = req.getHeader("User-Agent").toUpperCase();
		System.out.println( "userAgent==============>"+userAgent);
	    if(userAgent.indexOf(IS_ANDROID) > -1) {
	        return true;
		} else
			return false;
		
	}
	
	public boolean isMobile(HttpServletRequest req)
	{
		String userAgent = req.getHeader("User-Agent").toUpperCase();
		System.out.println( "userAgent==============>"+userAgent);
	    if(userAgent.indexOf(IS_MOBILE) > -1) {
	        return true;
		} else
			return false;
		
	}
	
	public String replaceCDATA2(String data) 
	{		
		StringBuffer buf = new StringBuffer();
		try
		{
			data = data.replaceAll("&quot;", "\"").replaceAll("&#039;", "\'");        
	        data = data.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
			
			data = StringFormater.replaceStr(StringFormater.replaceStr(data, "<", "("), ">", ")");
			
		} catch(Exception ex)
		{			
		}
		
		if (data.indexOf("<") > -1) {
			buf.append("<![CDATA[").append(data).append("]]>");
		} else if (data.indexOf("&") > -1) {
			buf.append("<![CDATA[").append(data).append("]]>");
		} else if (data.indexOf(">") > -1) {
			buf.append("<![CDATA[").append(data).append("]]>");
		}else{
			buf.append(data);
		}
		return buf.toString();

	}
%>
