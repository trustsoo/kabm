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
 * $Id: ClassPath.java,v 1.1.1.1 2005/01/30 13:43:48 Exp $
 *-----------------------------------------------------------------------------
 */

package jdf.framework.logic.spi.classloader;

// util packages

import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;

// io packages
// net packages

/**
 * <P>A class path that is composed of <CODE>ClassPathEntry</CODE> objects.
 * This class can be used in conjunction with a class loader to load 
 * classes and resources.
 *
 * @author Kristen Pol, Lutris Technologies
 * @version $Revision : 1.0 $
 */
class ClassPath {
    
    // private data members

    /** The class path Vector made up of ClassPathEntry objects. */
    private Vector classPath = null;

    // constructors

    /**
     * Constructs empty class path with no entries.
     *
     * @see #set
     * @see #add
     */
    public ClassPath() 
    {
    	this((Vector)null);
    }

    /**
     * Constructs class path with specified class path entries.  The 
     * parameter is assumed to be an array of directories, URLs, and/or 
     * zip files.
     *
     * @param entries The class path represented by a String array.
     */
    public ClassPath(String[] entries) 
    {
	    this(convertArrayToVector(entries));
    }

    /**
     * Constructs class path with specified class path entries.  The 
     * parameter is assumed to be an array of zip files and/or directories.
     *
     * @param entries The class path represented by a File array.
     */
    public ClassPath(File[] entries) 
    {
	    this(convertArrayToVector(entries));
    }

    /**
     * Constructs class path with specified class path entries.  The URLs can
     * represent directories and/or zip files on the local machine and/or
     * on remote machines.
     *
     * @param entries The class path represented by a URL array.
     */
    public ClassPath(URL[] entries) 
    {
    	this(convertArrayToVector(entries));
    }

    // private helper constructors

    /**
     * Constructs class path with specified class path entries.
     * Vector entries must be <CODE>ClassPathEntry</CODE> objects.
     *
     * @param entries The class path entries.
     * @see ClassPathEntry
     */
    private ClassPath(Vector entries) 
    {
        /* 
         * This constructor actually does all the work, because all 
         * other constructors call this one.
         */
        classPath = new Vector();
	    set(entries);
    }

    // public methods

    /**
     * Sets class path with specified class path entries.  The 
     * parameter is assumed to be an array of directories, URLs, and/or 
     * zip files.
     *
     * @param entries The class path represented by a String array.
     */
    public void set(String[] entries) 
    {
    	set(convertArrayToVector(entries));
    }

    /**
     * Sets class path with specified class path entries.  The 
     * parameter is assumed to be an array of zip files and/or directories.
     *
     * @param entries The class path represented by a File array.
     */
    public void set(File[] entries) 
    {
	    set(convertArrayToVector(entries));
    }

    /**
     * Sets class path with specified class path entries.  The URLs can
     * represent directories and/or zip files on the local machine and/or
     * on remote machines.
     *
     * @param entries The class path represented by a URL array.
     */
    public void set(URL[] entries) 
    {
	    set(convertArrayToVector(entries));
    }

    /**
     * Adds specified class path entries to class path.  The 
     * parameter is assumed to be an array of directories, URLs, and/or 
     * zip files.
     *
     * @param entries The class path entries to add.
     */
    public void add(String[] entries) 
    {
	    add(convertArrayToVector(entries));
    }

    /**
     * Adds specified class path entries to class path.  The 
     * parameter is assumed to be an array of zip files and/or directories.
     *
     * @param entries The class path entries to add.
     */
    public void add(File[] entries) 
    {
	    add(convertArrayToVector(entries));
    }

    /**
     * Adds specified class path entries to class path.  The URLs can
     * represent directories and/or zip files on the local machine and/or
     * on remote machines.
     *
     * @param entries The class path entries to add.
     */
    public void add(URL[] entries) 
    {
	    add(convertArrayToVector(entries));
    }

    /**
     * Clears class path by removing all entries.
     * 
     * @see #set
     * @see #add
     */
    public void clear() 
    {
	    classPath.removeAllElements();
    }

    /**
     * Get the number of entries in the classpath.
     *
     * @return The length ot the class path.
     */
    public int getLength() 
    {
	    return classPath.size();
    }

    /**
     * Gets an Enumeration of class path entries.
     *
     * @return an Enumeration of ClassPathEntry objects.
     * @see ClassPathEntry
     * @see #set
     */
    public Enumeration getPath() 
    {
	    return classPath.elements();
    }

    /**
     * Gets resource represented by specified file name.  The class path 
     * entries are searched in order to find the desired resource.  If the 
     * resource is not found in the class path, null is returned.
     *
     * @param name The file name of the resource.
     * @return the resource associated with the given file name, or null if 
     *         it can not be found.
     * @see Resource
     */
    public Resource getResource(String name) 
    {
        if (name == null) 
        {
            throw new NullPointerException("Null resource name passed to " + "getResource() for class path, " + this);
        }

        Resource resource = null;
        
        for (int i = 0; i < classPath.size(); i++) 
        {
            ClassPathEntry entry = null;
            entry = (ClassPathEntry)classPath.elementAt(i);
            //System.out.println("  checking: \"" + entry.getName() + "\"");
            resource = entry.getResource(name);

            if (resource != null) 
            {
                //System.out.println("  found: " + name);
                return resource;
            }
        }

        //System.out.println("  not found: " + name);
        return null;
    }

    
    // private helper methods
    
    /**
     * Sets class path with specified class path entries.  
     * Vector entries must be <CODE>ClassPathEntry</CODE> objects.
     * All null and duplicate entries are removed.
     *
     * @param entries The class path entries.
     * @see ClassPathEntry
     */
    private void set(Vector entries) 
    {
        /* 
         * This set method actually does all the work, 
         * since all the other set methods call this one.
         */
        classPath.removeAllElements();
        add(entries);
    }

    /**
     * Adds specified class path entries to class path.  
     * Vector entries must be <CODE>ClassPathEntry</CODE> objects.
     * All null and duplicate entries are removed.
     *
     * @param entries The class path entries.
     * @see ClassPathEntry
     */
    private void add(Vector entries) 
    {
        /* 
         * This add method actually does all the work, 
         * since all the other add methods call this one.
         */
        if (entries != null) {
            for (int i = 0; i < entries.size(); i++) {
            classPath.insertElementAt(entries.elementAt(i), i);
            }
        }
    }

    /**
     * Converts array of Objects to Vector.  Converts all array objects
     * to ClassPathEntry objects and removes nulls and duplicates.
     *
     * @param array The array to convert.
     * @return the Vector representation of the array.
     * @see ClassPathEntry
     */
    private static Vector convertArrayToVector(Object[] array) 
    {
        //FIXME: Is there really a need to support a nul array??
	    if (array != null) {
            Vector vector = new Vector();

            for (int i = 0; i < array.length; i++) 
            {
                Object object = array[i];
                ClassPathEntry entry = null;
                
                if (object instanceof String) 
                    entry = new ClassPathEntry((String)object);
                
                else if (object instanceof File)
                    entry = new ClassPathEntry((File)object);
                
                else if (object instanceof URL)
                    entry = new ClassPathEntry((URL)object);
                
                else
                    // This should not happen because the only public set 
                    // methods are for Strings, Files, and URLs
                    throw new ClassCastException("Type, " + object.getClass() + ", is not supported. " + "Expecting a String, File, or URL.");

                if (entry != null && ! vector.contains(entry)) 
                {
                    vector.addElement(entry);
                }
            }

    	    return vector;
	    }

	    return null;
    }
}