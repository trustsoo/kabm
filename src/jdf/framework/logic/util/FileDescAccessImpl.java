package jdf.framework.logic.util;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.util.SmartStringArray;

import java.io.*;


/**
 * 파일로 대하여 URI에 해당하는 패스에 바이트를 저장하거나 반환 받는다.
 */
public class FileDescAccessImpl implements DescAccessible
{
    // Root 디렉토리
    private File root;

    // 제외시키는 디렉토리 list
    private String[] excludeDirs = new String[] {};

    private String[] includeDirs = new String[] {};

    private String[] excludeFileTypes = new String[] {};
    
    private Config conf;

    /**
     * 디폴트로 현재 디렉토리를 ROOT 저장소로 사용한다.
     */
    public FileDescAccessImpl()
    {
        this(".");
    }

    private boolean isCorrectDir(String dir)
    {
        //Logger.info.println( ">>>>>>>> " +dir);
        if(dir==null)
            return false;
        
        
        
        
        readConfig();
        
        // include인 경우
        if (this.includeDirs.length > 0)
        {
            for (int i = 0; i < includeDirs.length; i++)
            {
                if (dir.indexOf(includeDirs[i])==0)
                    return true;
            }

            return false;

        } 
        // exclude인 경우
        else
        {

            for (int i = 0; i < excludeDirs.length; i++)
            {
                if (excludeDirs.equals(dir))
                    return false;
            }
            return true;
        }
    }

    private boolean isCorrectFile(String filename)
    {
        if(filename==null)
            return false;
        
        readConfig();
        
        for (int i = 0; i < excludeFileTypes.length; i++)
        {
            if (filename.toLowerCase().indexOf("." + excludeFileTypes[i]) > 0)
                return false;
        }
        return true;
    }

    /**
     * 설정된 패스 디렉토리를 ROOT 저장소로 사용한다.
     * 
     * @param pathname
     */
    public FileDescAccessImpl(String pathname)
    {
        this(new File(pathname));
    }

    /**
     * 설정된 패스 디렉토리를 ROOT 저장소로 사용한다.
     * 
     * @param root
     */
    public FileDescAccessImpl(File root)
    {
        this.root = root;

        
    }
    
    
    private void readConfig()
    {
        try
        {
            if(this.conf==null)
                conf = Configuration.lookup("/resource/anylogic/anybuilder");

            // 제외시키고자 하는 디렉토리
            String tmp = conf.getString("excludeDirs");
            this.excludeDirs = SmartStringArray.split(",", tmp);

            tmp = conf.getString("includeDirs");
            this.includeDirs = SmartStringArray.split(",", tmp);

            // 제외시키고자 하는 file type
            tmp = conf.getString("exculdeFileTypes");
            this.excludeFileTypes = SmartStringArray.split(",", tmp);

        } catch (Exception e)
        {

        }
    }

    /**
     * URI로 부터 저장된 바이트를 반환한다.
     * 
     * @param uri
     * @return byte[]
     */
    public byte[] read(String uri) throws Exception
    {
        String tokens[] = SmartStringArray.split("/", uri);

        if (tokens.length == 1)
            tokens = SmartStringArray.split("\\", uri);

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tokens.length - 1; i++)
        {
            if (tokens[i] != null && tokens[i].length() > 0)
                sb.append(tokens[i]).append("/");
        }
        sb.append(tokens[tokens.length - 1]);

        String tmp = sb.toString();

        byte[] buf = new byte[1024];
        BufferedInputStream bis = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        try
        {
            File file = new File(root, tmp);
            bis = new BufferedInputStream(new FileInputStream(file));
            int j;
            while ((j = bis.read(buf)) > -1)
                bos.write(buf, 0, j);

            buf = bos.toByteArray();
        } catch (Exception e)
        {
            throw e;
        } finally
        {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }

