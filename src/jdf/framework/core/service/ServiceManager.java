package jdf.framework.core.service;
/*
 * @(#)AbstractService.java
 *
 * NOTICE !      
 * You can copy or redistribute this code freely except commercial use,
 * If you want to use this program for commercial use, 
 * you must contact to me.
 *
 * And, you should not remove the information about the copyright notice 
 * and the author.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author
 */

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.StringFormater;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * <code>Service</code> interface를 구현한 객체를 등록/제거/관리하기 위한 Manager Class
 */
public class ServiceManager extends AbstractManager
{
    
	private final String LOG_ID="<at:ServiceManager> ";
	
    private static Map serviceList = new Hashtable();
    
    
    private final static String DIR_SERVICE = "jdf-service";
    
    
    private static String PATH_SERVICE_CONFIG = "/server/service";
    
    
    // 서비스 등록해서 관리하기 위한 저장소
    private static Context ctx = null;

	/**
	 * 
	 * @uml.property name="mgr"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private static ServiceManager mgr;

    
    
    private ServiceManager()
    {
        // 객체 생성을 막는다.
    }
    
    
    public static ServiceManager getInstance()
    {
        if(mgr == null)
            mgr = new ServiceManager();
            
        return mgr;
    }
        
    
    
    
    public void registRefService(String name, Service srvc)
    {
        serviceList.put(name, srvc);
    }
    
    public Service getRefService(String name)
    {
        return (Service) serviceList.get(name);
    }
    
    
    
    public Context getContext()
    {
        try
        {
            if(ctx != null)
                return ctx;
            
            Context initCtx = getInitialContext();
            
            try{
            	ctx = initCtx.createSubcontext(DIR_SERVICE);
            }catch(javax.naming.NameAlreadyBoundException ae){
            	ctx = (Context)initCtx.lookup(DIR_SERVICE);
            }catch(Exception e){
            	return null;
            }
            
            return ctx;
            
        }
        catch(Exception e) {
            e.printStackTrace();
            }
        
        return null;
    }
    
    
    

    /**
     * system startup시 등록해야할 모든 service를 시작하고, 등록시킨다.
     *
     */
    public void registStartupService()
    {
        
        String[] serviceNames = getServiceNames();
        
        for(int i=0; i<serviceNames.length; i++)
        {
            try
            {
                startService(serviceNames[i]);
                //Logger.debug.println("System startup service["+serviceNames[i]+"]");
            }
            catch(Exception e)
            {
                Logger.err.println(LOG_ID+"System startup service["+serviceNames[i]+"] fail");
                e.printStackTrace();
            }
        }
        
    }
    



    /**
     * 특정서비스를 startup시킨다.
     * Config에서 startup이 true인건만 실행시킨다.
     * 
     */
    public synchronized void startService(String serviceName)
        throws Exception
    {
        try
        {
            Config conf = Configuration.lookup(PATH_SERVICE_CONFIG+"/"+serviceName);
            
            //System.out.println("classname"+conf.getString("classname"));
            
            // 서비스 수행
            Service serviceInstance = (Service) Class.forName(conf.getString("classname")).newInstance();
            
            Config srvcConf = Configuration.lookup(PATH_SERVICE_CONFIG+"/"+serviceName+"/param");
            
            serviceInstance.setConfigInfo(srvcConf);    // Config setting
            serviceInstance.setName(serviceName);       // name setting
            
            // 서비스 등록
            registService(serviceName, serviceInstance);
            
            String whenStart = conf.getString("runAt", "now");
            
            if(whenStart.equals("now") || whenStart.length() == 0)
            {
                serviceInstance.start();
                Logger.info.println(LOG_ID+"<Service:"+serviceName+"> start");
            }
            /*
            else if(whenStart.equals("after") )
            {
                Logger.info.println("<Service:"+serviceName+"> start after Server Startup.");
            }
            else if(whenStart.equals("future") )
            {
                Logger.info.println("<Service:"+serviceName+"> start later.");
            }
            */
        }
        catch(Exception e)
        {        	
            e.printStackTrace(); 
            throw e;
        }
    }

