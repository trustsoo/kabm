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
 * $Id: ClassPathEntry.java,v 1.1.1.1 2005/01/30 13:43:48 Exp $
 *-----------------------------------------------------------------------------
 */
 


package jdf.framework.logic.spi.classloader;


// net packages
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipFile;

/**
 * <P><H3>Summary:</H3>
 * <P>A class path entry which is a URL representing either a local or a 
 * remote directory or zip file. Manages all the details for zip file support.
 * For example, local zip files (once opened) are kept open for the lifetime 
 * of the entry for increased performance.
 *
 * <P><H3>Features:</H3>
 * <UL>
 * <LI>Supports absolute and relative file names
 * <LI>Creates URL entry from given String, File, or URL
 * <LI>Adds "/" to end of URL for directories if necessary
 * <LI>Knows if URL is zip local or remote
 * <LI>Knows if URL is a zip file or directory
 * <LI>Keeps ZipFile open if the zip file is local 
 * </UL>
 * 
 * <P><B>Note: "zip files" are files with ".zip" or ".jar" extensions.</B>
 *
 * <P><H3>Class Path Entries:</H3>
 * <P>Example valid class path entries are:
 * <PRE>
 *     <EM>Files and directories on the local file system</EM>
 *     ../../java/classes
 *     /users/kristen/java/classes
 *     /users/kristen/java/classes/
 *     /users/kristen/java/zipfiles/MyClasses.zip
 *     /users/kristen/java/jarfiles/MyClasses.jar
 *     file:///users/kristen/java/classes
 *     file://localhost/users/kristen/java/classes
 *     <BR>
 *     <EM>Files and directories on a remote file system 
 *         (must be in URL format)</EM>
 *     ftp://www.foo.com/pub/java/classes
 *     file://www.foo.com/pub/java/classes/
 *     http://www.foo.com/web/java/classes/
 *     file://www.foo.com:8080/pub/java/zipfiles/MyClasses.zip
 *     http://www.foo.com:8080/web/java/jarfiles/MyClasses.jar
 * </PRE>
 *
 * <P>Note that the location of the entry includes the protocol, the host name, 
 * and the port while the file name is everything else.  For example,
 *
 * <PRE>
 *     http://www.foo.com:8080/web/java/jarfiles/MyClasses.jar
 * </PRE>
 * has the form [location][name] or
 * <PRE>
 *     [http://www.foo.com:8080/][/web/java/jarfiles/MyClasses.jar]
 * </PRE>
 * so the location is "http://www.foo.com:8080/" and the name is
 * "/web/java/jarfiles/MyClasses.jar".
 *
 * <P>Note that the two references
 * <PRE>
 *     /users/kristen/java/classes/
 *     file:///users/kristen/java/classes/
 * </PRE>
 * represent the same directory on a Unix machine, and
 * <PRE>
 *     C:/windows/java/classes/
 *     file:///C:/windows/java/classes/
 * </PRE>
 * are equivalent directories on a Windows box.
 *
 * <P>But the two references
 * <PRE>
 *     /users/kristen/java/classes/
 *     file://monet.lutris.com/users/kristen/java/classes/
 * </PRE>
 * are not equivalent even if the directory 
 * <EM>/users/kristen/java/classes/</EM> lives
 * on the machine named <EM>monet.lutris.com</EM> and all development
 * is on this machine.  Why?  Because the web (browser?) protocol is different
 * for URLs with host information and those without.  If no host is 
 * specified, the file is assumed to be on the local machine and the
 * path is determined from the <EM>ROOT</EM> of the machine.  If the
 * host is specified, then the <EM>ftp protocol</EM> is used and the path
 * is determined from the <EM>ftp ROOT</EM> (e.g. /users/ftp/) rather
 * than the machine's ROOT.  Thus, on a machine that support's anonymous
 * ftp, the following two directories are the same:
 * <PRE>
 *     /users/ftp/pub/classes/
 *     file://picasso.lutris.com/pub/classes/
 * </PRE>
 * assuming the development is being done on <EM>picasso.lutris.com</EM>.
 *
 * <H3>System Class Path Entries</H3>
 *
 * <P>The system class path is the system-dependent path of directories
 * and files (e.g. CLASSPATH on Unix and Windows) used by the system
 * class loader to load classes.  Valid system class path entries are 
 * directories and zip files, specified by absolute path or relative path 
 * on the system.  Any valid system class path entry is also valid to 
 * create a <CODE>ClassPathEntry</CODE>.
 *
 * <H3>Example</H3>
 *
 * <P>Here is an example of how to use a ClassPathEntry:
 * <PRE>
 *     ClassPathEntry entry = new ClassPathEntry("/home/java/Test.jar");
 *     System.out.println("My entry URL is " + entry.get());
 *     System.out.println("My entry string is " + entry.toString());
 *     System.out.println("My entry name " + entry.getName());
 *     System.out.println("My entry location " + entry.getLocation());
 *     System.out.println("My entry is a zip file? " + entry.isZipFile());
 *     Resource resource = entry.getResource("foo.gif");
 * </PRE>
 *
 * @author Kristen Pol, Lutris Technologies
 * @version $Revision : 1.0 $
 * @see java.net.URL
 */
