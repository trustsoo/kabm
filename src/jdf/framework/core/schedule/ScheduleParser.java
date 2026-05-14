package jdf.framework.core.schedule;

import jdf.framework.core.util.DateTime;
import jdf.framework.core.util.SmartStringArray;

import java.util.Calendar;
import java.util.Date;

/**
 * 스케줄 주기를 구하기 위한 클래스 아래 구문형식으로 스트링을 받아, 어떤 시점부터
 * 다음 스케줄 주기까지의 long을 얻는다.
 * [Syntax]
 * 
 * date = yyyy/mm/dd
 * time = hh:mm:ss
 *
 * [ date [startDay-endDay] | [every n[y|m|w|d]] | [on [[SU][MO][TU][WE][TH][FR][SA]]] ] | [at [date1,date2,date3] ]
 * [ time [startTime-endTime] | [every n[h|m|s]] | [at [time1,time2,time3] ]
 * 
 * 주의사항: on 은 순서대로 배열, at은 크기대로 배열(소->대)
 * Example:
 * 2001/10/18 ~ 2001/12/20 까지 매주 월 ~ 금요일, 9:00 ~ 15:00 까지 매 10분마다 동작
 * : date 2001/10/18-2001/12/20 on MOTUWETHFRSA time 09:00:00-15:00:00 every 10m
 *
 * 2001/10/18 ~ 2001/12/20 까지 매일, 9:00 ~ 22:00 까지 10:00, 12:00, 14:00 에 동작
 * : date 2001/10/18-2001/12/20 every 1d time 09:00:00-15:00:00 at 10:00:00,12:00:00,14:00:00
 *
 * 2001/10/18 ~ 2001/12/20 까지 매주 월,수,금,토요일, 9:00 ~ 15:00 까지 매 1시간30분마다 동작
 * : date 2001/10/18-2001/12/20 on MOWEFRSA time 09:00:00-15:00:00 every 90m
 *
 * 2001/10/18 ~ 2001/12/20 까지 2001/11/05, 2001/12/19, 9:00 ~ 15:00 까지 매 2시마다 동작
 * : date 2001/10/18-2001/12/20 at 2001/11/05,2001/12/19 time 09:00:00-15:00:00 every 2h
 *
 * Usage:
 * 2001/11/27 ~ 2001/12/20 까지 토요일마다 1:00 ~ 23:00 까지 매 두시간마다 동작
 * ScheduleParser parser = new ScheduleParser("date 2001/11/27-2001/12/20 on SA time 01:00:00-23:00:00 every 2h");
 * parser.parser();
 *
 * @author
 * @version 1.1, 21/12/05
 * @see     Scheduler
 * @see     SchedulerTask
 */
public class ScheduleParser
{
	private boolean isDate = false;

    public Date startDateTime;
    public Date endDateTime;


	private Date startDate;
	private Date endDate;

	private int everyDate = -1;
	private int everyDateType = -1;

	private boolean week[];

    private Date atDate[];

	private boolean isTime = false;

	private Date startTime;
	private Date endTime;

	private int everyTime = -1;
	private int everyTimeType = -1;

	private Date atTime[];

	private String weekMap[] = {"SU", "MO", "TU","WE", "TH", "FR", "SA"};
	private String weekMapHangul[] = {"일","월", "화", "수","목", "금", "토"};


	private final static int EVERY_SECOND = 0;
	private final static int EVERY_MINUTE = 1;
	private final static int EVERY_HOUR = 2;
	private final static int EVERY_DAY = 3;
	private final static int EVERY_WEEK = 4;
	private final static int EVERY_MONTH = 5;
	private final static int EVERY_YEAR = 6;

	public String inputSyntax;

	public ScheduleParser(String script)
	{
		schedule(script);
	}
    
