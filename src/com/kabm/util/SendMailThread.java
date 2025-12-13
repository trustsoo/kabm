package com.kabm.util;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.ConfigurationException;
import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.InteractionBean;
import jdf.framework.core.log.Logger;
import jdf.framework.core.mail.MailException;
import jdf.framework.core.mail.TemplateMail;


public class SendMailThread extends Thread
{
	private String CLASS_NAME = "<at:SendMailThread> ";
	
	private DataSet users;
	private String title = null;
	private String template = null;
	private Hashtable<String, String> mailArgs = null;
	
	public void setting( String template, String title, Hashtable<String, String> mailArgs, DataSet users)
	{
		this.users = users;
		this.title = title;
		this.template = template;
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
				persons[idx] = users.getText("name", idx);
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
			mailer.setHtmlTemplate(templateDir+template);			
			mailer.setSubject(title);
			mailer.setRecipients(recipients, persons);
			
			Iterator<String> ite = mailArgs.keySet().iterator();
			while(ite.hasNext())
			{
				String key = ite.next();
				mailer.setArg(key, mailArgs.get(key));
			}
			
			
			mailer.send();
			
			for(int idx=0; idx<users.getMaxDataSize(); idx++)
			{
				DataSet input = new DataSet();
				input.put("sender", conf.getString("admin"));	
				input.put("receiver", users.getText("email", idx));	
				input.put("msg", mailArgs.get("title") + " " + mailArgs.get("content"));	
				input.put("title", title );	
				input.put("user_id", mailArgs.get("user_id"));	
				input.put("cmd", "sendEmailHst");	
				InteractionBean interact = new InteractionBean();
				interact.execute( "/common/SendEmail" , input);
			}
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
		sendMail();
	}
}