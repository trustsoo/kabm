/*
 * Created on 2004-07-09
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jdf.framework.view.auth;

/**
 * @author plugger
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface Permission
{
	
	/**
	 * 
	 * 패스워드가  틀린경우 (횟수로 제한)
	 * -30 + - 횟수 
	 * 
	 */
	public final static int UNMATCHED_PSSWD_CNT = -30;
	
	 /**
     * 패스워드 최대횟수를 넘은경우
     * 
     */
	public final static int LOCKED_PSSWD = -99;
	
	/*
	 * 패스워드가 초기화 패스워드인경우(패스워드와 아이디가 동일)
	 * */
	public final static int INITIAL_PSSWD = -100;

	
	/**
	 * 권한이 없는경우 
	 * 
	 */
	public final static int NO_PERMISSION = -1;	
	
	
	/**
	 * 아이디가  틀린경우
	 * 
	 */
	public final static int UNREGISTED = -2;
	
	
	
	/**
	 * 패스워드가  틀린경우
	 * 
	 */
	public final static int UNMATCHED_PSSWD = -3;
	
	/*
	 * 민감정보 보호
	 * */
	public final static int NO_SECURITYINFO = -4;
	
	
	/*
	 * 패스워드 변경일 만료
	 * */
	public final static int PSSWD_UPD_DT_EXPIRER = -5;
	
	
	
	
		

}