public class ClassPathEntry 
{
    /* 
     * This could be made into an abstract class and be subclassed by 
     * LocalDirEntry, LocalZipEntry, RemoteDirEntry, and RemoteZipEntry.
     */

    // private data members

    /** The class path entry represented by a URL. */
    private URL entryURL = null;

    /** The ZipFile for this class path entry, if appropriate. */
    private ZipFile zipFile = null;
    
    // constructors

    /**
     * Constructs class path entry with specified String.  
     * The String is assumed to be either a directory or zip file on 
     * the local machine or a remote machine, which can be represented by the 
     * absolute file name, the relative file name, or a URL.
     *
     * <P>Examples:
     * <PRE>
     *     ClassPathEntry("../../java/classes");
     *     ClassPathEntry("/users/kristen/java/classes");
     *     ClassPathEntry("/users/kristen/java/classes/");
     *     ClassPathEntry("/users/kristen/java/MyClasses.zip");
     *     ClassPathEntry("/users/kristen/java/MyClasses.jar");
     *     ClassPathEntry("ftp://www.foo.com/pub/classes/");
     *     ClassPathEntry("file://www.foo.com/web/classes/");
     *     ClassPathEntry("http://www.foo.com/web/classes/");
     *     ClassPathEntry("http://www.foo.com:8080/web/classes/");
     *     ClassPathEntry("http://www.foo.com/web/classes/MyClasses.jar");
     * </PRE>
     *
     * @param entry The class path entry.
     */
    public ClassPathEntry(String entry) {
        this(convertEntryToURL(entry));
    }

    /**
     * Constructs class path entry with specified File.
     * The File can represent a directory or zip file on the local machine.
     *
     * <P>Examples:
     * <PRE>
     *     ClassPathEntry(new File("../../java/classes"));
     *     ClassPathEntry(new File("/users/kristen/java/classes"));
     *     ClassPathEntry(new File("/users/kristen/java/classes/"));
     *     ClassPathEntry(new File("/users/kristen/java/MyClasses.zip"));
     *     ClassPathEntry(new File("/users/kristen/java/MyClasses.jar"));
     * </PRE>
     *
     * @param entry The class path entry.
     */
    public ClassPathEntry(File entry) {
        this(convertEntryToURL(entry));
    }

    /**
     * Constructs class path entry with specified URL.  
     * The URL can represent a directory or zip file on the local machine 
     * or a remote machine.
     *
     * <P>Examples:
     * <PRE>
     *     ClassPathEntry(new URL("ftp://www.foo.com/pub/classes/"));
     *     ClassPathEntry(new URL("file://www.foo.com/web/classes/"));
     *     ClassPathEntry(new URL("http://www.foo.com/web/classes/"));
     *     ClassPathEntry(new URL("http://www.foo.com:8080/web/classes/"));
     *     ClassPathEntry(new URL("http://www.foo.com/web/MyClasses.jar"));
     * </PRE>
     *
     * @param entry The class path entry.
     */
    public ClassPathEntry(URL entry) {
	/* 
	 * This constructor actually does all the work, because all 
	 * other constructors call this one.
	 */
        try {
            this.entryURL = new URL(cleanUpURL(entry).toString());
        } catch (MalformedURLException mue) {
            System.out.println("Illegal class path entry: " + entry);
            this.entryURL = null;
        }
    }

    // public methods

