package jdf.framework.core.naming.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;


/**
 * <b><code>Naming</code></b>
 * <p>
 * 주문이나 그밖의 byte stream형태를 처리하기 위한 객체의 interface이다.
 * </p>
 *
 * @author  
 * @version 1.0
 * @see jdf.framework.core.io.FormatedInputStream
 * @see jdf.framework.core.io.FormatedOutputStream
 */
public interface Naming extends Remote
{
   
    public void bind(Name name, Object obj, String className)
        throws NamingException, RemoteException;
    
    public void rebind(Name name, Object obj, String className)
        throws NamingException, RemoteException;
    
    public void unbind(Name name)
        throws NamingException, RemoteException;
    
    public Object lookup(Name name)
        throws NamingException, RemoteException;
    
    public Collection list(Name name)
        throws NamingException, RemoteException;
    
    public Context createSubcontext(Name name)
        throws NamingException, RemoteException;
    


 // NamingServer를 구별하기 위해.
    public long getIndetity() throws RemoteException;
    

}
