/*-----------------------------------------------------------------------------
 * Enhydra Java Application Server
 * Copyright 1997-2000 Lutris Technologies, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 * 3. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *      This product includes Enhydra software developed by Lutris
 *      Technologies, Inc. and its contributors.
 * 4. Neither the name of Lutris Technologies nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY LUTRIS TECHNOLOGIES AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL LUTRIS TECHNOLOGIES OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *-----------------------------------------------------------------------------
 * $Id: Resource.java,v 1.1.1.1 2005/01/30 13:43:48 Exp $
 *-----------------------------------------------------------------------------
 */




package jdf.framework.logic.spi.classloader;


// io packages
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

// io exceptions
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * <P>A resource that is a file existing on the local machine or a remote
 * machine.  The properties of a resource include file name, location, size,
 * and last-modified time.  The location of the resource is represented by a 
 * <CODE>ClassPathEntry</CODE> and is either a directory or a zip file.
 * The file name, described by a String, is relative to the specified location.
 * The resource size is the file size in bytes and the resource time is the 
 * last-modified time of the file in milliseconds.
 *
 * <P>This is an abstract class.  All subclasses must determine the size 
 * and last-modified time for the resource, and must implement the 
 * <CODE>getInputStream</CODE> method.
 *
 * <P>
 * Resources can be ASCII or binary files, such as text files, HTML files, 
 * Java source-code files, Java byte-code files, Java archive (JAR) files, and 
 * gif files.  Some examples resource file names are:
 *
 * <PRE>
 *      /users/kristen/text/schedule.txt
 *      /webserver/htdocs/index.html
 *      /jdk1.1.5/src/java/util/Enumeration.java
 *      /jdk1.1.5/classes/java/util/Enumeration.class
 *      /users/kristen/lutris/lutris.jar
 *      /webserver/htdocs/banner.gif
 * </PRE>
 *
 * Since the file names are relative to the location, the beginning slash ("/")
 * does NOT mean that the file name is given as the absolute path on its 
 * host machine.
 *
 * @author Kristen Pol, Lutris Technologies
 * @version $Revision : 1.1 $\
 */
public abstract class Resource {

    // protected data members

    /** The file name of the resource relative to the location. */
    protected String name = null;

    /** The location of the resource, which can be local or remote. */
    protected ClassPathEntry location = null;

    /** The size of the resource in bytes. */
    protected long size = -1;

    /** The last-modified time of the resource when this object was created. */
    protected long lastModifiedTime = -1;

    // constructors

    /**
     * Constructs resource with specified name, location and log channel.  
     * The file name must be described relative to the location.  The location, 
     * described by the <CODE>ClassPathEntry</CODE>, can be a directory 
     * or a zip file on the local or a remote machine.
     *
     * <P>Assumes that all slashes in the <CODE>name</CODE> parameter are 
     * forward slashes ("/");
     * 
     * @param name The file name of the resource.
     * @param location The location of the resource.
     * @exception NullPointerException if either the <CODE>name</CODE> or 
     *         <CODE>location</CODE> parameters are null.
     * @see ClassPathEntry
     */
    protected Resource(String name, ClassPathEntry location) throws NullPointerException 
    {
        if (name == null || location == null) 
        {
            throw new NullPointerException();
        }
        this.name = name;
        this.location = location;
    }

    // public methods

    /**
     * Gets file name of resource set previously by constructor.
     *
     * @return the file name of the resource represented by a 
     *         <CODE>String</CODE>.
     */
    public String getName() 
    {
	    return name;
    }
    
    /**
     * Gets location of resource set previously by constructor.
     *
     * @return the location of the resource represented by a 
     *         <CODE>ClassPathEntry</CODE>.
     */
    public ClassPathEntry getLocation() 
    {
	    return location;
    }
    
    /**
     * Stringifies resource described previously by constructor.  The 
     * the file name and location are concatenated together to represent 
     * the resource.
     *
     * @return the resource represented by a <CODE>String</CODE> composed of
     *         the file name and location.
     */
    public String toString() 
    {
	    return "[" + location + "][" + name + "]";
    }
    
    /**
     * Gets size of resource in bytes.
     *
     * <P>All subclasses are expected to determine this size prior to 
     * this method being called.
     *
     * @return the size of the resource in bytes.
     */
    public long getSize() 
    {
	    return size;
    }
    
