package jdf.framework.core.mail;

/**
 * @(#)
 * Copyright 1999-2000 by  LG-EDS Systems, Inc.,
 * Information Technology Group, Application Architecture Team, 
 * Application Intrastructure Part.
 * 236-1, Hyosung-2dong, Kyeyang-gu, Inchun, 407-042, KOREA.
 * All rights reserved.
 * 
 * NOTICE !      You can copy or redistribute this code freely, 
 * but you should not remove the information about the copyright notice 
 * and the author.
 *
 *
 * @author  WonYoung Lee, wyounglee@lgeds.lg.co.kr.
 *
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.SmartStringArray;

/**
 * <xmp>
 *
 *
 * Java Mail API를 이용해 POP3 Cient 같은 프로그램을 쉽게 만들 수 있습니다.
 * 이 Mail 클래스는  Java Mail API를 이용해 한번 더 추상화 레벨을 올려 아주 간단하게
 * Mail를 보낼 수 있게 만들어 둔 클래스입니다.
 *. 
 * Sample Code :
 * ------------------------
 * String message = "안녕하세요?\n반가워요...";
 * try {
 *     Mail mailer = new Mail();
 *     mailer.setFrom("leewy7@kornet.net","홍길동");
 *     mailer.setRecipient("wyounglee@lgeds.lg.co.kr", "이원영");
 *     mailer.setSubject("안녕하세요");
 *     mailer.setTextContent(message);
 *     mailer.send();
 * } 
 * catch(Exception e){
 *     ...
 * } 
 * ------------------------
 *. 
 * 만약 HTML형식의 메일을 보내시려면, Mail.setTextContent() 대신 Mail.setHtmlContent()
 * 를 사용하시면 됩니다.
 *. 
 *. 
 *. 
 * PS: Mail.java는 Configuration.java파일을 필요로 합니다.
 *  각종 설정 정보를 특정파일에 기록해 두면서 사용하는 거지요.
 *. 
 * framework.conf 파일은 아래와 같은 형식으로 되어 있습니다.
 *. 
 * ############################################################################
 * #
 * # Java Development Framework Configuration File
 * # 1999년 11월 23일 현재
 * #
 * ############################################################################
 * # Java Mail API 1.1.2
 * 	mail.host    = localhost
 * 	mail.admin   = wyounglee@lgeds.lg.co.kr
 * 	mail.admin.name  = 이원영
 * 	mail.cc.flag = false
 * 	mail.cc      = wyounglee@lgeds.lg.co.kr
 * 	mail.templatedir = /home/mail/template
 * ############################################################################
 *.
 *. 
 *. 
 * 
 * </xmp>
 * FAQ 사이트 : http://java.sun.com/products/javamail/FAQ.html
 *
 * @see TemplateMail
 * @see jdf.framework.framework.core.Configuration
 */
public class Mail 
{
	private final String LOG_ID="<at:Mail> ";
	javax.mail.Message msg = null;
	InternetAddress cc = null;
	Session session = null;
	InternetAddress[] toAddr = null;
	Properties mailProp = null;
	File[] file = null;
	/* This class implements a typed DataSource from :
	 * 	an InputStream
	 *	a byte array
	 * 	a String
	 */

	class ByteArrayDataSource implements DataSource {
	    private byte[] data; // data
	    private String type; // content-type

	    /* Create a datasource from an input stream */
	    ByteArrayDataSource(InputStream is, String type) {
	        this.type = type;
	        try { 
	            ByteArrayOutputStream os = new ByteArrayOutputStream();
		    int ch;

		    while ((ch = is.read()) != -1)
	                // XXX : must be made more efficient by
		        // doing buffered reads, rather than one byte reads
		        os.write(ch);
		    data = os.toByteArray();

	        } catch (IOException ioex) { }
	    }

	    /* Create a datasource from a byte array */
	    ByteArrayDataSource(byte[] data, String type) {
	        this.data = data;
		this.type = type;
	    }

	    /* Create a datasource from a String */
	    ByteArrayDataSource(String data, String type) {
		try {
		    // Assumption that the string contains only ascii
		    // characters ! Else just pass in a charset into this
		    // constructor and use it in getBytes()
		    this.data = data.getBytes("utf-8");
		    //this.data = data.getBytes();
		} catch (UnsupportedEncodingException uex) { }
		//} catch (Exception uex) { }
			this.type = type;
	    }

