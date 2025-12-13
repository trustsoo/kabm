<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="jdf.framework.core.log.Logger"%>
<%@ page import="jdf.framework.view.auth.User"%>
<%@ page import="jdf.framework.core.http.SessionAttributes"%>
<%@ page import="jdf.framework.core.Config"%>
<%@ page import="jdf.framework.core.Configuration"%>
<%@ page import="jdf.framework.core.util.*"%>
<%@ page import="jdf.framework.core.data.*"%>
<%@ page import="java.util.*"%>

<%!	
	public String getEventsCalendar(javax.servlet.http.HttpServletRequest req)
	{
		Calendar cal=Calendar.getInstance(); //현재 시스템이 가지고 있는 날짜 데이터 가지고 오기
 	
 		//조회 하는 년/월이 오늘과 같은지 비교키 위하여 임시 기억...
 		int nowY=cal.get(Calendar.YEAR);
	 	int nowM=cal.get(Calendar.MONTH)+1;


	 	//STR : --조회 기준일 설정 
	 	int y=cal.get(Calendar.YEAR);
	 	int m=cal.get(Calendar.MONTH)+1;
	 	int d=cal.get(Calendar.DATE);
	 	
	 	String _y=req.getParameter("y");
 		String _m=req.getParameter("m");
 	
	 	if(_y!=null)  y=Integer.parseInt(_y);
	 	if(_m!=null)  m=Integer.parseInt(_m);
	 	//END : --조회 기준일 설정 

	 	
	 	//y년 m월 1일의 요일
	 	cal.set(y,m-1,1);
	 	y=cal.get(Calendar.YEAR);
	 	m=cal.get(Calendar.MONTH)+1;
	 	
	 	int w=cal.get(Calendar.DAY_OF_WEEK); //1(일)~7(토) => 일요일일때 w에 1. 메소드를 외우면 된다.
	 	int curM=cal.get(Calendar.MONTH)+1;
	 	int end = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	 	int pYY=cal.get(Calendar.YEAR)-1;
		int nYY=cal.get(Calendar.YEAR)+1;
	 	
	 	//---> 이전 달 
	 	cal.add(cal.MONTH, -1);  
	 	int pY=cal.get(Calendar.YEAR);
	 	int pM=cal.get(Calendar.MONTH)+1;

	 	//---> 이후 달 
	 	cal.add(cal.MONTH, +2);  
	 	int nY=cal.get(Calendar.YEAR);
	 	int nM=cal.get(Calendar.MONTH)+1; 

	//STR : 행사내역 조회
	DataSet edctScheduleInput = new DataSet();
	DataSet edctScheduleList = new DataSet();
	Set scSet = new HashSet();

	try
	{
		jdf.framework.core.data.InteractionBean interact = new jdf.framework.core.data.InteractionBean();
		String start_dt = y + ((m < 10)?"0":"")  + m + "01";
		String end_dt = y + ((m < 10)?"0":"")  + m + ((end < 10)?"0":"")+end;
			
  		edctScheduleInput.put("inYyyyMm",y + ((m < 10)?"0":"")  + m);
  		
		edctScheduleInput.put("cmd", "getMonthSchedule");		

		edctScheduleList = interact.execute("/edu/edct_schedule", edctScheduleInput);	

		for (int i=0 ; i < edctScheduleList.getCount("is_date"); i++){
			scSet.add(edctScheduleList.getText("fmt_is_date",i));
		}

	} catch(Exception ex)
	{}


	 StringBuffer sb = new StringBuffer();

	 	 
	 sb.append("<table class='tblCalendar'> ");
		sb.append("<colgroup> ");
		sb.append("    <col style='width:36px;'>");
		sb.append("    <col style='width:36px;'>");
		sb.append("    <col style='width:36px;'>");
		sb.append("    <col style='width:36px;'>");
		sb.append("    <col style='width:36px;'>");
		sb.append("    <col style='width:36px;'>");
		sb.append("    <col style='width:36px;'>");
	    sb.append("</colgroup> ");
		sb.append("<thead> ");
		
		sb.append("	<tr class='option'>");
		sb.append("	    <th><a onclick=\"javascript:getEventsCalendar('" + pYY + "','" + ((curM < 10)?"0":"")  + curM + "')\"><span class='btn'>«</span></a></th>");
		sb.append("	    <th><a onclick=\"javascript:getEventsCalendar('" + pY + "','" + ((pM < 10)?"0":"")  + pM  + "')\"><span class='btn'>‹</span></a></th>");
		sb.append("	    <th colspan='3'><span class='pre'>"+ y +"년 " + m + "월 "+"</span></th>");
		sb.append("	    <th><a onclick=\"javascript:getEventsCalendar('" + nY + "','" + ((nM < 10)?"0":"")  + nM + "')\"><span class='btn'>›</span></a></th>");
		sb.append("	    <th><a onclick=\"javascript:getEventsCalendar('" + nYY + "','" + ((curM < 10)?"0":"")  + curM + "')\"><span class='btn'>»</span></a></th>");
		sb.append("	</tr>");
 
		sb.append("	<tr class='week'> ");
		sb.append("		<th>일</td> ");
		sb.append("		<th>월</td> ");
		sb.append("		<th>화</td> ");
		sb.append("		<th>수</td> ");
		sb.append("		<th>목</td> ");
		sb.append("		<th>금</td> ");
		sb.append("		<th>토</td> ");
		sb.append("	</tr> ");
		sb.append("</thead> ");
		sb.append("<tbody> ");
				
		sb.append("<tr>");

		//앞부분 공백처리 :  6월의 1일은 토요일이라 w=7 따라서 공백 6번을 써준다.
		for(int i=1; i<w; i++){ 
			sb.append("<td class='blank'></td>" );
		}
		//1~마지막일 계산
		String calDayClass;
		String fmtDate;
		
		cal.add(cal.MONTH, -1);
		for(int i=1; i<=cal.getActualMaximum(Calendar.DATE); i++){
			calDayClass=w%7==1?"sun":(w%7==0?"sat":"");
	
			if ( y == nowY && m == nowM &&
			d == i) calDayClass += (calDayClass.length()==0)?"on":" on";   //오늘 이면 색깔 처리 
	       
	        //이벤트가 있는날이면 <b><a></a></b>
	        fmtDate = y + ((m < 10)?"0":"")  + m +  ((i < 10)?"0":"")  + i ; 
	        if(scSet.contains(fmtDate)){  // "20170426"
	        	sb.append("<td style='cursor:pointer;' onclick=\"javascript:getEduList('"  + fmtDate + "')\" class='" + calDayClass +" calendar-event-mark'><strong>" + i + "</strong></td>");
	        }else{
				sb.append("<td class='" + calDayClass +"'>" + i + "</td>");
			}
			
			w++;
			if(w%7==1&&i!=cal.getActualMaximum(Calendar.DATE)){  //1주 이동
				sb.append("</tr>");			
				sb.append("<tr>");
			}
		}
		
		//뒷부분 공백처리
		if(w%7!=1){
			if(w%7==0){
				sb.append("<td class='blank'></td>");
			}else{
				for(int i=w%7; i<=7; i++)
				sb.append("<td class='blank'></td>");
			}
			sb.append("</tr>");	
		}
	
		sb.append("</tbody> ");
		sb.append("</table>");
		
	 	return sb.toString();
	}
	
%>

