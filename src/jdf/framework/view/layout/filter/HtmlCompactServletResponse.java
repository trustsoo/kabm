package jdf.framework.view.layout.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * 
 * Html의 양을 줄여주는 HttpServletResponseWrapper class
 * 
 * compact 규칙
 * 1) 연속된 space는 하나로 줄여준다.
 * 2) new Line은 없에준다.
 * 
 * 제약사항: <textarea>와 같은 태그내에서는 사용하지 않는것이 좋다.
 * 
 * 
 * @author
 * @version 1.0
 * @since 2004-03-08 오후 5:49:00
 * 
 */
public class HtmlCompactServletResponse extends HttpServletResponseWrapper
{
    private HtmlCompactPrintWriter pwriter;

    public HtmlCompactServletResponse(HttpServletResponse resp) throws IOException
    {
        super(resp);
        
        //System.out.println("--- HtmlCompactServletResponse 생성");

        pwriter= new HtmlCompactPrintWriter(resp.getOutputStream());
    }

    public PrintWriter getWriter()
    {
        return pwriter.getWriter();
    }
    public ServletOutputStream getOutputStream()
    {
		//System.out.println(" ** 4");
        return pwriter.getStream();
    }
    

	public void flushBuffer() throws IOException
	{
		//System.out.println("flush");
		super.flushBuffer();
		pwriter.flush();
	}

	
	    

    private static class HtmlCompactServletStream extends ServletOutputStream
    {
        OutputStream baos;

        HtmlCompactServletStream(OutputStream baos)
        {
            this.baos= baos;
			//System.out.println("--- HtmlCompactServletStream 생성");
        }
        public void write(int param) throws IOException
        {
            //System.out.print("*"+param);
            baos.write(param);
        }

        /**
         * @see ServletOutputStream#println(String)
         */
        public void println(String arg0) throws IOException
        {
            System.out.println(arg0);
            super.println(arg0);
        }
		@Override
		public boolean isReady() {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public void setWriteListener(WriteListener arg0) {
			// TODO Auto-generated method stub
			
		}



    }

    private static class HtmlCompactPrintWriter
    {

        private OutputStream hos;

        private PrintWriter pw;
        private ServletOutputStream sos;

        HtmlCompactPrintWriter(OutputStream out)
        {
            this.hos= new HtmlCompactOutputStream(out);
            this.pw= new PrintWriter(hos);
            this.sos= new HtmlCompactServletStream(hos);
            
			//System.out.println("--- HtmlCompactPrintWriter 생성");

        }

        public PrintWriter getWriter()
        {
			return pw;
        }
        public ServletOutputStream getStream()
        {
        	return sos;
        }

        public PrintWriter getPrintWriter()
        {
			return this.pw;
        }
        
        
        public void flush() throws IOException
        {
        	this.pw.flush();
        	this.sos.flush();
        	this.hos.flush();
        	
        }

    }

}