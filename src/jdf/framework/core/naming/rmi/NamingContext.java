package jdf.framework.core.naming.rmi;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.rmi.MarshalledObject;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.InvalidNameException;
import javax.naming.LinkRef;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NotContextException;
import javax.naming.OperationNotSupportedException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.ServiceUnavailableException;
import javax.naming.spi.NamingManager;
import javax.naming.spi.ResolveResult;

public class NamingContext implements Context, java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7145223858651890518L;

	/**
	 * 
	 * @uml.property name="naming"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	Naming naming;

    Hashtable env;
    Name prefix;

    NameParser parser= new NamingParser();

 // Remote 객체를 위한 임시저장소
    private static Map cache= new Hashtable();

    private boolean isSameVM= false;

	/**
	 * 
	 * @uml.property name="vmNaming"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private static Naming vmNaming;


    /**
     * �ڽ��� VM�� NamingServer�� ��Ͻ�Ų��.
     *
     */
    public static void setLocalNaming(Naming vm)
    {
        vmNaming= vm;
    }

	/**
	 * 
	 * @uml.property name="naming"
	 */
	public Naming getNaming() {
		return naming;
	}

    public NamingContext(Hashtable e, Name baseName, Naming server) throws NamingException
    {

        //System.out.println("NamingContext =============== 2 **********");

        if (baseName == null)
            this.prefix= parser.parse("");
        else
            this.prefix= baseName;

        if (e != null)
            this.env= (Hashtable) e.clone();
        else
            this.env= new Hashtable();

        this.naming= server;

        //System.out.println("init -------- "+baseName);
    }

    public void rebind(String name, Object obj) throws NamingException
    {
        rebind(getNameParser(name).parse(name), obj);
    }

    public void rebind(Name name, Object obj) throws NamingException
    {
        Hashtable env= getEnv(name);

        checkRef(env);

        try
        {
            String className;

            if (obj instanceof java.rmi.Remote)
                cache.put(getAbsoluteName(name), obj);

            // Referenceable
            if (obj instanceof Referenceable)
                obj= ((Referenceable) obj).getReference();

            if (!(obj instanceof Reference))
            {
                // Normal object - serialize
                className= obj.getClass().getName();
                obj= new MarshalledObject(obj);
            }
            else
                className= ((Reference) obj).getClassName();

            naming.rebind(getAbsoluteName(name), obj, className);
        }
        catch (IOException e)
        {
            naming= null;
            NamingException ex= new CommunicationException();
            ex.setRootCause(e);
            throw ex;
        }
    }

    public void bind(String name, Object obj) throws NamingException
    {
        bind(getNameParser(name).parse(name), obj);
    }

    /**
     * binding한다.
     *
     *
     */
    public void bind(Name name, Object obj) throws NamingException
    {
        //System.out.println("name="+name);
        //System.out.println("obj="+obj.getClass().getName());

        Hashtable env= getEnv(name);
        checkRef(env);

        if (name.size() > 1)
        {
            Context ctx= null;

            try
            {
            	// 우선 생성해 보고
                ctx= (Context) lookup(name.getPrefix(1));

                //System.out.println("1----->"+name.getPrefix(1));
            }
            catch (Exception e)
            {
                ctx= createSubcontext(name.getPrefix(1));
                //System.out.println("2----->"+name.getPrefix(1));

            }

            ctx.bind(name.getSuffix(1), obj);

            return;
        }

        try
        {
            String className;

            if (obj instanceof java.rmi.Remote)
                cache.put(getAbsoluteName(name), obj);

            // Referenceable
            if (obj instanceof Referenceable)
                obj= ((Referenceable) obj).getReference();

            if (!(obj instanceof Reference))
            {
                // Normal object - serialize
                className= obj.getClass().getName();
                obj= new MarshalledObject(obj);

                //System.out.println("className="+className);

            }
            else
            {
                className= ((Reference) obj).getClassName();
            }

            naming.bind(getAbsoluteName(name), obj, className);

        }
        catch (IOException e)
        {
            naming= null;
            NamingException ex= new CommunicationException();
            ex.setRootCause(e);
            throw ex;
        }
    }

    public Object lookup(String name) throws NamingException
    {
        return lookup(getNameParser(name).parse(name));
    }

    public Object lookup(Name name) throws NamingException
    {

        Hashtable env= getEnv(name);

        checkRef(env);

        // Empty?
        if (name.isEmpty())
            return new NamingContext(env, prefix, naming);

        try
        {
            Object res= null;
            Name absName= getAbsoluteName(name);

         // 같은 VM인 경우는 cache에서 가져온다.
            if (isSameVM)
            {
                res= cache.get(absName);
              //System.out.println("캐쉬에서 찾는다 "+absName);

                if (res != null)
                    return res;

            }

            res= naming.lookup(absName);
            if (res instanceof MarshalledObject)
            {

                return ((MarshalledObject) res).get();
            }
            else if (res instanceof Context)
            {

                // Add env
                Enumeration keys= env.keys();
                while (keys.hasMoreElements())
                {
                    String key= (String) keys.nextElement();
                    ((Context) res).addToEnvironment(key, env.get(key));
                }
                return res;
            }
            else if (res instanceof ResolveResult)
            {

                // Dereference partial result
                try
                {
                    Object resolveRes= ((ResolveResult) res).getResolvedObj();
                    if (resolveRes instanceof LinkRef)
                    {
                        String ref= ((LinkRef) resolveRes).getLinkName();
                        Context ctx;
                        try
                        {
                            if (ref.startsWith("./"))
                                ctx= (Context) lookup(ref.substring(2));
                            else
                                ctx= (Context) new InitialContext(env).lookup(ref);

                            return ctx.lookup(((ResolveResult) res).getRemainingName());
                        }
                        catch (ClassCastException e)
                        {
                            throw new NotContextException(ref + " is not a context");
                        }
                    }
                    else
                    {
                        try
                        {
                            Context ctx= (Context) NamingManager.getObjectInstance(resolveRes, absName, this, env);
                            return ctx.lookup(((ResolveResult) res).getRemainingName());
                        }
                        catch (ClassCastException e)
                        {
                            throw new NotContextException();
                        }
                    }
                }
                catch (NamingException e)
                {
                    throw e;
                }
                catch (Exception e)
                {
                    NamingException ex= new NamingException("Could not dereference object");
                    ex.setRootCause(e);
                    throw ex;
                }
            }
            else if (res instanceof LinkRef)
            {

                // Dereference link
                try
                {
                    String ref= ((LinkRef) res).getLinkName();
                    if (ref.startsWith("./"))
                        return lookup(ref.substring(2));
                    else
                        return new InitialContext(env).lookup(ref);
                }
                catch (NamingException e)
                {
                    throw e;
                }
                catch (Exception e)
                {
                    NamingException ex= new NamingException("Could not dereference object");
                    ex.setRootCause(e);
                    throw ex;
                }
            }
            else if (res instanceof Reference)
            {

                // Dereference object
                try
                {
                    return NamingManager.getObjectInstance(res, absName, this, env);
                }
                catch (NamingException e)
                {
                    throw e;
                }
                catch (Exception e)
                {
                    NamingException ex= new NamingException("Could not dereference object");
                    ex.setRootCause(e);
                    throw ex;
                }
            }
            return res;
        }
        catch (IOException e)
        {
            naming= null;
            NamingException ex= new CommunicationException();
            ex.setRootCause(e);
            throw ex;
        }
        catch (ClassNotFoundException e)
        {
            NamingException ex= new CommunicationException();
            ex.setRootCause(e);
            throw ex;
        }
    }

    public void unbind(String name) throws NamingException
    {
        unbind(getNameParser(name).parse(name));
    }

    public void unbind(Name name) throws NamingException
    {
        Hashtable env= getEnv(name);

        checkRef(env);

        try
        {
            naming.unbind(getAbsoluteName(name));
        }
        catch (IOException e)
        {
            naming= null;
            NamingException ex= new CommunicationException();
            ex.setRootCause(e);
            throw ex;
        }
    }

    public void rename(String oldname, String newname) throws NamingException
    {
        rename(getNameParser(oldname).parse(oldname), getNameParser(newname).parse(newname));
    }

    public void rename(Name oldName, Name newName) throws NamingException
    {
        bind(newName, lookup(oldName));
        unbind(oldName);
    }

    public NamingEnumeration list(String name) throws NamingException
    {
        return list(getNameParser(name).parse(name));
    }

    public NamingEnumeration list(Name name) throws NamingException
    {
        Hashtable env= getEnv(name);

        checkRef(env);

        try
        {
            return new NamingEnumerationImpl(naming.list(getAbsoluteName(name)));
        }
        catch (IOException e)
        {
            naming= null;
            NamingException ex= new CommunicationException();
            ex.setRootCause(e);
            throw ex;
        }
    }

    public String composeName(String name, String prefix) throws NamingException
    {
        Name result= composeName(parser.parse(name), parser.parse(prefix));
        return result.toString();
    }

    public Name composeName(Name name, Name prefix) throws NamingException
    {
        Name result= (Name) (prefix.clone());
        result.addAll(name);
        return result;
    }

    public NameParser getNameParser(String name) throws NamingException
    {
        return parser;
    }

    public NameParser getNameParser(Name name) throws NamingException
    {
        return getNameParser(name.toString());
    }

    public Context createSubcontext(String name) throws NamingException
    {
        return createSubcontext(getNameParser(name).parse(name));
    }

    public Context createSubcontext(Name name) throws NamingException
    {
        Hashtable env= getEnv(name);

        checkRef(env);

        try
        {
            return naming.createSubcontext(getAbsoluteName(name));
        }
        catch (IOException e)
        {
            naming= null;
            NamingException ex= new CommunicationException();
            ex.setRootCause(e);
            throw ex;
        }
    }

    public Object addToEnvironment(String propName, Object propVal) throws NamingException
    {
        Object old= env.get(propName);
        env.put(propName, propVal);
        return old;
    }

    public Object removeFromEnvironment(String propName) throws NamingException
    {
        return env.remove(propName);
    }

    public Hashtable getEnvironment() throws NamingException
    {
        return env;
    }

    public void close() throws NamingException
    {
        env= null;
        naming= null;
    }

    public String getNameInNamespace() throws NamingException
    {
        return prefix.toString();
    }

    // NYI

    public NamingEnumeration listBindings(String name) throws NamingException
    {
        throw new OperationNotSupportedException();
    }

    public NamingEnumeration listBindings(Name name) throws NamingException
    {
        throw new OperationNotSupportedException();
    }

    public void destroySubcontext(String name) throws NamingException
    {
        throw new OperationNotSupportedException();
    }

    public void destroySubcontext(Name name) throws NamingException
    {
        throw new OperationNotSupportedException();
    }

    public Object lookupLink(String name) throws NamingException
    {
        throw new OperationNotSupportedException();
    }

    public Object lookupLink(Name name) throws NamingException
    {
        throw new OperationNotSupportedException();
    }

    // Y overrides ---------------------------------------------------

    // Package protected ---------------------------------------------

    // Protected -----------------------------------------------------

    // Private -------------------------------------------------------

    private String host= "localhost"; // 프로바이더 주소
    private int port= 1099; // 프로바이더 포트

    /**
     * 여기서 분배작업을 해야겠군....
     *
     */
    private synchronized void checkRef(Hashtable env) throws NamingException
    {

        if (naming == null)
        {

            //"sotp://localhost:8001"

            // Locate naming service
            try
            {

                if (env.get(Context.PROVIDER_URL) != null)
                {
                    //System.out.println("Context.PROVIDER_URL="+env.get(Context.PROVIDER_URL));

                    StringTokenizer tkn= new StringTokenizer((String) env.get(Context.PROVIDER_URL), ":");
                    tkn.nextToken();

                    host= tkn.nextToken();

                    if (host.indexOf("//") > -1)
                        host= host.substring(2);

                    try
                    {
                        port= Integer.parseInt(tkn.nextToken());
                    }
                    catch (Exception ex)
                    {
                        // Use default;
                    }

                }
                else
                {
                    String providerUrl= System.getProperty(Context.PROVIDER_URL);

                    if (providerUrl != null)
                    {
                        StringTokenizer tkn= new StringTokenizer(providerUrl, ":");
                        tkn.nextToken();

                        host= tkn.nextToken();

                        if (host.indexOf("//") > -1)
                            host= host.substring(2);

                        try
                        {
                            port= Integer.parseInt(tkn.nextToken());
                        }
                        catch (Exception ex)
                        {
                            // Use default;
                        }
                    }

                }

                //System.out.println("Connecting to "+host+":"+port);
                //System.out.println("JNDI:"+env.toString());

                Socket s= new Socket(host, port);

                try
                {
                    ObjectInputStream in= new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
                    naming= (Naming) in.readObject();

                    if (vmNaming != null && naming.getIndetity() == vmNaming.getIndetity())
                    {
                        isSameVM= true;
                        naming= vmNaming;
                    }

                }
                catch (Exception e)
                {
                    NamingException ex= new CommunicationException(e.getMessage());
                    ex.setRootCause(e);
                    throw ex;
                }
                finally
                {
                    if (s != null)
                        s.close();
                }

            }
            catch (IOException e2)
            {
                NamingException ex= new ServiceUnavailableException(e2.getMessage());
                ex.setRootCause(e2);
                throw ex;
            }
        }
     // naming이 null이 아니면
        else if (!isSameVM)
        {
            try
            {
                if (vmNaming != null && naming.getIndetity() == vmNaming.getIndetity())
                {
                    isSameVM= true;
                    naming= vmNaming;
                }
            }
            catch (Exception re)
            {
                NamingException ex= new CommunicationException(re.getMessage());
                ex.setRootCause(re);
                throw ex;
            }

        }

    }

    private Name getAbsoluteName(Name n) throws NamingException
    {
        if (n.isEmpty())
            return composeName(n, prefix);
        else if (n.get(0).toString().equals("")) // Absolute name
            return n.getSuffix(1);
        else // Add prefix
            return composeName(n, prefix);
    }

    private Hashtable getEnv(Name n) throws InvalidNameException
    {
        if (n.size() == 0)
            return env;

        if (n.get(0).startsWith("java:"))
        {
            return getEnvForScheme("java:", n);
        }
        else if (n.get(0).startsWith("sotp:"))
        {
            return getEnvForScheme("sotp:", n);
        }
        else
        {
            return env;
        }
    }

    private Hashtable getEnvForScheme(String url, Name n) throws InvalidNameException
    {
        String scheme= n.get(0).substring(url.length());
        if (scheme.equals(""))
        {
            // Scheme was "url:/..."
            n.remove(0);
            if (n.size() > 0)
            {
                if (n.get(0).equals("") && n.size() > 1)
                {
                    // Scheme was "url://somehost"

                    // Get provider
                    String provider= n.get(1);
                    Hashtable newEnv= (Hashtable) env.clone();
                    newEnv.put(Context.PROVIDER_URL, provider);
                    n.remove(0);
                    n.remove(0);
                    return newEnv;
                }
                else
                {
                    return env;
                }
            }
            else
            {
                return env;
            }
        }
        else
        {
            // Scheme was "url:foo" -> reinsert "foo"
            n.remove(0);
            n.add(0, scheme);
            return env;
        }
    }
    // Inner classes -------------------------------------------------
}