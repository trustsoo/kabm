package com.kabm.util;

import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.http.HttpServletRequest;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.data.DataSet;
import jdf.framework.core.db.ConnectionManager;
import jdf.framework.core.http.SessionAttributes;
import jdf.framework.view.auth.User;
import jdf.framework.view.menu.entity.MenuItem;

public class AdminLogger
{
	private static final String LOG_ID = "<AdminLogger> ";
	
	public static void print(HttpServletRequest request)
	{
		print(request, "", "");
	}
	
	public static String getSearchEngine(String referer)
	{
		String result = "";
		
		if( (referer.toLowerCase()).indexOf("daum.net") >= 0 ) result = "DAUM";
		else if( (referer.toLowerCase()).indexOf("naver.com") >= 0 ) result = "NAVER";
		else if( (referer.toLowerCase()).indexOf("google") >= 0 ) result = "GOOGLE";
		
		return result;
	}
	
	public static void print(HttpServletRequest request, String etc1, String etc2)
	{
		try
		{
			
			SessionAttributes sessionAttribute = null;
			User session_user = null;
			String refererURI = "";
			try
			{    		
				sessionAttribute = new SessionAttributes(request); 
				session_user = (User)(sessionAttribute).getAttribute(User.SESSION_KEY );
				
				refererURI = new URI(request.getHeader("referer")).getHost();
			
			} catch(Exception ex)
			{
			}
			
			Object mItem = request.getAttribute(jdf.framework.view.menu.MenuContext.MENU);
			
			MenuItem __templetMENU = new MenuItem();
			if( mItem != null )
				__templetMENU = (MenuItem)request.getAttribute(jdf.framework.view.menu.MenuContext.MENU);
			
			String ip = NetworkUtil.getRemoteAddr(request);
			String menu_id = __templetMENU.getId();
			String user_id = "";
			String user_nm = "";
			if( session_user != null) user_id = session_user.getId();
			if( session_user != null) user_nm = session_user.getName();
			
			write(ip ,menu_id , user_id, user_nm,  (request.getRequestURI()).toString() ,getSearchEngine(refererURI) ,etc2);
		}catch(Exception e){
			
		}
	}
	
	public static void print(DataSet input)
	{
		write(input.getText("ip"), input.getText("menu_id"), input.getText("user_id"), input.getText("user_nm"), input.getText("etc1"), input.getText("etc2"), input.getText("etc3") );
	}
	
	public static void print(DataSet input, String uri, String etc1, String etc2)
	{
		write(input.getText("ip"), input.getText("menu_id"), input.getText("user_id"), input.getText("user_nm"), uri , etc1, etc2 );
	}
	
	
	public static void write(String ip, String menu,String user_id,String user_nm,  String uri, String etc1, String etc2)
	{			
		
		if( menu == null || "".equals(menu)) return;
		
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO TB_KABM_ADMIN_LOG")
		   .append("(ip, menu_id, user_id, user_nm, uri, etc1, etc2, reg_dt)").append("\n")
		   .append("VALUES").append("\n")
		   .append("( ?, ?, ?, ?, ?, ?,?, getdate())");
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try
		{
			Config conf = Configuration.lookup("/logger");
			String poolName = conf.getString("logDBPool");
			
			System.out.println(sql.toString());
			System.out.println("ip:"+ip);
			System.out.println("menu:"+menu);
			System.out.println("user_id:"+user_id);
			System.out.println("user_nm:"+user_nm);
			System.out.println("uri:"+uri);
			System.out.println("etc1:"+etc1);
			System.out.println("etc2:"+etc2);
			conn = ConnectionManager.getConnection(poolName);
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, ip);
			pstmt.setString(2, menu);
			pstmt.setString(3, user_id);
			pstmt.setString(4, user_nm);
			pstmt.setString(5, uri);
			pstmt.setString(6, etc1);
			pstmt.setString(7, etc2);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch(Exception ex)
		{
			ex.printStackTrace();
			System.out.println(LOG_ID+ex.toString());
		} finally
		{
			if(pstmt != null) try{pstmt.close();pstmt=null;}catch(Exception ex){}
			if(conn != null) try{conn.close();conn=null;}catch(Exception ex){}
		}
	}
}