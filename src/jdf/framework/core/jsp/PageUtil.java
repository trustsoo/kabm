/*
 * Created on 2004-04-29
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jdf.framework.core.jsp;

import jdf.framework.core.data.DataSet;

import java.util.Vector;

public class PageUtil
{

	public Vector<Object> temp_vec;
	DataSet dataSet;
	public int totalCnt = 0;
	int cur_page = 1;
	public PageVector pv;


	/**
	* 페이지 URL을 리턴해 주는 메소드
	* param scr_id 화면 번호
	* return String 페이지URL
	*/

	public PageUtil(int row_cnt, DataSet ds, String key_str, int cur_page)
	{
		dataSet = ds;
		temp_vec = formatData(key_str);
		this.cur_page = cur_page;
		pv = new PageVector(row_cnt, temp_vec);
		pv.setOrdinalPage(cur_page);
	}
	
	public PageUtil(int row_cnt, Vector<Object> vec, int cur_page)
	{
		temp_vec = new Vector<Object>();
		totalCnt = vec.size();
		for (int idx = 0; idx < totalCnt; idx++)
		{
			temp_vec.add(idx + "");
		}
		this.cur_page = cur_page;
		pv = new PageVector(row_cnt, temp_vec);
		pv.setOrdinalPage(cur_page);
		
	}
	
	public Vector<Object> formatData(String key_str)
	{
		Vector<Object> vec = new Vector<Object>();

		if (dataSet != null)
			totalCnt = dataSet.getCount(key_str);

		for (int idx = 0; idx < totalCnt; idx++)
		{
			vec.add(idx + "");
		}
		return vec;
	}

	public int getStartRow()
	{
		return pv.getStartRow();
	}

	public int getEndRow()
	{
		return pv.getEndRow();
	}

	/**
	* 페이징 소스를 만들어 낸다.
	* <PRE></PRE>
	*
	* @param int cur_page
	*
	* @return java.lang.String( sb.toString() )
	*/
	public String getMovePage(int cur_page)
	{
		StringBuffer sb = new StringBuffer();
		String[] img =
			{
				"/images/comm/board_allow_1.gif",
				"/images/comm/board_allow_2.gif",
				"/images/comm/board_allow_3.gif",
				"/images/comm/board_allow_4.gif",
				"/images/comm/board_allow_5.gif",
				"/images/comm/board_allow_6.gif" };
		//int[] cur_pg = {1, getPrevPageNo(), getNextPageNo(), getLastPageNo()};

		if (!pv.isFirstPage())
		{
			sb.append("<A HREF=\"javascript:js_submit('1');\" TITLE=\'첫 페이지로..\'>");
			sb.append("<IMG SRC=\'" + img[0] + "\' ALIGN=\'ABSMIDDLE\'  BORDER=0>");
			sb.append("</A>\n");
		}
		else
		{
			sb.append("<IMG SRC=\'" + img[0] + "\' BORDER=0 ALIGN=\'ABSMIDDLE\' >\n");
		}

		if (pv.getPrevPageUnit() > 0)
		{
			sb.append(
				"<A HREF=\"javascript:js_submit('"
					+ pv.getPrevPageUnit()
					+ "');\" TITLE=\'"
					+ pv.getPrevPageUnit()
					+ " 페이지로..\'>");
			sb.append("<IMG SRC=\'" + img[1] + "\' BORDER=0 ALIGN=\'ABSMIDDLE\' >");
			sb.append("</A>\n");
			sb.append("<IMG SRC=\'" + img[2] + "\' BORDER=0 ALIGN=\'ABSMIDDLE\' >");

		}
		else
		{

			sb.append("<IMG SRC=\'" + img[1] + "\' BORDER=0 ALIGN=\'ABSMIDDLE\' >\n");
			sb.append("<IMG SRC=\'" + img[2] + "\' BORDER=0 ALIGN=\'ABSMIDDLE\' >\n");

		}

		int[] num = pv.Arraynum();

		for (int idx = 0; idx < num.length; idx++)
		{
			if (num[idx] > pv.getLastPageNo())
			{
				//sb.append(num[idx]);

			}
			else if (num[idx] == cur_page)
			{
				sb.append("<FONT COLOR='15AC95'><B>[" + num[idx] + "]</B></FONT>\n");
			}
			else
			{
				sb.append("<A HREF=\"javascript:js_submit('" + num[idx] + "')\" CLASS=\'noti\'>");
				sb.append("[" + num[idx] + "]");
				sb.append("</A>\n");
			}
		}

		if (pv.getLastPageNo() > pv.endpg)
		{
			sb.append("<IMG SRC=\'" + img[3] + "\' BORDER=0 ALIGN=\'ABSMIDDLE\' >");
			sb.append(
				"<A HREF=\"javascript:js_submit('"
					+ pv.getNextPageUnit()
					+ "');\" TITLE=\'"
					+ pv.getNextPageUnit()
					+ " 페이지로..\'>");
			sb.append("<IMG SRC=\'" + img[4] + "\' BORDER=0 ALIGN=\'ABSMIDDLE\' >");
			sb.append("</A>\n");
		}
		else
		{
			sb.append("<IMG SRC=\'" + img[3] + "\' BORDER=0 ALIGN=\'ABSMIDDLE\' >\n");
			sb.append("<IMG SRC=\'" + img[4] + "\' BORDER=0 ALIGN=\'ABSMIDDLE\' >\n");
		}

		if (!pv.isLastPage())
		{
			sb.append("<A HREF=\"javascript:js_submit('" + pv.getLastPageNo() + "');\" TITLE=\'마지막 페이지로..\'>");
			sb.append("<IMG SRC=\'" + img[5] + "\' BORDER=0 ALIGN=\'ABSMIDDLE\' >");
			sb.append("</A>\n");
		}
		else
		{

			sb.append("<IMG SRC=\'" + img[5] + "\' BORDER=0 ALIGN=\'ABSMIDDLE\' >\n");
		}

		return sb.toString();
	}

	public static void main(String arg[])
	{
		//PageUtil pu = new PageUtil(10 , null , "aaa" , 1);
		//for( int idx = getStartRow() ; idx < getEndRow() ; idx++ )
	}

}