    /**
     * Gets last-modification time of resource at the time this
     * Resource object was created.
     *
     * <P>All subclasses are expected to determine this time prior to 
     * this method being called.
     *
     * @return the last-modified time of the resource in milliseconds.
     */
    public long getLastModifiedTime() 
    {
	    return lastModifiedTime;
    }

    /**
     * Gets last-modified time of resource in milliseconds.
     *
     * @deprectated 
     * @see getlastModifiedTime
     */
    public long getTime() 
    {
	    return lastModifiedTime;
    }
    
    /**
     * Get current last-modification time of resource.  This is the
     * time on the disk file the resource is associated with.
     *
     * @return the last-modified time of the permanent copy of the resource
     * in milliseconds.
     */
    public abstract long getCurrentLastModifiedTime() throws FileNotFoundException;

    /**
     * Determine if the resource has been modified since it was loaded.
     *
     * @return <CODE>true</CODE> if the resource has been modified, 
     *     <CODE>false</CODE> if not.
     */
    public boolean hasBeenModified() throws FileNotFoundException 
    {
        long currentTime = getCurrentLastModifiedTime();
	    System.out.println("hasBeenModified: " + getName()
			  + ": current time=" + currentTime
			  + ", last time=" + lastModifiedTime);
        return (currentTime > lastModifiedTime);
    }

    /**
     * Get the resouce as a byte array when the size is known.
     *
     * @param inputStream Resource input stream
     * @return an array of bytes representing the resource.
     * @exception IOException if the byte array can not be constructed.
     */
    private byte[] getBytesKnowSize(InputStream inputStream) throws IOException 
    {
        byte[] bytes = new byte[(int)size];
        int bytesRead;

        // Even though the total size to read is known, there is
        // no guarantee that it's returned all in one read.
        int idx = 0;

        while (idx < size) 
        {
            if (inputStream.available() == 0) 
            {
                break;
            }
            if ((bytesRead = inputStream.read(bytes, idx, ((int)size)-idx)) == -1) 
            {
                break;
            }
            idx += bytesRead;
        }
            
        if (idx != size) 
        {
            throw new IOException("Read of resource expected " + size + " bytes, got " + idx + " bytes");
        }
        return bytes;
    }

    /**
     * Get the resouce as a byte array when the size is not known.
     *
     * @param inputStream Resource input stream
     * @return an array of bytes representing the resource.
     * @exception IOException if the byte array can not be constructed.
     */
    private byte[] getBytesUnknowSize(InputStream inputStream) throws IOException 
    {
        int bytesRead;
        byte[] byteBuffer = new byte[10240];
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        
        while (true) 
        {
            if (inputStream.available() == 0) 
            {
                break;
            }
            if ((bytesRead = inputStream.read(byteBuffer)) == -1) 
            {
                break;
            }
            byteStream.write(byteBuffer, 0, bytesRead);
        }

        return byteStream.toByteArray();
    }

    /**
     * Gets byte array representing resource.
     *
     * <P>This method relies on the implementation of 
     * <CODE>getInputStream</CODE>.
     *
     * @return an array of bytes representing the resource.
     * @exception IOException if the byte array can not be constructed.
     */
    public byte[] getBytes() throws IOException 
    {
	    InputStream inputStream = getInputStream();
	    
        if (inputStream == null) 
        {
            throw new IOException("Null InputStream for resource, " + this);
        }

        try 
        {
            if (size < 0) 
            {
                return getBytesUnknowSize(inputStream);
            }
            else 
            {
                return getBytesKnowSize(inputStream);
            }
        }
        finally 
        {
            inputStream.close();
        }
    }

    /**
     * Gets input stream representing resource.
     *
     * <P>This method is abstract and must be implemented by all subclasses.
     * The <CODE>getBytes</CODE> method also depends upon this implementation.
     *
     * @return the input stream that represents the resource.
     * @exception IOException if the input stream can not be constructed.
     */
    public abstract InputStream getInputStream() throws IOException;
    
    /**
     * Determines if the specified resource is equal to this resource.
     * Resources are considered equal if the file names, locations, sizes, 
     * and last-modified times are all the same.
     *
     * @return true if the resources are equal, false if not.
     */
    public boolean equals(Resource resource) 
    {
	    if (name.equals(resource.getName())
            && location.equals(resource.getLocation())
            && (size == resource.getSize())
            && (lastModifiedTime == resource.getLastModifiedTime())) 
        {
	        return true;
	    } 

	    return false;
    }
}