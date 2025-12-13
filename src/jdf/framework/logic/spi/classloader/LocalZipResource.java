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
 * $Id: LocalZipResource.java,v 1.1.1.1 2005/01/30 13:43:48 Exp $
 *-----------------------------------------------------------------------------
 */


package jdf.framework.logic.spi.classloader;




// io packages
import java.io.InputStream;

// io exceptions
import java.io.IOException;
import java.io.FileNotFoundException;

// zip packages
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

/**
 * <P>A <CODE>Resource</CODE> that is an entry in 
 * a specified zip file on the local machine.  The zip file is represented by a 
 * <CODE>ClassPathEntry</CODE>, and the filename is specified by a String.
 *
 * @author Kristen Pol, Lutris Technologies
 * @version $Revision : 1.1 $
 * @see java.util.zip.ZipFile
 * @see java.util.zip.ZipEntry
 */
public class LocalZipResource extends Resource {

    // private data members

    /** The ZipEntry that represents this resource. */
    private ZipEntry zipEntry = null;

    // constructors

    /**
     * Constructs local zip file resource with specified name and location.
     *
     * @param name The file name of the resource.
     * @param location The location of the resource.
     * @exception FileNotFoundException if the desired file does not exist or
     *         does not have read permission.
     * @see Resource
     * @see ClassPathEntry
     */
    protected LocalZipResource(String name, ClassPathEntry location) throws FileNotFoundException 
    {
        super(name, location);
        ZipFile zipFile = location.getZipFile();
        
        if (zipFile == null) 
        {
            throw new FileNotFoundException( "There is no zip file associated with resource: "
                                                 + location);
        }
        
        try 
        {
            zipEntry = zipFile.getEntry(name);
            if (zipEntry == null) 
            {
                throw new FileNotFoundException("Entry, " + name
                                                        + ", does not exist in zip "
                                                        + "file, " + zipFile);
            }

            size = zipEntry.getSize();
            lastModifiedTime = zipEntry.getTime();

        }
        catch (IOException e) 
        {
            throw new FileNotFoundException("Entry, " + name
                                                + ", does not exist in zip file, "
                                                + zipFile + ", or is " + "corrupt: "
                                                + e.getMessage());
        }
    }
    
    // public methods
    
    /**
     * Gets input stream representing resource.
     *
     * @return the input stream that represents the resource.
     * @exception IOException if the input stream can not be constructed.
     * @see InputStream
     */
    public InputStream getInputStream() throws IOException 
    {
        ZipFile zipFile = location.getZipFile();
        
        if (zipFile == null)
            throw new IOException("Failed to get zip file for location, should not be able to get here without a zip file");
        
        return zipFile.getInputStream(zipEntry);
    }

    /**
     * Get current last-modification time of resource.  This is the
     * time on the disk file the resource is associated with.
     *
     * @return the last-modified time of the permanent copy of the resource
     * in milliseconds.
     */
    public long getCurrentLastModifiedTime() throws FileNotFoundException 
    {
        return zipEntry.getTime();
    }
}