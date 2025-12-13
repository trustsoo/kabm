package com.kabm.util;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.UUID;

public class IDGenerator
{
	public String generate()
	{
		int s_IDCacheSize = Integer.getInteger("IDCacheSize", 200).intValue();
		LinkedList s_llIDs = new LinkedList();
	    long[] lIDs = null;
    	
	    if (s_llIDs.size() == 0) {
	        try {
	    		lIDs = getID(s_IDCacheSize);
	    	} catch (RemoteException re) {
	    		throw new RuntimeException(re.getMessage());
	    	}
	    	
	    	for (int i = 0; i < s_IDCacheSize; i++) {
	    		s_llIDs.add(new Long(lIDs[i]));
	    	}
	    }
	    return (s_llIDs.removeFirst()).toString();
	}
   
	public String generateUuid()
	{
		return UUID.randomUUID().toString();//.replace("-", "");
	}
	
	public long[] getID(int nSize) throws RemoteException  {
		 long[] arrID = new long[nSize];
		 int m_nSeq = 0;
		 long m_oldTime = System.currentTimeMillis();
	
		 if (nSize <= 0) {
			 throw new IllegalArgumentException("IDGenerator's generate() : nSize is " + nSize);
		 }
		   
		 for (int i = 0; i < nSize; i++) {
			 if (m_nSeq == 99999) {
				 try {
			        // 30 is rough value(rule of thumb).
			        // Less than 30 may cause the problem.
			        // Do not believe the Thread.sleep()
			        Thread.sleep(30);
				 } catch (InterruptedException e) {
				 }
			 }
			 
			 long lNewTime = System.currentTimeMillis();
			
			 if (lNewTime == m_oldTime) {
				 ++m_nSeq;
			 } else {
				 m_nSeq = 0;
			 }
			 m_oldTime = lNewTime;
			 
			 arrID[i] = (m_nSeq % 100000L) + lNewTime * 100000L + (int)(Math.random()*100000000);
		 }
		 return arrID;
	}
			
}