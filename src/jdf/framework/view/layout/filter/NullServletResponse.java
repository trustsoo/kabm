package jdf.framework.view.layout.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

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
     * @see ServletResponse#flushBuffer()
     */
    public void flushBuffer() throws IOException
    {
        System.out.println("flushBuffer");
    }

    /**
     * @see ServletResponse#getBufferSize()
     */
    public int getBufferSize()
    {
        System.out.println("getBufferSize");
        return 0;
    }

    /**
     * @see ServletResponse#getCharacterEncoding()
     */
    public String getCharacterEncoding()
    {
        System.out.println("getCharacterEncoding");
        return null;
    }

    /**
     * @see ServletResponse#getLocale()
     */
    public Locale getLocale()
    {
        System.out.println("getLocale");
        return null;
    }

    /**
     * @see ServletResponse#getOutputStream()
     */
    public ServletOutputStream getOutputStream() throws IOException
    {
        System.out.println("getOutputStream");
        return null;
    }

    /**
     * @see ServletResponse#getWriter()
     */
    public PrintWriter getWriter() throws IOException
    {
        System.out.println("getWriter");
        return null;
    }

    /**
     * @see ServletResponse#isCommitted()
     */
    public boolean isCommitted()
    {
        System.out.println("isCommitted");
        return false;
    }

    /**
     * @see ServletResponse#reset()
     */
    public void reset()
    {
        System.out.println("reset");
    }

    /**
     * @see ServletResponse#resetBuffer()
     */
    public void resetBuffer()
    {
        System.out.println("resetBuffer");
    }

    /**
     * @see ServletResponse#setBufferSize(int)
     */
    public void setBufferSize(int arg0)
    {
        System.out.println("setBufferSize");
    }

    /**
     * @see ServletResponse#setContentLength(int)
     */
    public void setContentLength(int arg0)
    {
        System.out.println("setContentLength");        
    }

    /**
     * @see ServletResponse#setContentType(String)
     */
    public void setContentType(String arg0)
    {
        System.out.println("setContentType");        
    }

    /**
     * @see ServletResponse#setLocale(Locale)
     */
    public void setLocale(Locale arg0)
    {
        System.out.println("setLocale");        
    }









    /**
     * @see HttpServletResponse#addCookie(Cookie)
     */
    public void addCookie(Cookie arg0)
    {
        System.out.println("0");    
    }

    /**
     * @see HttpServletResponse#addDateHeader(String, long)
     */
    public void addDateHeader(String arg0, long arg1)
    {
        System.out.println("1");        
    }

    /**
     * @see HttpServletResponse#addHeader(String, String)
     */
    public void addHeader(String arg0, String arg1)
    {
        System.out.println("2");
    }

    /**
     * @see HttpServletResponse#addIntHeader(String, int)
     */
    public void addIntHeader(String arg0, int arg1)
    {
        System.out.println("3");
    }

    /**
     * @see HttpServletResponse#containsHeader(String)
     */
    public boolean containsHeader(String arg0)
    {
        System.out.println("4");
        return false;
    }

    /**
     * @see HttpServletResponse#encodeRedirectUrl(String)
     * @deprecated
     */
    public String encodeRedirectUrl(String arg0)
    {
        System.out.println("5");
        return null;
    }

    /**
     * @see HttpServletResponse#encodeRedirectURL(String)
     */
    public String encodeRedirectURL(String arg0)
    {
        System.out.println("6");
        return null;
    }

    /**
     * @see HttpServletResponse#encodeUrl(String)
     * @deprecated
     */
    public String encodeUrl(String arg0)
    {
        System.out.println("encodeUrl");
        return null;
    }

    /**
     * @see HttpServletResponse#encodeURL(String)
     */
    public String encodeURL(String arg0)
    {
        System.out.println("encodeURL");
        return null;
    }

    /**
     * @see HttpServletResponse#sendError(int, String)
     */
    public void sendError(int arg0, String arg1) throws IOException
    {
        System.out.println("sendError" +arg1);
    }

    /**
     * @see HttpServletResponse#sendError(int)
     */
    public void sendError(int arg0) throws IOException
    {
        System.out.println("sendError2" + arg0);
    }

    /**
     * @see HttpServletResponse#sendRedirect(String)
     */
    public void sendRedirect(String arg0) throws IOException
    {
        System.out.println("sendRedirect");
    }

    /**
     * @see HttpServletResponse#setDateHeader(String, long)
     */
    public void setDateHeader(String arg0, long arg1)
    {
        System.out.println("setDateHeader");
    }

    /**
     * @see HttpServletResponse#setHeader(String, String)
     */
    public void setHeader(String arg0, String arg1)
    {
        System.out.println("setHeader");
    }

    /**
     * @see HttpServletResponse#setIntHeader(String, int)
     */
    public void setIntHeader(String arg0, int arg1)
    {
        System.out.println("setIntHeader");
    }

    /**
     * @see HttpServletResponse#setStatus(int, String)
     * @deprecated
     */
    public void setStatus(int arg0, String arg1)
    {
        System.out.println("setStatus");
    }

    /**
     * @see HttpServletResponse#setStatus(int)
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