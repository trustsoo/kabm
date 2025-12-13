package jdf.framework.logic.adapter.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.ConfigurationException;
import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.Field;
import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.core.data.schema.Processor;
import jdf.framework.core.log.Logger;
import jdf.framework.core.log.LoggerFactory;
import jdf.framework.core.log.LoggerWriter;
import jdf.framework.core.util.StringFormater;
import jdf.framework.core.util.Utility;
import jdf.framework.core.xml.DocBuilder;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


public class HttpProcessor extends Processor
{	
	private String LOG_ID = "";
	
	
	private static LoggerWriter logger = null;
	static
	{
		try
		{
			logger = LoggerFactory.getLoggerWriter("legacy");
		} catch(ConfigurationException ce)
		{
			
		}
	}
	
	private String xPathSeperator = "/";
	
	public HttpProcessor()
    {
        super();
        
    }
	
	private ResponseHandler<ByteArrayInputStream> responseBytesHandler = new ResponseHandler<ByteArrayInputStream>() 
	{
		 
        @Override
        public ByteArrayInputStream handleResponse(HttpResponse response) throws IOException {
 
            int status = response.getStatusLine().getStatusCode(); // HTTP 상태코드         
                         
            if (status == HttpStatus.SC_OK) { // 200 인경우 성공
                HttpEntity entity = response.getEntity();
                
                //return entity != null ? entity.getContent() : null;
                return entity != null ? new ByteArrayInputStream(EntityUtils.toByteArray(entity)) : null;
            } else {
                ClientProtocolException e = new ClientProtocolException(status+"");
                throw e; // 상태코드 200이 아닌경우 예외발생
            }
 
        }
         
    };
	
	
	
	
	@Override
	protected void executeAll(DataSet input, DataSet output) throws Exception 
	{	
		
		ByteArrayInputStream bi = null;
		
		try
		{
			
			String url = getProperty(input, "url");
			String method = getProperty(input, "method");
			String charset = getProperty(input, "charset");
			String rtn_type = getProperty(input, "rtn-type");	
			String xPathRoot = getProperty(input, "xPathRoot");
			String _timeoutSec = getProperty(input, "timeoutSec");
			String contentType = getProperty(input, "contentType");
			String reqeust_type = getProperty(input, "reqeust-type");
			String request_xml_schema_uri = null;
			
			 
			Config conf = Configuration.lookup("/resource/anylogic/HttpProcessor/target-server");
			String host = conf.getString("url");
			
			System.out.println(">>>>>>>>>>>>>>url: "+url);
			System.out.println(">>>>>>>>>>>>>>reqeust-type: "+reqeust_type);
			
			url = host + url;
			
			
			System.out.println(">>>>>>>>>>>>>>url: "+url);
			
			int timeoutMils = 10 * 1000;
			
			try
			{
				timeoutMils = Integer.parseInt(_timeoutSec) * 1000;
			} catch(Exception ex)
			{				
			}
			
					
			
			if("POST".equals(method))
			{
				if(reqeust_type != null && "xml".equals(reqeust_type))
				{
					request_xml_schema_uri = getProperty(input, "request-xml-schema-uri");
					
					if(request_xml_schema_uri.indexOf("http://") == -1)
					{
						request_xml_schema_uri = host + request_xml_schema_uri;
					}
					
					
					bi = doXMLRequest(input, output, url, charset, request_xml_schema_uri, timeoutMils);
					
				}else if(reqeust_type != null && "json".equals(reqeust_type)){
					
					bi = doJsonRequest(input, output, url, charset, timeoutMils);
				}
				else
				{
					
					bi = doPost(input, output, url, charset, timeoutMils);
				}
				
				
			} else if("GET".equals(method))
			{	
				bi = doGet(input, output, url, charset, timeoutMils);				
			}
			
			if("xml".equals(rtn_type))
			{						
				Document doc = DocBuilder.getDocument(bi);
				parseResponseXML(doc, xPathRoot, output, charset);
			}else if("json".equals(rtn_type)){
				
				parseResponseJson(bi, charset, output);
			}
			
			
		} catch(Exception ex)
		{
			throw ex;
		} finally
		{
			if(bi != null) try{ bi.close(); }catch(Exception ex){}
		}
		
	}
	
	
	/*
	 * private ByteArrayInputStream doXML(DataSet input, DataSet output, String url, String charset, String request_xml_schema_uri, int timeoutMils) throws ClientProtocolException, Exception
	{
		HttpClient httpClient = new DefaultHttpClient();
		ByteArrayInputStream bi = null;
		try
		{
			HttpPost httpPost = new HttpPost(url);			
			httpPost.setHeader("Content-Type", "application/xml;charset=utf-8");
			
			String xmlString = makeXMLRequestString(input, request_xml_schema_uri);	
			
			System.out.println("xmlString:"+xmlString);
			
			
			StringEntity xmlEntity = new StringEntity(xmlString, HTTP.UTF_8);
			xmlEntity.setChunked(true);
			httpPost.setEntity(xmlEntity);		
			
			
			Logger.info.println(LOG_ID+"POST(XML) REQ:"+url);
			
			httpClient.getParams().setParameter("http.protocol.expect-contiune", false);
			httpClient.getParams().setParameter("http.connection.timeout", timeoutMils);
			httpClient.getParams().setParameter("http.socket.timeout", timeoutMils);
			
			
			
			HttpResponse response = httpClient.execute(httpPost);
			System.out.println(response.getStatusLine().getStatusCode());
			
		} catch(ClientProtocolException ce) 
		{
			throw ce;
		}catch(Exception ex)
		{
			throw ex;
		}
		return bi;
	}
	 * 
	 * */
	
