package jdf.framework.core.naming;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

//import org.objectweb.jonas.common.Trace;
//import org.enhydra.naming.ContainerNaming;

/**
 * Naming Manager for an EJB Server.
 * this singleton class must exist in each jonas server.
 */
public class NamingManager //implements ContainerNaming 
{

    //private static final int trace = Trace.DB_3;

    private static final String JNDI_PREFIX = "java:comp/env/";

    // Naming Context associated with the thread
    private ThreadLocal threadContext = new ThreadLocal();

    // Contexts
    private InitialContext ictx = null;
    private Context serverContext = null;

    // Env.
    private Hashtable myEnv = null;

	/**
	 * 
	 * @uml.property name="unique"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	// ------------------------------------------------------------------
	// Singleton management:
	// - the constructor is private.
	// - use static method getInstance to retrieve/create the instance.
	// ------------------------------------------------------------------
	private static NamingManager unique = null;


    /**
     * Create the naming manager.
     * useful to create InitialContext because it may throw an exception.
     */
    private NamingManager() throws NamingException {
	// Create the initial context
	// Don't use jonas.properties anymore for jndi properties
	// jndi.properties will be used instead.
	try {
	    System.setProperty("java.naming.factory.initial", "anyframex.naming.InitialContextFactory");
	    
	    
	    ictx = new InitialContext();
	    myEnv = ictx.getEnvironment();
	} catch (NamingException n) {
	    //Trace.errln("NamingManager: "+n.getExplanation());
	    Throwable t = n.getRootCause();
	    if (t != null) {
		if (t.getMessage().startsWith("Connection refused to host:")) {
		    //Trace.errln("NamingManager: rmi registry not started ?");
		} else if (t.getMessage().startsWith("error during remote invocation")) {
		    //Trace.errln("NamingManager: jrmi registry not started ?");
		} else {
		    //Trace.errln("NamingManager: "+t.getMessage());
		}
	    }
	    throw n;
	}

	// Create a CompNamingContext global for this server
	// not really used today, but could be useful later.
	serverContext = new CompNamingContext("server", myEnv);
    }

    public static NamingManager getInstance() throws NamingException {
	if (unique == null) {
	    unique = new NamingManager();
	}
	return unique;
    }


    // ------------------------------------------------------------------
    // ContainerNaming implementation
    // ------------------------------------------------------------------

    /**
     * return the initialContext used in this jonas server
     */
    public InitialContext getInitialContext() 
    {
	    return ictx;
    }

    /**
     * Create Context for application and component environments.
     * (formally known as createComponentContext)
     */
    public Context createEnvironmentContext(String namespace) throws NamingException {
	CompNamingContext ctx = new CompNamingContext(namespace);
	return (Context) ctx;
    }

    /**
     * return the Context associated with the current thread.
     */
    public Context getComponentContext() {

	Context ctx = (Context) threadContext.get();
	/*
	if (ctx == null) {
	    //Trace.outln(trace, "NamingManager: getComponentContext: null context");
	} else {
	    try {
		//Trace.outln(trace, "NamingManager: getComponentContext: "+ctx.getNameInNamespace());
	    } catch (NamingException e) {
		//Trace.errln("NamingManager: getComponentContext: (bad name)");
	    }
	}
	*/
	return ctx;
    }

    /**
     * associate this CompNamingContext with the current thread.
     * this method should be called in preinvoke/postinvoke
     * and when we build the bean environment.
     */
    public Context setComponentContext(Context ctx) {
	/*
	if (ctx == null) {
	    //Trace.outln(trace, "NamingManager: setComponentContext: reset to null");
	} else {
	    try {
		//Trace.outln(trace, "NamingManager: setComponentContext: "+ctx.getNameInNamespace());
	    } catch (NamingException e) {
		//Trace.errln("NamingManager: setComponentContext: (bad name)");
	    }
	}
	*/
	Context ret = (Context) threadContext.get();
	threadContext.set(ctx);
	return ret;
    }

    /**
     * Return the environment for JNDI
     * This is used only for handles today.
     */
    public Hashtable getEnv() {
	return myEnv;
    }

    public Context createImmutableEnvironmentContext(String namespace) throws NamingException {
	return null;
    }

	// ------------------------------------------------------------------
	// other proprietary methods
	// ------------------------------------------------------------------

	/**
	 * gets the server component context
	 * This is used only internally in the jonas NamingManager.
	 * 
	 * @uml.property name="serverContext"
	 */
	public Context getServerContext() {
		return serverContext;
	}

}