        return buf;
    }

    /**
     * URI로 부터 저장된 바이트를 스트림형태로 반환한다.
     * 
     * @param uri
     * @return InputStream
     */
    public InputStream readStream(String uri) throws Exception
    {
        return new ByteArrayInputStream(read(uri));
    }

    /**
     * URI로 바이트를 저장한다.
     * 
     * @param uri
     * @param b
     */
    public void write(String uri, byte[] b) throws Exception
    {
        String tokens[] = SmartStringArray.split("/", uri);

        if (tokens.length == 1)
            tokens = SmartStringArray.split("\\", uri);

        File parent = root;

        for (int i = 0; i < tokens.length - 1; i++)
        {
            if (tokens[i] != null && tokens[i].length() > 0)
            {
                File child = new File(parent, tokens[i]);
                child.mkdir();
                parent = child;
            }
        }

        File desc = new File(parent, tokens[tokens.length - 1]);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(desc));
        bos.write(b);
        bos.flush();
        bos.close();
    }

    /**
     * URI로 스트링을 저장한다.
     * 
     * @param uri
     * @param b
     */
    public void write(String uri, String s) throws Exception
    {
        write(uri, s.getBytes());
    }

    /**
     * URI로 스트림을 저장한다.
     * 
     * @param uri
     * @param b
     */
    public void write(String uri, InputStream in) throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);

        byte[] buf = new byte[1024];

        int j;
        while ((j = in.read(buf)) > -1)
            bos.write(buf, 0, j);

        buf = bos.toByteArray();

        write(uri, buf);
    }

    /**
     * URI에 해당하는 Descriptor가 존재하는지 여부를 확인한다.
     * 
     * @param uri
     * @return boolean 존재 여부
     */
    public boolean exists(String uri) throws Exception
    {
        String tokens[] = SmartStringArray.split("/", uri);

        if (tokens.length == 1)
            tokens = SmartStringArray.split("\\", uri);

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tokens.length - 1; i++)
        {
            if (tokens[i] != null && tokens[i].length() > 0)
                sb.append(tokens[i]).append("/");
        }
        sb.append(tokens[tokens.length - 1]);

        String tmp = sb.toString();

        try
        {
            File file = new File(root, tmp);
            return file.exists();
        } catch (Exception e)
        {
            throw e;
        }
    }

    /**
     * 전체 URI의 리스트를 얻어온다.
     * 
     * @return String[] URI의 리스트
     */
    public String[] list() throws Exception
    {
        SmartStringArray array = new SmartStringArray();

        getFileNameArray(true, root, "", array);

        return array.toArray();
    }

    private static void getFileNameArray(boolean isFirst, File dir, String baseDir, SmartStringArray array)
    {
        File[] files = dir.listFiles();

        if (!isFirst)
            baseDir = baseDir + "/" + dir.getName();

        for (int i = 0; i < files.length; i++)
        {
            if (!files[i].isDirectory())
            {
                if (files[i].getName().endsWith(".xml"))
                {
                    String name = baseDir + "/" + files[i].getName();
                    array.add(name);
                }
            } else
            {
                getFileNameArray(false, files[i], baseDir, array);
            }
        }
    }

    /**
     * 해당 URI 디렉토리내의 리스트를 얻어온다.
     * 
     * @return String[] URI의 리스트
     */
    public String[] list(String uri) throws Exception
    {
        
        if (uri == null)
            return new String[] {};
        
        
        // uri 마지막에 / 또는 \ 가 붙어있으면 제거한다.
        int lstPoint = uri.length()-1;
        if(lstPoint>0 && (uri.lastIndexOf("\\")==lstPoint || uri.lastIndexOf("/")==lstPoint) )
            uri = uri.substring(0,lstPoint);
        

        String tokens[] = SmartStringArray.split("/", uri);

        if (tokens.length == 1)
            tokens = SmartStringArray.split("\\", uri);

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tokens.length - 1; i++)
        {
            if (tokens[i] != null && tokens[i].length() > 0)
                sb.append(tokens[i]).append("/");
        }
        sb.append(tokens[tokens.length - 1]);

        String tmp = sb.toString();
        
        System.out.println( root + "       "+tmp);

        File file = new File(root, tmp);

        SmartStringArray array = new SmartStringArray();

        File[] files = file.listFiles();
        
        
        //Logger.info.println( "***   >>>>>>>> [" +uri+"]");
        
        // /처럼 하나인 경우는
        if(uri.length()==1)
            uri = "";
        // 처음 문자가 / 로 시작하지 않으면
        else if(uri.indexOf("/")!=0)
            uri = "/"+uri;

        for (int i = 0; i < files.length; i++)
        {
            if (files[i].isDirectory())
            {
                String dirName = files[i].getName();
                String fullDir = uri + "/" + dirName;

                if (isCorrectDir(fullDir))
                    array.add("D " + dirName);
            }
        }

        for (int i = 0; i < files.length; i++)
        {
            if (!files[i].isDirectory())
            {
                String fileName = files[i].getName();

                if (this.isCorrectFile(fileName))
                    array.add("F " + fileName);
            }

        }

        return array.toArray();
    }

    public static void main(String args[]) throws Exception
    {
        FileDescAccessImpl access = new FileDescAccessImpl(".");

        byte b[] = { 49, 50, 51, 52, 53, 54, 55, 56, 57, 48 };

        // /a/b/c
        String uri = args[0];
        access.write(uri, b);
        b = access.read(uri);
        System.out.println(new String(b, 0, b.length));
    }
}