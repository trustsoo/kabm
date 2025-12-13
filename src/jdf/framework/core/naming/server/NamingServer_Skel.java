// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 2005-03-09 ���� 10:10:03
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 

package jdf.framework.core.naming.server;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.MarshalException;
import java.rmi.Remote;
import java.rmi.UnmarshalException;
import java.rmi.server.Operation;
import java.rmi.server.RemoteCall;
import java.rmi.server.Skeleton;
import java.rmi.server.SkeletonMismatchException;

import javax.naming.Name;

// Referenced classes of package anyframex.naming.server:
//            NamingServer

public final class NamingServer_Skel
    implements Skeleton
{

    public NamingServer_Skel()
    {
    }

    public void dispatch(Remote remote, RemoteCall remotecall, int i, long l)
        throws Exception
    {
        if(i < 0)
        {
            if(l == 0x78388d75267a5392L)
                i = 0;
            else
            if(l == 0x7aec96a3a0e3404cL)
                i = 1;
            else
            if(l == 0xf4b9358702e1c11fL)
                i = 2;
            else
            if(l == 0xd446363e48d84a96L)
                i = 3;
            else
            if(l == 0xf508f880b993d0a2L)
                i = 4;
            else
            if(l == 0x763a3b7e6c4ef72fL)
                i = 5;
            else
            if(l == 0x71794c52d565da8fL)
                i = 6;
            else
                throw new UnmarshalException("invalid method hash");
        } else
        if(l != 0x5b4f90de7a473f79L)
            throw new SkeletonMismatchException("interface hash mismatch");
        NamingServer namingserver = (NamingServer)remote;
        switch(i)
        {
        case 0: // '\0'
            Name name;
            Object obj;
            String s;
            try
            {
                ObjectInput objectinput4 = remotecall.getInputStream();
                name = (Name)objectinput4.readObject();
                obj = objectinput4.readObject();
                s = (String)objectinput4.readObject();
            }
            catch(IOException ioexception11)
            {
                throw new UnmarshalException("error unmarshalling arguments", ioexception11);
            }
            catch(ClassNotFoundException classnotfoundexception4)
            {
                throw new UnmarshalException("error unmarshalling arguments", classnotfoundexception4);
            }
            finally
            {
                remotecall.releaseInputStream();
            }
            namingserver.bind(name, obj, s);
            try
            {
                remotecall.getResultStream(true);
            }
            catch(IOException ioexception5)
            {
                throw new MarshalException("error marshalling return", ioexception5);
            }
            break;

        case 1: // '\001'
            Name name1;
            try
            {
                ObjectInput objectinput = remotecall.getInputStream();
                name1 = (Name)objectinput.readObject();
            }
            catch(IOException ioexception6)
            {
                throw new UnmarshalException("error unmarshalling arguments", ioexception6);
            }
            catch(ClassNotFoundException classnotfoundexception)
            {
                throw new UnmarshalException("error unmarshalling arguments", classnotfoundexception);
            }
            finally
            {
                remotecall.releaseInputStream();
            }
            javax.naming.Context context = namingserver.createSubcontext(name1);
            try
            {
                ObjectOutput objectoutput = remotecall.getResultStream(true);
                objectoutput.writeObject(context);
            }
            catch(IOException ioexception1)
            {
                throw new MarshalException("error marshalling return", ioexception1);
            }
            break;

        case 2: // '\002'
            remotecall.releaseInputStream();
            long l1 = namingserver.getIndetity();
            try
            {
                ObjectOutput objectoutput1 = remotecall.getResultStream(true);
                objectoutput1.writeLong(l1);
            }
            catch(IOException ioexception2)
            {
                throw new MarshalException("error marshalling return", ioexception2);
            }
            break;

        case 3: // '\003'
            Name name2;
            try
            {
                ObjectInput objectinput1 = remotecall.getInputStream();
                name2 = (Name)objectinput1.readObject();
            }
            catch(IOException ioexception7)
            {
                throw new UnmarshalException("error unmarshalling arguments", ioexception7);
            }
            catch(ClassNotFoundException classnotfoundexception1)
            {
                throw new UnmarshalException("error unmarshalling arguments", classnotfoundexception1);
            }
            finally
            {
                remotecall.releaseInputStream();
            }
            java.util.Collection collection = namingserver.list(name2);
            try
            {
                ObjectOutput objectoutput2 = remotecall.getResultStream(true);
                objectoutput2.writeObject(collection);
            }
            catch(IOException ioexception3)
            {
                throw new MarshalException("error marshalling return", ioexception3);
            }
            break;

        case 4: // '\004'
            Name name3;
            try
            {
                ObjectInput objectinput2 = remotecall.getInputStream();
                name3 = (Name)objectinput2.readObject();
            }
            catch(IOException ioexception8)
            {
                throw new UnmarshalException("error unmarshalling arguments", ioexception8);
            }
            catch(ClassNotFoundException classnotfoundexception2)
            {
                throw new UnmarshalException("error unmarshalling arguments", classnotfoundexception2);
            }
            finally
            {
                remotecall.releaseInputStream();
            }
            Object obj1 = namingserver.lookup(name3);
            try
            {
                ObjectOutput objectoutput3 = remotecall.getResultStream(true);
                objectoutput3.writeObject(obj1);
            }
            catch(IOException ioexception4)
            {
                throw new MarshalException("error marshalling return", ioexception4);
            }
            break;

        case 5: // '\005'
            Name name4;
            Object obj2;
            String s1;
            try
            {
                ObjectInput objectinput5 = remotecall.getInputStream();
                name4 = (Name)objectinput5.readObject();
                obj2 = objectinput5.readObject();
                s1 = (String)objectinput5.readObject();
            }
            catch(IOException ioexception12)
            {
                throw new UnmarshalException("error unmarshalling arguments", ioexception12);
            }
            catch(ClassNotFoundException classnotfoundexception5)
            {
                throw new UnmarshalException("error unmarshalling arguments", classnotfoundexception5);
            }
            finally
            {
                remotecall.releaseInputStream();
            }
            namingserver.rebind(name4, obj2, s1);
            try
            {
                remotecall.getResultStream(true);
            }
            catch(IOException ioexception9)
            {
                throw new MarshalException("error marshalling return", ioexception9);
            }
            break;

        case 6: // '\006'
            Name name5;
            try
            {
                ObjectInput objectinput3 = remotecall.getInputStream();
                name5 = (Name)objectinput3.readObject();
            }
            catch(IOException ioexception10)
            {
                throw new UnmarshalException("error unmarshalling arguments", ioexception10);
            }
            catch(ClassNotFoundException classnotfoundexception3)
            {
                throw new UnmarshalException("error unmarshalling arguments", classnotfoundexception3);
            }
            finally
            {
                remotecall.releaseInputStream();
            }
            namingserver.unbind(name5);
            try
            {
                remotecall.getResultStream(true);
            }
            catch(IOException ioexception)
            {
                throw new MarshalException("error marshalling return", ioexception);
            }
            break;

        default:
            throw new UnmarshalException("invalid method number");
        }
    }

	/**
	 * 
	 * @uml.property name="operations"
	 */
	public Operation[] getOperations() {
		return (Operation[]) operations.clone();
	}

    private static final Operation operations[] = {
        new Operation("void bind(javax.naming.Name, java.lang.Object, java.lang.String)"), new Operation("javax.naming.Context createSubcontext(javax.naming.Name)"), new Operation("long getIndetity()"), new Operation("java.util.Collection list(javax.naming.Name)"), new Operation("java.lang.Object lookup(javax.naming.Name)"), new Operation("void rebind(javax.naming.Name, java.lang.Object, java.lang.String)"), new Operation("void unbind(javax.naming.Name)")
    };
    //private static final long interfaceHash = 0x5b4f90de7a473f79L;

}