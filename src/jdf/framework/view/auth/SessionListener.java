package jdf.framework.view.auth;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.InteractionBean;
import jdf.framework.core.log.Logger;
import jdf.framework.view.auth.SessionManager;
import jdf.framework.view.auth.User;



public class SessionListener implements HttpSessionListener
{

	private final String LOG_ID = "<at:SessionListener> ";
	
	@Override
	public void sessionCreated(HttpSessionEvent arg0) 
	{			
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) 
	{	
		User user = null;
		HttpSession session = null;
		try
		{
			session = arg0.getSession();
			
			Logger.debug.println(LOG_ID+session.getId()+" is removed.");
			
			SessionManager.getInstance().remove(session.getId());
			
			//doDB((String)session.getAttribute("user_id"));
			/*SessionManager sessionManager = SessionManager.getInstance();
			sessionManager.remove(session.getId());*/
			
		} catch(Exception ex)
		{
			
			ex.printStackTrace();
			
		}
	}
	
	
	private void doDB(String user_id)
	{
		if(user_id == null) return;
		
		try
		{
			InteractionBean interact = new InteractionBean();
			DataSet input = new DataSet();
			input.put("user_id", user_id);
			input.put("cmd", "ins_logout_hist");
			interact.execute("/user/Login", input);
		} catch(Exception ex)
		{
			Logger.warn.println("<at:SessionListener> create logout history error. cause:"+ex.toString());
		}
	}
}