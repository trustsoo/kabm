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
 * $Id: LocalDirResource.java,v 1.1.1.1 2005/01/30 13:43:48 Exp $
 *-----------------------------------------------------------------------------
 */

package jdf.framework.logic.spi.classloader;





// io packages
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;

// io exceptions
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * <P>A <CODE>Resource</CODE> that is a file on the local machine in 
 * a specified directory.  The directory is represented by a 
 * <CODE>ClassPathEntry</CODE>, and the filename is specified by a String.
 *
 * @author Kristen Pol, Lutris Technologies
 * @version $Revision : 1.1 $
 * @see java.io.File
 */
public class LocalDirResource extends Resource {

    // private data members

    /** The file that represents this resource. */
    private File file = null;

    // constructors

    /**
     * Constructs local directory resource with specified name and location.
     *
     * @param name The file name of the resource.
     * @param location The location of the resource.
     * @exception FileNotFoundException if the desired file does not exist or 
     *         does not have read permission.
     * @see Resource
     * @see ClassPathEntry
     */
    protected LocalDirResource(String name, ClassPathEntry location) throws FileNotFoundException {
        super(name, location);
        String locName = location.getName();    //entryURL.getFile();
        if (locName == null) {
            throw new FileNotFoundException("The name for location, "
                                                + location + ", is null");
        }
        file = new File(locName, name);
        if (!file.exists() || !file.canRead()) {
            throw new FileNotFoundException("File, " + file
                                                + ", does not exist or does not "
                                                + "have read permission");
        }
        size = file.length();
        lastModifiedTime = file.lastModified();
    }

    // public methods

    /**
     * Gets input stream representing resource.
     *
     * @return the input stream that represents the resource.
     * @exception IOException if the input stream can not be constructed.
     * @see InputStream
     */
    public InputStream getInputStream() throws IOException {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new IOException(file.getName() + " is not found.");
        }
    }

    /**
     * Get current last-modification time of resource.  This is the
     * time on the disk file the resource is associated with.
     *
     * @return the last-modified time of the permanent copy of the resource
     * in milliseconds.
     */
    public long getCurrentLastModifiedTime() throws FileNotFoundException {
        return file.lastModified();
    }

    /**
     * Get the file associate with this resource.
     */
    public File getFile() {
        return file;
    }
}