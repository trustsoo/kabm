/*
 * Created on 2004-05-18
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jdf.framework.logic.servlet;

import jdf.framework.core.log.Logger;
import jdf.framework.core.pool.cache.CacheManager;
import jdf.framework.core.pool.cache.CacheManagerFactory;
import jdf.framework.logic.spi.cache.HttpCachePropagator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;


/**
 * @author
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CacheEventReceiver extends HttpServlet implements AnyLogicControl
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CacheManagerFactory cft;
	/**
	 * 스키마 URI를 Configration으로 부터 가져온다.
	 * @param config ServletConfig
	 */
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);

		cft = CacheManagerFactory.getInstance();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		InputStream in = req.getInputStream();
		ObjectInputStream oin = new ObjectInputStream(in);

		try
		{

			int mode = oin.readInt();
			

			String trcode = oin.readUTF();
			Logger.debug.println("<CacheEventReceiver> read header : "+mode+" " + trcode);

			Object key = oin.readObject();
			Logger.debug.println("<CacheEventReceiver> read key : " + key.getClass().getName());

			Object data = oin.readObject();
			Logger.debug.println("<CacheEventReceiver> read data : " + key.getClass().getName());

			CacheManager mgr = cft.getCacheManager(trcode);
			if (mgr != null)
			{

				if (mode == HttpCachePropagator.INSERT)
				{
					mgr.insert(key, data, false);

				}
				else if (mode == HttpCachePropagator.REMOVE)
				{
					mgr.reset(false);
				}

			}
			
			Logger.debug.println("<CacheEventReceiver> receive complet. "+key);

		}
		catch (Exception e)
		{
			Logger.debug.println("<CacheEventReceiver> process error", e);
		}

	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		doGet(req, res);
	}

}