    /**
	 * (밀리초) 기준으로 생성한다.
     * 주의 : 밀리초 기준이지만, 초단위보다 작게 즉, 1000보다 작은 값은 에러를 유발한다.
     * @param startTime 시작시간
     * @param period (단위:밀리초)
     */
	public ScheduleParser(Date startTime, long period)
	{
		String stime = DateTime.getString(startTime, "HH:mm:ss");
		
		String tempperiod = (period / 1000) +"";
		
		String script = "date on SUMOTUWETHFRSA time "+ stime +"-23:59:59 every " + tempperiod + "s";
	    
	    //System.out.println( script );
	    
	    schedule(script);
	}
    
    /**
	 * (밀리초) 기준으로 생성한다.
     * 주의 : 밀리초 기준이지만, 초단위보다 작게 즉, 1000보다 작은 값은 에러를 유발한다.
     * @param period (단위:밀리초)
     */
    public ScheduleParser(long period)
	{
		String tempperiod = (period /1000) +"";
		
		String script = "date on SUMOTUWETHFRSA time 00:00:00-23:59:59 every " + tempperiod + "s";
	    
	    //System.out.println( script );
	    
	    schedule(script);
	}	
	
	private void schedule(String script)
	{
		inputSyntax = script;
	}

	/**
	 * 현재시간을 기준으로 다음 주기까지 걸리는 시간을 long값으로 반환한다.
     * @return 다음주기시간 - 현재시간
	 */
	public long getNextWaitTimeFromNow()
	{
		Date nowDate = new Date();
		long nexttime = getNextTime(nowDate);

	    //Logger.debug.println( "Next Action Date: " + DateTime.getString( new Date(nowDate.getTime() + nexttime), "yyyy년 M월 d일 E요일 HH:mm:ss") );
		
		return nexttime;
	}

	/**
	 * 스크립트에 의해 최초로 실행되는 Date객체를 반환한다.
	 * 
	 * @uml.property name="startDateTime"
	 */
	public Date getStartDateTime() {
		Calendar startDateCal = Calendar.getInstance();
		startDateCal.setTime(startDate);

		Calendar resultDateCal = startDateCal;

		if (isDate) {

			//at 설정
			if (atDate != null) {
				resultDateCal.setTime(atDate[0]);
			}

			//on 설정
			else if (week != null) {
				//int index = startDateCal.get(startDateCal.DAY_OF_WEEK) - 1; //금일 해당요일
				int index = startDateCal.get(Calendar.DAY_OF_WEEK) - 1; //금일 해당요일
				for (int i = index; i < index + 7; i++) {
					if (week[i % 7]) {
						break;
					} else
						resultDateCal.add(Calendar.DATE, 1);
				}
			}

			//every 설정
			else if (everyDate != -1 && everyDateType != -1) {
				if (everyDateType == EVERY_DAY)
					resultDateCal.add(Calendar.DATE, everyDate);
				else if (everyDateType == EVERY_WEEK)
					resultDateCal.add(Calendar.DAY_OF_WEEK, everyDate);
				else if (everyDateType == EVERY_MONTH)
					resultDateCal.add(Calendar.MONTH, everyDate);
				else if (everyDateType == EVERY_YEAR)
					resultDateCal.add(Calendar.YEAR, everyDate);
			}
		}

		if (isTime) {
			//at 설정
			if (atTime != null) {
				Calendar startTimeCal = Calendar.getInstance();
				startTimeCal.setTime(atTime[0]);

				//startTimeCal.set(resultDateCal.get(resultDateCal.YEAR), resultDateCal.get(resultDateCal.MONTH), resultDateCal.get(resultDateCal.DATE));	//날짜를 맞춤
				startTimeCal.set(
					resultDateCal.get(Calendar.YEAR),
					resultDateCal.get(Calendar.MONTH),
					resultDateCal.get(Calendar.DATE)); //날짜를 맞춤
				resultDateCal = startTimeCal;
			}

			//every 설정
			else if (everyTime != -1 && everyTimeType != -1) {
				Calendar startTimeCal = Calendar.getInstance();
				startTimeCal.setTime(startTime);

				if (everyTimeType == EVERY_SECOND)
					startTimeCal.add(Calendar.SECOND, everyTime);
				else if (everyTimeType == EVERY_MINUTE)
					startTimeCal.add(Calendar.MINUTE, everyTime);
				else if (everyTimeType == EVERY_HOUR)
					startTimeCal.add(Calendar.HOUR_OF_DAY, everyTime);

				//startTimeCal.set(resultDateCal.get(resultDateCal.YEAR), resultDateCal.get(resultDateCal.MONTH), resultDateCal.get(resultDateCal.DATE));	//날짜를 맞춤
				startTimeCal.set(
					resultDateCal.get(Calendar.YEAR),
					resultDateCal.get(Calendar.MONTH),
					resultDateCal.get(Calendar.DATE)); //날짜를 맞춤
				resultDateCal = startTimeCal;
			}
		}

		return resultDateCal.getTime();

	}