    /**
     * Gets class path entry set previously by constructor.
     *
     * @return the class path entry represented by a URL.
     */
    public URL getURL() {
	    return entryURL;
    }

    /**
     * Gets file name of class path entry.  For example, if the entry is 
     * "http://www.foo.com:8080/java/classes/MyClasses.jar", the name is 
     * "/java/classes/MyClasses.jar".  The beginning slash does not mean
     * that its an absolute file name on its host machine.  The file name 
     * is always <EM>relative</EM> to the location.
     *
     * @return the file name of the class path entry.
     */
    public String getName() {
	// XXX: Don't use a beginning slash? (kp)
	    return entryURL.getFile();
    }

    /**
     * Gets location of class path entry.  For example, if the entry is 
     * "http://www.foo.com:8080/java/classes/MyClasses.jar", the location is 
     * "http://www.foo.com:8080/".
     *
     * @return the file name for the class path entry.
     */
    public String getLocation() {
        if (entryURL.getPort() != -1) {
            return entryURL.getProtocol() + "://" + entryURL.getHost() + ":" +
               entryURL.getPort() + "/";
        } else {
            return entryURL.getProtocol() + "://" + entryURL.getHost() + "/";
        }
    }
    
    /**
     * Stringifies class path entry set previously by constructor.
     *
     * @return the class path entry represented by a stringified URL.
     */
    public String toString() {
        if (entryURL != null) {
            return entryURL.toString();
        } else {
            return "null";
        }
    }

    /**
     * Gets resource with the specified name from class path entry.  
     * The result will be null if the resource can not be found.
     *
     * @param name The file name associated with the resource.
     * @return the resource from the class path entry, or null if it is 
     *         not found.
     * @see Resource
     */
    public Resource getResource(String name) {
	    name = convertSlashes(name);
        // FIXME: This is rather inefficient, as it creates a
        // resource and throws an exception just to check a path
        // entry.  Probably need to have a ClassPathEntry per resource
        // type .
	    Resource resource = null;
        try {
            if (isDirectory() && isLocal()) {
                resource = new LocalDirResource(name, this);
            } else if (isZipFile() && isLocal()) {
                resource = new LocalZipResource(name, this);
            } else if (isDirectory() && isRemote()) {
                // FIXME: Test RemoteDirResource, then add (kp)
                // resource = new RemoteDirResource(name, this);
                System.out.println("Cannot get remote directory resource.");
                resource = null;
            } else if (isZipFile() && isRemote()) {
                // FIXME: Test RemoteZipResource, then add (kp)
                // resource = new RemoteZipResource(name, this);
                System.out.println("Cannot get remote jar file resource.");
                resource = null;
            }
        } catch (FileNotFoundException e) {
            //없지만 무조건 LocalDirResource나 LocalZipResource를 하고 본다.
            //그리고, 원하는 name이 존재하지 않거나, 에러나면, FileNotFoundException를 생성할때 발생하므로,
            //이곳으로 흘러온다. null를 리턴한다.
            //System.out.println("File not found: " + name + ": " + e.getClass().getName());
            resource = null;
        }
        return resource;
    }

    /**
     * Determines if class path entry is a zip file.  Anything ending 
     * in ".zip" or ".jar" is considered a zip file.
     *
     * @return true if the class path entry is a zip file, false if it is not.
     */
    public boolean isZipFile() {
	    return (toString().endsWith(".jar") || toString().endsWith(".zip"));
    }

    /**
     * Determines if class path entry is a directory.  Anything that is 
     * not a zip file is considered a directory.
     *
     * @return true if the class path entry is a directory, false if it is not.
     * @see #isZipFile
     */
    public boolean isDirectory() {
	    return (!isZipFile());
    }
    
    /**
     * Determines if class path entry is local.  Anything that has
     * no host name or a host name of "localhost" is considered local. 
     *
     * @return true if the class path entry is remote, false if it is not.
     */
    public boolean isLocal() {
        String host = entryURL.getHost();
        if (host.equals("") || host.equalsIgnoreCase("localhost") || 
            host.equals("127.0.0.1")) {
            return true;
        }
	    return false;
    }

    /**
     * Determines if class path entry is remote. Anything that is not 
     * local is considered remote.
     *
     * @return true if the class path entry is remote, false if it is not.
     * @see #isLocal
     */
    public boolean isRemote() {
	    return (!isLocal());
    }