	    public InputStream getInputStream() throws IOException {
		if (data == null)
		    throw new IOException("no data");
		return new ByteArrayInputStream(data);
	    }

	    public OutputStream getOutputStream() throws IOException {
		throw new IOException("cannot do this");
	    }

	    public String getContentType() {
	        return type;
	    }

	    public String getName() {
	        return "dummy";
	    }
	}

	public Mail() throws MailException
	{
		this(null);
	}
	
	/**
	 * 
	 */
	public Mail(Properties props) throws MailException 
	{
		super();
		String adminName = null;
		String admin = null;
		String cc_flag = null;
		String cc_addr = null;
		try
		{
			if(props == null)
			{			
				Config conf = Configuration.lookup("/mail");
				
				String mailAuth = conf.getString("auth", "false");
				
				props = new java.util.Properties();
				props.put("mail.smtp.host", conf.getString("host"));
				props.put("mail.smtp.port", conf.getString("port", "25"));
				props.put("mail.smtp.auth", mailAuth);				
				props.put("authID", conf.getString("authID"));
				props.put("adminPassword", conf.getString("adminPassword"));
				
				admin = conf.getString("admin");
				adminName = conf.getString("adminName");
				cc_flag = conf.getString("ccFlag").toLowerCase().trim();
				cc_addr = conf.getString("cc");
				if("true".equals(mailAuth))
				{
					SMTPAuthenticator auth = new SMTPAuthenticator(conf.getString("authID"), conf.getString("adminPassword"));	
					Logger.debug.println(LOG_ID+conf.getString("authID")+", "+conf.getString("adminPassword"));
					session  = Session.getDefaultInstance(props, auth);
					session  = Session.getInstance(props, auth);
				} 
				else
				{
					
					//session  = Session.getDefaultInstance(props, null);
					session  = Session.getInstance(props, null);
				}
				
			}else
			{	
				
				admin = props.getProperty("admin");
				adminName = props.getProperty("adminName");
				String mailAuth = props.getProperty("mail.smtp.auth", "false");
				cc_flag = props.getProperty("ccFlag");
				cc_addr = props.getProperty("cc");
				if("true".equals(mailAuth))
				{
					SMTPAuthenticator auth = new SMTPAuthenticator(props.getProperty("authID"), props.getProperty("adminPassword"));	
					Logger.debug.println(LOG_ID+props.getProperty("authID")+", "+props.getProperty("adminPassword"));
					session  = Session.getDefaultInstance(props, auth);
					session  = Session.getInstance(props, auth);
				} 
				else
				{	
					session  = Session.getInstance(props, null);
				}
				
			}			
		
			session.setDebug(true);
	
			msg = new MimeMessage(session);
			msg.setSentDate(new java.util.Date());
			
			InternetAddress from = new InternetAddress(admin,adminName, "utf-8");
			msg.setFrom(from);
			
	
			if ( cc_flag.equalsIgnoreCase("true") ) 
			{
				
				String[] ccAddr = SmartStringArray.split(",", cc_addr);
				
				InternetAddress[] cc = new InternetAddress[ccAddr.length];
				for(int idx=0; idx<cc.length;idx++)
					cc[idx] = new InternetAddress(ccAddr[idx]);
				
				msg.setRecipients(Message.RecipientType.CC, cc);
			}
		} catch(MessagingException e)
		{
			throw new MailException(e.getMessage());
		} catch(Exception e) 
		{
			throw new MailException(e.getMessage());
		}
		
	}
	
	public void setFile(File[] file)
	{
		this.file = file;
	}

	/**
	 * 
	 */
	public void send() throws MailException {
		try 
		{
			Address[] address = msg.getAllRecipients();
			StringBuffer sb = new StringBuffer();
			for(Address a : address)
			{
				sb.append(a.toString()).append(" ");
			}
			
			Transport.send(msg);
			
			Logger.debug.println(LOG_ID+sb.toString()+" mail sent.");
		}
		catch(MessagingException e){
			e.printStackTrace();
			try {Thread.sleep(5);}catch(InterruptedException ex) {}
			throw new MailException(e.getMessage());
		}
	}
	
	
	
	
	public void setCarbonCopy(String[] address) throws MailException 
	{
		if ( address != null && address.length > 0 )
		{
			try 
			{
				InternetAddress[] users = new InternetAddress[address.length];
				for ( int i=0; i< address.length; i++ ) {
					users[i] = new InternetAddress(address[i]);
				}
				msg.setRecipients(Message.RecipientType.CC, users);
			}
			catch(AddressException e) 
			{
				throw new MailException(e.getMessage());
			}
			catch(MessagingException e)
			{
				throw new MailException(e.getMessage());
			}
		}
	}