	private ByteArrayInputStream doXMLRequest(DataSet input, DataSet output, String url, String charset, String request_xml_schema_uri, int timeoutMils) throws ClientProtocolException, Exception
	{
		HttpClient httpClient = new DefaultHttpClient();
		ByteArrayInputStream bi = null;
		try
		{
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/xml;charset=utf-8");
			httpPost.setHeader("Accept" , "application/xml");
			httpPost.setHeader("User-Agent" , "CS");
			httpPost.addHeader("Cache-Control", "no-cache");
			
			String xmlString = makeXMLRequestString(input, request_xml_schema_uri);
			
			
			
			StringEntity xmlEntity = new StringEntity(xmlString, HTTP.UTF_8);
			httpPost.setEntity(xmlEntity);		
			
			
			Logger.info.println(LOG_ID+"REQ POST(XML) : "+url);
			logger.println(LOG_ID+"REQ POST(XML) :"+url);
			
			Logger.info.println(LOG_ID+"REQ POST(XML) Body :"+xmlString);
			logger.println(LOG_ID+"REQ POST(XML) Body :"+xmlString);
			
			
			httpClient.getParams().setParameter("http.protocol.expect-contiune", false);
			httpClient.getParams().setParameter("http.connection.timeout", timeoutMils);
			httpClient.getParams().setParameter("http.socket.timeout", timeoutMils);
			
			
			bi = httpClient.execute(httpPost, responseBytesHandler);
			
		} catch(ClientProtocolException ce) 
		{
			throw ce;
		}catch(Exception ex)
		{
			throw ex;
		} finally
		{
			httpClient.getConnectionManager().shutdown();
		}
		return bi;
	}
	
	
	private ByteArrayInputStream doJsonRequest(DataSet input, DataSet output, String url, String charset, int timeoutMils) throws ClientProtocolException, Exception
	{
		HttpClient httpClient = new DefaultHttpClient();
		ByteArrayInputStream bi = null;
		try
		{
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
			httpPost.setHeader("Accept" , "application/json");
			httpPost.setHeader("User-Agent" , "CS");
			httpPost.addHeader("Cache-Control", "no-cache");
			
			JSONObject holder = getJsonObjectFromDataSet(input);		
			StringEntity se = new StringEntity(holder.toString(), HTTP.UTF_8);
			httpPost.setEntity(se);
			
			Logger.info.println(LOG_ID+"REQ POST :"+url);
			logger.println(LOG_ID+"REQ POST : "+url);
			logger.println(LOG_ID+"REQ POST INPUT : "+input.toString());
			
			httpClient.getParams().setParameter("http.protocol.expect-contiune", false);
			httpClient.getParams().setParameter("http.connection.timeout", timeoutMils);
			httpClient.getParams().setParameter("http.socket.timeout", timeoutMils);
			
			
			bi = httpClient.execute(httpPost, responseBytesHandler);
			
		} catch(ClientProtocolException ce) 
		{
			throw ce;
		}catch(Exception ex)
		{
			throw ex;
		}finally
		{
			httpClient.getConnectionManager().shutdown();
		}
		return bi;
	}
	

