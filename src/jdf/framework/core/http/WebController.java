package jdf.framework.core.http;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.InteractionBean;
import jdf.framework.core.data.ResourceException;
import jdf.framework.core.log.Logger;


/**
 * /servletName/list.do 와 같이 호출하면 내부에 있는 list 라는 메쏘드를 수행한다. 일반적인 목적의 servlet 을 쉽게 만들수 있도록 한다.
 * 
 * @author
 * 
 */
public class WebController extends HttpServlet
{

	private final static String LOG_ID = "<f:WebController> ";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Map methodMap = new HashMap();

	private ServletConfig config;

	private String jspDirectory;
	
	protected String thisServletName="";
	
	protected InteractionBean interact;

	/**
	 * 
	 * 
	 */
	public WebController() {

		initReflectInfo();
	}

	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);

		this.config = config;

		this.jspDirectory = config.getInitParameter("jsp.directory");
		
		this.thisServletName = config.getServletName();
		

	}

	/**
	 * 
	 * 
	 */
	private void initReflectInfo()
	{
		Method[] methods = getClass().getMethods();

		for (int i = 0; i < methods.length; i++) {
			this.methodMap.put(methods[i].getName(), methods[i]);

			// Logger.debug.println(LOG_ID + " reflect info " + methods[i].getName());
		}

	}

	/**
	 * BLD 를 실행한다.
	 * 
	 * @param bldName
	 * @param input
	 * @param output
	 * @throws ResourceException
	 */
	public void executeBL(String bldName, Object input, Object output) throws ResourceException
	{
		
		interact.execute(bldName, input, output);
	}

	/**
	 * BLD 를 실행한다. output 필요없음.
	 * 
	 * @param bldName
	 * @param input
	 * @param output
	 * @throws ResourceException
	 */
	public DataSet executeBL(String bldName, Object input) throws ResourceException
	{
		return interact.execute(bldName, input);
	}

	/**
	 * 
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		doPost(req, resp);
	}

	/**
	 * 기본 method 이름 결정
	 * 
	 * @return
	 */
	protected String getDefaultMethodName()
	{
		return null;
	}

	/**
	 * 실질적인 메쏘드 호출이 일어나는 곳
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		this.interact = new InteractionBean();
		
		String pathInfo = req.getPathInfo();

		String methodName = getMethodName(req, resp);

		if (methodName == null) {
			throw new ServletException("url path is not correct");
		}

		Logger.debug.println(LOG_ID + "process cmd:" + methodName);

		

		ViewMeta view = new ViewMeta();

		// /servletAliasName/tr500.jsp 와 같이 요청이 온경우
		// pathInfo는 /tr500.jsp 로 들어오고, 이것을 바로 presentation 한다.
		if (pathInfo != null && pathInfo.indexOf(".jsp") > 0)
			view.setViewPage(this.jspDirectory + pathInfo);

		try {
			doBefore(req, resp, methodName);
			
			Method m = (Method) this.methodMap.get(methodName);
			if (m == null)
				throw new ServletException(methodName + " method is not exist");

			try {
				m.invoke(this, new Object[] { req, resp, view });
			} catch (InvocationTargetException ite) {
				boolean isErrProcess=onError(req, resp, methodName, ite.getTargetException());
				if(!isErrProcess)
					throw ite.getTargetException();
			}

			String viewPage = view.getViewPage();
			if (viewPage == null) {
				String viewName = view.getViewName();
				if (viewName != null)
					viewPage = this.config.getInitParameter(viewName);
			}
			
			// ViewMeta 가 동작하지 않으면 bypass
			if(!view.isEnable()) {
				req.setAttribute("templet-bypass", "true");
				return;
			}
				

			// 메쏘르 처리를 마친후,view 페이지를 보여준다.
			if (viewPage != null) {

				if (view.getViewMethod() == ViewMeta.DISPATCH) {
					
					if(!view.isProcessTemplate())
						req.setAttribute("templet-bypass", "true");
					
					this.getServletContext().getRequestDispatcher(viewPage).forward(req, resp);
				}
				else if (view.getViewMethod() == ViewMeta.REDIRECT)
					resp.sendRedirect(viewPage);
			}
			else if(!view.isProcessTemplate())
				req.setAttribute("templet-bypass", "true");
				

		} catch (ServletException se) {
			boolean isErrProcess=onError(req, resp, methodName, se);
			if(!isErrProcess)
				throw se;
		} catch (IOException ie) {
			boolean isErrProcess=onError(req, resp, methodName, ie);
			if(!isErrProcess)
				throw ie;
		} catch (Throwable e) {
			boolean isErrProcess=onError(req, resp, methodName, e);
			if(!isErrProcess)
				throw new ServletException(e.toString());
		} finally {
			doAfter(req, resp, methodName);
		}
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doBefore(HttpServletRequest req, HttpServletResponse resp, String methodName)
			throws ServletException, IOException
	{

	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doAfter(HttpServletRequest req, HttpServletResponse resp, String methodName)
			throws ServletException, IOException
	{

	}

	/**
	 * when error occus,
	 * 
	 * @param req
	 * @param resp
	 * @param methodName
	 * @param e
	 * @throws ServletException
	 * @throws IOException
	 */
	protected boolean onError(HttpServletRequest req, HttpServletResponse resp, String methodName, Throwable e)  throws ServletException, IOException
	{
		//e.printStackTrace();
		return false;
		
	}

	/**
	 * /servletName/method.do
	 * 
	 * 메쏘드 명을 추출한다. 경우에 따라 이 메쏘드를 재정의하여 사용자가 원하는 메쏘드 추출방식을 정할 수 있다.
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	protected String getMethodName(HttpServletRequest req, HttpServletResponse resp)
	{
		String pathInfo = req.getPathInfo();

		if (pathInfo == null || pathInfo.length() < 2)
			return this.getDefaultMethodName();

		String methodName = pathInfo.substring(1);
		int p = methodName.indexOf(".");
		if (p > 0) {
			methodName = methodName.substring(0, p);
		}

		return methodName;

	}
	
	
	public static void xbind(HttpServletRequest req, Object model) throws IllegalArgumentException
	{
		if (model instanceof DataSet) {
			DataSet ds = (DataSet) model;
			ds.setProperty("xbind", "true");
			bindDataset(req, ds, null);

		}
	}

	/**
	 * DataSet 또는 JavaBean(POJO) Model 객체에 Http Request 를 분석하여 값을 설정한다.
	 * 
	 * @param req
	 * @param model
	 */
	public static void bind(HttpServletRequest req, Object model) throws IllegalArgumentException
	{
		if (model == null)
			return;

		// DataSet 인 경우
		if (model instanceof DataSet) {
			DataSet ds = (DataSet) model;
			bindDataset(req, ds, null);

		} else {
			try {

				bindObjectByFilter(req, model, null);
			} catch (Exception e) {
				throw new IllegalArgumentException(e.toString());
			}
		}
	}
	
	
	/**
	 * 
	 * @param req
	 * @param model
	 * @throws IllegalArgumentException
	 */
	public static void bindByFilter(HttpServletRequest req, Object model, String prefix) throws IllegalArgumentException
	{
		if (model == null)
			return;

		// DataSet 인 경우
		if (model instanceof DataSet) {
			DataSet ds = (DataSet) model;
			bindDataset(req, ds, prefix);

		} else {
			try {

				bindObjectByFilter(req, model, prefix);
			} catch (Exception e) {
				throw new IllegalArgumentException(e.toString());
			}
		}
	}
	

	/**
	 * DataSet 또는 JavaBean(POJO) Model 객체에 Http Request 를 분석하여 값을 설정한다. 단 해당 propertyName 값만을 설정한다.
	 * 
	 * <pre>
	 * bind(request, input, &quot;name&quot;);
	 * bind(request, input, &quot;age&quot;);
	 * bind(request, input, &quot;title&quot;);
	 * 
	 * </pre>
	 * 
	 * @param req
	 * @param model
	 * @param propertyName
	 * @throws IllegalArgumentException
	 */
	public static void bind(HttpServletRequest req, Object model, String propertyName) throws IllegalArgumentException
	{
		if (model == null)
			return;

		// DataSet 인 경우
		if (model instanceof DataSet) {
			DataSet ds = (DataSet) model;

			String[] values = req.getParameterValues(propertyName);
			if (values == null)
				return;

			for (int i = 0; i < values.length; i++) {
				// if (i !=0 ) paramValue += ",";
				Object paramValue = values[i];

				// System.out.println(i+" "+paramName+":"+paramValue);
				ds.put(propertyName, paramValue, i);

			}

		} else {
			try {

				bindObject(req, model, propertyName);
			} catch (Exception e) {
				throw new IllegalArgumentException(e.toString());
			}
		}
	}

	/**
	 * DataSet 객체에 값을 설정한다.
	 * 
	 * @param req
	 * @param ds
	 * @param prefix
	 */
	private static void bindDataset(HttpServletRequest req, DataSet ds, String prefix)
	{
		boolean isUnEscapeHtml = false;
		
		if(ds.getProperty("xbind") != null && "true".equals(ds.getProperty("xbind")))
		{
			isUnEscapeHtml = true;
		}
		
		Enumeration attrFieldName = req.getAttributeNames();
		while(attrFieldName.hasMoreElements())
		{
			String keyName = (String)attrFieldName.nextElement();			
			Object obj = null;
			try
			{
				
				obj = req.getAttribute(keyName);
		
				if(obj != null)
				{					
					if(obj.getClass().getName().equals("java.lang.String"))
					{
						String val = (String)obj;
						if(isUnEscapeHtml) val = org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4(val); 
						ds.put(keyName, val);
					}
					else if(obj.getClass().getName().equals("java.lang.Boolean"))
						ds.put(keyName, (Boolean)obj);
					else if(obj.getClass().getName().equals("java.lang.Integer"))
						ds.put(keyName, obj);
					else if(obj.getClass().getName().equals("java.lang.Double"))
						ds.put(keyName, obj);
					else
						ds.put(keyName, (String)obj);
				}
			} catch(Exception ex)
			{}
				
		}
		//Logger.debug.println(LOG_ID+ds.toString());
		
		Enumeration fieldNames = req.getParameterNames();
		
		while (fieldNames.hasMoreElements()) {
			String paramName = (String) fieldNames.nextElement();

			String[] values = req.getParameterValues(paramName);
			if (values == null)
				continue;
			
			for (int i = 0; i < values.length; i++) {
				// if (i !=0 ) paramValue += ",";
				Object paramValue = values[i];
				
				
				// System.out.println(i+" "+paramName+":"+paramValue);
				
				if(prefix==null)
				{
					if(paramValue.getClass().getName().equals("java.lang.String")){
						String val = (String)paramValue;
						if(isUnEscapeHtml) val = org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4(val); 
						ds.put(paramName, val, i);
					}
					else					
						ds.put(paramName, paramValue, i);
				}
				// prefix 로 시작하는 property 만 세팅하고 prefix는 제거한다.
				else if(paramName.indexOf(prefix)==0)
				{
					paramName = paramName.substring(prefix.length());
					if(paramValue.getClass().getName().equals("java.lang.String")){
						String val = (String)paramValue;
						if(isUnEscapeHtml) val = org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4(val); 
						ds.put(paramName, val, i);
					}else						
						ds.put(paramName, paramValue,i);
				}

			}
		}		
				
	}

	/**
	 * JavaBean Model 에 Request 값을 세팅한다.
	 * 
	 * @param req
	 * @param model
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private static void bindObjectByFilter(HttpServletRequest req, Object model, String prefix) throws IntrospectionException,
			IllegalAccessException, InvocationTargetException
	{

		BeanInfo info = Introspector.getBeanInfo(model.getClass());

		PropertyDescriptor[] props = info.getPropertyDescriptors();

		// 매번 property 에 해당하는 Method를 찾지 않기 위하여
		for (int i = 0; i < props.length; i++) {

			Method m = props[i].getWriteMethod();

			// write method를 사용할 수 있다면
			if (m != null) {
				String paramName = props[i].getName();
				if(prefix==null)
					prefix="";
				String[] values = req.getParameterValues(prefix+paramName);

				if (values == null)
					continue;

				// 값이 한개인 경우
				if (values.length == 1) {
					setValue(model, m, values[0]);
				}
			}
		}
	}

	/**
	 * 일반 JavaBean 형태의 Object에 HtppServletRequest 데이터를 세팅한다.
	 * 
	 * @param req
	 * @param model
	 * @param propertyName
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private static void bindObject(HttpServletRequest req, Object model, String propertyName)
			throws IntrospectionException, IllegalAccessException, InvocationTargetException
	{

		BeanInfo info = Introspector.getBeanInfo(model.getClass());

		PropertyDescriptor[] props = info.getPropertyDescriptors();

		// 매번 property 에 해당하는 Method를 찾지 않기 위하여
		for (int i = 0; i < props.length; i++) {

			Method m = props[i].getWriteMethod();

			// write method를 사용할 수 있다면
			if (m != null) {
				String paramName = props[i].getName();

				if (paramName.equals(propertyName)) {
					String[] values = req.getParameterValues(paramName);

					if (values == null)
						continue;

					// 값이 한개인 경우
					if (values.length == 1) {
						setValue(model, m, values[0]);
					}

					return;
				}
			}
		}
	}

	/**
	 * method 에 값을 세팅한다.
	 * 
	 * @param bean
	 * @param m
	 * @param val
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private static void setValue(Object bean, Method m, Object val) throws InvocationTargetException,
			IllegalAccessException
	{
		try {
			m.invoke(bean, new Object[] { val });
		}
		// 타입이 맞지 않는 exception이 발생하면, 그때 체크
		catch (java.lang.IllegalArgumentException iae) {

			Object setValue = null;

			Class paramType = m.getParameterTypes()[0];

			if (paramType == val.getClass())
				setValue = val;
			else if (paramType == int.class || paramType == Integer.class)
			{
				try
				{
					setValue = Integer.valueOf(val.toString());
				} catch(Exception ex)
				{
					setValue = 0;
				}
			}
			else if (paramType == float.class || paramType == Float.class)
				setValue = Float.valueOf(val.toString());
			else if (paramType == double.class || paramType == Double.class)
			{
				try
				{
					setValue = Double.valueOf(val.toString());
				} catch(Exception ex)
				{
					setValue = 0;
				}
			}
			else if (paramType == long.class || paramType == Long.class)
				setValue = Long.valueOf(val.toString());
			else if (paramType == String.class)
				setValue = val.toString();

			m.invoke(bean, new Object[] { setValue });
		}
	}

	public void log(String message, Throwable t)
	{
		Logger.debug.println("["+this.thisServletName+"] " +message,t);
		
	}

	public void log(String msg)
	{
		Logger.debug.println("["+this.thisServletName+"] " +msg);
	}
	
	

}
