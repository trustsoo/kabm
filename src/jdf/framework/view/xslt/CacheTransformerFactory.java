package jdf.framework.view.xslt;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import jdf.framework.core.log.Logger;


/**
 * 해당 xsl 에 다른 TransFormer 객체를 반납해 준다.
 * 
 * 
 * 
 * 
 * @author
 */

public class CacheTransformerFactory
{

	private final static String LOG_ID = "<t:CacheTransformerFactory> ";

	private static Map cache = new HashMap();

	private static TransformerFactory tf = TransformerFactory.newInstance();

	/**
	 * getTransFormer
	 * 
	 * @param ctx
	 * @param xsl
	 * @return
	 * @throws TransformerConfigurationException
	 */
	public static Transformer getTransFormer(ServletContext ctx, String xsl) throws TransformerConfigurationException
	{
		return getTransFormer(ctx, xsl, true);
	}

	/**
	 * getTransFormer
	 * 
	 * @param ctx
	 * @param xsl
	 * @param isCache
	 * @return
	 * @throws TransformerConfigurationException
	 */
	public synchronized static Transformer getTransFormer(ServletContext ctx, String xsl, boolean isCache)
			throws TransformerConfigurationException
	{
		Transformer xform = null;

		if (isCache)
			xform = (Transformer) cache.get(xsl);

		try {
			if (xform == null) {

				XslResolver resolver = new XslResolver(ctx, xsl);
				tf.setURIResolver(resolver);

				xform = tf.newTransformer(new StreamSource(ctx.getResourceAsStream(xsl)));

				cache.put(xsl, xform);
			}
		} catch (TransformerConfigurationException tce) {
			throw tce;
		}

		return xform;
	}

	/**
	 * 
	 * @author
	 * 
	 */
	private static class XslResolver implements URIResolver
	{
		String base_path;

		String nav_style;

		private XslResolver(ServletContext context, String mainXslPathUrl) {
			String mainXslPath = context.getRealPath(mainXslPathUrl);

			this.base_path = (new File(mainXslPath)).getParent();

			Logger.debug.println(LOG_ID + "base_path " + base_path);

		}

		public Source resolve(String href, String base)
		{
			Logger.debug.println(LOG_ID + "include " + href + " " + base);

			StringBuffer path = new StringBuffer(this.base_path);
			path.append("/");
			path.append(href);

			File file = new File(path.toString());
			if (file.exists())
				return new StreamSource(file);
			return null;
		}
	}
}
