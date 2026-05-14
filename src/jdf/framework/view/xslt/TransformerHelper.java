/*
 * Created on 2003. 5. 28.
 *
 * To change the template for this generated file go to
 * WindowPreferencesJavaCode GenerationCode and Comments
 */
package jdf.framework.view.xslt;

import javax.servlet.ServletContext;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * XSL transform Helper
 * 
 * @author
 * 
 * To change the template for this generated type comment go to
 * WindowPreferencesJavaCode GenerationCode and Comments
 */
public class TransformerHelper {

	private final static String LOG_ID = "<t:TransformerHelper> ";

	public static void trans(ServletContext ctx, String xslName, InputStream in, OutputStream out, boolean isCache)
			throws Exception {
		try {
			Transformer xform = CacheTransformerFactory.getTransFormer(ctx, xslName, isCache);

			xform.transform(new StreamSource(in), new StreamResult(out));

		} catch (Exception e) {
			jdf.framework.core.log.Logger.err.println(LOG_ID + "transform error", e);
			throw new Exception(e.getMessage() + " : " + e.getClass().getName());
		}

	}

}
