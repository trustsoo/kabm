package jdf.framework.core.http;

import jdf.framework.core.HTTPResultConstant;
import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.InteractionBean;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.HtmlFormat;
import jdf.framework.core.util.StringFormater;
import jdf.framework.core.xml.XMLUtil;
import jdf.framework.logic.transform.*;
import jxl.write.WritableWorkbook;
import net.sf.json.JSONArray;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.HttpJspPage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * JSP 에서 controller 역활을 하기 위한 JSP 확장 class
 * 
 * @author
 * 
 */
abstract public class JspController extends WebController implements HttpJspPage
{
	private final static String LOG_ID = "<f:JspController> ";

	private ServletConfig config;

	/**
	 * 생성자
	 */
	public JspController() {
		super();
	}

	final public void init(ServletConfig config) throws ServletException
	{
		this.config = config;
		jspInit();
	}

	final public ServletConfig getServletConfig()
	{
		return config;
	}

	// This one is not final so it can be overridden by a more precise method
	public String getServletInfo()
	{
		return "A Superclass for an JSP Controller"; // maybe better?
	}

	/**
	 * 
	 */
	public void destroy()
	{
		jspDestroy();

	}

	/**
	 * 기본적으로 method 라는 파라미터 에서 메쏘드 명을 추출한다.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	protected String getMethodName(ServletRequest request, ServletResponse response)
	{
		String methodName = request.getParameter("cmd");
		if (methodName == null)
			return getDefaultMethodName();
		else
			return methodName;
	}
	


	/**
	 * context (application) 영역에서 속성값을 가져온다.
	 * @param var 속성명
	 * @return 속성값
	 */
	protected String getParam(String var){
		return (String)this.getServletContext().getAttribute(var);
	}
	/**
	 * The entry point into service.
	 */

