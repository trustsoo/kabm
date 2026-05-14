package com.kabm.util;


import jdf.framework.core.log.Logger;
import jdf.framework.core.util.Utility;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class HttpUtil
{
	private static final String CLASS_NAME = "<HttpUtil> ";
	
	private static final int TIMEOUT =  5000;
	
	public static UrlEncodedFormEntity setSendMessage(String emp_cd, String to, String title,String content)
		throws UnsupportedEncodingException
	{
		UrlEncodedFormEntity entity = null;
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add( new BasicNameValuePair("empcode", emp_cd));
		nvps.add( new BasicNameValuePair("to", to));
		nvps.add( new BasicNameValuePair("subject", title));
		nvps.add( new BasicNameValuePair("bodytext", content));
		
		nvps.add( new BasicNameValuePair("openapi", "true"));
		nvps.add( new BasicNameValuePair("encodetype", "UTF-8"));
		nvps.add( new BasicNameValuePair("autoarchivesent", "false"));
		nvps.add( new BasicNameValuePair("priority", "false"));
		nvps.add( new BasicNameValuePair("secure", "false"));
		
		entity = new UrlEncodedFormEntity( nvps, "UTF-8");
		return entity;
		
	}
	
	public static boolean checkStatus(String body) {
		
		boolean isok = false;
		
		JSONObject result_json = null;
		try
		{
			result_json = (JSONObject)JSONSerializer.toJSON(body);
			JSONObject result = result_json.getJSONObject("result");  
			String code = result.getString("code");
			
			if( code != null && "200".equals(code.trim())) isok = true;
			      
		}catch(ClassCastException ce){
			Logger.err.println(CLASS_NAME+Utility.getStackTrace(ce));
		}catch(NullPointerException ne){
			Logger.err.println(CLASS_NAME+Utility.getStackTrace(ne));
		}catch(Exception e){
			Logger.err.println(CLASS_NAME+Utility.getStackTrace(e));
		}
		
		return isok;
	}

	public static String stringReceive(String url) throws Exception{
		
		int TIMEOUT_VALUE = 10*1000;
		
		URL api_url = new URL(url);
		URLConnection conn = null;
		BufferedReader br = null;
		StringBuffer result = new StringBuffer();
		try
		{
			conn = api_url.openConnection();
			conn.setConnectTimeout(TIMEOUT_VALUE);
			conn.setReadTimeout(TIMEOUT_VALUE);
			conn.setDoOutput(true);
		
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
			
			while(true){
				String tmp = br.readLine();
				if(tmp == null) 
					break;
				result.append(tmp);
			}
		}catch(IOException ie){
			Logger.err.println(CLASS_NAME+Utility.getStackTrace(ie));
		}catch(Exception e){
			Logger.err.println(CLASS_NAME+Utility.getStackTrace(e));
		}finally{
			if( br != null)try{ br.close();}catch(IOException ie){};
		}
		return result.toString();
		
	}
	
	public static boolean stringReceiveCheck(String url, String params, String charSet) throws Exception{
		
		int TIMEOUT_VALUE = 10*1000;
		boolean isok = false;
		
		
		URL api_url = new URL(url);
		URLConnection conn = null;
		BufferedReader br = null;
		StringBuffer result = new StringBuffer();
		InputStream in = null;
		OutputStream out = null;
		try
		{
			conn = api_url.openConnection();
			conn.setConnectTimeout(TIMEOUT_VALUE);
			conn.setReadTimeout(TIMEOUT_VALUE);
			conn.setDoOutput(true);
			out = conn.getOutputStream();
			OutputStreamWriter wr = new OutputStreamWriter(out);
	        wr.write(params);
	        wr.flush();
	        
	        in = conn.getInputStream();
			br = new BufferedReader(new InputStreamReader(in,charSet));
		
			
			while(true){
				String tmp = br.readLine();
				if(tmp == null) 
					break;
				result.append(tmp);
			}
			isok = checkStatus( result.toString());
		}catch(IOException ie){
			Logger.err.println(CLASS_NAME+Utility.getStackTrace(ie));
		}catch(Exception e){
			Logger.err.println(CLASS_NAME+Utility.getStackTrace(e));
		}finally{
			if( in != null)try{ in.close();}catch(IOException ie){};
			if( br != null)try{ br.close();}catch(IOException ie){};
			if( out != null)try{ out.close();}catch(IOException ie){};
		}
		return isok;
		
	}
	
	public static boolean httpSend(String url, UrlEncodedFormEntity entity)
	{
		boolean isok = false;
		HttpClient httpClient = new DefaultHttpClient();
		InputStreamReader in = null;
		try
		{
			
			
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity( entity);
			HttpResponse response = httpClient.execute(httpPost);
			
			String result = "";
			in = new InputStreamReader( response.getEntity().getContent() );
			BufferedReader rd = new BufferedReader(in);
			StringBuffer sb = new StringBuffer();
			while(( result = rd.readLine()) != null ){
				sb.append( result);
			}
			Logger.debug.println( CLASS_NAME + sb.toString());
			in.close();
			
		}catch(IOException ie){
			Logger.err.println(CLASS_NAME+Utility.getStackTrace(ie));
		}catch(Exception e){
			Logger.err.println(CLASS_NAME+Utility.getStackTrace(e));
		}finally{
			if( in != null) try{ in.close(); }catch(IOException ie){}
		}
		
		return isok;
	}
	
}