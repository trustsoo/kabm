/**
 * @(#) FileUtility.java
 * @(#)
 * Copyright 1999-2000 by  LG-EDS Systems, Inc.,
 * Information Technology Group, Application Architecture Team, 
 * Application Infrastructure Part.
 * 236-1, Hyosung-2dong, Kyeyang-gu, Inchun, 407-042, KOREA.
 * All rights reserved.
 * 
 * NOTICE !      You can copy or redistribute this code freely, 
 * but you should not remove the information about the copyright notice 
 * and the author.
 * 
 * @author  Soo-Kyung Lim, sukyunglim@lgeds.lg.co.kr.
 */

package jdf.framework.core.io;

import java.io.File;

 /**
 * <p>
 * 화일에 관련된 Utility Class
 * </p>
 *
 * @author  Soo-Kyung Lim, sukyunglim@lgeds.lg.co.kr.
 * @version 1.0
 */
 
public final class FileUtility {

	/**
	 * Don't let anyone instantiate this class
	 */
	private FileUtility() {}




	/**
	 * 디렉토리 전체를 삭제하는 메쏘드
	 *
	 * @param  java.lang.String 삭제할 directory 절대경로
	 * @return boolean 삭제 성공여부
	 */
	public final static boolean deleteDir(String dirName) throws Exception{
	    File file = new File(dirName);
        return deleteDir(file);
    }

	/**
	 * 디렉토리 전체를 삭제하는 메쏘드
	 *
	 * @param  java.io.File 삭제할 directory의 File객체
	 * @return boolean 삭제 성공여부
	 */
	public final static boolean deleteDir(File dir) throws Exception{
	    boolean result = false;
	    
   	    if ( dir.isDirectory() ) {
   	        File[] fileList = dir.listFiles();
            int count = fileList.length;
            if ( count != 0 ) 
	            for ( int i=0; i<count; i++) 
	                result = deleteDir( fileList[i] );
            result = dir.delete();
	    }
	    else if ( dir.isFile() ) 
	        result = dir.delete();
        return result;
	}

	/**
	 * 디렉토리 명을 바꾸는메쏘드
	 * 최하위 디렉토리 명만을 바꿀 수 있다.
	 *
	 * @param  source String 소스 directory명(상대경로)
	 * @param  target String 대상directory명(상대경로)
	 * @return boolean 변경 성공여부
	 */
	public final static boolean renameDir(String source, String target) throws Exception{

	    
        File sourceDir = new File(source);
        File targetDir = new File(target);
        
        if ( !sourceDir.isDirectory() ) 
   			throw new IllegalArgumentException("Invalid directory : " + sourceDir);
		
        return sourceDir.renameTo(targetDir);
    }
}