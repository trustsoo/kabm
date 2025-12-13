package com.kabm.filter;



/**
 * 정해진 고정의 상수들을 정의한다.
 * ex) session Filed...
 * @author advan94
 *
 */
public interface JDFContext
{
    
	//user_id, user_nm, user_grp_cd, user_grp_nm, dept_id, dept_nm,user_pwd
	
	public final static String COOKIE_KEY = "cid";
    public final static String COOKIE_AUTHKEY = "cauthkey";
    public final static String USER_LANGUAGE = "user_lang";
    
    public final static String MAP_EMPINFO_KEY = ".EMPINFO";
    
    public final static String DUPLICATE_LOGIN_KEY = "DUPLICATE_EXIST";
    
    public final static String LOGIN_DIV_LOGIN = "LOGIN";
    public final static String LOGIN_DIV_LOGOUT = "LOGOUT";
    
}