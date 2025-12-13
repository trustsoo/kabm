/*
 * @(#)Queue.java
 *
 * NOTICE !
 * You can not copy or redistribute this code.
 *
 *
 * @author
 */
package jdf.framework.core.util;


import java.util.*;


/**
 * <p>
 * java.util.Stack과 는 반대로 FIFO(First In First Out)인 Queue를 구현한 Class
 *
 * @author
 * @version 1.0
 */


public final class Queue extends LinkedList // performance때문에 Vector대신 LinkedList로 
{
   
   
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;



/**
    *  기본 생성자
    */
   public Queue() 
   {
        super();
   }
   
   
   /**
 *  Queue가 비었는지 검사
 *  @return 비었으면 true
 *
   @roseuid 3A39D0020051
   */
    public boolean isEmpty() 
    {
    	return size() == 0;
     
    }
    

    /**
    @roseuid 3A39D0020191
    */
    public synchronized Object dequeue() throws EmptyQueueException 
    {
    	 //Object obj = peek();
         //removeFirst();
         
         if(size() == 0) 
    		throw new EmptyQueueException();
    		
         return remove(0);
         //return obj;
    }
    
    /**
    @roseuid 3A39D00201FF
    */
    public synchronized void enqueue(Object item) 
    {
    	addLast(item);
    	//add(item);
        //return item;
    }
    
    /**
    @roseuid 3A39D0020336
    */
    public synchronized int search(Object o) 
    {
        return indexOf(o);
    }
    
    
        
    /**
    @roseuid 3A39D0020123
    */
    public Object peek() throws EmptyQueueException
    {
    	if(size() == 0) 
    		throw new EmptyQueueException();
    		
    	return get(0);
    }
    


   
}
