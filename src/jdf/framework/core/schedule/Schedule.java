package jdf.framework.core.schedule;

import java.util.Date;

/** * XML 정보(management.xml의 scheduler 엘리먼트에서 * 한 당위의 배치작업 정보가 들어가는 Class */
public class Schedule
{
    // 배치작업명
    private String name;
    
    private Date startTime=null;
    
    // 배치작업 주기
    private long period;

    // 배치작업 class
    private Class process;

    // 배치 스크립트
    private String script;

	/**
	 * 
	 * @uml.property name="task"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	// 스케줄 Task
	private SchedulerTask task;

    
    /**
     * 배치 스크립트가 있는 배치작업
     *
     */
    public Schedule(String name, String script, Class process)
    {
        this.name=name;
        this.script=script;
        this.process=process;
    }
    
    /**
     * 시작시간이 없는 배치작업
     *
     */
    public Schedule(String name, long period, Class process)
    {
        this.name=name;
        this.period=period;
        this.process=process;
    }
    
    
    /**
     * 시작시간이 있는 배치작업
     *
     */
    public Schedule(String name, Date startTime, long period, Class process)
    {
        
        this.name=name;
        this.startTime=startTime;
        this.period=period;
        this.process=process;
    }

	/**
	 * 
	 * @uml.property name="task"
	 */
	public void setTask(SchedulerTask task) {
		this.task = task;
	}

	/**
	 * 
	 * @uml.property name="task"
	 */
	public SchedulerTask getTask() {
		return task;
	}

	/**
	 * 
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @uml.property name="period"
	 */
	public long getPeriod() {
		return period;
	}

	/**
	 * 
	 * @uml.property name="process"
	 */
	public Class getProcess() {
		return process;
	}

	/**
	 * 
	 * @uml.property name="startTime"
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * 
	 * @uml.property name="script"
	 */
	public String getScript() {
		return script;
	}

}
    