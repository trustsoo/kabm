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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.io.StreamUtil;

/**
 * <xmp>
 *
 *
 * Java에서 메일을 발송할 수 있는 참한 솔루션이 있습니다.  플렛폼이나 웹서버와는 상관없이 
 * Java를 사용하는 모든 곳에서 아주 쉽게 메일을 보낼 수 있습니다.
 * http://www.javasoft.com 에 가셔서 Java Activation Framewrok API와 Java Mail API를
 * 다운 받으세요. 각각의 패키지에는 activation.jar 파일과 mail.jar 파일이 있을 겁니다.
 * TemplateMail 클래스를 이용하시려면 Java Activation FrameWork이 필요합니다.
 *.
 *.
 * TemplateMail 클래스는 Mail 클래스를 상속한 것으로서, setXXXTemplate() 메소드와
 * setArg() 메소드가 추가되어 있습니다.
 *.
 * TemplateMail 클래스는 발송할 메일 포멧을 text나 html 파일로 만들어 둔 후,
 * 메일 내용에 특정한 변수값만 대입하여 발송하고자 할 때 사용합니다.
 *. 
 * 사용하는 방법은 다음과 같습니다.
 *.
 * Sample Code :
 * ------------------------
 * try {
 *     TemplateMail mailer = new TemplateMail();
 *     mailer.setHtmlTemplate("template.html");
 * 
 *     mailer.setFrom("leewy7@kornet.net","홍길동");
 *     mailer.setSubject("안녕하세요");
 * 
 *     mailer.setRecipient("wyounglee@lgeds.lg.co.kr", "이원영");
 *     mailet.setArg("name", "이원영");
 *     mailet.setArg("birthday", "1999-06-28");
 *     mailer.send();
 * } 
 * catch(Exception e){
 *     ...
 * } 
 * ------------------------
 *. 
 * 이때, template.html 이라는 텍스트 파일은 아래와 같이 작성해 두시면 되지요.
 * --------------------------
 * <html>
 * <head><title>생일축하</title></head>
 * <body>
 * 안녕하세요, <@ name >님. 귀하의 생일은 <@ birthday >입니다.
 * </body></html>
 *.
 * --------------------------
 * (어느 디렉토리에 만드냐구요? 아래의 PS를 참고 하세요)
 *. 
 * 많은 사람들에게 똑같은 형식의 메일을 보낼 때 아주 유용하겠죠? 아래 부분을 필요한 만큼
 * LOOP 돌면서 메일을 발송하시면 됩니다.
 *. 
 *     mailer.setRecipient("wyounglee@lgeds.lg.co.kr", "이원영");
 *     mailet.setArg("name", "이원영");
 *     mailet.setArg("birthday", "1999-06-28");
 *     mailer.send();
 *. 
 *. 
 *. 
 * 요즘 대부분의 메일 시스템은 WEB으로 되어 있어서 HTML형식의 메일을 볼 수 있지만
 * 아직까지도 TEXT형태의 메일만 볼 수 있는 시스템이 있지요... 따라서 HTML을 볼 수 있는 시스템
 * 에서는 HTML로 보고, HTML을 읽을 수 없는 메일 시스템에서는 TEXT기반의 메일을 봐야 할 필요
 * 성이 있습니다. 이것을 모두 지원해 주기 위해서는 HTML 형태와 TEXT형태의 메일을 모두 발송
 * 해야 할 것입니다.
 * 이렇게 하려면 TemplateMail의 setTextAndHtmlTemplate("text_file", "html_file") 이라는
 * 메소드를 이용하시면 됩니다.
 *. 
 * ------------------------
 * try {
 *     TemplateMail mailer = new TemplateMail();
 *     mailer.setTextAndHtmlTemplate("template.text", "template.html");
 * 
 *     mailer.setFrom("leewy7@kornet.net","홍길동");
 *     mailer.setSubject("안녕하세요");
 * 
 *     mailer.setRecipient("wyounglee@lgeds.lg.co.kr", "이원영");
 *     mailet.setArg("name", "이원영");
 *     mailet.setArg("birthday", "1999-06-28");
 *     mailer.send();
 * } 
 * catch(Exception e){
 *     ...
 * } 
 * ------------------------
 * 물론 이때, template.text와 template.html이라는 두 파일을 미리 만들어 두셔야지요.
 *. 
 * 이렇게 하시면 multipart/alternative 로 두개의 메일이 첨부되어 날아 갑니다.
 *. 
 *.
 *. 
 *. 
 * PS: Configuration 클래스를 필요로 합니다.
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
 * mail.host    = localhost
 * mail.admin   = trustsoo@gmail.com
 * mail.admin.name  = 조은호
 * mail.cc.flag = false
 * mail.cc      = trustsoo@gmail.com
 * mail.templatedir = /home/mail/template
 * ############################################################################
 *.
 * 여기서 주목할 사항 중에 하나는 Mail 클래스가 템플릿 메일 파일을 
 * com.lgeds.framework.mail.templatedir에 설정된 디렉토리에서 찾는 다는 것입니다.
 * 따라서 위에 경우에 템플릿 파일을 /home/mail/template 디렉토리에 만드시면 됩니다.
 *. 
 *. 
 *. 
 * PS3: http://power.lgeds.lg.co.kr
 * 
 * </xmp>
 *
 * @see Mail
 * @see jdf.framework.framework.core.Configuration
 */

public class TemplateMail extends Mail
{
	private final int TEXTL_TEMPLATE = 0;
	private final int HTML_TEMPLATE = 1;
	private final int TEXT_HTML_TEMPLATE = 2;
	protected java.util.Hashtable args = new java.util.Hashtable();
	private String textTemplateSource = null;
	private String htmlTemplateSource = null;
	private int template_type = TEXTL_TEMPLATE;
	
	
	
	
	/**
	 * 
	 */
	public TemplateMail() throws MailException
	{
		super();
	}
	
