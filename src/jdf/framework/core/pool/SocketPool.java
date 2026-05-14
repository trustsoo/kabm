package jdf.framework.core.pool;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.ConfigurationException;

import java.io.IOException;
import java.net.Socket;
import java.util.*;


/**
 * java.net.Socket 객체를 pooling하기 위한 class
 * 
 * @author
 * 
 */
public final class SocketPool
{

    private static Map socketPools = new Hashtable();

    private LinkedList free;

    private List used;

    private int count;

    // Configuration variable
    private String HOST;

    private int PORT;

    private int MAX_COUNT;

    private int INITIAL_COUNT;

    private int TIMEOUT;

    private boolean BLOCK;

    // Constructor
    private SocketPool(String poolname) throws IOException
    {

        free = new LinkedList();
        used = new ArrayList();

        try
        {
            setConfig(poolname);
        } catch (Exception e)
        {
            throw new IOException(poolname + " socket pool configuration fail.");
        }

        while (count < INITIAL_COUNT)
        {
            addSocket();
        }
    }

    /**
     * pool 이름에 해당하는 pooling되는 socket 객체를 가져온다.
     *  
     * @param poolname
     * @return
     * @throws IOException
     */
    public synchronized static SocketPool getInstance(String poolname) throws IOException
    {
        SocketPool pool = (SocketPool) socketPools.get(poolname);

        if (pool == null)
        {
            pool = new SocketPool(poolname);

            socketPools.put(poolname, pool);
        }

        return pool;
    }

    /**
     * SocketPool 객체를 가져온다.
     * 
     * @return
     * @throws IOException
     */
    public static SocketPool getInstance() throws IOException
    {
        try
        {
            return getInstance(getDefaultPoolName());
        } catch (Exception e)
        {
            throw new IOException(e.getMessage());
        }

    }

    private synchronized void setConfig(String poolname) throws Exception
    {
        try
        {
            Config conf = Configuration.lookup("/network/socketPool/" + poolname);

            HOST = conf.getString("ip");
            PORT = conf.getInt("port");

            INITIAL_COUNT = conf.getInt("initialCapacity", 2);
            MAX_COUNT = conf.getInt("maxCapacity", 6);

            TIMEOUT = conf.getInt("timeout", 60 * 1000);
            BLOCK = conf.getBoolean("isBlocking", false);
        } catch (ConfigurationException ce)
        {
            ce.printStackTrace();
            throw ce;

        }
    }

    private static String defaultPoolName = null;

    private static String getDefaultPoolName() throws Exception
    {
        if (defaultPoolName == null)
        {

            try
            {
                Config conf = Configuration.lookup("/network/socketPool");

                defaultPoolName = conf.getString("default");
            } catch (ConfigurationException ce)
            {
                ce.printStackTrace();
                throw ce;
            }

            return defaultPoolName;
        }

        return defaultPoolName;

    }

    /*
     * 
     * //Loading the configuration from pool.properties file. private static
     * void loadConf() { ResourceBundle rb = ResourceBundle.getBundle("pool") ;
     * HOST = rb.getString("host"); PORT =
     * Integer.parseInt(rb.getString("port")); MAX_COUNT =
     * Integer.parseInt(rb.getString("maxCons")); INITIAL_COUNT =
     * Integer.parseInt(rb.getString("initialCons")); String timeout =
     * rb.getString("timeout"); String block = rb.getString("block");
     * if(block!=null) { BLOCK = Boolean.getBoolean(block); TIMEOUT =
     * Integer.parseInt(timeout); } System.out.println("Socket Pooling
     * Configuration ****************"); System.out.println("Host : "+HOST);
     * System.out.println("Port : "+PORT); System.out.println("Max_Count :
     * "+MAX_COUNT); System.out.println("Min_Count : "+INITIAL_COUNT);
     * System.out.println("BLOCK : "+BLOCK); System.out.println("TIMEOUT :
     * "+TIMEOUT);
     * System.out.println("---------------------------------------------"); }
     */

    public Socket getSocket() throws IOException
    {
        return getSocket(BLOCK, TIMEOUT);
    }

    /**
     * Socket 객체를 가져온다.
     * 
     * @param block
     * @param timeout
     * @return
     * @throws IOException
     */
    public synchronized Socket getSocket(boolean block, long timeout) throws IOException
    {
        if (free.isEmpty())
        {
            if (count < MAX_COUNT)
            {
                addSocket();
            } else if (block)
            {
                try
                {
                    synchronized (this)
                    {
                        wait(timeout);
                    }
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                if (free.isEmpty())
                {
                    if (count < MAX_COUNT)
                    {
                        addSocket();
                    } else
                    {
                        throw new IOException("Timeout waiting for a socket to be released.");
                    }
                }
            } else
            {
                throw new IOException("Maximum number of allowed Sockets reached");
            }
        }
        Socket sck = null;
        synchronized (used)
        {
            sck = (Socket) free.removeLast();
            used.add(sck);
        }
        return sck;
    }

    /**
     * Socket 객체를 반환한다.
     * 
     * @param sck
     * @throws IOException
     */
    public synchronized void release(Socket sck) throws IOException
    {

        boolean isSocketLive = true;

        try
        {
            sck.getKeepAlive();
        } catch (IOException e)
        {
            isSocketLive = false;

            try
            {
                System.err.println("<SocketPool> socket close");
                sck.close();

            } catch (Exception ee)
            {
            }

        }

        if (used.contains(sck))
        {
            int idx = used.indexOf(sck);
            used.remove(idx);
            if (isSocketLive)
                free.add(sck);
            else
                count--;
        } else
        {
            throw new IOException("Socket " + sck + " did not come from this SocketPool");
        }
        notify();
    }

    /**
     * Pooling되는 모든 Socket 을 close 한다.
     * 
     * @throws IOException
     */
    public synchronized void closeAll() throws IOException
    {
        for (int i = 0; i < free.size(); i++)
        {
            Socket sck = (Socket) free.remove(i);
            try
            {
                sck.close();
            } catch (IOException e)
            {
                System.out.println(e.toString());
            }
        }

        for (int i = 0; i < used.size(); i++)
        {
            used.remove(i);
        }
    }

    private void addSocket() throws IOException
    {
        Socket sck = new Socket(HOST, PORT);
        free.add(sck);
        count++;
    }
}