    /**
     * 서비스를 등록시킨다.
     *
     */
    private void registService(String serviceName, Service service)
    {
        try
        {
            //services.put(serviceName, service);
            
            if(getContext()==null) {
                Logger.err.println(LOG_ID+"Context is null");
            }
                
            getContext().bind( serviceName, service);

            Logger.info.println("<Service:"+serviceName+"> regist.");
        }
        catch(Exception e)
        {
           Logger.err.println(LOG_ID+"<Service:"+serviceName+"> regist fail. cause "+e.toString());
        }
        
    }
    

    
    /**
     * config의 모든 서비스명을 배열로 가져온다.
     *
     */
    public String[] getServiceNames()
    {
        try
        {
            
            //String htsServiceHeader = "/hts/service";
            
            Config conf = Configuration.getInitial();
            
            List keyList = conf.keys();
            List list = new ArrayList();
            
            
            for(int j=0;j< keyList.size(); j++ )
            {
                String key = (String) keyList.get(j);
                //System.out.println("KEY : " + key);                
                // 일반 Property를 위해서
                if(! key.startsWith("/") )
                    key = "/"+StringFormater.replaceStr(key, ".", "/");
                    
                
                int l=key.lastIndexOf("classname");
                if( key.startsWith(PATH_SERVICE_CONFIG)&&  l> 0 )
                {
                    //System.out.println(key.substring(htsServiceHeader.length()+1,l-1));
                    list.add(key.substring(PATH_SERVICE_CONFIG.length()+1,l-1) );
                }
                
                    
            }
            
            return (String[]) list.toArray(new String[] {});                    
        }
        catch(Exception e)
        {
            return new String[] {};
        }
    }
    
    /**
     * 서비스명을 가진 해당 Service 객체를 return한다.
     *
     */
    public Service getService(String serviceName) throws javax.naming.NamingException
    {
        //return (Service) services.get(serviceName);
        
        
        return (Service) getContext().lookup( serviceName);
        
    }

	/**
	 * 
	 * @uml.property name="serviceList"
	 */
	public List getServiceList() {
		//ArrayList list = new ArrayList( services.values() );
		ArrayList list = new ArrayList();

		try {
			InitialContext initCtx = new InitialContext();

			NamingEnumeration names = initCtx.list(DIR_SERVICE);

			while (names.hasMore()) {
				NameClassPair namePair = (NameClassPair) names.next();

				list.add(getContext().lookup(namePair.getName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

    



    
    
    /**
     * 서비스중에서 모든 상태가 완결된 상태에서 뜨는 서비스들을
     * 시작한다.
     *
     *
     */
    public void startLastServices() throws ServiceException
    {
        String serviceName=null;
        
        try
        {
            List services = getServiceList();

            for(int j=0; j<services.size(); j++)
            {
				Object obj = services.get(j);
               
				Service srvc = (Service) services.get(j);
                
                serviceName = srvc.getName();
                    
                Config conf = Configuration.lookup( PATH_SERVICE_CONFIG +"/"+serviceName);
                
                boolean isStatup = conf.getBoolean("startup",true);
                
                String whenStart = conf.getString("runAt", "now");
            
                if("after".equals(whenStart) )
                {
                    srvc.start();
                    Logger.info.println(LOG_ID+"<Service> ["+serviceName+"] start");
                }
             }
        }
        catch(Exception ne)
        {
            ne.printStackTrace();
            
            throw new ServiceException(serviceName+ " service start fail. cause "+ne.toString());
        }
        
    }
    
    
    
    
    /**
     * 모든 서비스를 종료시킨다.
     * 
     *
     */
    public void stopAllServices() throws ServiceException
    {
            
            List services = getServiceList();
            
            for(int j=0; j<services.size(); j++)
            {
                Service srvc = (Service) services.get(j);
                
                try
                {
                
                    srvc.stop();
                    
                    //Logger.info.println( srvc.getInstanceID()+" stop");
                }
                catch(RuntimeException re)
                {
                    Logger.err.println(LOG_ID+ srvc.getInstanceID()+" stop fail. cause "+re.toString());
                }
                catch(Exception e)
                {
                    Logger.err.println(LOG_ID+ srvc.getInstanceID()+" stop fail. cause "+e.toString());
                }
            }

    }        
        
    
    
    
    
    public void initialize() throws RuntimeException
    {
        registStartupService();
        
        startLastServices();
    }
    
    
}