	/**
	 * 
	 */
	public TemplateMail(Properties props) throws MailException
	{
		super(props);
	}

	/**
	 * @param template java.lang.String
	 */
	public TemplateMail(String template) throws MailException
	{
		super();
		setTemplate(template);
	}
	
	public TemplateMail(Properties props, String template) throws MailException
	{
		super();
		setTemplate(template);
	}
	/**
	 * @return java.lang.String
	 * @param s java.lang.String
	 */
	private String parseTemplate(String s)
	{
		StringBuffer content = new StringBuffer();
		while (s.length() > 0)
		{
			int position = s.indexOf("<@");
			if (position == -1)
			{
				content.append(s);
				break;
			}
			if (position != 0)
				content.append(s.substring(0, position));

			if (s.length() == position + 2)
				break;
			String remainder = s.substring(position + 2);

			int markEndPos = remainder.indexOf(">");
			if (markEndPos == -1)
				break;

			String argname = remainder.substring(0, markEndPos).trim();
			String value = (String) args.get(argname);
			if (value != null)
				content.append(value);

			if (remainder.length() == markEndPos + 1)
				break;
			s = remainder.substring(markEndPos + 1);
		}
		return content.toString();
	}

	/**
	 * 
	 */
	public void send() throws MailException
	{

		String textContent = "", htmlContent = "";

		/*if (template_type == TEXTL_TEMPLATE || template_type == TEXT_HTML_TEMPLATE)
		{
			if (textTemplateSource == null)
				throw new MailException("No Template");
			textContent = parseTemplate(textTemplateSource);
		}*/
		if (template_type == HTML_TEMPLATE || template_type == TEXT_HTML_TEMPLATE)
		{
			if (htmlTemplateSource == null)
				throw new MailException("No Template");
			htmlContent = parseTemplate(htmlTemplateSource);
		}

		/*if (template_type == TEXTL_TEMPLATE)
			super.setTextContent(textContent);
		else if (template_type == HTML_TEMPLATE)
			super.setHtmlContent(htmlContent);
		else*/
			super.setTextAndHtmlContent(null, htmlContent);

		super.send();
	}

	/**
	 * @param name java.lang.String
	 * @param value java.lang.String
	 */
	public void setArg(String name, String value)
	{
		args.put(name, value);
	}

	/**
	 * @param template java.lang.String
	 *
	 * @see #setTemplate(java.lang.String)
	 * @see #setTextTemplate(java.lang.String)
	 * @see #setTextAndHtmlTemplate(java.lang.String,java.lang.String)
	 */
	public void setHtmlTemplate(String template) throws MailException
	{
		template_type = HTML_TEMPLATE;

		FileInputStream fi = null;
		try
		{
			java.io.File file = new java.io.File(template);
			
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			fi = new FileInputStream(file);
			StreamUtil.copy(fi, bo);
			
			
			/*in = new java.io.BufferedReader(new java.io.FileReader(file));
			StringBuffer buf = new StringBuffer();
			String line;
			while ((line = in.readLine()) != null)
				buf.append(line + "\n");
			htmlTemplateSource = buf.toString();*/
			htmlTemplateSource = new String(bo.toByteArray(), "utf-8");
			
			//System.out.println(htmlTemplateSource);
			
			//htmlTemplateSource = bo.toString("utf-8");
			
		}
		catch (Exception e)
		{
			throw new MailException(e.getMessage());
		}
		finally
		{
			try
			{
				if (fi != null)
					fi.close();
			}
			catch (Exception e)
			{
			}
		}
	}

	/**
	 * @deprecated
	 * Same as setTextTemplate(String template);
	 * @param template java.lang.String
	 *
	 * @see #setTextTemplate(java.lang.String)
	 * @see #setHtmlTemplate(java.lang.String)
	 * @see #setTextAndHtmlTemplate(java.lang.String,java.lang.String)
	 */
	public void setTemplate(String template) throws MailException
	{
		this.setTextTemplate(template);
	}

	/**
	 * @param textTemplate java.lang.String
	 * @param htmlTemplate java.lang.String
	 *
	 * @see #setTemplate(java.lang.String)
	 * @see #setTextTemplate(java.lang.String)
	 * @see #setHtmlTemplate(java.lang.String)
	 */
	public void setTextAndHtmlTemplate(String textTemplate, String htmlTemplate) throws MailException
	{
		setTextTemplate(textTemplate);
		setHtmlTemplate(htmlTemplate);
		template_type = TEXT_HTML_TEMPLATE;
	}

	/**
	 * @deprecated
	 * @param template java.lang.String
	 *
	 * @see #setTemplate(java.lang.String)
	 * @see #setHtmlTemplate(java.lang.String)
	 * @see #setTextAndHtmlTemplate(java.lang.String,java.lang.String)
	 */
	public void setTextTemplate(String template) throws MailException
	{
		template_type = TEXTL_TEMPLATE;

		java.io.BufferedReader in = null;
		try
		{
			Config conf = Configuration.getInitial();
			java.io.File file = new java.io.File(template);
			in = new java.io.BufferedReader(new java.io.FileReader(file));
			StringBuffer buf = new StringBuffer();
			String line;
			while ((line = in.readLine()) != null)
				buf.append(line + "\n");
			textTemplateSource = buf.toString();
		}
		catch (Exception e)
		{
			throw new MailException(e.getMessage());
		}
		finally
		{
			try
			{
				if (in != null)
					in.close();
			}
			catch (Exception e)
			{
			}
		}
	}
}