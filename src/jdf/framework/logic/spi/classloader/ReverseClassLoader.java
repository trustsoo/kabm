package jdf.framework.logic.spi.classloader;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * 이 로더는 원하는 클래스의 객체만을 로드한다.
 * 기타 내부적으로 쓰이는 클래스 또는 인터페이스등은, 부모로더에 의해 로드된다.
 * 예를 들어 서버처럼 항상 동작중인 프로그램의 일부분의 클래스를 동적으로 교체 가능하게 하기 위해서는,
 * 클래스를 새로이 교체 시키고자 할때, new로써 새로이 생성하면 된다.
 *
 * 사용법:
 * <pre>
 * ReverseClassLoader loader = new ReverseClassLoader();
 * loader.setClassPath(".");
 * try {
 *     Class cls = loader.loadCls("test.HelloWorld");
 *     Object obj = cls.newInstance();
 * } catch (ClassNotFoundException e) {
 *     e.printStackTrace();
 * }
 * </pre>
 *
 * 특이사항: 
 * 이 소스내의 loadCls() 대신 loadClass()를 이용하여 로드한다면, ClassLoader의 메소드를 오버라이딩이 되면서, 
 * 위의 설명과 다르게, 이 ReverseClassLoader로 로드하려는 클래스내에 있는 여타 다른 클래스(new를 이용한 로드), 
 * 또는 구현한 인터페이스 또는 확장한 상위클래스등 모두 우선적으로 이 로더가 먼저 로드하려고 시도하고 실패한다면,
 * 부모로더(기본적으로 시스템로더)가 인계받아, 기본적인 클래스 로드 알고리즘(부모부터 거슬러 내려옴)에 의해 로드한다.
 * @author Kristen Pol, Lutris Technologies
 * @version 1.0
 */
public class ReverseClassLoader extends ClassLoader
{
    /**
     * Information kept about a loaded class.
     */
    public class ClassResource {
        /** Class object */
        private Class classObj;
        
        /** Resource were the class was obtained. */
        private Resource resource;
        
        /**
         * Constructor.
         */
        public ClassResource(Class classObj, Resource resource) {
            this.classObj = classObj;
            this.resource = resource;
        }
        
        /**
         * Get the class object.
         */
        public Class getClassObj() {
            return classObj;
        }

        /**
         * Get the resource.  Maybe null if the class was not loaded by
         * a ReverseClassLoader.
         */
        public Resource getResource() {
            return resource;
        }
    }

    private Hashtable loadedClasses = new Hashtable();

    private List urlList  = new ArrayList();

    private ClassPath classPath = new ClassPath();

    private ClassLoader parent;

    private List _prefix;

    /**
     * 한번이라도 loadCls()메소드를 호출했는지 여부:
     * 왜냐하면 loadCls()를 호출해도 현재 loadClass()를 오버로딩 시켜놓은 상태라서
     * loadCls()를 호출하는 본래의 목적인 원하는 클래스 하나만 이 로더를 이용하여
     * 로드하여야 하는데, 내 패스에 그 원하는 클래스내부의 어떤 클래스가 놓여있다면,
     * loadClass()가 그 내부 클래스를 시스템 로더가 아닌 이 로더를 이용하여 로드하므로, 
     * 목적이 틀려진다. 그것을 방지하기 위하여 내부적으로 loadCls()를 한번이라도 호출하였다면,
     * 이 로더는 게속 그 목적에 맞게 이용 되어져야만 한다.
     */
    private boolean isUse_loadCls;

    public ReverseClassLoader()
    {
        super();
        parent = ClassLoader.getSystemClassLoader();
    }

    public ReverseClassLoader(ClassLoader parent)
    {
        this.parent = parent;
    }





