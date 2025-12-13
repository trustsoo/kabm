package jdf.framework.view.layout.filter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


/**
 * XSLT 변환 Filter
 * 
 * @author
 *
 */
public class XSLTFilter implements Filter {
	private ServletContext ctx;

	private String xslt;

	private TransformerFactory tf = TransformerFactory.newInstance();

	private Transformer xform;

	private static class ByteArrayServletStream extends ServletOutputStream {
		ByteArrayOutputStream baos;

		ByteArrayServletStream(ByteArrayOutputStream baos) {
			this.baos = baos;
		}

		public void write(int param) throws java.io.IOException {
			baos.write(param);
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

	private static class ByteArrayPrintWriter {
		private ByteArrayOutputStream baos = new ByteArrayOutputStream();

		private PrintWriter pw = new PrintWriter(baos);

		private ServletOutputStream sos = new ByteArrayServletStream(baos);

		public PrintWriter getWriter() {
			return pw;
		}

		public ServletOutputStream getStream() {
			return sos;
		}

		byte[] toByteArray() {
			return baos.toByteArray();
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		ctx = filterConfig.getServletContext();
		xslt = filterConfig.getInitParameter("xslt");

		if (xslt == null || xslt.length() == 0)
			xslt = "/xml/xml.xsl";

		ctx.log("Filter " + filterConfig.getFilterName() + " using xslt "
				+ xslt);
		try {
			xform = tf.newTransformer(new StreamSource(ctx
					.getResourceAsStream(xslt)));
		} catch (Exception e) {
			ctx.log("Could not intialize transform", e);
			throw new ServletException("Could not initialize transform", e);
		}
	}

	public String httpReqLine(HttpServletRequest req) {
		StringBuffer ret = jdf.framework.view.menu.util.RequestURL.getUrl(req);
		String query = req.getQueryString();
		if (query != null) {
			ret.append("?").append(query);
		}
		return ret.toString();
	}

	public String getHeaders(HttpServletRequest req) throws IOException {
		Enumeration en = req.getHeaderNames();
		StringBuffer sb = new StringBuffer();
		while (en.hasMoreElements()) {
			String name = (String) en.nextElement();
			sb.append(name).append(": ").append(req.getHeader(name)).append(
					"\n");
		}
		return sb.toString();
	}

	public void doFilter(javax.servlet.ServletRequest servletRequest,
			javax.servlet.ServletResponse servletResponse,
			javax.servlet.FilterChain filterChain) throws java.io.IOException,
			javax.servlet.ServletException {
		System.out.println("<START>");

		HttpServletRequest hsr = (HttpServletRequest) servletRequest;
		final HttpServletResponse resp = (HttpServletResponse) servletResponse;
		ctx.log("Accessing filter for " + httpReqLine(hsr) + " "
				+ hsr.getMethod());

		final ByteArrayPrintWriter pw = new ByteArrayPrintWriter();
		final boolean[] xformNeeded = new boolean[1];

		HttpServletResponse wrappedResp = new HttpServletResponseWrapper(resp) {
			public PrintWriter getWriter() {
				System.out.println("get Writer");
				return pw.getWriter();
			}

			public ServletOutputStream getOutputStream() {
				System.out.println("get getOutputStream");
				return pw.getStream();
			}

			public void setContentType(String type) {
				if (type.equals("text/xml")) {
					ctx.log("Converting xml to html");
					resp.setContentType("text/html");
					xformNeeded[0] = true;
				} else {
					resp.setContentType(type);
				}
			}
		};

		filterChain.doFilter(servletRequest, wrappedResp);
		byte[] bytes = pw.toByteArray();
		if (bytes == null || (bytes.length == 0)) {
			ctx.log("No content!");
			return;
		}

		// System.out.println("L:::"+ new String(bytes));

		if (xformNeeded[0] == true) {
			try {
				// Note: This can be _very_ inefficient for large
				// transforms
				// Such transforms should be pre-calculated.
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				xform.transform(new StreamSource(
						new ByteArrayInputStream(bytes)),
						new StreamResult(baos));
				byte[] xformBytes = baos.toByteArray();
				// This fixes a bug in the original published
				// tip, which did not set the content length
				// to the _new_ length implied by the xform.
				resp.setContentLength(xformBytes.length);
				resp.getOutputStream().write(xformBytes);
				ctx.log("XML -> HTML conversion completed");
			} catch (Exception e) {
				e.printStackTrace();
				throw new ServletException("Unable to transform document", e);
			}
		} else {
			resp.getOutputStream().write(bytes);
		}
	}

	public void destroy() {
		ctx.log("Destroying filter...");
	}
}