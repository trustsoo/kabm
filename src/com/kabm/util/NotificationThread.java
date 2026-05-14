package com.kabm.util;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.ConfigurationException;
import jdf.framework.core.data.DataSet;
import jdf.framework.core.log.Logger;
import jdf.framework.core.mail.MailException;
import jdf.framework.core.mail.TemplateMail;
import jdf.framework.core.util.SmartStringArray;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;


public class NotificationThread extends Thread
{
	private String CLASS_NAME = "<at:NotificationThread> ";
	
	private String mode = null;
	private String hagoutMsg = null;
	private DataSet users;
	private String cmpny_no = null;
	private String mailTitle = null;
	private Hashtable<String, String> mailArgs = null;
	
	
	public void setting(String cmpny_no, String mode, String hagoutMsg, DataSet users)
	{
		this.cmpny_no = cmpny_no;
		this.mode = mode;
		this.hagoutMsg = hagoutMsg;
		this.users = users;
	}
	
	//Mail이 포함된 경우..
	public void setting(String cmpny_no, String mode, String mailTitle, Hashtable<String, String> mailArgs, String hagoutMsg, DataSet users)
	{
		this.cmpny_no = cmpny_no;
		this.mode = mode;
		this.hagoutMsg = hagoutMsg;
		this.users = users;
		this.mailTitle = mailTitle;
		this.mailArgs = mailArgs;
	}
	
	
	private void sendMail()
	{
		TemplateMail mailer = null;
		try
		{
			String[] recipients = new String[users.getMaxDataSize()];
			String[] persons = new String[users.getMaxDataSize()];
			for(int idx=0; idx<users.getMaxDataSize(); idx++)
			{
				recipients[idx] = users.getText("email", idx);
				persons[idx] = users.getText("emp_nm", idx);
			}
			
			Config conf = Configuration.lookup("/mail");
			String templateDir = conf.getString("templateDir");
			Properties props = new Properties();
			props.put("mail.smtp.host", conf.getString("host"));
			props.put("mail.smtp.port", conf.getString("port", "25"));
			props.put("mail.smtp.auth", conf.getString("auth"));
			props.put("admin", conf.getString("admin"));
			props.put("adminName", conf.getString("adminName"));
			
			mailer = new TemplateMail();
			mailer.setHtmlTemplate(templateDir+"notification.html");			
			mailer.setSubject(mailTitle);
			mailer.setRecipients(recipients, persons);
			
			//mailer.setRecipient("advan94@gmail.com", "advan94");
			
			Iterator<String> ite = mailArgs.keySet().iterator();
			while(ite.hasNext())
			{
				String key = ite.next();
				mailer.setArg(key, mailArgs.get(key));
			}
			
			
			mailer.send();
		} catch(ConfigurationException cfe)
		{
			Logger.warn.println(CLASS_NAME+cfe.getMessage());
		} catch(MailException mex)
		{
			Logger.warn.println(CLASS_NAME+mex.getMessage());
		}catch(Exception ex)
		{
			
			Logger.warn.println(CLASS_NAME+ex.getMessage());
		}
	}
	
	
	
	public void run()
	{
		process();
		
	}
	
	public void process()
	{
		
		
		//DataSet userData = new DataSet();
		/*if(mode.indexOf("MAIL") != -1 || mode.indexOf("HANGOUT") != -1)
		{
			try
			{
				userData = getNotiUserInfos();
			} catch(Exception ex)
			{
				Logger.warn.println(CLASS_NAME+ex.getMessage());
			}
		}*/
		
		String[] strArray = SmartStringArray.split("|", mode);
		for(String aa: strArray)
		{
			if(aa.equals("MAIL")) sendMail();
		}		
	}
	
	/*private DataSet getNotiUserInfos() throws Exception
	{
		InteractionBean interact = new InteractionBean();
		DataSet input = new DataSet();
		input.put("cmd", "getUsersNotiInfo");
		DataSet output = new DataSet();
		try
		{
			String[] userArray = new String[users.size()];
			userArray =	users.toArray(userArray);
			
			input.put("emp_nos", SmartStringArray.join(",", userArray));
			output = interact.execute("/organization/Emp", input);
		} catch(Exception ex)
		{
			throw ex;
		}
		return output;
	}*/	
	
	
}