    /**
     * Determines if specified class path entry is equal to this entry. 
     * The entries are considered equal if the URLs are the same.
     *
     * @param cpe The class path entry to check for equality.
     * @return true if the class path entry is equal, false if it is not.
     */
    public boolean equals(Object o) {
        if (o instanceof ClassPathEntry) {
            return entryURL.equals(((ClassPathEntry)o).getURL());
        }
        return false;
    }

    /**
     * Gets zip file associated with class path entry if appropriate.  If 
     * a zip file is not found, null will be returned.
     *
     * @return the zip file associated with this class path entry if 
     *         found, null if not.
     */
    public ZipFile getZipFile() {
        if (zipFile == null) {
            return doGetZipFile();
        }
        return zipFile;
    }

    /**
     * Gets zip file associated with class path entry if appropriate.  If 
     * a zip file is not found, null will be returned.
     *
     * @return the zip file associated with this class path entry if 
     *         found, null if not.
     */
    private synchronized ZipFile doGetZipFile() {
        if (zipFile == null) {
            if (isZipFile() && isLocal()) {
            try {
                zipFile = new ZipFile(getName());
            } catch (IOException e) {
                System.out.println("Cannot create zip file " + getName() + ".");
                zipFile = null;
            }
            } else {
            zipFile = null;
            }
        }
        return zipFile;
    }
    // private helper methods

    /**
     * Cleans up URL by fixing slashes.  All back slashes are converted to 
     * forward slashes ("/"). A slash is added to the end of the URL if 
     * it is a directory and does not already have an ending slash.
     *
     * @param url The URL to clean up.
     * @return a cleaned up version of the URL.
     */
    private static URL cleanUpURL(URL url) {
        URL newURL = null;
        try {
                //FIXME: This code converts `.' to `/'.
            String urlString = url.toString();
            if (!urlString.endsWith("/") && !urlString.endsWith(".jar") &&
            !urlString.endsWith(".zip")) {
            newURL = new URL(convertSlashes(url.toString() + "/"));
            } else {
            newURL = new URL(convertSlashes(url.toString()));
            }
        } catch (MalformedURLException e) {
            // FIXME: Log. Cannot use logChannel because it's not static.
                // FIXME: should just throw exception..
            newURL = null;
        }
        return newURL;
    }

    /**
     * Converts Object to URL.  Only URLs, Files, and Strings 
     * can be successfully converted.  All other object types will 
     * result in a null return.
     *
     * @param object The object to convert.
     * @return the URL representing the object, or null if the conversion 
     *         was not successful.
     */
    private static URL convertEntryToURL(Object object) {
        if (object instanceof java.net.URL) {
            return (URL)object;
        } else if (object instanceof java.lang.String) {
            return convertEntryToURL((String)object);
        } else if (object instanceof java.io.File) {
            return convertEntryToURL(object.toString());
        } else {
            // FIXME: Log. Cannot use logChannel because it's not static.
                // FIXME: should just throw exception.
            return null;
        }
    }

    /**
     * Converts String to URL.
     *
     * @param string The string to convert.
     * @return the URL representation of the given string.
     */
    private static URL convertEntryToURL(String string) {
        try {
            return new URL(string);
        } catch (MalformedURLException e) {
             //URL로 표현이 되지 않을때
            try {
                    String absPath = (new File(string)).getAbsolutePath();
                    // Convert directory separator to "/", since only "/" is a
                    // legal separator in URLs
                    absPath = absPath.replace(File.separatorChar, '/');
                    // If an absolute path doesn't start with a '/', assume it's
                    // windows and add it.
                    if (!absPath.startsWith("/")) {
                        absPath = "/" + absPath;
            }
                    return new URL("file://" + absPath);
            } catch (MalformedURLException e2) {
            // FIXME: Log. Cannot use logChannel because it's not static.
            return null;
            }
        }
    }

    /**
     * Converts all system path separators to "/".  This is to accommodate 
     * both Windows and Unix for URL generation and searching zip files.
     *
     * @param string The string to convert.
     * @return the string with all slashes converted.
     */
    private static String convertSlashes(String string) {
        string.replace(File.separatorChar, '/');
        return string.replace('\\', '/');
    }
}