	final public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException
	{
		this.interact = new InteractionBean();
		
		//casting exceptions will be raised if an internal error.
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		this.thisServletName = req.getRequestURI();

		_jspService(req, resp);

		String methodName = getMethodName(request, response);

		if (methodName == null)
			return;


		ViewMeta view = new ViewMeta();

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
			
			// ViewMeta 가 동작하지 않으면 bypass
			if(!view.isEnable()) {
				req.setAttribute("templet-bypass", "true");
				return;
			}
				


			String viewPage = view.getViewPage();
			if (viewPage == null) {
				String viewName = view.getViewName();
				if (viewName != null)
					viewPage = this.config.getInitParameter(viewName);
			}

			// 메쏘르 처리를 마친후,view 페이지를 보여준다.
			if (viewPage != null) {
				
				if(viewPage.indexOf("/")!=0) {
					viewPage = getBaseDirectory(req)+"/"+viewPage;
				}

				if (view.getViewMethod() == ViewMeta.DISPATCH) 
				{
					
					if(view.getTemplateName() != null && !"".equals(view.getTemplateName()))
						req.setAttribute("templet", view.getTemplateName());
					
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
			{
				if(e.getMessage() != null)				
					throw new ServletException(e.getMessage());
				else
					throw new ServletException(e.toString());
			}
		} finally {
			doAfter(req, resp, methodName);
		}
	}

	/**
	 * 현재 호출되는 JSP 페이지의 directory 위치를 반환한다.
	 * @param req HttpServletRequest
	 * @return 디렉토리 경로
	 */
	protected String getBaseDirectory(HttpServletRequest req) {
		String fullPath=req.getRequestURI();
		int stx = req.getContextPath().length();
		int etx = fullPath.lastIndexOf("/");
		if(etx<=stx)
			etx=fullPath.length();
		return fullPath.substring(stx,etx);
	}	
	
	/**
	 * abstract method to be provided by the JSP processor in the subclass Must be defined in subclass.
	 */

	/**
	 * 
	 * @see HttpJspPage#_jspService(HttpServletRequest,
	 *      HttpServletResponse)
	 */
	abstract public void _jspService(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException,
			IOException;

	/**
	 * 
	 * @see javax.servlet.jsp.JspPage#jspDestroy()
	 */
	public void jspDestroy()
	{

	}

	/**
	 * 
	 * @see javax.servlet.jsp.JspPage#jspInit()
	 */
	public void jspInit()
	{

	}
	
	/**
	 * JQuery-Grid에서 통용되는  XML을 리턴한다.
	 * @Method getJQGridXML
	 * @return String
	 * @param data
	 * @param blockNames
	 * @return jqGridXML
	 * @throws Exception
	 */
	protected String getJQGridXML(DataSet data, String[] blockNames) throws Exception
	{
		return getJQGridXML(data, blockNames, "UTF-8");
	}
	
	/**
	 * JQuery-Grid에서 통용되는  XML을 리턴한다.
	 * @Method getJQGridXML
	 * @return String
	 * @param data
	 * @param blockNames
	 * @param encoding
	 * @return jqGridXML
	 * @throws Exception
	 */
	protected String getJQGridXML(DataSet data, String[] blockNames, String encoding) throws Exception
	{
		ByteArrayOutputStream bos = null;
		String result = null;
		try
		{
			DataSet dest = data.copyDataSetByBlockNames(blockNames);
			
			
			
			dest.setProperty(TransFormConstant.TOT_CNT, data.getText(TransFormConstant.TOT_CNT));
			dest.setProperty(TransFormConstant.CUR_PG, data.getText(TransFormConstant.CUR_PG));
			dest.setProperty(TransFormConstant.ROW_PER_PAGE, data.getText(TransFormConstant.ROW_PER_PAGE));
			dest.setProperty(TransFormConstant.GRID_TYPE, data.getText(TransFormConstant.GRID_TYPE));
			
			bos = new ByteArrayOutputStream();
			TransformerFactory factory = TransformerFactory.getInstance();
			Transformer former = factory.getTransformer(TransformerType.GRID_TYPE);
			former.setDefaultEncoding(encoding);
			former.transform(dest, bos);			
			result = bos.toString(encoding);
			
			data = null;
			
		} catch(Exception ex)
		{
			throw ex;
		} finally
		{
			if(bos != null) try{bos.close();}catch(Exception ex){}
		}
		return result;
	}
	
	protected String getJsTreeJson(DataSet data, String[] blockNames) throws Exception
	{
		return getJsTreeJson(data, blockNames, "UTF-8");
	}
	
	protected String getJsTreeJson(DataSet data, String[] blockNames, String encoding) throws Exception
	{
		ByteArrayOutputStream bos = null;
		String result = null;
		try
		{

			DataSet dest = data.copyDataSetByBlockNames(blockNames);
			
			
			bos = new ByteArrayOutputStream();
			TransformerFactory factory = TransformerFactory.getInstance();
			Transformer former = factory.getTransformer(TransformerType.TREE_JSON_TYPE);
			former.setDefaultEncoding("UTF-8");
			former.transform(dest, bos);
			result = bos.toString(encoding);
			
			data = null;
			
		} catch(Exception ex)
		{
			throw ex;
		} finally
		{
			if(bos != null) try{bos.close();}catch(Exception ex){}
		}
		return result;
	}
	
	/**
	 * JsTree 검색 내용을 JsonArray 형태로 리턴한다.
	 * 
	 * @param data
	 * @param blockNames
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getJsTreeJsonSearch(DataSet data, String[] blockNames) throws ServletException, IOException, Exception {
		DataSet dest = data.copyDataSetByBlockNames(blockNames);
		List<ConcurrentMap<String, Object>> tree_list = new ArrayList<ConcurrentMap<String, Object>>();
		
		for (int x=0; x<dest.getMaxDataSize(); x++) {
			ConcurrentMap<String, Object> node_map = new ConcurrentHashMap<String, Object>();
			ConcurrentMap<String, Object> attr = new ConcurrentHashMap<String, Object>();
			String data_no = "";
			String p_data_no = "";
			String data_nm = "";
			
			for (int y=0; y<dest.getBlocks()[0].getFields().length; y++) {
				String fname = dest.getBlocks()[0].getFields()[y].getName();
				attr.put(fname, dest.getText(fname, x));
				
				/**
				 * <쿼리 조회시 필수 설정 값>
				 * 코드는 data_no
				 * 부모 코드는 p_data_no (최상위 노드의 부모 코드는 -1)
				 * 코드명은 data_nm
				 */
				if ("data_no".equals(fname)) {
					data_no = dest.getText(fname, x);
				} else if ("p_data_no".equals(fname)) { 
					p_data_no = dest.getText(fname, x);
				} else if ("data_nm".equals(fname)) { 
					data_nm = dest.getText(fname, x);
				}
			}
			
			attr.put("id", data_no);
			node_map.put("attr", attr);
			node_map.put("data", data_nm);
			node_map.put("state", "disabled");
			node_map.put("children", new ArrayList<ConcurrentMap<String, Object>>());
			
			if ("-1".equals(p_data_no)) {
				tree_list.add(node_map); // 최상위 노드 추가
			} else {
				for (int z=0; z<tree_list.size(); z++) {
					ConcurrentMap<String, Object> root_node = (ConcurrentMap<String, Object>)tree_list.get(z);
					String top_data_no = ((ConcurrentMap<String, Object>)root_node.get("attr")).get("data_no").toString();
					List<ConcurrentMap<String, Object>> child_node = (ArrayList<ConcurrentMap<String, Object>>)root_node.get("children");
					
					boolean is_break = getChildNode(child_node, node_map, top_data_no, p_data_no); // 트리 구조 생성 (재귀함수)
					if (is_break) break;
				}
			}
		}
		