	private JSONObject getJsonObjectFromDataSet(DataSet params) {

	    //all the passed parameters from the post request
	    //iterator used to loop through all the parameters
	    //passed in the post request
	    Iterator iter = params.entrySet().iterator();

	    //Stores JSON
	    JSONObject holder = new JSONObject();

	    //using the earlier example your first entry would get email
	    //and the inner while would get the value which would be 'foo@bar.com' 
	    //{ fan: { email : 'foo@bar.com' } }
	    //While there is another entry
	    while (iter.hasNext()) 
	    {
	        //gets an entry in the params
	    	DataSet.Entry pairs = (DataSet.Entry)iter.next();
	    	
	        //creates a key for Map
	        String key = (String)pairs.getKey();
	        String value = params.getText(key);
	        
	        holder.put(key, value);
	        
//	        Logger.debug.println("value..  " +params.getText(key));
//	        //Create a new map
//	        Map m = (Map)pairs.getValue();   
//
//	        //object for storing Json
//	        JSONObject data = new JSONObject();
//
//	        //gets the value
//	        Iterator iter2 = m.entrySet().iterator();
//	        while (iter2.hasNext()) 
//	        {
//	        	Map.Entry pairs2 = (Map.Entry)iter2.next();
//	            data.put((String)pairs2.getKey(), (String)pairs2.getValue());
//	        }
//
//	        //puts email and 'foo@bar.com'  together in map
//	        holder.put(key, data);
//	        
	        Logger.debug.println("key: " + key + "-"+" value: " +value);
	    }
	    
	    return holder;
	}
	
	
	private ByteArrayInputStream doPost(DataSet input, DataSet output, String url, String charset, int timeoutMils) throws ClientProtocolException, Exception
	{
		HttpClient httpClient = new DefaultHttpClient();
		ByteArrayInputStream bi = null;
		try
		{
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("User-Agent" , "CS");
			httpPost.addHeader("Cache-Control", "no-cache");
			/*if(contentType != null && !"".equals(contentType))
				httpPost.setHeader("Content-Type", contentType);*/
			List<NameValuePair> params = getNameValueParamList(input);			
			UrlEncodedFormEntity urlEncoed = new UrlEncodedFormEntity(params, charset);			
			httpPost.setEntity(urlEncoed);
			
			Logger.info.println(LOG_ID+"REQ POST :"+url);
			logger.println(LOG_ID+"REQ POST : "+url);
			logger.println(LOG_ID+"REQ POST INPUT : "+input.toString());
			
			httpClient.getParams().setParameter("http.protocol.expect-contiune", false);
			httpClient.getParams().setParameter("http.connection.timeout", timeoutMils);
			httpClient.getParams().setParameter("http.socket.timeout", timeoutMils);
			
			
			bi = httpClient.execute(httpPost, responseBytesHandler);
			
		} catch(ClientProtocolException ce) 
		{
			throw ce;
		}catch(Exception ex)
		{
			throw ex;
		}finally
		{
			httpClient.getConnectionManager().shutdown();
		}
		return bi;
	}
	
	
	private ByteArrayInputStream doGet(DataSet input, DataSet output, String url, String charset, int timeoutMils) throws ClientProtocolException, Exception
	{
		HttpClient httpClient = new DefaultHttpClient();
		ByteArrayInputStream bi = null;
		try
		{
			List<NameValuePair> params = getNameValueParamList(input);
			
			String queryParams = "";
			
			if(params != null)			
				queryParams = "?"+URLEncodedUtils.format(params, charset);
			
			
		    Logger.info.println(LOG_ID+"REQ GET : "+url + queryParams);
		    logger.println(LOG_ID+"REQ GET : "+url + queryParams);
			
			HttpGet httpGet = new HttpGet(url + queryParams);
			httpGet.setHeader("User-Agent" , "CS");
			httpGet.addHeader("Cache-Control", "no-cache");
			/*if(contentType != null && !"".equals(contentType))
				httpGet.setHeader("Content-Type", "application/xml;charset=utf-8");*/
			httpClient.getParams().setParameter("http.protocol.expect-contiune", false);
			httpClient.getParams().setParameter("http.connection.timeout", timeoutMils);
			httpClient.getParams().setParameter("http.socket.timeout", timeoutMils);
			
			bi = httpClient.execute(httpGet, responseBytesHandler);
			
			
		} catch(ClientProtocolException ce) 
		{
			throw ce;
		}catch(Exception ex)
		{
			throw ex;
		}finally
		{
			httpClient.getConnectionManager().shutdown();
		}
		return bi;
	}
	
	
	private String makeXMLRequestString(DataSet input, String xmlSchemaUri) throws Exception
	{
		org.w3c.dom.Document doc;
		StringWriter writer = null;
		String result = null;
		try 
		{
			doc = DocBuilder.getDocument(xmlSchemaUri);
			
			Node node = doc.getDocumentElement();			
			
			//Attribute remove			
			removeXmlAttribute(node);
			
			
			NodeList list = node.getChildNodes();
			
			int nodeLength = list.getLength();
			
			for(int idx=0; idx<nodeLength; idx++)
			{
				Node childNode = list.item(idx);
				
				if(childNode.getNodeType() != Node.ELEMENT_NODE) continue;
				
				String nodeName = childNode.getNodeName();				
				
				//Attribute remove
				removeXmlAttribute(childNode);
				
				
				String value = input.getText(nodeName);
				childNode.setTextContent(value);
			}
			
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer= tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			
			writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			result = writer.getBuffer().toString();
			writer.flush();
			
			
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			throw ex;
		} finally
		{			
			if(writer != null) try{writer.close();} catch(Exception ex){}
			
		}
		
		return result;
		
	}
	
