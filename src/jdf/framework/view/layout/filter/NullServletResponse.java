package jdf.framework.view.layout.filter;

import java.io.*;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * BufferTextStream
 *
 * <p>
 * ServletOutputStream을 확장하여, Jsp /servlet의 output을
 * 실제 socket output stream으로 출력하지 않고,
 * 메모리 상에서 buffering 하는 역활을 한다.
 * </p>
 *
 * @author
 * @version 1.0 2003/03/01
 *
 * @see ServletOutputStream
 *
 **/

public class NullServletResponse implements HttpServletResponse
{
    
    public NullServletResponse()
    {
    }
    
    /**
     * @see javax.servlet.ServletResponse#flushBuffer()
     */
    public void flushBuffer() throws IOException
    {
        System.out.println("flushBuffer");
    }

    /**
     * @see javax.servlet.ServletResponse#getBufferSize()
     */
    public int getBufferSize()
    {
        System.out.println("getBufferSize");
        return 0;
    }

    /**
     * @see javax.servlet.ServletResponse#getCharacterEncoding()
     */
    public String getCharacterEncoding()
    {
        System.out.println("getCharacterEncoding");
        return null;
    }

    /**
     * @see javax.servlet.ServletResponse#getLocale()
     */
    public Locale getLocale()
    {
        System.out.println("getLocale");
        return null;
    }

    /**
     * @see javax.servlet.ServletResponse#getOutputStream()
     */
    public ServletOutputStream getOutputStream() throws IOException
    {
        System.out.println("getOutputStream");
        return null;
    }

    /**
     * @see javax.servlet.ServletResponse#getWriter()
     */
    public PrintWriter getWriter() throws IOException
    {
        System.out.println("getWriter");
        return null;
    }

    /**
     * @see javax.servlet.ServletResponse#isCommitted()
     */
    public boolean isCommitted()
    {
        System.out.println("isCommitted");
        return false;
    }

    /**
     * @see javax.servlet.ServletResponse#reset()
     */
    public void reset()
    {
        System.out.println("reset");
    }

    /**
     * @see javax.servlet.ServletResponse#resetBuffer()
     */
    public void resetBuffer()
    {
        System.out.println("resetBuffer");
    }

    /**
     * @see javax.servlet.ServletResponse#setBufferSize(int)
     */
    public void setBufferSize(int arg0)
    {
        System.out.println("setBufferSize");
    }

    /**
     * @see javax.servlet.ServletResponse#setContentLength(int)
     */
    public void setContentLength(int arg0)
    {
        System.out.println("setContentLength");        
    }

    /**
     * @see javax.servlet.ServletResponse#setContentType(String)
     */
    public void setContentType(String arg0)
    {
        System.out.println("setContentType");        
    }

    /**
     * @see javax.servlet.ServletResponse#setLocale(Locale)
     */
    public void setLocale(Locale arg0)
    {
        System.out.println("setLocale");        
    }









    /**
     * @see javax.servlet.http.HttpServletResponse#addCookie(Cookie)
     */
    public void addCookie(Cookie arg0)
    {
        System.out.println("0");    
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#addDateHeader(String, long)
     */
    public void addDateHeader(String arg0, long arg1)
    {
        System.out.println("1");        
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#addHeader(String, String)
     */
    public void addHeader(String arg0, String arg1)
    {
        System.out.println("2");
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#addIntHeader(String, int)
     */
    public void addIntHeader(String arg0, int arg1)
    {
        System.out.println("3");
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#containsHeader(String)
     */
    public boolean containsHeader(String arg0)
    {
        System.out.println("4");
        return false;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#encodeRedirectUrl(String)
     * @deprecated
     */
    public String encodeRedirectUrl(String arg0)
    {
        System.out.println("5");
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#encodeRedirectURL(String)
     */
    public String encodeRedirectURL(String arg0)
    {
        System.out.println("6");
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#encodeUrl(String)
     * @deprecated
     */
    public String encodeUrl(String arg0)
    {
        System.out.println("encodeUrl");
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#encodeURL(String)
     */
    public String encodeURL(String arg0)
    {
        System.out.println("encodeURL");
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#sendError(int, String)
     */
    public void sendError(int arg0, String arg1) throws IOException
    {
        System.out.println("sendError" +arg1);
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#sendError(int)
     */
    public void sendError(int arg0) throws IOException
    {
        System.out.println("sendError2" + arg0);
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#sendRedirect(String)
     */
    public void sendRedirect(String arg0) throws IOException
    {
        System.out.println("sendRedirect");
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#setDateHeader(String, long)
     */
    public void setDateHeader(String arg0, long arg1)
    {
        System.out.println("setDateHeader");
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#setHeader(String, String)
     */
    public void setHeader(String arg0, String arg1)
    {
        System.out.println("setHeader");
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#setIntHeader(String, int)
     */
    public void setIntHeader(String arg0, int arg1)
    {
        System.out.println("setIntHeader");
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#setStatus(int, String)
     * @deprecated
     */
    public void setStatus(int arg0, String arg1)
    {
        System.out.println("setStatus");
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#setStatus(int)
     */
    public void setStatus(int arg0)
    {
        System.out.println("setStatus");
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#getContentType()
     */
    public String getContentType() {
        
        return null;
    }
    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#setCharacterEncoding(java.lang.String)
     */
    public void setCharacterEncoding(String arg0) {
        // TODO Auto-generated method stub

    }

	@Override
	public void setContentLengthLong(long arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getHeader(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getHeaderNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getHeaders(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return 0;
	}
}