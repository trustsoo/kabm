package jdf.framework.view.layout.filter;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * 
 * HTML 출력 Stream을 가로채서,
 * 공백,연속 CR/LF를 제거해서 HTML의 용량을 줄여준다.
 * 
 * 
 * @author
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class HtmlCompactOutputStream extends FilterOutputStream
{
	private Stack stack = new Stack();

	private int quotation_count = 0;
	
	// <pre>나 <script> 태그들이 시작되면 true, 아니면 false
	private boolean openScript = false;

	private int last_char = -1;

	// quotation mark
	private final static int QUOTATION = 0x27;

	// quotation mark
	private final static int DOUBLE_QUOTATION = 0x22;

	private final static int SPACE = 0x20;

	private final static int TAB = 0x09;

	private final static int LF = 0x0A;

	private final static int CR = 0x0D;

	private final static int SLASH = 0x2F;

	private boolean slash_line = false;

	private ByteArrayOutputStream bos = new ByteArrayOutputStream();

	//private ByteArrayOutputStream bosTo = new ByteArrayOutputStream();

	public HtmlCompactOutputStream(OutputStream out)
	{
		super(new BufferedOutputStream(out, 8192));

		//System.out.println("--- HtmlCompactOutputStream 생성 "+out.getClass().getName());
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#close()
	 */
	public void close() throws IOException
	{
		//System.out.println("--- closer ");

		super.close();
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#flush()
	 */
	public void flush() throws IOException
	{
		//System.out.println("--- flush ");
		super.flush();
	}

	/**
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	public void write(byte b[], int off, int len) throws IOException
	{
		//Logger.debug.println("> " + len);
		if ((off | len | (b.length - (len + off)) | (off + len)) < 0)
			throw new IndexOutOfBoundsException();

		for (int i = 0; i < len; i++)
		{
			write(b[off + i]);
			//writeToOutputStream(b[off + i], bosTo);
		}
		/*
		byte[] tmp = bosTo.toByteArray();
		this.out.write(tmp,0, tmp.length);
		bosTo.reset();
		*/
	}

	/**
	 * @see java.io.OutputStream#write(byte[])
	 */
	public void write(byte[] b) throws IOException
	{
		//System.out.println("--- b ");
		write(b, 0, b.length);
	}

	public void write(int val) throws IOException
	{
		writeToOutputStream(val, this.out);
	}

	/**
	 * @see java.io.OutputStream#write(int)
	 */
	private void writeToOutputStream(int val, OutputStream tmpOut) throws IOException
	{
		//System.out.print(val);
		bos.write(val);

		boolean isProcess = true;

		int last_val = 0;
		Integer int_val = null;

		if (val == QUOTATION)
		{

			try
			{
				int_val = (Integer) stack.peek();
				last_val = int_val.intValue();

				if (last_val == QUOTATION)
				{
					quotation_count--;
					stack.pop();
					//System.out.println("-->"+val);
				}
				else
				{
					quotation_count++;
					stack.push(new Integer(val));
				}
			}
			catch (EmptyStackException ee)
			{
				quotation_count++;
				stack.push(new Integer(val));
				//System.out.println("<--"+val);
			}

		}

		else if (val == DOUBLE_QUOTATION)
		{
			try
			{
				int_val = (Integer) stack.peek();
				last_val = int_val.intValue();

				if (last_val == DOUBLE_QUOTATION)
				{
					quotation_count--;
					stack.pop();
					//System.out.println("-->"+val);
				}
				else
				{
					quotation_count++;
					stack.push(new Integer(val));
				}
			}
			catch (EmptyStackException ee)
			{
				quotation_count++;
				stack.push(new Integer(val));
				//System.out.println("<--"+val);
			}

		}

		//  //가 나타나면 그 라인의 LF는 그대로 처리해 준다.
		if (last_char == SLASH && val == SLASH)
			slash_line = true;

		if (val == LF)
		{
			String tmp = bos.toString().toLowerCase();
			//System.out.println(tmp);
			bos.reset();

			if (tmp.indexOf("<pre") >= 0 || tmp.indexOf("<textarea") >= 0 || tmp.indexOf("<script") >= 0)
			{
				quotation_count++;
				openScript = true;
			}

			if (tmp.indexOf("</pre") >= 0 || tmp.indexOf("</textarea") >= 0 || tmp.indexOf("</script") >= 0) 
			{
				quotation_count--;
				openScript = false;
			}
				
			
			if(!openScript && quotation_count > 0)
				quotation_count--;
				
				
		}	

		if (quotation_count < 1)
		{
			quotation_count=0;
			
			if (((this.last_char == SPACE || this.last_char == TAB) && (val == SPACE || val == TAB))
				|| val == CR
				|| val == LF)
				isProcess = false;

			if (slash_line && val == LF)
			{
				isProcess = true;
				slash_line = false;

			}
		}

		if (isProcess)
		{
			//super.write(val);
			tmpOut.write(val);
			this.last_char = val;
		}

	}

}