		JSONArray jsonArray = JSONArray.fromObject(tree_list);
		
		return jsonArray.toString();
	}
	
	/**
	 * 자식(node_map)의 p_dept_no와 일치하는 부모(root_node)의 dept_no를 찾아
	 * 해당 부모의 children에 자식을 추가한다.
	 * 
	 * @param root_node (부모 노드 정보)
	 * @param node_map (자식 노드 정보)
	 * @param top_data_no (최상위 노드 번호)
	 * @param p_data_no (자식 노드의 상위 번호)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean getChildNode(List<ConcurrentMap<String, Object>> root_node, ConcurrentMap<String, Object> node_map, String top_data_no, String p_data_no) {
		boolean is_break = false;
		
		if (top_data_no.equals(p_data_no)) { // 최상위 바로 하위 노드는 무조건 추가
			root_node.add(node_map);
		} else {
			for (int i=0; i<root_node.size(); i++) {
				if (!is_break) {
					ConcurrentMap<String, Object> node = (ConcurrentMap<String, Object>)root_node.get(i);
					String s_data_no = ((ConcurrentMap<String, Object>)node.get("attr")).get("data_no").toString();
					String s_rel = ((ConcurrentMap<String, Object>)node.get("attr")).get("rel").toString();
					List<ConcurrentMap<String, Object>> child_node = (List<ConcurrentMap<String, Object>>)node.get("children");
					
					if (s_data_no.equals(p_data_no)) {
						child_node.add(node_map);
						is_break = true;
					} else {
						if (!"default".equals(s_rel)) {
							is_break = getChildNode(child_node, node_map, top_data_no, p_data_no);
						}
					}
				}
			}
		}
		
		return is_break;
	}
	
	protected String getJsTreeJsonEx(DataSet data, String[] blockNames) throws Exception
	{
		return getJsTreeJsonEx(data, blockNames, "UTF-8");
	}
	
	protected String getJsTreeJsonEx(DataSet data, String[] blockNames, String encoding) throws Exception
	{
		ByteArrayOutputStream bos = null;
		String result = null;
		try
		{

			DataSet dest = data.copyDataSetByBlockNames(blockNames);
			
			
			bos = new ByteArrayOutputStream();
			TransformerFactory factory = TransformerFactory.getInstance();
			Transformer former = factory.getTransformer(TransformerType.TREE_JSON_TYPE_EX);
			former.setDefaultEncoding(encoding);
			//former.transformForAll(dest, bos);
			result = bos.toString(encoding);
			
			data = null;
			
		} catch(Exception ex)
		{
			throw ex;
		} finally
		{
			if(bos != null) try{bos.close();}catch(Exception ex){}
		}
		return result;
	}	
	
	/**
	 * JSON 형태의 데이터를 리턴한다.
	 * @Method getJson
	 * @return String
	 * @param data
	 * @param blockNames
	 * @return jqGridXML
	 * @throws Exception
	 */
	protected String getDynamicTreeJson(DataSet data, String[] blockNames) throws Exception
	{
		return getDynamicTreeJson(data, blockNames, "UTF-8");
	}

	/**
	 * JSON 형태의 데이터를 리턴한다.
	 * @Method getJson
	 * @return String
	 * @param data
	 * @param blockNames
	 * @param encoding
	 * @return jqGridXML
	 * @throws Exception
	 */
	protected String getDynamicTreeJson(DataSet data, String[] blockNames, String encoding) throws Exception
	{
		ByteArrayOutputStream bos = null;
		String result = null;
		try
		{	
			DataSet dest = data.copyDataSetByBlockNames(blockNames);
			
			bos = new ByteArrayOutputStream();
			TransformerFactory factory = TransformerFactory.getInstance();
			Transformer former = factory.getTransformer(TransformerType.DYNAMIC_TREE_JSON_TYPE);
			former.setDefaultEncoding(encoding);
			former.transform(dest, bos);
			result = bos.toString(encoding);
			
			data = null;
			
		} catch(Exception ex)
		{
			throw ex;
		} finally
		{
			if(bos != null) try{bos.close();}catch(Exception ex){}
		}
		return result;
	}
	
	/**
	 * JSON 형태의 데이터를 리턴한다.
	 * @Method getJson
	 * @return String
	 * @param data
	 * @param blockNames
	 * @return jqGridXML
	 * @throws Exception
	 */
	protected String getJson(DataSet data, String[] blockNames) throws Exception
	{
		return getJson(data, blockNames, "UTF-8");
	}
	
	/**
	 * JSON 형태의 데이터를 리턴한다.
	 * @Method getJson
	 * @return String
	 * @param data
	 * @param blockNames
	 * @param encoding
	 * @return jqGridXML
	 * @throws Exception
	 */
	protected String getJson(DataSet data, String[] blockNames, String encoding) throws Exception
	{
		ByteArrayOutputStream bos = null;
		String result = null;
		try
		{	
			DataSet dest = data.copyDataSetByBlockNames(blockNames);
			
			
			String json_type = data.getText(TransFormConstant.JSON_DATA_TYPE);
			
			int tot_page = 1;
			int row_per_page = 0;
			int tot_cnt = 0;
			int curPg = 1;
			int prev_key = -1;
			int next_key = -1;
			try
			{	
				
				if(json_type == null || "".equals(json_type))
				{
					row_per_page = dest.getInt(TransFormConstant.ROW_PER_PAGE);
					tot_cnt = dest.getInt(TransFormConstant.TOT_CNT);
					curPg = dest.getInt(TransFormConstant.CUR_PG);
				}
				else if ("datatable".equals(json_type))
				{
					tot_cnt = data.getInt(TransFormConstant.TOT_CNT);	// 전체건수
				}
				else //if jqgrid
				{
					row_per_page = data.getInt(TransFormConstant.ROW_PER_PAGE);
					tot_cnt = data.getInt(TransFormConstant.TOT_CNT);
					curPg = data.getInt(TransFormConstant.CUR_PG);
				}
				
				try
				{
					if (tot_cnt % row_per_page == 0) {
						tot_page = tot_cnt / row_per_page;
		            } else {
		            	tot_page = (tot_cnt / row_per_page) + 1;
		            }
				} catch(Exception ex)
				{
					tot_page = 1;
				}
			} catch(Exception ex)
			{				
			}
			
			dest.put(TransFormConstant.TOT_PAGE, tot_page);			
			dest.put(TransFormConstant.JSON_DATA_TYPE, json_type);
			
			
			if(!"".equals(data.getText(TransFormConstant.PAGE_KEY)))
			{
				
				int temp = 0;
				try
				{
					temp = data.getInt(TransFormConstant.DATA_KEY, 0);
				} catch(Exception ex)
				{}
				
				if(temp == 0)
				{
					prev_key =  data.getInt(TransFormConstant.PAGE_KEY) - row_per_page;
				} else
				{
					prev_key = temp - row_per_page - 1;
				}
				
				//prev_key = prev_key - 1;
				
				if(prev_key < 0) prev_key = -1;
				
				
				if(data.getMaxDataSize() == row_per_page)
				{
					try
					{
						next_key = data.getInt(TransFormConstant.DATA_KEY, data.getMaxDataSize()-1);
					} catch(Exception ex)
					{
						
						next_key = -1; 
					}
				}
				
				if(next_key < 0) next_key = -1; 
				
				dest.put(TransFormConstant.NEXT_KEY, next_key+"");
				dest.put(TransFormConstant.PREV_KEY , prev_key+"");
			}
				
			
			
			
			bos = new ByteArrayOutputStream();
			TransformerFactory factory = TransformerFactory.getInstance();
			Transformer former = factory.getTransformer(TransformerType.JSON_TYPE);
			former.setDefaultEncoding(encoding);
			former.transform(dest, bos);
			result = bos.toString(encoding);
			
			if(json_type == null || "".equals(json_type))			
				result = getJsonFormat(result);
			else if ("datatable".equals(json_type))
				result = getDataTableJsonFormat(tot_cnt, blockNames, result);
			else
				result = getGridJsonFormat(tot_page, tot_cnt, curPg, prev_key, next_key, result);
			
			data = null;
			
		} catch(Exception ex)
		{
			throw ex;
		} finally
		{
			if(bos != null) try{bos.close();}catch(Exception ex){}
		}
		return result;
	}
	
	protected String getJsonFormat(String data)
	{
		return getJsonFormat(HTTPResultConstant.RESULT_OK, "success", data);
		
	}
	
	
	protected String getJsonFormat(String code, String msg, String data)
	{
		StringBuffer json = new StringBuffer();	
		msg = HtmlFormat.translateJson(msg);
		json.append("{\n")
		    .append("\"result\":{\n")
			.append("  	\"code\":").append("\""+code+"\"").append(",\n");
			if(data != null && !"".equals(data))
			{
				json.append("  	\"msg\":").append("\""+msg+"\"").append(",\n")
			    .append("  	\"data\":").append(data).append("\n");
			} else
			{
				json.append("  	\"msg\":").append("\""+msg+"\"").append("\n");
			}
			json.append("}\n")
			.append("}");
		
		return json.toString();		
	}
	
	/**
	 * DataTable용 Json 생성
	 * 
	 * @param records
	 * @param blockNames
	 * @param data
	 * @return
	 */
	protected String getDataTableJsonFormat(int tot_cnt, String[] blockNames, String data)
	{
		data = data.substring(1);
		data = data.substring(0, data.length()-1);		
		
		StringBuffer json = new StringBuffer();
		json.append("{\n")
			.append("  	\"recordsTotal\":").append(tot_cnt).append(",\n")
			.append(data).append("\n")
			.append("}");
		
		return json.toString();
	}
	
	protected String getGridJsonFormat(int totalPage, int records, int page,  int prev_key, int next_key, String data)
	{
		data = data.substring(1);
		data = data.substring(0, data.length()-1);		
		
		StringBuffer json = new StringBuffer();
		json.append("{\n")
			.append("  	\"total\":").append("\""+totalPage+"\"").append(",\n")
			.append("  	\"records\":").append("\""+records+"\"").append(",\n")
			.append("  	\"page\":").append("\""+page+"\"").append(",\n")
			.append("  	\"prev_key\":").append("\""+prev_key+"\"").append(",\n")
			.append("  	\"next_key\":").append("\""+next_key+"\"").append(",\n")						
			.append(data).append("\n")
			.append("}");
		
		/*json.append("{\n")
		    .append("\"result\":{\n")
			.append("  	\"code\":").append("\""+code+"\"").append(",\n")
			.append("  	\"msg\":").append("\"success\"").append(",\n")
		    .append("  	\"data\":").append(data).append("\n")
		    .append("}\n")
			.append("}");*/
		
		return json.toString();
		
	}
	
	protected void responseJson(String data, HttpServletResponse resp) throws Exception
	{
		
		PrintWriter pw = null;		
		try
		{
			resp.reset();			
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			pw = resp.getWriter();
			pw.print(data);
		} catch(Exception ex)
		{
			throw ex;
		}
	}
	
	
	
	protected String getSOAP(DataSet data, String[] blockNames) throws Exception
	{
		return getSOAP(data, blockNames, "UTF-8");
	}
	
	protected String getSOAP(DataSet data, String[] blockNames, String encoding) throws Exception
	{
		ByteArrayOutputStream bos = null;
		String result = null;			
		try
		{
			
			DataSet dest = data.copyDataSetByBlockNames(blockNames);
			int tot_page = 1;
			try
			{			
				int row_per_page = dest.getInt(TransFormConstant.ROW_PER_PAGE);
				int tot_cnt = dest.getInt(TransFormConstant.TOT_CNT);
				try
				{
					if (tot_cnt % row_per_page == 0) {
						tot_page = tot_cnt / row_per_page;
		            } else {
		            	tot_page = (tot_cnt / row_per_page) + 1;
		            }
				} catch(Exception ex)
				{
					tot_page = 1;
				}
			} catch(Exception ex)
			{				
			}
			
			dest.put(TransFormConstant.TOT_PAGE, tot_page);
			bos = new ByteArrayOutputStream();
			TransformerFactory factory = TransformerFactory.getInstance();
			Transformer former = factory.getTransformer(TransformerType.SOAP_TYPE);
			former.setDefaultEncoding(encoding);			
			former.transform(dest, bos);
			result = bos.toString(encoding);
			result = "<?xml version=\"1.0\" encoding=\""+encoding+"\"?>\n"+result;
			//result = getXMLFormat(result);
			
			data = null;
			
		} catch(Exception ex)
		{
			throw ex;
		} finally
		{
			if(bos != null) try{bos.close();}catch(Exception ex){}
		}
		return result;
	}
	
	
	/**
	 * XML 형태의 데이터를 리턴한다.
	 * @Method getXML
	 * @return String
	 * @param data
	 * @param blockNames
	 * @return jqGridXML
	 * @throws Exception
	 */
	protected String getXML(DataSet data, String[] blockNames) throws Exception
	{
		return getXML(data, blockNames, "UTF-8");
	}
	
	/**
	 * XML 형태의 데이터를 리턴한다.
	 * @Method getXML
	 * @return String
	 * @param data
	 * @param blockNames
	 * @param encoding
	 * @return jqGridXML
	 * @throws Exception
	 */
	protected String getXML(DataSet data, String[] blockNames, String encoding) throws Exception
	{
		ByteArrayOutputStream bos = null;
		String result = null;			
		try
		{
			
			DataSet dest = data.copyDataSetByBlockNames(blockNames);
			int tot_page = 1;
			try
			{			
				int row_per_page = dest.getInt(TransFormConstant.ROW_PER_PAGE);
				int tot_cnt = dest.getInt(TransFormConstant.TOT_CNT);
				try
				{
					if (tot_cnt % row_per_page == 0) {
						tot_page = tot_cnt / row_per_page;
		            } else {
		            	tot_page = (tot_cnt / row_per_page) + 1;
		            }
				} catch(Exception ex)
				{
					tot_page = 1;
				}
			} catch(Exception ex)
			{				
			}
			
			dest.put(TransFormConstant.TOT_PAGE, tot_page);
			bos = new ByteArrayOutputStream();
			TransformerFactory factory = TransformerFactory.getInstance();
			XmlTransformer former = factory.newXmlTransformer();
			former.setDefaultEncoding(encoding);
			former.setXmlType(XmlTransformer.XML_ELEMENT_CENTRIC);
			former.transform(dest, bos);			
			result = bos.toString(encoding);
			result = StringFormater.replaceStr(StringFormater.replaceStr(result, "<result>", ""), "</result>", "");
			result = getXMLFormat(result);
			
			data = null;
			
		} catch(Exception ex)
		{
			throw ex;
		} finally
		{
			if(bos != null) try{bos.close();}catch(Exception ex){}
		}
		return result;
	}
	
	
	protected String getXMLFormat(String data)
	{
		return getXMLFormat(HTTPResultConstant.RESULT_OK, "success", data);
		
	}
	
	protected String getXMLFormat(String code, String msg, String data)
	{
		StringBuffer sb = new StringBuffer();				
	    sb.append("<result>\n")
		.append("  	<code>").append(code).append("</code>\n")
		.append("  	<msg>").append(XMLUtil.replaceCDATA(msg)).append("</msg>\n")
	    .append("  	<data>").append(data).append("</data>\n")			    
	    .append("</result>");		
		return sb.toString();
		
	}
	
	protected void responseXML(String data, HttpServletResponse resp) throws Exception
	{
		
		PrintWriter pw = null;
		
		try
		{
			resp.reset();			
			resp.setContentType("text/xml");
			resp.setCharacterEncoding("UTF-8");
			//pw = new PrintWriter(new OutputStreamWriter(resp.getOutputStream(), "UTF-8"));
			//pw = resp.getOutputStream()
			pw = resp.getWriter();
			pw.write(data);
			//pw.write(data);
		} catch(Exception ex)
		{
			throw ex;
		}
	}
	
	protected void responseXML(String data, String encoding, HttpServletResponse resp) throws Exception
	{
		
		PrintWriter pw = null;
		
		try
		{
			resp.reset();			
			resp.setContentType("text/xml");
			resp.setCharacterEncoding(encoding);
			//pw = new PrintWriter(new OutputStreamWriter(resp.getOutputStream(), "UTF-8"));
			//pw = resp.getOutputStream()
			pw = resp.getWriter();
			pw.write(data);
			//pw.write(data);
		} catch(Exception ex)
		{
			throw ex;
		}
	}
	

	public String getXls(DataSet data, String[] blockNames) throws Exception
    {
        return getXls("", "", data, blockNames, "utf-8");
    }
	
	public String getXls(String title, String condition, DataSet data, String[] blockNames) throws Exception
	{
	    return getXls(title, condition, data, blockNames, "utf-8");
	}
	
	public String getXls(String title, String condition, DataSet data, String[] blockNames, String encoding) throws Exception
    {
	   return getXls(title, condition, null , null,  data, blockNames, encoding);
    }
   
	public String getXls(String title, String condition, WritableWorkbook workbook, File infile, DataSet data, String[] blockNames, String encoding) throws Exception
	{
		ByteArrayOutputStream bos = null;
		File file = null;
		try
		{
			bos = new ByteArrayOutputStream();
			TransformerFactory factory = TransformerFactory.getInstance();  

			DataSet dest = data.copyDataSetByBlockNames(blockNames);


			XlsTransformerImpl xlsForm = (XlsTransformerImpl)factory.getTransformer(TransformerType.EXCEL_TYPE);
			xlsForm.setDefaultEncoding(encoding);
			if(title.equals(""))  file = xlsForm.transform(dest, null, bos);
			else if( workbook != null ) file = xlsForm.transform(title, condition, workbook, infile, dest, null, bos);
			else file = xlsForm.transform(title, condition, dest, null, bos);

			data = null;

			Logger.info.println(LOG_ID+file.getAbsolutePath()+" is created.");
		   
	   } catch(Exception ex)
	   {
			throw ex;
	   } finally
	   {
			if(bos != null) try{bos.close();}catch(Exception ex){}
	   }
	   return file.getAbsolutePath();
	}
	
	/**
	 * List<ConcurrentMap<String, Object> 형태의 데이터를 리턴한다.
	 * @Method getMapList
	 * @param cmd
	 * @param xml_path
	 * @param block
	 * @param div_cd
	 * @param prnt_cd
	 * @param cmpny_no
	 * @returnList<ConcurrentMap<String, Object>
	 * @throws Exception
	 */
	protected List<ConcurrentMap<String, Object>> getMapList(String cmd, String xml_path, String block, String div_cd, String prnt_cd, String cmpny_no) throws Exception {
		DataSet input = new DataSet();
		DataSet output = new DataSet();
		List<ConcurrentMap<String, Object>> result = null;
		
		try {
			input.put("div_cd", (div_cd == null ? "" : div_cd));
			input.put("prnt_cd", (prnt_cd == null ? "" : prnt_cd));
			input.put("cmpny_no", (cmpny_no == null ? "" : cmpny_no));
			input.put("cmd", cmd);
			
			InteractionBean interact = new InteractionBean();
			output = interact.execute(xml_path, input);
			
			String[] blockNames = new String[1];
			blockNames[0] = block;
			
			result = getMapList(output, blockNames, "utf-8");
		} catch (Exception ex) {
			throw ex;
		}
		
		return result;
	}
    
    /**
	 * List<ConcurrentMap<String, Object> 형태의 데이터를 리턴한다.
	 * @Method getMapList
	 * @param cmd
	 * @param xml_path
	 * @param block
	 * @param params
	 * @returnList<ConcurrentMap<String, Object>
	 * @throws Exception
	 */
    protected List<ConcurrentMap<String, Object>> getMapList(String cmd, String xml_path, String block, String params) throws Exception {
		DataSet input = new DataSet();
		DataSet output = new DataSet();
		List<ConcurrentMap<String, Object>> result = null;
		
		try {
			String[] arrParams = params.split("&");
			for (String param : arrParams) {
				if (param.indexOf("=") > -1) {
					input.put(param.split("=", -1)[0], param.split("=", -1)[1]);
				}
			}
			input.put("cmd", cmd);
			
			InteractionBean interact = new InteractionBean();
			output = interact.execute(xml_path, input);
			
			String[] blockNames = new String[1];
			blockNames[0] = block;
			
			result = getMapList(output, blockNames, "utf-8");
		} catch (Exception ex) {
			throw ex;
		}
		
		return result;
	}
	
	/**
	 * List<ConcurrentMap<String, Object> 형태의 데이터를 리턴한다.
	 * @Method getMapList
	 * @return List<ConcurrentMap<String, Object>
	 * @param data
	 * @param blockNames
	 * @param encoding
	 * @throws Exception
	 */
	protected List<ConcurrentMap<String, Object>> getMapList(DataSet data, String[] blockNames, String encoding) throws Exception {
		List<ConcurrentMap<String, Object>> result = null;
		
		try {
			DataSet dest = data.copyDataSetByBlockNames(blockNames);
			result = new ArrayList<ConcurrentMap<String, Object>>();
			
			for (int x=0; x<dest.getMaxDataSize(); x++) {
				ConcurrentMap<String, Object> map = new ConcurrentHashMap<String, Object>();
				
				for (int y=0; y<dest.getBlocks()[0].getFields().length; y++) {
					String fname = dest.getBlocks()[0].getFields()[y].getName();
					map.put(fname, dest.getText(fname, x));
				}
				result.add(map);
			}
			
			data = null;
		} catch(Exception ex) {
			throw ex;
		}
		
		return result;
	}
}