	public void setContent(java.lang.Object o,  java.lang.String type)  throws MailException 
	{
		try {
			msg.setContent(o,type);
		}
		catch(MessagingException e){
			throw new MailException(e.getMessage());
		}
		catch(Throwable e){
			throw new MailException(e.getMessage());
		}
	}

	public void setContent(Multipart mp) throws MailException 
	{
		try {
			msg.setContent(mp);
		}
		catch(MessagingException e){
			throw new MailException(e.getMessage());
		}
		catch(Throwable e){
			throw new MailException(e.getMessage());
		}
	}

	/**
	 * Default - the value of mail.admin.
	 * 명시적으로 setFrom(email) 혹은 setFrom(email, name) 을 사용하지 않으면 
	 * configuration 파일의 mail.admin 값으로 셋팅됩니다.<br>
	 * 명시적으로 발신자를 바꾸고자 할 때 사용하세요....
	 *
	 * @param from java.lang.String
	 * @see #setFrom(java.lang.String, java.lang.String)
	 */
	public void setFrom(String address) throws MailException {
		try {
			InternetAddress user = new InternetAddress(address);
			msg.setFrom(user);
		}catch(AddressException e) {
			throw new MailException(e.getMessage());
		}
		catch(MessagingException e){
			throw new MailException(e.getMessage());
		}
	}

	/**
	 * Default - the value of mail.admin.
	 * 명시적으로 setFrom(email) 혹은 setFrom(email, name) 을 사용하지 않으면 
	 * configuration 파일의 mail.admin 값으로 셋팅됩니다.<br>
	 * 명시적으로 발신자를 바꾸고자 할 때 사용하세요....
	 *
	 * @param from java.lang.String
	 * @param from java.lang.String
	 *
	 * @see #setFrom(java.lang.String)
	 */
	public void setFrom(java.lang.String address, java.lang.String personal) throws MailException {
		try {
			InternetAddress user = new InternetAddress(address, personal, "utf-8");
			msg.setFrom(user);
		}
		catch(Exception e) {
			throw new MailException(e.getMessage());
		}
	}

	/**
	 * @deprecated
	 * @param content java.lang.Object
	 * 일부 메일서버는....Content-Transfer-Encoding을 무조건..base64로 해야 한글 컨텐츠를 보낼수 있다.    
	 */
	public void setHtmlContent(String html) throws MailException {
		try {
			msg.setDataHandler(new DataHandler(
				new ByteArrayDataSource(html, "text/html; charset=utf-8")));
			msg.setHeader("Content-Transfer-Encoding", "7bit");
		
			
			/*msg.setDataHandler(new DataHandler(
					new ByteArrayDataSource(html, "text/html; charset=EUC-KR")));
			msg.setHeader("Content-Transfer-Encoding", "base64");*/
		}
		catch(MessagingException e) {
			throw new MailException(e.getMessage());
		}
		catch(Exception e) {
			throw new MailException(e.getMessage());
		}
	}

	/**
	 * @param to java.lang.String
	 */
	public void setRecipient(String recipient) throws MailException {
		try {
			InternetAddress[] users = { new InternetAddress(recipient) };
			msg.setRecipients(Message.RecipientType.TO, users);
		}
		catch(AddressException e) {
			throw new MailException(e.getMessage());
		}
		catch(MessagingException e) {
			throw new MailException(e.getMessage());
		}
		catch(Exception e) {
			throw new MailException(e.getMessage());
		}
	}

	/**
	 * @param address e-mail address
	 * @param personal sender name
	 */
	public void setRecipient(java.lang.String address, java.lang.String personal) throws MailException {
		try {
			InternetAddress[] users = { new InternetAddress(address, personal,"utf-8") };
			msg.setRecipients(Message.RecipientType.TO, users);
		}
		catch(AddressException e) {
			throw new MailException(e.getMessage());
		}
		catch(MessagingException e) {
			throw new MailException(e.getMessage());
		}
		catch(Exception e) {
			throw new MailException(e.getMessage());
		}
	}

