package jdf.framework.logic.adapter.file;

import jdf.framework.core.log.Logger;
import jdf.framework.core.util.StringFormater;

/**
 * 
 * 
 * @author
 *
 */
public class AdapterProperties
{
    private final static String LOG_ID="<ResourceAdapter:file> ";


    
    // 각 필드 구분자인 경우 구분 문자를 세팅
    private String delimiter;
    
    // 각 라인 구부문자
    private String lineDelimiter;
    
    // 파일들이 존재하는 root 디렉토리
    private String rootDirectory;
    
    
    private String mode;
    
    private boolean isLengthModeVal = false;
    
    
    private String numberMode;
    
    private boolean isTextNumberModeVal = false;
    
    // read 또는 write 모드인지 결정
    private String readWrite;
    
    private boolean isWriteModeVal = false;
    

    /**
     * @return Returns the delimiter.
     */
    public String getDelimiter()
    {
        return delimiter;
    }

    /**
     * @param delimiter The delimiter to set.
     */
    public void setDelimiter(String delimiter)
    {
        delimiter = StringFormater.replaceStr(delimiter, "\\n", "\n");
        delimiter = StringFormater.replaceStr(delimiter, "\\r", "\r");
        delimiter = StringFormater.replaceStr(delimiter, "\\t", "\t");
        
        this.delimiter = delimiter;
        //Logger.info.println(LOG_ID+"delimiter ["+delimiter+"]");
    }


    /**
     * @return Returns the rootDirectory.
     */
    public String getRootDirectory()
    {
        return rootDirectory;
    }

    /**
     * @param rootDirectory The rootDirectory to set.
     */
    public void setRootDirectory(String rootDirectory)
    {
        if(rootDirectory.lastIndexOf("/") !=  rootDirectory.length()-1 
               && rootDirectory.lastIndexOf("\\") !=  rootDirectory.length()-1
        )
            rootDirectory=rootDirectory+"/";
        
        this.rootDirectory = rootDirectory;
        //Logger.info.println(LOG_ID+"rootDirectory ["+rootDirectory+"]");
    }

    /**
     * @return Returns the lineDelimiter.
     */
    public String getLineDelimiter()
    {
        return lineDelimiter;
    }

    /**
     * @param lineDelimiter The lineDelimiter to set.
     */
    public void setLineDelimiter(String lineDelimiter)
    {
        lineDelimiter = StringFormater.replaceStr(lineDelimiter, "\\n", "\n");
        lineDelimiter = StringFormater.replaceStr(lineDelimiter, "\\r", "\r");
        lineDelimiter = StringFormater.replaceStr(lineDelimiter, "\\t", "\t");
        
        this.lineDelimiter = lineDelimiter;
        
        //Logger.info.println(LOG_ID+"lineDelimiter ["+lineDelimiter+"]");
    }

    /**
     * @return Returns the mode.
     */
    public String getMode()
    {
        return mode;
    }

    /**
     * @param mode The mode to set.
     */
    public void setMode(String mode)
    {
        this.mode = mode;
        
        if("length".equals(mode))
            this.isLengthModeVal=true;
    }
    
    
    public boolean isLengthMode()
    {
        return this.isLengthModeVal;
    }

    /**
     * @return Returns the numberMode.
     */
    public String getNumberMode()
    {
        return numberMode;
    }

    /**
     * @param numberMode The numberMode to set.
     */
    public void setNumberMode(String numberMode)
    {
        this.numberMode = numberMode;
        
        if("text".equals(numberMode))
            this.isTextNumberModeVal=true;
    }
    
    
    public boolean isTextNumber()
    {
        return this.isTextNumberModeVal;
    }

    /**
     * @param readWrite The readWrite to set.
     */
    public void setReadWrite(String readWrite)
    {
        this.readWrite = readWrite;
        if(readWrite!=null && readWrite.trim().toLowerCase().equals("write"))
            this.isWriteModeVal=true;
    }


    public boolean isWriteMode()
    {
        return this.isWriteModeVal;
    }

}