	private void removeXmlAttribute(Node node)
	{
		NamedNodeMap attributes = node.getAttributes();
		int attrLen = attributes.getLength();
		for(int k=0; k<attrLen; k++)
		{
			Node n = attributes.item(k);					
			attributes.removeNamedItem(n.getNodeName());
		}
	}
	
	
	private List<NameValuePair> getNameValueParamList(DataSet input)
	{
		
		IOSchema schema = getIOSchema();
		
		Block[] inBlock = schema.getInputBlocks();
        
        
		List<NameValuePair> params = new ArrayList<NameValuePair>();	        
        
        for(int i=0; i<inBlock.length; i++)
        {
        	jdf.framework.core.data.schema.Field[] fields = inBlock[i].getFields();
        	
        	for(int k=0; k<fields.length; k++)
        	{
        		String fieldName = fields[k].getName();        		
        		
        		int valCnt = input.getCount(fieldName);
        		
        		for(int idx=0; idx<valCnt; idx++)
    			{
    				params.add(new BasicNameValuePair(fieldName, input.getText(fieldName, idx)));
    			}
        		
        		//input.getText(fieldName)
        	}
        }
        
		return params;
	}
	
	private Document removeXmlns(Document doc, String charset) throws Exception
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			DOMSource source = new DOMSource(doc.getDocumentElement());
			StreamResult result = new StreamResult(baos);
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			transformer.transform(source, result);
		
		
			String orgxXML = new String(baos.toByteArray(), charset);
			