    /**
     * 어떤 특별한 팩키지명을 가진 클래스만 이 로더를 이용하여 읽고자 할때 설정하고 loadClass를 이용하여 로드한다.
     * 가능하면, 로드하려는 클래스들은 패스상에서 leaf에 해당되면 좋다.
     */
    public synchronized void setPackagePrefix(String prefix)
    {
        /**
         * 이것을 사용한 경우는 특별히 로드하려는 클래스 내부에 inner클래스가 존재할경우,
         * 그 inner 클래스도 같은 로더로 로드하여야 나중에 재로드 할때 에러가 발생하지 않았다.
         * 처음읽을때는 부모(시스템) 로더로 읽어도 잘 동작하였는데, 클래스에 변경이 생겨 재로드 하였을 때,
         * java.lang.LinkageError에러가 발생하였다..
         * 아직 이유는 잘 모르겠다.
         */
        List temp = new ArrayList();
        temp.add(prefix);

        setPackagePrefix( temp );
    }

    public synchronized void addPackagePrefix(String prefix)
    {
        if (_prefix == null)
            setPackagePrefix(prefix);
        else
            _prefix.add(prefix);
    }

    /**
     * 어떤 특별한 팩키지명을 가진 클래스만 이 로더를 이용하여 읽고자 할때 설정하고 loadClass를 이용하여 로드한다.
     * 가능하면, 로드하려는 클래스들은 패스상에서 leaf에 해당되면 좋다.
     */
    public synchronized void setPackagePrefix(List list)
    {
        /**
         * 이것을 사용한 경우는 특별히 로드하려는 클래스 내부에 inner클래스가 존재할경우,
         * 그 inner 클래스도 같은 로더로 로드하여야 나중에 재로드 할때 에러가 발생하지 않았다.
         * 처음읽을때는 부모(시스템) 로더로 읽어도 잘 동작하였는데, 클래스에 변경이 생겨 재로드 하였을 때,
         * java.lang.LinkageError에러가 발생하였다..
         * 아직 이유는 잘 모르겠다.
         */
        if (list == null)
            throw new IllegalArgumentException();

        this._prefix = list;
    }

    public Class loadCls(String name) throws ClassNotFoundException
    {
        return this.loadCls(name, true);
    }
    
    /**
     * loadClass()와 다르게 로드하고자 하는 클래스만 이 로더를 이용하여 로드하고, 나머지 내부에 쓰이는 클래스 및
     * 기타 상속한 클래스, 구현한 인터페이스등 무조건 부모로더(기본적으로 시스템로더)로 로드한다.
     * @see loadClass
     */
    public Class loadCls(String name, boolean resolve) throws ClassNotFoundException
    {
        isUse_loadCls = true;

        Class cl = null;
        try
        {
            //허용되는 팩키지 경로
            if ( _prefix != null )
            {
                for (int i = 0 ; i < _prefix.size() ; i++ )
                {
                    String prefix = (String) _prefix.get(i);

                    if (prefix != null && prefix.length() > 0 && prefix.length() < name.length() )
                    {
                        String temp = name.substring(0, prefix.length());
                        if ( temp.equals(prefix) )
                        {
                            cl = loadClassResource(name, resolve).getClassObj();
                        }
                    }
                }
            }
            else
            {
                //_prefix가 null일때
                cl = loadClassResource(name, resolve).getClassObj();
            }     	
        }
        catch (ClassNotFoundException e)
        {
            cl = null;
        }

        if (cl != null)
            return cl;
        else
            return parent.loadClass(name);
    }


    /**
     * ReverseClassLoader로 로드하려는 클래스내에 있는 여타 다른 클래스(new를 이용한 로드), 
     * 또는 구현한 인터페이스 또는 확장한 상위클래스등 모두 우선적으로 이 로더가 먼저 로드하려고 시도하고 실패한다면,
     * 부모로더(기본적으로 시스템로더)가 인계받아, 기본적인 클래스 로드 알고리즘(부모부터 거슬러 내려옴)에 의해 로드한다.
     * @see loadCls
     */

    public Class loadClass(String name, boolean resolve) throws ClassNotFoundException
    {
        if (isUse_loadCls)
        {
            return parent.loadClass(name);
        }
        else
        {
            Class cl = null;
            try
            {
                //허용되는 팩키지 경로
                if ( _prefix != null )
                {
                    for (int i = 0 ; i < _prefix.size() ; i++ )
                    {
                        String prefix = (String) _prefix.get(i);

                        if (prefix != null && prefix.length() > 0 && prefix.length() < name.length() )
                        {
                            String temp = name.substring(0, prefix.length());
                            if ( temp.equals(prefix) )
                            {
                                cl = loadClassResource(name, resolve).getClassObj();
                            }
                        }
                    }
                }
                else
                {
                    //_prefix가 null일때
                    cl = loadClassResource(name, resolve).getClassObj();
                }  
            }
            catch (ClassNotFoundException e)
            {
                cl = null;
            }

            if (cl != null)
                return cl;
            else
                return parent.loadClass(name);
        }
    }
    

