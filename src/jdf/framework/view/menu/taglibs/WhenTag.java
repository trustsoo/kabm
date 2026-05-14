package jdf.framework.view.menu.taglibs;

import jdf.framework.view.menu.entity.MenuItem;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;


/**
 * <m:when test="${menu.idx}==0">
 * 
 * @author
 * 
 */
public class WhenTag extends BodyTagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 591652962929448650L;

	private boolean isProcess = false;

	private final static int CASE_UNDEFINED = -99;

	// !=
	private final static int CASE_NEQ = -1;

	// ==
	private final static int CASE_EQ = 0;

	// <
	private final static int CASE_LT = 1;

	// <=
	private final static int CASE_LTE = 2;

	// >
	private final static int CASE_GT = 3;

	// >=
	private final static int CASE_GTE = 4;

	private String menuPropertyName;

	private String checkValue;

	private int mode = CASE_UNDEFINED;

	/**
	 * 값 세팅과 동시에 파싱
	 * 
	 * @param test
	 */
	public void setTest(String test) {
		parseText(test);
	}

	/**
	 * case1) ${menu.idx}>0 case2) {
	 * 
	 * @param txt
	 */
	private void parseText(String txt) {
		int p1 = txt.indexOf("{");
		int p2 = txt.indexOf("}");

		// menu.idx
		String beanTxt = txt.substring(p1 + 2, p2);
		int p3 = beanTxt.indexOf(".");

		this.menuPropertyName = beanTxt.substring(p3 + 1);

		int t;

		if ((t = txt.indexOf("==")) > 0) {
			this.mode = CASE_EQ;
			t++;
		} else if ((t = txt.indexOf("!=")) > 0) {
			this.mode = CASE_NEQ;
			t++;
		}

		else if ((t = txt.indexOf("<=")) > 0 || (t = txt.indexOf("=<")) > 0) {
			this.mode = CASE_LTE;
			t++;
		}
		// 조건검색 순서가 중요하단다.
		else if ((t = txt.indexOf("<")) > 0) {
			this.mode = CASE_LT;
		} else if ((t = txt.indexOf(">=")) > 0 || (t = txt.indexOf("=>")) > 0) {
			this.mode = CASE_GTE;
			t++;
		}
		// 조건검색 순서가 중요하단다.
		else if ((t = txt.indexOf(">")) > 0) {
			this.mode = CASE_GT;
		}

		this.checkValue = txt.substring(t + 1).trim();

	}

	/**
	 * 데이터 검증
	 * 
	 * @param value
	 * @return
	 */
	boolean isConditionOk(Object value) {
		// System.out.println(" 검사 1 :" + value);
		// System.out.println(" 검사 2 :" + mode);
		// System.out.println(" 검사 3 :" + this.checkValue);

		if (value == null) {
			return false;
		}

		if (mode == CASE_UNDEFINED) {
			if ("true".equals(value))
				return true;

			if (value instanceof Boolean) {
				Boolean b = (Boolean) value;
				return b.booleanValue();
			}

			return false;
		}

		if (mode == CASE_EQ && value instanceof String)
			return value.equals(this.checkValue);
		else if (mode == CASE_NEQ && value instanceof String)
			return !value.equals(this.checkValue);

		if (mode == CASE_EQ && value instanceof Boolean) {
			return (this.checkValue.equals(value.toString()));

		} else if (mode == CASE_NEQ && value instanceof Boolean) {
			return (!this.checkValue.equals(value.toString()));

		}

		else if (mode == CASE_LT) {
			double d1 = Double.parseDouble(value.toString());
			double d2 = Double.parseDouble(this.checkValue);

			return (d1 < d2);
		} else if (mode == CASE_LTE) {
			double d1 = Double.parseDouble(value.toString());
			double d2 = Double.parseDouble(this.checkValue);

			return (d1 <= d2);
		} else if (mode == CASE_GT) {
			double d1 = Double.parseDouble(value.toString());
			double d2 = Double.parseDouble(this.checkValue);

			return (d1 > d2);
		} else if (mode == CASE_GTE) {
			double d1 = Double.parseDouble(value.toString());
			double d2 = Double.parseDouble(this.checkValue);

			return (d1 >= d2);
		}

		else if (mode == CASE_EQ) {
			double d1 = Double.parseDouble(value.toString());
			double d2 = Double.parseDouble(this.checkValue);

			return (d1 == d2);
		} else if (mode == CASE_NEQ) {
			double d1 = Double.parseDouble(value.toString());
			double d2 = Double.parseDouble(this.checkValue);

			return (d1 != d2);
		}

		return false;

	}

	String getMenuPropertyName() {
		return this.menuPropertyName;
	}

	String getCheckValue() {
		return this.checkValue;
	}

	int getMode() {
		return this.mode;
	}

	/**
	 * doStartTag
	 * 
	 */
	public int doStartTag() throws JspException {

		try {
			ListTag listTag = (ListTag) findAncestorWithClass(this, ListTag.class);

			// list tag에 의해 현재 선택된 메뉴를 가져온다.
			MenuItem menu = listTag.getMenuItem();

			boolean isContinue = false;

			if ("selected".equals(this.menuPropertyName)) {
				boolean m = listTag.isSelected();
				isContinue = isConditionOk(new Boolean(m));

			} else if ("access".equals(this.menuPropertyName)) {
				boolean m = listTag.isAccessible();
				isContinue = isConditionOk(new Boolean(m));
			} else if ("isFirst".equals(this.menuPropertyName)) {
				boolean m = listTag.isFirst();
				isContinue = isConditionOk(new Boolean(m));
			} else if ("isLast".equals(this.menuPropertyName)) {
				boolean m = listTag.isLast();
				isContinue = isConditionOk(new Boolean(m));
			} else {
				String val = menu.getParam(this.menuPropertyName);
				isContinue = isConditionOk(val);
			}

			if (isContinue) {
				isProcess = true;

				return EVAL_BODY_AGAIN;
			}

		} catch (Exception e) {
			throw new JspException("Error: IOException while writing to client ");
		}
		isProcess = false;
		return SKIP_BODY;
	}

	public int doEndTag() throws JspException {
		// System.out.println("EqualsTag:doEndTag");
		try {
			if (isProcess) {
				BodyContent bc = this.getBodyContent();
				bc.writeOut(bc.getEnclosingWriter());
			}

		} catch (IOException ioe) {
			throw new JspException("Error: IOException while writing to client ");
		}
		return EVAL_PAGE;
	}

}