	/**
	 * 스크립트에 의해 마지막으로 실행되는 Date객체를 반환한다.
	 * 
	 * @uml.property name="endDateTime"
	 */
	public Date getEndDateTime() {
		Calendar endDateCal = Calendar.getInstance();
		endDateCal.setTime(endDate);

		Calendar startDateCal = Calendar.getInstance();
		startDateCal.setTime(startDate);

		Calendar resultDateCal = endDateCal;

		if (isDate) {
			//at 설정
			if (atDate != null) {
				resultDateCal.setTime(atDate[atDate.length - 1]);
			}

			//on 설정
			else if (week != null) {
				//int index = endDateCal.get(endDateCal.DAY_OF_WEEK) - 1; //금일 해당요일
				int index = endDateCal.get(Calendar.DAY_OF_WEEK) - 1; //금일 해당요일
				for (int i = index + 7; i > index; i--) {
					if (week[i % 7])
						break;
					else
						resultDateCal.roll(Calendar.DATE, -1);
				}
			}

			//every 설정
			else if (everyDate != -1 && everyDateType != -1) {
				Calendar tempCal = Calendar.getInstance();

				while (true) {
					tempCal = (Calendar) startDateCal.clone();

					if (everyDateType == EVERY_DAY)
						startDateCal.add(Calendar.DATE, everyDate);
					else if (everyDateType == EVERY_WEEK)
						startDateCal.add(Calendar.DAY_OF_WEEK, everyDate);
					else if (everyDateType == EVERY_MONTH)
						startDateCal.add(Calendar.MONTH, everyDate);
					else if (everyDateType == EVERY_YEAR)
						startDateCal.add(Calendar.YEAR, everyDate);

					if (startDateCal.after(endDateCal)) {
						resultDateCal = tempCal;
						break;
					}
				}

			}
		}

		if (isTime) {
			//at 설정
			if (atTime != null) {
				Calendar endTimeCal = Calendar.getInstance();
				endTimeCal.setTime(atTime[atTime.length - 1]);

				//endTimeCal.set(resultDateCal.get(resultDateCal.YEAR), resultDateCal.get(resultDateCal.MONTH), resultDateCal.get(resultDateCal.DATE));	//날짜를 맞춤
				endTimeCal.set(resultDateCal.get(Calendar.YEAR), resultDateCal
					.get(Calendar.MONTH), resultDateCal.get(Calendar.DATE)); //날짜를 맞춤
				resultDateCal = endTimeCal;
			}

			//every 설정
			else if (everyTime != -1 && everyTimeType != -1) {
				Calendar startTimeCal = Calendar.getInstance();
				startTimeCal.setTime(startTime);

				Calendar endTimeCal = Calendar.getInstance();
				endTimeCal.setTime(endTime);

				Calendar tempCal = Calendar.getInstance();

				int day = startTimeCal.get(Calendar.DATE); //날짜의 필드가 시간을 너무 더해서 다음날로 넘었가는지 여부를 판별하기 위하여 저장

				while (true) {
					tempCal = (Calendar) startTimeCal.clone();
					if (everyTimeType == EVERY_SECOND)
						startTimeCal.add(Calendar.SECOND, everyTime);
					else if (everyTimeType == EVERY_MINUTE)
						startTimeCal.add(Calendar.MINUTE, everyTime);
					else if (everyTimeType == EVERY_HOUR)
						startTimeCal.add(Calendar.HOUR_OF_DAY, everyTime);

					if (startTimeCal.after(endTimeCal)
						|| day != startTimeCal.get(Calendar.DATE)) //시간이 더해서 다음날로 넘어갔을때도 해당
					{
						//tempCal.set(resultDateCal.get(resultDateCal.YEAR), resultDateCal.get(resultDateCal.MONTH), resultDateCal.get(resultDateCal.DATE));	//날짜를 맞춤
						tempCal.set(
							resultDateCal.get(Calendar.YEAR),
							resultDateCal.get(Calendar.MONTH),
							resultDateCal.get(Calendar.DATE)); //날짜를 맞춤
						resultDateCal = tempCal;
						break;
					}
				}

			}
		}

		return resultDateCal.getTime();

	}

    