    /**
     * Sets class loader with specified class path.  The parameter is
     * assumed to be either a directory, URL, or zip file.
     *
     * @param path The class path to be used when loading classes.
     */
    public void setClassPath(String path) 
    {
	    setClassPath(new String[] {path});
    }

    /**
     * Sets class loader with specified class path.  The parameter is
     * assumed to be an array of directories, URLs, and/or zip files.
     *
     * @param path The class path to be used when loading classes.
     */
    public synchronized void setClassPath(String[] path) 
    {
	    classPath.set(path);
    }

    /**
     * Sets class loader with specified class path.  The parameter is
     * assumed to be either a zip file or directory.
     *
     * @param path The class path to be used when loading classes.
     */
    public void setClassPath(File path) 
    {
	    setClassPath(new File[] {path});
    }

    /**
     * Sets class loader with specified class path.  The parameter is
     * assumed to be an array of zip files and/or directories.
     *
     * @param path The class path to be used when loading classes.
     */
    public synchronized void setClassPath(File[] path) 
    {
	    classPath.set(path);
    }

    /**
     * Sets class loader with specified class path.  The parameter is
     * represent a directory or zip file on the local machine or a 
     * remote machine.
     *
     * @param path The class path to be used when loading classes.
     */
    public void setClassPath(URL path) 
    {
	    setClassPath(new URL[] {path});
    }

    /**
     * Sets class loader with specified class path.  The parameter is
     * represent directories and/or zip files on the local machine and/or  
     * on remote machines.
     *
     * @param path The class path to be used when loading classes.
     */
    public synchronized void setClassPath(URL[] path) 
    {
	    classPath.set(path);
    }

    /**
     * Adds specified class path to beginning of existing path.  
     * The parameter is
     * assumed to be either a directory, URL, or zip file.
     *
     * @param path The class path to be added to current class path.
     */
    public void addClassPath(String path) 
    {
	    addClassPath(new String[] {path});
    }

    /**
     * Adds specified class path to beginning of existing path.  
     * The parameter is
     * assumed to be an array of directories, URLs, and/or zip files.
     *
     * @param path The class path to be added to current class path.
     */
    public synchronized void addClassPath(String[] path) 
    {
	    classPath.add(path);
    }

    /**
     * Adds specified class path to beginning of existing path.  
     * The parameter is assumed to be either a zip file or directory.
     *
     * @param path The class path to be added to current class path.
     */
    public void addClassPath(File path) 
    {
	    addClassPath(new File[] {path});
    }

    /**
     * Adds specified class path to beginning of existing path.  
     * The parameter is
     * assumed to be an array of zip files and/or directories.
     *
     * @param path The class path to be added to current class path.
     */
    public synchronized void addClassPath(File[] path) 
    {
	    classPath.add(path);
    }

    /**
     * Adds specified class path to beginning of existing path.  
     * The parameter is
     * represent a directory or zip file on the local machine or a 
     * remote machine.
     *
     * @param path The class path to be added to current class path.
     */
    public void addClassPath(URL path) 
    {
	    addClassPath(new URL[] {path});
    }

    /**
     * Adds specified class path to beginning of existing path.  
     * The parameter is
     * represent directories and/or zip files on the local machine and/or  
     * on remote machines.
     *
     * @param path The class path to be added to current class path.
     */
    public synchronized void addClassPath(URL[] path) 
    {
	    classPath.add(path);
    }

    /**
     * Clears class path entries.
     * 
     * @see #setClassPath
     */
    public synchronized void clearClassPath() 
    {
	    classPath.clear();
    }

