package jdf.framework.core.http;


/**
 * MultiActionServlet에서 사용하는 View 정보 class 일반적으로 JSP 나 presentation url 경로를 가지고
 * 있는다.
 * 
 * @author
 * 
 */
public class ViewMeta {
	public final static int REDIRECT = 0;

	public final static int DISPATCH = 1;

	// 페이지 경로
	private String viewPage;

	// dispatch인지 redirect인지 여부
	private int viewMethod = DISPATCH;

	// 실제 jsp 경로가 아닌 meta 로 표현된 이름
	private String viewName;

	// anyTEMPLET 의 template 을 filtering 할것인지 여부
	private boolean processTemplate = true;

	// template 이름
	private String templateName;

	// view meta 가 동작할지 여부
	private boolean enableFlag = true;

	/**
	 * 기본 생성자
	 * 
	 */
	public ViewMeta() {

	}

	/**
	 * view 페이지를 dispatch 또는 redirect 할지를 결정한다.
	 * 
	 * @return
	 */
	public int getViewMethod() {
		return viewMethod;
	}

	/**
	 * view 하는 방법을 설정한다.
	 * 
	 * @param viewMethod
	 */
	public void setViewMethod(int viewMethod) {
		this.viewMethod = viewMethod;
	}

	/**
	 * view page 정보를 가져온다.
	 * 
	 * @return
	 */
	public String getViewPage() {
		return viewPage;
	}

	/**
	 * view 정보를 설정한다.
	 * 
	 * @param viewPage
	 */
	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}

	/**
	 * 이름을 가져온다.
	 * 
	 * @return
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * 이름으로 설정한다. 이 이름에 대한 실제 경로정보는 web.xml 의 servlet 초기 설정에 정의되어 있다.
	 * 
	 * @param viewName
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * template filter 처리여부
	 * 
	 * @param process
	 */
	public void processTemplate(boolean process) {
		this.processTemplate = process;
	}

	public boolean isProcessTemplate() {
		return this.processTemplate;
	}

	/**
	 * template 이름을 설정한다.
	 * 
	 * @param name
	 */
	public void setTemplateName(String name) {
		this.templateName = name;
	}

	/**
	 * template 명을 가져온다.
	 * 
	 * @return
	 */
	public String getTemplateName() {
		return this.templateName;
	}

	public void enable() {
		this.enableFlag = true;
	}

	public void disable() {
		this.enableFlag = false;
	}

	/**
	 * ViewMeta 가 동작할지 여
	 * 
	 * @return
	 */
	public boolean isEnable() {
		return this.enableFlag;
	}
	
	
	
	

}