    /**
	 * 들어온 시간(now)을 기준으로 다음 주기까지 걸리는 시간을 long값으로 반환한다.
     * @param now 특정 시간
     * @return 다음주기시간 - 들어온 특정 시간
	 */
    private long getNextTime(Date now)
    {
        try
        {
        	Calendar nowCal = Calendar.getInstance();
            nowCal.setTime(now);

            //미리 시작날짜시간과 끝날짜시간을 구해둔다.(파싱할때 구함)
            Calendar startDateTimeCal = Calendar.getInstance();
            startDateTimeCal.setTime(startDateTime);
            Calendar endDateTimeCal = Calendar.getInstance();
            endDateTimeCal.setTime(endDateTime);

            if (nowCal.before(startDateTimeCal)) //기준일이 시작날짜시간 이전인가여부
            {
                return startDateTimeCal.getTime().getTime() - nowCal.getTime().getTime();
            }

            else if ( endDateTimeCal.before(nowCal) ) //끝날짜시간이 기준일 이전인가여부
            {
                return -1;
            }

            //실질적으로 영역에 들어올때
            else
            {
                Date tempDate = getDate(nowCal);
                Calendar tempDateCal = Calendar.getInstance();
                tempDateCal.setTime(tempDate);
                
                if (tempDateCal.after(nowCal) ) //tempDate가 기준일과 날짜만 비교하였을때 크다면, 최초 시간부만 구해서 더한다.
                {
                    //시간필드를 시작날짜시간과 매칭
                    tempDateCal.set( Calendar.HOUR_OF_DAY, startDateTimeCal.get(Calendar.HOUR_OF_DAY) );
		            tempDateCal.set( Calendar.MINUTE, startDateTimeCal.get(Calendar.MINUTE) );
		            tempDateCal.set( Calendar.SECOND, startDateTimeCal.get(Calendar.SECOND) );
		            tempDateCal.set( Calendar.MILLISECOND, startDateTimeCal.get(Calendar.MILLISECOND) );

                    return tempDateCal.getTime().getTime() - nowCal.getTime().getTime();
                }
                else
                {
                    Date tempTime = getTime(nowCal);

                    if (tempTime == null)   //시간이 지나버렸다면
                    {
                        Calendar tempNowCal = (Calendar)nowCal.clone();
                        tempNowCal.add(Calendar.DATE, 1);

                        //날짜 +1을 했지만, 이것 날짜 크기가 끝날짜시간보다 클수 있다. 크다면, -1을 반환한다.
                        if ( endDateTimeCal.equals(tempNowCal) )
                        {
                            return -1;
                        }

                        tempDate = getDate(tempNowCal);

                        tempDateCal.setTime(tempDate);
                        //시간필드를 시작날짜시간의 시간부와 매칭
                        tempDateCal.set( Calendar.HOUR_OF_DAY, startDateTimeCal.get(Calendar.HOUR_OF_DAY) );
                        tempDateCal.set( Calendar.MINUTE, startDateTimeCal.get(Calendar.MINUTE) );
                        tempDateCal.set( Calendar.SECOND, startDateTimeCal.get(Calendar.SECOND) );
                        tempDateCal.set( Calendar.MILLISECOND, startDateTimeCal.get(Calendar.MILLISECOND) );
                        
                        return tempDateCal.getTime().getTime() - nowCal.getTime().getTime();
                    }
                    
                    Calendar tempTimeCal = Calendar.getInstance();
                    tempTimeCal.setTime(tempTime);

                    //시간필드를 시작날짜시간의 시간부와 매칭
                    tempDateCal.set( Calendar.HOUR_OF_DAY, tempTimeCal.get(Calendar.HOUR_OF_DAY) );
                    tempDateCal.set( Calendar.MINUTE, tempTimeCal.get(Calendar.MINUTE) );
                    tempDateCal.set( Calendar.SECOND, tempTimeCal.get(Calendar.SECOND) );
                    tempDateCal.set( Calendar.MILLISECOND, tempTimeCal.get(Calendar.MILLISECOND) );

                    return tempDateCal.getTime().getTime() - nowCal.getTime().getTime();
                }
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }
    
    private Date getDate(Calendar nowCal) throws Exception
    {
        Calendar temp = Calendar.getInstance();
        
        Calendar startDateTimeCal = Calendar.getInstance();
        startDateTimeCal.setTime(startDateTime);
        
        //at 설정
        if (atDate != null )
        {
            for (int i = 0; i < atDate.length ; i++ )
            {
                temp.setTime(atDate[i]);
                //시간필드 0으로 세팅
                temp.set( Calendar.HOUR_OF_DAY, 0 );
                temp.set( Calendar.MINUTE, 0 );
                temp.set( Calendar.SECOND, 0 );
                temp.set( Calendar.MILLISECOND, 0 );
                
                if ( !temp.before(nowCal) )
                {
                    break;
                }
            }
        }
        
        //on 설정
        else if (week != null)
        {
            temp = (Calendar)nowCal.clone();
            //시간필드 0으로 세팅
            temp.set( Calendar.HOUR_OF_DAY, 0 );
            temp.set( Calendar.MINUTE, 0 );
            temp.set( Calendar.SECOND, 0 );
            temp.set( Calendar.MILLISECOND, 0 );

            //int index = nowCal.get(nowCal.DAY_OF_WEEK) - 1; //오늘의 요일
            int index = nowCal.get(Calendar.DAY_OF_WEEK) - 1; //오늘의 요일
            int count = 0;
            for (int i = index; i < index + 7 ; i++ )
            {
                if ( week[i%7] )
                {
                    temp.add(Calendar.DATE, count);
                    break;
                }
                count++;
            }
        }

        
        //every 설정
        else if (everyDate != -1 && everyDateType != -1)
        {
            temp = (Calendar)startDateTimeCal.clone();
            //시간필드 0으로 세팅
            temp.set( Calendar.HOUR_OF_DAY, 0 );
            temp.set( Calendar.MINUTE, 0 );
            temp.set( Calendar.SECOND, 0 );
            temp.set( Calendar.MILLISECOND, 0 );

            while (true)
            {
                if ( temp.before(nowCal) )
                {
                    if (everyDateType == EVERY_DAY)
                        temp.add(Calendar.DATE, everyDate);
                    else if (everyDateType == EVERY_WEEK)
                        temp.add(Calendar.DAY_OF_WEEK, everyDate);
                    else if (everyDateType == EVERY_MONTH)
                        temp.add(Calendar.MONTH, everyDate);
                    else if (everyDateType == EVERY_YEAR)
                        temp.add(Calendar.YEAR, everyDate);
                }
            }

        }

        return temp.getTime();
    }



    private Date getTime(Calendar nowCal) throws Exception 
    {
        Date result = null;
        Calendar temp = Calendar.getInstance();
        
        /*
            끝날짜시간의 시간부가 기준시간보다 전이라면, 다음날이므로 날짜부를 다시 구하기 위하여 null 반환
        */
        Calendar endDateTimeCal = Calendar.getInstance();
        endDateTimeCal.setTime(endDateTime);
        
        temp = (Calendar)endDateTimeCal.clone();
        //날짜필드 매칭
        temp.set( Calendar.YEAR, nowCal.get(Calendar.YEAR) );
        temp.set( Calendar.MONTH, nowCal.get(Calendar.MONTH) );
        temp.set( Calendar.DATE, nowCal.get(Calendar.DATE) );
        if ( temp.before(nowCal) )
        {
            return null;
        }


        /*
            시간영역에 안에 들어오는 여부를 판별하여, 계산
        */
        Calendar startDateTimeCal = Calendar.getInstance();
        startDateTimeCal.setTime(startDateTime);

        temp = (Calendar)startDateTimeCal.clone();
        //날짜필드 매칭
        temp.set( Calendar.YEAR, nowCal.get(Calendar.YEAR) );
        temp.set( Calendar.MONTH, nowCal.get(Calendar.MONTH) );
        temp.set( Calendar.DATE, nowCal.get(Calendar.DATE) );
        
        if ( temp.before(nowCal) )	//시작시간이 현재시간 이전이라면
        {
            //at 설정
            if (atTime != null)
            {
                for (int i = 0; i < atTime.length ; i++ )
                {
                    Calendar timeCal = Calendar.getInstance();
                    timeCal.setTime(atTime[i]);
                    //날짜필드 매칭
                    timeCal.set( Calendar.YEAR, nowCal.get(Calendar.YEAR) );
                    timeCal.set( Calendar.MONTH, nowCal.get(Calendar.MONTH) );
                    timeCal.set( Calendar.DATE, nowCal.get(Calendar.DATE) );
                    
                    if ( nowCal.before(timeCal) )
                    {
                        result = atTime[i];
                        break;
                    }
                }
            }

            //every 설정
            else if (everyTime != -1 && everyTimeType != -1)
            {
                while (true)
                {
                    if (everyTimeType == EVERY_SECOND)
                        temp.add(Calendar.SECOND, everyTime);
                    else if (everyTimeType == EVERY_MINUTE)
                        temp.add(Calendar.MINUTE, everyTime);
                    else if (everyTimeType == EVERY_HOUR)
                        temp.add(Calendar.HOUR_OF_DAY, everyTime);
                    
                    if ( nowCal.before(temp) )
                    {
                        result = temp.getTime();
                        break;
                    }

                }
            }
        
        }

        //현재시간이 시작시간 이전
        else
        {
            result = temp.getTime();
        }

        return result;
    }


    /**
	 * 생성자에게 넘겨받은 시간설정 스트링을 파싱한다.
	 */
	public void parser() throws ScheduleParseException
	{
		try
		{
			String entity[] = SmartStringArray.split(" ", inputSyntax.toLowerCase() );
			
			int date_index = -1;
			int time_index = -1;

			for (int i = 0 ;i < entity.length ; i++)
			{
				if ( "date".equals(entity[i]) )
				{
					date_index = i;
					isDate = true;
				}
				else if ( "time".equals(entity[i]) )
				{
					time_index = i;
					isTime = true;
				}
			}

			if (isDate && !isTime)
			{
				throw new Exception("Time is not exist in Syntax!");
			}

			//date
			if (date_index+1 != -1)
			{
				int index = (time_index != -1) ? time_index : entity.length;
				boolean isEvery = false;
                boolean isAt = false;
                boolean isOn = false;
				
				for (int i = date_index; i < index ; i++ )
				{
					if ( "every".equals(entity[i]) )
					{
						everyDate = Integer.parseInt( entity[i+1].substring(0, entity[i+1].length()-1) );
						
						String temp = entity[i+1].substring(entity[i+1].length()-1);
						
						if (temp.equals("y"))
							everyDateType = EVERY_YEAR;
						else if (temp.equals("m"))
							everyDateType = EVERY_MONTH;
						else if (temp.equals("w"))
							everyDateType = EVERY_WEEK;
						else if (temp.equals("d"))
							everyDateType = EVERY_DAY;

						isEvery = true;
					}

					else if (entity[i].indexOf("-") != -1 )
					{
						String temp[] = SmartStringArray.split("-", entity[i] );
						
						startDate = DateTime.getDate( temp[0], "yyyy/MM/dd");
						endDate = DateTime.getDate( temp[1], "yyyy/MM/dd");
					}

					//Every가 존재하면, on은 동작하지 않음
					else if (!isEvery && !isAt && entity[i].equals("on") )
					{
						week = new boolean[7];

						for (int j = 0; j < week.length ;j++ )
						{
							if ( entity[i+1].indexOf( weekMap[j].toLowerCase() ) != -1 )
                            {
								week[j] = true;
                                isOn = true;
                            }
						}
					}

                    else if ( !isEvery && !isOn && entity[i].equals("at") )
                    {
                        String temp[] = SmartStringArray.split(",", entity[i+1] );

                        atDate = new Date[temp.length];

						for (int j = 0; j < temp.length ;j++ )
						{
							atDate[j] = DateTime.getDate( temp[j], "yyyy/MM/dd");

							if (startDate != null && endDate != null)
							{
    							if ( atDate[j].getTime() > endDate.getTime() )
    							{
    								throw new Exception(" atDate is bigger than endDate.");
    							}
    							if ( atDate[j].getTime() < startDate.getTime() )
    							{
    								throw new Exception(" atDate is smaller than startDate.");
    							}
    						}
						}
                    }
				}

				if (startDate == null || endDate ==  null)
				{
					startDate = DateTime.getDate( "2001/01/01", "yyyy/MM/dd");
					endDate = DateTime.getDate( "2030/12/31", "yyyy/MM/dd");
				}
			}
			
			//time
			if ( time_index+1 != -1 )
			{
				boolean isEvery = false;

				for (int i = time_index +1; i < entity.length ; i++ )
				{
					if ( "every".equals(entity[i]) )
					{
						everyTime = Integer.parseInt( entity[i+1].substring(0, entity[i+1].length()-1) );
						
						String temp = entity[i+1].substring(entity[i+1].length()-1);
						
						if (temp.equals("h"))
							everyTimeType = EVERY_HOUR;
						else if (temp.equals("m"))
							everyTimeType = EVERY_MINUTE;
						else if (temp.equals("s"))
							everyTimeType = EVERY_SECOND;
						
						isEvery = true;
					}

					else if (entity[i].indexOf("-") != -1 )
					{
						String temp[] = SmartStringArray.split("-", entity[i] );
						
						startTime = DateTime.getDate( temp[0], "HH:mm:ss");
						endTime = DateTime.getDate( temp[1], "HH:mm:ss");
					}

					//Every가 존재하면, at은 동작하지 않음
					else if (!isEvery && entity[i].equals("at") )
					{
						String temp[] = SmartStringArray.split(",", entity[i+1] );
						
						atTime = new Date[temp.length];

						for (int j = 0; j < temp.length ;j++ )
						{
							atTime[j] = DateTime.getDate( temp[j], "HH:mm:ss");

							if (startTime != null && endTime != null)
							{
    							if ( atTime[j].getTime() > endTime.getTime() )
    							{
    								throw new Exception(" atTime is bigger than endTime.");
    							}
    							if ( atTime[j].getTime() < startTime.getTime() )
    							{
    								throw new Exception(" atTime is smaller than startTime.");
    							}
    						}
						}
					}
				}

				if (startTime == null || endTime ==  null)
				{
					startTime = DateTime.getDate( "00:00:00", "HH:mm:ss");
					endTime = DateTime.getDate( "23:59:59", "HH:mm:ss");
				}
				
				
			}

            startDateTime = getStartDateTime();
            endDateTime = getEndDateTime();
		
		}
		catch (Exception e)
		{
			throw new ScheduleParseException("Not Supported Syntax. [" +e.toString() + "]");
		}
	}

	public class ScheduleParseException extends Exception
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -2244139878105337168L;

		public ScheduleParseException(String str)
		{
			super(str);
		}
	}

    
    
    
    public String toString()
	{
		StringBuffer buf = new StringBuffer();

		if (startDate != null) buf.append("starting Date: " + startDate + "\n");
		if (endDate != null) buf.append("ending Date: " + endDate + "\n");
		
		buf.append("everyDate: " + everyDate + "\n");
		buf.append("everyDateType: " + everyDateType + "\n");

        if (atDate != null)
		{
			for (int i = 0; i < atDate.length;i++ )
			{
				if (atDate[i] != null) buf.append("at atDate[" + i + "]: " + atDate[i] + "\n");
			}
		}

		if (week != null)
		{
			for (int i = 0; i < week.length ;i++ )
			{
				buf.append(weekMapHangul[i] + ": " + week[i] + "\n");
			}
		}

		if (startTime != null) buf.append("starting Time: " + startTime + "\n");
		if (endTime != null) buf.append("ending Time: " + endTime + "\n");
		
		buf.append("everyTime: " + everyTime + "\n");
		buf.append("everyTimeType: " + everyTimeType + "\n");

		if (atTime != null)
		{
			for (int i = 0; i < atTime.length;i++ )
			{
				if (atTime[i] != null) buf.append("at atTime[" + i + "]: " + atTime[i] + "\n");
			}
		}
		
		return buf.toString();
	}

    public void println(Date date)
    {
        System.out.println( DateTime.getString( date, "yyyy년 M월 d일 E요일 HH:mm:ss") );
    }

	public static void main(String[] args) throws Exception
	{
		//ScheduleParser parser = new ScheduleParser("date 2001/10/18-2002/02/19 on SUMOTUWETHFRSA time 01:00:00-09:00:00 at 02:00:00,04:00:00");
		//ScheduleParser parser = new ScheduleParser("date 2001/10/18-2002/03/19 on TUTHFRSA time 01:00:00-23:00:00 every 2m");
		//ScheduleParser parser = new ScheduleParser("date on MOTUWETHFR time 01:00:00-23:00:00 every 2m");
		//ScheduleParser parser = new ScheduleParser("date on MOTUWETHFR time at 06:00:00");
		//ScheduleParser parser = new ScheduleParser("date on SUMOTUWETHFR time every 2h");
        //ScheduleParser parser = new ScheduleParser("date 2001/10/18-2002/02/20 at 2001/10/20,2001/11/20,2002/02/20 time 01:00:00-09:00:00 at 02:00:00,04:00:00");
        ScheduleParser parser = new ScheduleParser(3*60*60*1000);
		parser.parser();
        System.out.println(parser);
        System.out.println("====================================================================");
        System.out.println(DateTime.getString( parser.startDateTime, "yyyy년 M월 d일 E요일 HH:mm:ss") );
        System.out.println(DateTime.getString( parser.endDateTime, "yyyy년 M월 d일 E요일 HH:mm:ss") );
		System.out.println("====================================================================");
		
		long times = System.currentTimeMillis();

		long nexttime = parser.getNextWaitTimeFromNow();
		System.out.println("====================================================================");
		System.out.println("Elapsed time : " + (System.currentTimeMillis() - times));

		System.out.println( "Next Date: " + DateTime.getString( new Date(new Date().getTime() + nexttime), "yyyy년 M월 d일 E요일 HH:mm:ss") );
		System.out.println( "Wait Time(long value): " + nexttime);
	}
}