    /**
     * Gets class path for class loader defined previously by constructor and
     * <CODE>setClassPath</CODE>/<CODE>addClassPath</CODE> methods.
     *
     * @return the class path represented by an <CODE>Enumeration</CODE> 
     *         of URL objects.
     * @see #setClassPath
     * @see #addClassPath
     * <!-- FIXME: should really return a format that can be passed
     *      to another class loader, like an array -->
     */
    public URL[] getClassPath() {
        int len = classPath.getLength();
        URL[] urlPath = new URL[len];
	    Enumeration cpeEnum = classPath.getPath();
        
        for (int i = 0; i < len; i++) 
        {
            ClassPathEntry cpe = (ClassPathEntry)cpeEnum.nextElement();
            urlPath[i] = cpe.getURL();
        }
        return urlPath;
    }   

    /**
     * Parse a class-path string using the system path separator.
     */
    public static String[] parseClassPath(String path) {
	    String systemSeparator = System.getProperty("path.separator");
        
        if (systemSeparator == null)
            throw new NullPointerException("path.separator property not defined");
        
        StringTokenizer tokenizer = new StringTokenizer(path, systemSeparator);
        String[] parsed = new String[tokenizer.countTokens()];
        
        for (int i = 0; tokenizer.hasMoreTokens(); i++) 
        {
            parsed[i] = tokenizer.nextToken();
        }
        return parsed;
    }

    /**
     * Gets class path from system.
     *
     * @return the system class path represented by an
     *         array of URL objects.
     */
    public static URL[] getSystemClassPath() {
	    // Parse system class path into its components
	    String systemClassPath = System.getProperty("java.class.path");
        if (systemClassPath == null) {
            systemClassPath = "";
        }
        String[] parsedPath = parseClassPath(systemClassPath);

        // Convert to URLs, dropping invalid entries
        Vector urlVector = new Vector(parsedPath.length);
        
        for (int i = 0; i < parsedPath.length; i++) 
        {
            try 
            {
                urlVector.addElement(new URL("file", "", parsedPath[i]));
            } catch (MalformedURLException mue) {
                // Do not add this entry
	        }
        }

        URL[] urlArray = new URL[urlVector.size()];
        urlVector.copyInto(urlArray);
        return urlArray;
    }

    /**
     * Loads and, optionally, resolves the specified class, returning the
     * <CODE>ClassResource</CODE> object.
     *
     * @param className The name of the class to load.
     * @param resolve Set to <CODE>true</CODE> for class resolution, 
     *        <CODE>false</CODE> for no resolution.
     * @return Object containing class and resource.
     * @exception ClassNotFoundException if this loader and the system loader
     *            can not find or successfully load the class.
     * @see #setClassPath
     * @see #addClassPath
     * @see #loadClass
     */
    private ClassResource loadClassResource(String className, boolean resolve) throws ClassNotFoundException 
    {
        try {
            ClassResource cr = checkForLoadedClass(className);
            if (cr == null) {
                cr = doLoadClass(className);
                
                if ( resolve )
                    resolveClass(cr.getClassObj());
            }
	        return cr;
        } catch (ClassNotFoundException except) {
            throw except;
        } catch (IOException except) {
            throw new ClassNotFoundException(except.getClass().getName() + ": " + except.getMessage());
        } catch (RuntimeException except) {
            throw except;
        } catch (Error except) {
            throw except;
        }
    }

    /**
     * Loads specified class.  If possible, the class will loaded from the
     * class path defined previously with <CODE>setClassPath</CODE>, 
     * <CODE>addClassPath</CODE>, and/or the constructor.  Otherwise, the 
     * class will be passed off to the parent class loader.  Classes from 
     * the java.* packages cannot be loaded by this class loader so will 
     * be passed off to the parent class loader.
     *
     * @param className The name of the class to be loaded, e.g. 
     * @return Object containing class and resource.
     * @exception ClassNotFoundException if this loader and the system loader
     *            can not find or successfully load the class.
     * @see #setClassPath
     * @see #addClassPath
     */
    private synchronized ClassResource doLoadClass(String className) throws ClassNotFoundException, IOException 
    {

        // Must check loaded class table again, as we are now synchronized.
        ClassResource cr = checkForLoadedClass(className);
        if (cr != null)
        {
            return cr;
        }
        else
        {
            String fileName = className.replace('.', '/').concat(".class");
            
            Resource resource = getResourceObject(fileName);
            if (resource != null) {
                byte[] classBytes = resource.getBytes();
                Class c = defineClass(className, classBytes, 0, classBytes.length);
                cr = new ClassResource(c, resource);
                loadedClasses.put(className, cr);

                 //System.out.println("-------------" + className);
            }
            if (cr == null) {
                throw new ClassNotFoundException(className);             
            }
            return cr;
        }
    }

