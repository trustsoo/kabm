// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 2005-03-09 ���� 10:10:11
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 

package jdf.framework.core.naming.server;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;
import java.rmi.MarshalException;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.rmi.UnmarshalException;
import java.rmi.server.Operation;
import java.rmi.server.RemoteCall;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;
import java.util.Collection;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;

import jdf.framework.core.naming.rmi.Naming;

public final class NamingServer_Stub extends RemoteStub
    implements Naming
{

    public NamingServer_Stub()
    {
    }

    public NamingServer_Stub(RemoteRef remoteref)
    {
        super(remoteref);
    }

    public void bind(Name name, Object obj, String s)
        throws RemoteException, NamingException
    {
        try
        {
            if(useNewInvoke)
            {
                super.ref.invoke(this, $method_bind_0, new Object[] {
                    name, obj, s
                }, 0x78388d75267a5392L);
            } else
            {
                RemoteCall remotecall = super.ref.newCall(this, operations, 0, 0x5b4f90de7a473f79L);
                try
                {
                    ObjectOutput objectoutput = remotecall.getOutputStream();
                    objectoutput.writeObject(name);
                    objectoutput.writeObject(obj);
                    objectoutput.writeObject(s);
                }
                catch(IOException ioexception)
                {
                    throw new MarshalException("error marshalling arguments", ioexception);
                }
                super.ref.invoke(remotecall);
                super.ref.done(remotecall);
            }
        }
        catch(RuntimeException runtimeexception)
        {
            throw runtimeexception;
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(NamingException namingexception)
        {
            throw namingexception;
        }
        catch(Exception exception)
        {
            throw new UnexpectedException("undeclared checked exception", exception);
        }
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    public Context createSubcontext(Name name)
        throws RemoteException, NamingException
    {
        try
        {
            if(useNewInvoke)
            {
                Object obj = super.ref.invoke(this, $method_createSubcontext_1, new Object[] {
                    name
                }, 0x7aec96a3a0e3404cL);
                return (Context)obj;
            }
            RemoteCall remotecall = super.ref.newCall(this, operations, 1, 0x5b4f90de7a473f79L);
            try
            {
                ObjectOutput objectoutput = remotecall.getOutputStream();
                objectoutput.writeObject(name);
            }
            catch(IOException ioexception)
            {
                throw new MarshalException("error marshalling arguments", ioexception);
            }
            super.ref.invoke(remotecall);
            Context context;
            try
            {
                ObjectInput objectinput = remotecall.getInputStream();
                context = (Context)objectinput.readObject();
            }
            catch(IOException ioexception1)
            {
                throw new UnmarshalException("error unmarshalling return", ioexception1);
            }
            catch(ClassNotFoundException classnotfoundexception)
            {
                throw new UnmarshalException("error unmarshalling return", classnotfoundexception);
            }
            finally
            {
                super.ref.done(remotecall);
            }
            return context;
        }
        catch(RuntimeException runtimeexception)
        {
            throw runtimeexception;
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(NamingException namingexception)
        {
            throw namingexception;
        }
        catch(Exception exception)
        {
            throw new UnexpectedException("undeclared checked exception", exception);
        }
    }

    public long getIndetity()
        throws RemoteException
    {
        try
        {
            if(useNewInvoke)
            {
                Object obj = super.ref.invoke(this, $method_getIndetity_2, null, 0xf4b9358702e1c11fL);
                return ((Long)obj).longValue();
            }
            RemoteCall remotecall = super.ref.newCall(this, operations, 2, 0x5b4f90de7a473f79L);
            super.ref.invoke(remotecall);
            long l;
            try
            {
                ObjectInput objectinput = remotecall.getInputStream();
                l = objectinput.readLong();
            }
            catch(IOException ioexception)
            {
                throw new UnmarshalException("error unmarshalling return", ioexception);
            }
            finally
            {
                super.ref.done(remotecall);
            }
            return l;
        }
        catch(RuntimeException runtimeexception)
        {
            throw runtimeexception;
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(Exception exception)
        {
            throw new UnexpectedException("undeclared checked exception", exception);
        }
    }

    public Collection list(Name name)
        throws RemoteException, NamingException
    {
        try
        {
            if(useNewInvoke)
            {
                Object obj = super.ref.invoke(this, $method_list_3, new Object[] {
                    name
                }, 0xd446363e48d84a96L);
                return (Collection)obj;
            }
            RemoteCall remotecall = super.ref.newCall(this, operations, 3, 0x5b4f90de7a473f79L);
            try
            {
                ObjectOutput objectoutput = remotecall.getOutputStream();
                objectoutput.writeObject(name);
            }
            catch(IOException ioexception)
            {
                throw new MarshalException("error marshalling arguments", ioexception);
            }
            super.ref.invoke(remotecall);
            Collection collection;
            try
            {
                ObjectInput objectinput = remotecall.getInputStream();
                collection = (Collection)objectinput.readObject();
            }
            catch(IOException ioexception1)
            {
                throw new UnmarshalException("error unmarshalling return", ioexception1);
            }
            catch(ClassNotFoundException classnotfoundexception)
            {
                throw new UnmarshalException("error unmarshalling return", classnotfoundexception);
            }
            finally
            {
                super.ref.done(remotecall);
            }
            return collection;
        }
        catch(RuntimeException runtimeexception)
        {
            throw runtimeexception;
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(NamingException namingexception)
        {
            throw namingexception;
        }
        catch(Exception exception)
        {
            throw new UnexpectedException("undeclared checked exception", exception);
        }
    }

    public Object lookup(Name name)
        throws RemoteException, NamingException
    {
        try
        {
            if(useNewInvoke)
            {
                Object obj = super.ref.invoke(this, $method_lookup_4, new Object[] {
                    name
                }, 0xf508f880b993d0a2L);
                return obj;
            }
            RemoteCall remotecall = super.ref.newCall(this, operations, 4, 0x5b4f90de7a473f79L);
            try
            {
                ObjectOutput objectoutput = remotecall.getOutputStream();
                objectoutput.writeObject(name);
            }
            catch(IOException ioexception)
            {
                throw new MarshalException("error marshalling arguments", ioexception);
            }
            super.ref.invoke(remotecall);
            Object obj1;
            try
            {
                ObjectInput objectinput = remotecall.getInputStream();
                obj1 = objectinput.readObject();
            }
            catch(IOException ioexception1)
            {
                throw new UnmarshalException("error unmarshalling return", ioexception1);
            }
            catch(ClassNotFoundException classnotfoundexception)
            {
                throw new UnmarshalException("error unmarshalling return", classnotfoundexception);
            }
            finally
            {
                super.ref.done(remotecall);
            }
            return obj1;
        }
        catch(RuntimeException runtimeexception)
        {
            throw runtimeexception;
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(NamingException namingexception)
        {
            throw namingexception;
        }
        catch(Exception exception)
        {
            throw new UnexpectedException("undeclared checked exception", exception);
        }
    }

    public void rebind(Name name, Object obj, String s)
        throws RemoteException, NamingException
    {
        try
        {
            if(useNewInvoke)
            {
                super.ref.invoke(this, $method_rebind_5, new Object[] {
                    name, obj, s
                }, 0x763a3b7e6c4ef72fL);
            } else
            {
                RemoteCall remotecall = super.ref.newCall(this, operations, 5, 0x5b4f90de7a473f79L);
                try
                {
                    ObjectOutput objectoutput = remotecall.getOutputStream();
                    objectoutput.writeObject(name);
                    objectoutput.writeObject(obj);
                    objectoutput.writeObject(s);
                }
                catch(IOException ioexception)
                {
                    throw new MarshalException("error marshalling arguments", ioexception);
                }
                super.ref.invoke(remotecall);
                super.ref.done(remotecall);
            }
        }
        catch(RuntimeException runtimeexception)
        {
            throw runtimeexception;
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(NamingException namingexception)
        {
            throw namingexception;
        }
        catch(Exception exception)
        {
            throw new UnexpectedException("undeclared checked exception", exception);
        }
    }

    public void unbind(Name name)
        throws RemoteException, NamingException
    {
        try
        {
            if(useNewInvoke)
            {
                super.ref.invoke(this, $method_unbind_6, new Object[] {
                    name
                }, 0x71794c52d565da8fL);
            } else
            {
                RemoteCall remotecall = super.ref.newCall(this, operations, 6, 0x5b4f90de7a473f79L);
                try
                {
                    ObjectOutput objectoutput = remotecall.getOutputStream();
                    objectoutput.writeObject(name);
                }
                catch(IOException ioexception)
                {
                    throw new MarshalException("error marshalling arguments", ioexception);
                }
                super.ref.invoke(remotecall);
                super.ref.done(remotecall);
            }
        }
        catch(RuntimeException runtimeexception)
        {
            throw runtimeexception;
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(NamingException namingexception)
        {
            throw namingexception;
        }
        catch(Exception exception)
        {
            throw new UnexpectedException("undeclared checked exception", exception);
        }
    }

    private static final Operation operations[] = {
        new Operation("void bind(javax.naming.Name, java.lang.Object, java.lang.String)"), new Operation("javax.naming.Context createSubcontext(javax.naming.Name)"), new Operation("long getIndetity()"), new Operation("java.util.Collection list(javax.naming.Name)"), new Operation("java.lang.Object lookup(javax.naming.Name)"), new Operation("void rebind(javax.naming.Name, java.lang.Object, java.lang.String)"), new Operation("void unbind(javax.naming.Name)")
    };
    //private static final long interfaceHash = 0x5b4f90de7a473f79L;
    private static final long serialVersionUID = 2L;
    private static boolean useNewInvoke;
    private static Method $method_bind_0;
    private static Method $method_createSubcontext_1;
    private static Method $method_getIndetity_2;
    private static Method $method_list_3;
    private static Method $method_lookup_4;
    private static Method $method_rebind_5;
    private static Method $method_unbind_6;

    static 
    {
        try
        {
            (java.rmi.server.RemoteRef.class).getMethod("invoke", new Class[] {
                java.rmi.Remote.class, java.lang.reflect.Method.class, java.lang.Object[].class, Long.TYPE
            });
            useNewInvoke = true;
            $method_bind_0 = (Naming.class).getMethod("bind", new Class[] {
                javax.naming.Name.class, java.lang.Object.class, java.lang.String.class
            });
            $method_createSubcontext_1 = (Naming.class).getMethod("createSubcontext", new Class[] {
                javax.naming.Name.class
            });
            $method_getIndetity_2 = (Naming.class).getMethod("getIndetity", new Class[0]);
            $method_list_3 = (Naming.class).getMethod("list", new Class[] {
                javax.naming.Name.class
            });
            $method_lookup_4 = (Naming.class).getMethod("lookup", new Class[] {
                javax.naming.Name.class
            });
            $method_rebind_5 = (Naming.class).getMethod("rebind", new Class[] {
                javax.naming.Name.class, java.lang.Object.class, java.lang.String.class
            });
            $method_unbind_6 = (Naming.class).getMethod("unbind", new Class[] {
                javax.naming.Name.class
            });
        }
        catch(NoSuchMethodException _ex)
        {
            useNewInvoke = false;
        }
    }
}