			Logger.debug.println(LOG_ID+"BEFORE:\n"+orgxXML);
			logger.println(LOG_ID+"RES : "+orgxXML);
			orgxXML = orgxXML.replaceAll("(<\\?[^<]*\\?>)?", "") /* remove preamble */
			  		.replaceAll("xmlns.*?(\"|\').*?(\"|\')", "") /* remove xmlns declaration */
			  		.replaceAll("(<)(\\w+:)(.*?>)", "$1$3") /* remove opening tag prefix */
			  		.replaceAll("(</)(\\w+:)(.*?>)", "$1$3"); /* remove closing tags prefix */
			
			
			Logger.debug.println("AFTER:\n"+orgxXML);
			
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(orgxXML));
			
			return doc = db.parse(is);
		} catch (TransformerConfigurationException e) {			
			throw e;
		} catch (TransformerException e) {
			throw e;
		} catch (ParserConfigurationException e) {
			throw e;
		} catch (SAXException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally
		{
			if(baos != null) try{baos.close();}catch(Exception ex){}
		}
	}
	
	
	
	private void parseResponseXML(Document doc, String xPathRoot, DataSet output, String charset) throws XPathExpressionException
	{	
		try
		{
			doc = removeXmlns(doc, charset);
		} catch(Exception ex)
		{
			Logger.warn.println(LOG_ID+Utility.getStackTrace(ex));
		}
		
		
		IOSchema schema = getIOSchema();		
		Block[] outBlock = schema.getOutputBlocks();
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		//xpath.setNamespaceContext(new HardcodedNamespaceResolver());
		String expression = null;
		
		
		for(int idx=0; idx<outBlock.length; idx++)
		{
			String blockName = outBlock[idx].getName();
			int loopCnt = outBlock[idx].getIterationNum();
			
			if(loopCnt == 0)
			{
				loopCnt = 999999; 
			}
			
			Field[] fields = outBlock[idx].getFields();
			
			for(int kdx=0; kdx<fields.length; kdx++)
			{
				Field field = fields[kdx];
				
				if("".equals(blockName))
				{
					expression = xPathRoot+xPathSeperator+field.getName();
				} else
				{
					expression = xPathRoot+xPathSeperator+blockName+xPathSeperator+field.getName();
				}
				
				
				NodeList nodes = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
				
				
				
				int limit = nodes.getLength();
				
				//System.out.println("expression:"+expression+", node.length:"+limit);
				
				
				if(limit >= loopCnt)
				{
					limit = loopCnt;
				}
				
				
				for(int i = 0, n = limit; i < n; i++)
				{
					Node node = nodes.item(i);					
					
					String kk = node.getNodeName();
					
					if(field.getProperty("datasetPrefix") != null)
					{
						kk = field.getProperty("datasetPrefix")+kk;
					}
					
					
					output.put(kk, node.getTextContent(), i);
				}
				
			}
		}		
	}
	
	private void parseResponseJson(ByteArrayInputStream bi, String charset, DataSet output) throws IOException
	{	
		
		JSONObject jsonobj = (JSONObject)JSONValue.parse(new InputStreamReader(bi,charset));
		
		if(!jsonobj.isEmpty()){
			String result = (String) jsonobj.get("result");
		    String result_message = (String) jsonobj.get("result_message");
		    
			if(result.equals("OK")){
				
				if(jsonobj.get("response") instanceof JSONArray){
					JSONArray jsonArr = (JSONArray)jsonobj.get("response");
					
					for(int i=0; i<jsonArr.size(); i++){
						
						JSONObject data = (JSONObject)jsonArr.get(i);
						
						Iterator<String> iter = data.keySet().iterator();
						
						while(iter.hasNext()){
							String key = iter.next();
							output.put(key, data.get(key), i);
						}
					}
				}else{
					
					JSONObject data = (JSONObject)jsonobj.get("response");
					Iterator<String> iter = data.keySet().iterator();
					
					while(iter.hasNext()){
						String key = iter.next();
						output.put(key, data.get(key));
					}
				}
			}
		}
	}
	
	public void setProperty(String key, String value)
    {
        this.processProperties.setProperty(key, value);
    }
	
	
	private String getProperty(DataSet input, String key)
    {
        String val = input.getProperty(key);

        if (val != null)
        {
            return val;
        }
        val = input.getText(key);
        if (val != null && val.length() > 0)
            return val;

        return this.getProperty(key);
    }
	
	
	public static void main(String[] args){
		
		
		HttpProcessor httpPro = new HttpProcessor(); 
		DataSet output = new DataSet();
		
	}
	
}