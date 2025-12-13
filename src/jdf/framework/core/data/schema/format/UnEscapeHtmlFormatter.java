package jdf.framework.core.data.schema.format;



public class UnEscapeHtmlFormatter extends Formatter
{
	private String HTML_VERSION = null;
	
	
	public UnEscapeHtmlFormatter(String format)
	{
		if(format == null)  HTML_VERSION = "4.0";
		
		HTML_VERSION = format;
	}
	
	public String format(Object data)
	{
		
		String result = null;
		
		if("4.0".equals(HTML_VERSION))
			result = org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4((String)data);
		else if("3.0".equals(HTML_VERSION))
			result = org.apache.commons.lang3.StringEscapeUtils.unescapeHtml3((String)data);
		
		if(result == null) result = (String) data;
		
		
		return result;
	}
}