    /**
     * Gets specified resource object.
     *
     * @param name The name of the resource.
     * @return the resource if found, null if not.
     * @see Resource
     */
    public Resource getResourceObject(String name) 
    {
        Resource resource;
	    
        try {
            resource = classPath.getResource(name);
            if (resource == null) {
                //System.out.println("getResource not found: " + name);
            } else {
                //System.out.println("getResource finished: " + name);
            }
        } catch (RuntimeException except) {
            throw except;
        } catch (Error except) {
            throw except;
        }

        return resource;
    }

    public void setLocalClassPath()
    {
        String pathSeparator = System.getProperty("path.separator");
        String classPath = System.getProperty("java.class.path");
        
        StringTokenizer st = new StringTokenizer(classPath, pathSeparator);
        
        setClassPath( st.nextToken() );
        
        while (st.hasMoreTokens()) 
        {
            addClassPath( st.nextToken() );
        }
        
    }

    /**
     * Check the table of loaded classes to determine if a class is already
     * loaded.  Purposely not synchronized, allowing a quick check for a class
     * being loaded.  A second check, which higher level synchronization is
     * required if the class is not found.  This function does logs a message
     * when a class is found.
     *
     * @param className The class to look up.
     * @return The ClassResource object for the class, or null if not found.
     */
    private ClassResource checkForLoadedClass(String className) 
    {
        ClassResource cr = (ClassResource)loadedClasses.get(className);
        
        if (cr != null)
            System.out.println("loadClass already loaded: " + className);

        return cr;
    }

    /**
     * Gets specified resource as input stream.
     *
     * @param name The name of the resource.
     * @return an input stream representing the specified resource or
     *         null if the resource is not found.
     */
    public InputStream getResourceAsStream(String name) 
    {
        Resource resource = getResourceObject(name);
        if (resource != null) {
            try {
                return resource.getInputStream();
            } catch (IOException except) {
                return null;  // This is what getResourceAsStream does..
            }
        }
        return null;
    }

    /**
     * Gets specified resource as array of bytes.
     *
     * @param name The name of the resource.
     * @return an array of bytes representing the specified resource or
     *         null if the resource is not found.
     */
    public byte[] getResourceAsByteArray(String name) 
    {
        Resource resource = getResourceObject(name);
        if (resource != null) {
            try {
                return resource.getBytes();
            } catch (IOException except) {
                return null;  // This is what getResourceAsStream does..
            }
        }
        return null;
    }


    /**
     * Determine if the classes loaded by this class loader have been modified.
     * If any file associated with loaded in the class loader's class path has
     * changed, the classes should be reloaded.  If the classes need to be 
     * reloaded, a new instance of this class loader must be created 
     * because a particular class loader instance can only load classes 
     * once.
     *
     * @return <CODE>true</CODE> if the classes should be reloaded,
     *     <CODE>false</CODE> if not.
     */
    public boolean shouldReload() 
    {
	    System.out.println("Checking for out-of-date classes");

        Enumeration classes = loadedClasses.elements();
        while (classes.hasMoreElements()) {
            boolean isModified;
            try {
                isModified = ((ClassResource)classes.nextElement()).getResource().hasBeenModified();
            } catch (FileNotFoundException except) {
		        System.out.println("File for loaded class can no longer be accessed");
                isModified = true;
            }
            if (isModified) {
		        System.out.println("Loaded classes have been modified");
                return true;
            }
        }
	    System.out.println("Loaded classes have not been modified");
        return false;
    }
    

    public static void main( String[] args ) throws Exception
    {
        ReverseClassLoader cl = new ReverseClassLoader();
        cl.setClassPath(".");

        Class clazz = cl.loadCls("test.HelloWorld");
        Object obj = clazz.newInstance();

        System.out.println( "Use Loader: " + obj.getClass().getClassLoader().getClass().getName() );
    }
}
