package jdf.framework.core.log;

import java.io.*;

/**
 * <p>
 * 일반적인 byte stream에서 16진수의 값과 문자열값의 출력형태를 얻기위한 Class
 * </p>
 *
 * <pre>
 * 다음과 같은 출력을 얻을 수 있다.
 * 6E 61 6D 65 20 20 20 20 20 20 00 86 14 73 00  : name      
 * 00 1C 54 00 00 00 1F 55 35 31 9A              :
 * </pre>
 *
 * @author
 * @version 1.0
 */

public class HexLogFormat implements LogFormat
{

    private static String NEWLINE = System.getProperty("line.separator");

    private boolean isVeiwStr = true;
    private int columnSize = 15;

    public HexLogFormat()
    {
        super();
    }

    public HexLogFormat(int size, boolean view)
    {
        super();
        columnSize = size;
        isVeiwStr = view;
    }

    public void setColumnSize(int size)
    {
        columnSize = size;
    }

    public void setStringView(boolean x)
    {
        isVeiwStr = x;
    }

    private String formating(byte[] x)
    {
        int byteLen = x.length;

        //if(size<byteLen) byteLen = size;

        // Initial size 512
        ByteArrayOutputStream tempBytes = new ByteArrayOutputStream(512);

        StringBuffer tempHexStr = new StringBuffer();

        try
        {

            for (int i = 0; i < byteLen; i++)
            {
                if (x[i] == '\n' || x[i] == '\r')
                    tempBytes.write(' '); // 공백으로 넣는다.
                else
                    tempBytes.write(x[i]);

                int unsigned = (int) (x[i] & 0xff);

                if (unsigned < 16) // 15까지는 한자리가 나오므로 
                    tempHexStr.append("0");

                tempHexStr.append(Integer.toHexString(unsigned).toUpperCase() + " ");

                int mod;
                if (i == byteLen - 1 && (mod = (i + 1) % columnSize) != 0)
                    for (int j = 0; j < columnSize - mod; j++)
                        tempHexStr.append("   ");

                //if( i ==  (i/columnSize)-1+ columnSize*(i/columnSize) || i== byteLen-1)
                if (((i + 1) % columnSize) == 0 || i == byteLen - 1)
                {
                    if (isVeiwStr)
                        tempHexStr.append(" :" + tempBytes.toString());
                    tempHexStr.append(NEWLINE);

                    tempBytes.reset();

                }
            }
        }
        catch (Exception e)
        {
            return "";
        }
        finally
        {
            tempBytes = null;
        }

        return tempHexStr.toString();
    }

    //private static java.text.SimpleDateFormat formatter =
       // new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.KOREA);

    private static java.text.SimpleDateFormat formatter;

    static {
        String format = Logger.DEFAULT_LOG_TIME_FORMAT;

        try
        {
            jdf.framework.core.Config conf = jdf.framework.core.Configuration.lookup("/logger");

            format = conf.getString("dateFormat", Logger.DEFAULT_LOG_TIME_FORMAT);
        }
        catch (Exception e)
        {
        }

        formatter = new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
    }

    /**
     * 날자 포맷을 설정
     * 
     * @param form
     */
    public static void setDateFormat(String form)
    {
        formatter = new java.text.SimpleDateFormat(form, java.util.Locale.KOREA);
    }

    java.util.Date datetime = new java.util.Date();

    public String formating(LogInfo log)
    {
        StringBuffer result = new StringBuffer();

        datetime.setTime(log.time);

        result.append(formatter.format(datetime));

        if (log.sMode != null)
            result.append(" ").append(log.sMode);

        switch (log.mode)
        {
            case LogInfo.INFO :
                result.append(" ").append(log.strData);

                break;

            case LogInfo.DUMP :

                result.append(NEWLINE).append(formating(log.data));

                break;

            case LogInfo.ERR :

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                PrintWriter writer = new PrintWriter(bytes, true);
                log.error.printStackTrace(writer);

                result.append(NEWLINE).append(bytes.toString());
                bytes = null;
                writer = null;
                break;
        }

        //result.append(NEWLINE);

        return result.toString();
    }

}
