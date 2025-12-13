package jdf.framework.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdf.framework.core.log.Logger;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.core.xml.XMLReferer;


/**
 * <b><code>MessageBox</code></b>
 * <p>
 * CODE -> 메세지로 변환하기 위한 Class
 * </p>
 * 
 * 예제
 * <pre>
 * MessageBox msgBox = new MessageBox();
 * String msg = msgBox.getMessage("그룹코드", "메세지코드");
 * </pre>
 * 
 * @author
 * @version 1.0
 */
public class MessageBox implements Message
{
	private final static String LOG_ID="<f:MessageBox> ";
	private static Map msgBox;
	private static Map msgListBox;
	private static List langList;
	
	
	private static String defaultLang = null;
	
	public static void initialize() throws Exception
	{			
		Config conf = Configuration.lookup("/message");
		
		String dir = conf.getString("dir", Configuration.getConfigPath()+File.pathSeparator+"messages"+File.pathSeparator);
		
		defaultLang = conf.getString("default", "kr");
		
		msgBox = new HashMap();
		msgListBox = new HashMap();
		langList = new ArrayList();
		
		String[] supportedLanguage = SmartStringArray.split(",", conf.getString("supportedLang", "kr"));
		for(int idx=0; idx<supportedLanguage.length;idx++)
		{
			initialize(supportedLanguage[idx], dir, supportedLanguage[idx]+".xml");
			langList.add(supportedLanguage[idx]);
		}
		
		
		
		
	}

	private static void initialize(String lang, String dir, String filename) throws Exception
	{
		Logger.debug.println(LOG_ID+"initialize dir=" + dir + ",file=" + filename);

		try
		{
			

			XMLReferer reader = new XMLReferer(dir, filename);

			/*      XML 을 읽는다.       */
			reader.lookup("/messageBox/group"); //lookup으로 고정

			while (reader.next())
			{
				reader.mark();

				String grpCode = reader.getString("code");

				reader.lookup("msg");

				while (reader.next())
				{
					String msgCode = reader.getString("code");

					MsgKey key = new MsgKey(lang, grpCode, msgCode);

					String msgText = reader.getText();

					Logger.debug.println(LOG_ID+lang+"-"+grpCode+"-"+msgCode+"-"+msgText);

					msgBox.put(key, msgText);

					List list = (List) msgListBox.get(grpCode);
					if (list == null)
					{
						list = new ArrayList();
						msgListBox.put(grpCode, list);
					}
					list.add(new String[] { msgCode, msgText });

				}

				reader.reset();

			}

		}
		catch (Exception e)
		{
			//e.printStackTrace();

			Logger.err.println(LOG_ID+"error. " + e.toString());
			throw e;
		}
	}

	private final static String DEFAULT_GRP = "system";

	private String grpCode = DEFAULT_GRP;
	private String code;

	public MessageBox()
	{
	}

	public MessageBox(String code)
	{
		this.code = code;
	}

	public MessageBox(String grpCode, String code)
	{
		this.grpCode = grpCode;
		this.code = code;
	}

	public MessageBox(String grpCode, int code)
	{
		this.grpCode = grpCode;
		this.code = String.valueOf(code);
	}

	public String toString()
	{

		return getMessage(grpCode, code);
	}

	private void check()
	{
		try
		{
			if (msgBox == null)
				initialize();
		}
		catch (Exception e)
		{
			return;
		}
	}

	/**
     * message.xml 에서 
     * 
     * @param grpCode 그룹코드
     * @param code 메세지 코드
     */
	public String getMessage(String grpCode, String code)
	{
		return getMessage(defaultLang, grpCode, code);
	}
	
    /**
     * message.xml 에서 
     * 
     * @param grpCode 그룹코드
     * @param code 메세지 코드
     */
	public String getMessage(String lang, String grpCode, String code)
	{
		check();

		if (code == null || code.length() == 0)
			return "";
		
		if(lang == null) lang = defaultLang;
		
		if(!langList.contains(lang)) lang = defaultLang;
		
		
		MsgKey key = new MsgKey(lang, grpCode, code);

		return (String) msgBox.get(key);
	}

	public String getMessage(String grpCode, int code)
	{
		return getMessage(defaultLang, grpCode, String.valueOf(code));
	}
	
	public String getMessage(String lang, String grpCode, int code)
	{
		return getMessage(lang, grpCode, String.valueOf(code));
	}
	

	public static void main(String[] args) throws Exception
	{
		MessageBox box = new MessageBox();
		System.out.println(box.getMessage("kr","ACL",  "ACL0001"));
		System.out.println(box.getMessage("en","ACL",  "ACL0001"));
		//initialize();
		
		//initialize("kr", ".", "message.xml");

		//System.out.println(getMessage("신용거래구분", "01"));

	}

	/* (non-Javadoc)
	 * @see jdf.framework.core.Message#getList(java.lang.String)
	 */
	@Override
	public List getList(String grpCode) {
		// TODO Auto-generated method stub
		return null;
	}

	/*public java.util.List getList(String grpCode)
	{
		check();

		return (List) msgListBox.get(grpCode);
	}*/

}