	/**
	 * @param recipients java.lang.String[]
	 */
	public void setRecipients(String[] recipients) throws MailException {
		try {
			InternetAddress[] users = new InternetAddress[recipients.length];
			for ( int i=0; i< recipients.length; i++ ) {
				users[i] = new InternetAddress(recipients[i]);
			}
			msg.setRecipients(Message.RecipientType.TO, users);
		}
		catch(AddressException e) {
			throw new MailException(e.getMessage());
		}
		catch(MessagingException e) {
			throw new MailException(e.getMessage());
		}
		catch(Exception e) {
			throw new MailException(e.getMessage());
		}
	}
	
	public void setRecipients(String[] recipients, String[] person) throws MailException {
		try {
			InternetAddress[] users = new InternetAddress[recipients.length];
			for ( int i=0; i< recipients.length; i++ ) {
				users[i] = new InternetAddress(recipients[i], person[i], "utf-8");
			}
			msg.setRecipients(Message.RecipientType.TO, users);
		}
		catch(AddressException e) {
			throw new MailException(e.getMessage());
		}
		catch(MessagingException e) {
			throw new MailException(e.getMessage());
		}
		catch(Exception e) {
			throw new MailException(e.getMessage());
		}
	}

	/**
	 * @param recipients Vector
	 */
	public void setRecipients(java.util.Vector recipients) throws MailException {
		try {
			InternetAddress[] users = new InternetAddress[recipients.size()];
			java.util.Enumeration enm = recipients.elements();
			for(int i=0 ; enm.hasMoreElements(); i++ ) {
				String recipient = (String)enm.nextElement();
				users[i] = new InternetAddress(recipient);
			}
			msg.setRecipients(Message.RecipientType.TO, users);
		}
		catch(AddressException e) {
			throw new MailException(e.getMessage());
		}
		catch(MessagingException e) {
			throw new MailException(e.getMessage());
		}
		catch(Exception e) {
			throw new MailException(e.getMessage());
		}
	}

	/**
	 * @param subject java.lang.String
	 */
	public void setSubject(String subject) throws MailException {
		try {
			((MimeMessage)msg).setSubject(subject, "utf-8");
		}
		catch(MessagingException e){
			throw new MailException (e.getMessage());
		}
	}

	/**
	 * @param text java.lang.Object
	 * @param html java.lang.Object
	 */
	public void setTextAndHtmlContent(String text, String html) throws MailException {
		try {
			Multipart mp = new MimeMultipart();
			
			MimeBodyPart mbp1 = new MimeBodyPart();
			/*mbp1.setDataHandler(
				new DataHandler(
					new ByteArrayDataSource(
						html, "text/html; charset=utf-8")
				)
			);
			mbp1.setHeader("Content-Transfer-Encoding", "7bit");
			*/
			
			mbp1.setDataHandler(new javax.activation.DataHandler(new ByteArrayDataSource(new java.io.ByteArrayInputStream(html.getBytes("UTF-8")), "text/html; charset=utf-8")));			
			mbp1.setHeader("Content-Transfer-Encoding", "base64");
			mp.addBodyPart(mbp1);
			
			if(text != null)
			{
				MimeBodyPart mbp2 = new MimeBodyPart();
				mbp2.setDataHandler(
					new DataHandler(
						new ByteArrayDataSource(text, "text/plain; charset=utf-8")
					)
				);
				mbp2.setHeader("Content-Transfer-Encoding", "7bit");
				mp.addBodyPart(mbp2);
			}
			
			
			if(file != null && file.length > 0)
			{	
				
				try
				{	
					for(int idx=0; idx<file.length; idx++)
					{
						MimeBodyPart mbp_file = new MimeBodyPart();					
				        javax.activation.DataSource source = new javax.activation.FileDataSource(file[idx]);
				        mbp_file.setDataHandler(new javax.activation.DataHandler(source));
				        mbp_file.setFileName(MimeUtility.encodeText( file[idx].getName(), "utf-8", "B") );
				        
				        mp.addBodyPart(mbp_file);
					}
				} catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			
			//mp.setSubType("alternative");
			msg.setContent(mp);
		}
		catch(MessagingException e) {
			throw new MailException(e.getMessage());
		}
		catch(Exception e) {
			throw new MailException(e.getMessage());
		}
	}

	/**
	 * @deprecated
	 * @param text java.lang.String
	 */
	public void setTextContent(String text) throws MailException {
		try {
			msg.setContent(text, "text/plain; charset=utf-8");
			msg.setHeader("Content-Transfer-Encoding", "7bit");
		}
		catch(MessagingException e) {
			throw new MailException(e.getMessage());
		}
		catch(Exception e) {
			throw new MailException(e.getMessage());
		}
	}
}