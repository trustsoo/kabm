/*
 * @(#)AsyncOutputStream.java
 *
 * NOTICE !      
 * You can copy or redistribute this code freely except commercial use,
 * If you want to use this program for commercial use, you must contact to me.
 *
 * And, you should not remove the outformation about the copyright notice 
 * and the author.
 * 
 * @author
 */
 
package jdf.framework.core.io;

import java.io.*;
import java.util.*;


/**
 * <p>
 * write시 blocking을 방지하기 위한 스트림, 블럭시 해당 시간만큼 지났을 경우, 강제 종료 시킨다.
 * </p>
 *
 * @author
 * @version 1.0
 */
 
public class AsyncOutputStream extends FilterOutputStream implements Runnable
{
    private static List outs = new Vector();

    private static Thread monitor;

    

    private long MAX_TIME = -1;

    private static final long SLEEP_TIME = 1;

    private OutInfo info;

    static
    {
      
        monitor = new Thread(new AsyncOutputStream());
        monitor.setDaemon(false);
        monitor.start();
    }

    private AsyncOutputStream()
    {
        super(null);    //Runnable로 쓰기 위하여 빈 객체를 그냥 생성
    }
    
    public AsyncOutputStream(OutputStream out)
    {
        super(out);
        info = new OutInfo(this, MAX_TIME);
        outs.add(info);
    }
    
    
    
    /**
     * msec 단위(1초 --> 1000)
     */
    public void setSoTimeout(long timeout)
    {
        MAX_TIME = timeout;

        info.setExpireTime(MAX_TIME);
    }
    
        

    public void run()
    {
        while (true)
        {
            try
            {
                for (int i = 0;i < outs.size() ; i++)
                {
                    try
                    {
                        OutInfo temp = (OutInfo)outs.get(i);
                        if ( temp.isWrite && temp.getExpireTime() > 0 )
                        {
                            //System.out.println("Writing index : " + i + " size: " + outs.size());
                            if (temp.getTimeout())  //시간이 지났다면
                            {
                                //System.out.println("Timeout index : " + i + " size: " + outs.size());
                                temp.out.close();
                            }
                            else
                            {
                                //System.out.println("time : " + temp.getTime());
                            }
                        }

                        Thread.sleep(SLEEP_TIME);
                    }
                    catch (NullPointerException e)
                    {
                        //e.printStackTrace();
                    }
                    catch (InterruptedException e)
                    {
                        //e.printStackTrace();
                    }

                }	
                
                Thread.sleep(SLEEP_TIME);
            }
            catch (Exception e)
            {
                //e.printStackTrace();
                
            }
            finally
            {
              try {
              Thread.sleep(SLEEP_TIME);
              } catch(Exception e)
              {
              }
            }
            
        }    
    }

    public void close() throws IOException 
    {
        //System.out.println("close index: " + outs.indexOf(info) + " size: " + outs.size());
        //리스트에서 해당 스트림 삭제
        outs.remove(info);
        out.close();
    }

    public void flush() throws IOException 
    {
        info.isWrite = true;
        info.updateTime();
        
        out.flush();
        
        info.isWrite = false;
    }

    public void write(byte[] b) throws IOException 
    {
        info.isWrite = true;
        info.updateTime();
        
        out.write(b);
        
        info.isWrite = false;
    }

    public void write(byte[] b, int off, int len) throws IOException 
    {
        info.isWrite = true;
        info.updateTime();
        
        out.write(b, off, len);
        
        info.isWrite = false;
    }

    public void write(int b) throws IOException 
    {
        info.isWrite = true;
        info.updateTime();
        
        out.write(b);

        info.isWrite = false;
    }

    private class OutInfo
    {
        public OutputStream out;
        public boolean isWrite = false;
        
        public OutInfo(OutputStream out, long expireTime)
        {
            this.out = out;
            this.expireTime = expireTime;
        }

        
        //시간 관련 사항     
        private long time;

        private long expireTime;

        public void setExpireTime(long time)
        {
            this.expireTime = time;
        }

        public long getExpireTime()
        {
            return expireTime;
        }

        public void updateTime() 
        {
            time = System.currentTimeMillis();
        }

        public long getTime()
        {
            return temp - time;
        }

        private long temp;

        /**
         * 시간이 지났으면 true 반환
         */
        public boolean getTimeout()
        {
            temp = System.currentTimeMillis();
            
            if ( (temp - time) >= expireTime)
            {
                return true;
            }

            return false;
        }
    };
};
