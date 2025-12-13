package jdf.framework.logic.spi.parser;

import jdf.framework.core.data.schema.FieldType;
import jdf.framework.core.util.Hexa;

/**
 * <p>
 * 문자는 ASCII형태로 숫자는 binary형태로 parsing한다.
 * </p>
 * 
 * @author
 * @version 1.1
 */
public final class ParseUtil implements FieldType
{

    private final static String ENCODE = "KSC5601";

    //private final static int VAR_LENGTH_MODE = -1;

    // space
    private final static byte SPACE = 0x20;

    private final static byte ZERO = 0x30;

    //private final static int DOUBLE_LEN = 8;

    /**
     * 문자열을 trim 하여 반환하고 아무값이없으면 0 으로 반환
     * 
     * @param x
     * @return
     */
    public static String toTrimedStringNum(String x)
    {
        String y = toTrimedString(x);

        if (y != null && y.length() > 0)
            return y;
        else
            return "0";
    }

    /**
     * 문자열중의 모든 빈칸을 제거한다. 문자열 중간의 빈칸도 제거한다.
     * 
     * @param x
     * @return
     */
    public static String toTrimedString(String x)
    {
        if (x == null || x.length() == 0)
            return x;

        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < x.length(); i++)
        {
            char y = x.charAt(i);
            if (y != ' ')
                buf.append(y);
        }

        return buf.toString();
    }

    /**
     * byte array를 String으로 변환시킨다.
     * 
     */
    public static String transBytesToString(byte[] src, int startPoint, int len) throws TranslationException
    {
        try
        {
            return new String(src, startPoint, len, ENCODE);
        } catch (java.io.UnsupportedEncodingException ue)
        {
            throw new TranslationException("해당 Encoding을 지원하지 않습니다.");
        }
    }

    /**
     * byte stream 을 int형으로 변환시킨다
     */
    public static int transBytesToInt(byte[] src, int stPoint, int len)
    {
        int result = 0;
        int tempByte = 0;

        int total = stPoint + len;

        for (int i = stPoint; i < total; i++)
        {
            result <<= 8;
            tempByte = src[i];

            if (tempByte < 0)
            {
                tempByte &= 0x7f;
                result += tempByte;
                result += 128;
            } else
                result += tempByte;
        }
        return result;
    }

    /**
     * little -edian 값을 숫자로 변환
     * 
     * @param src
     * @param stPoint
     * @param len
     * @return
     */
    public static int transLittleEndianBytesToInt(byte[] src, int stPoint, int len)
    {
        int result = 0;
        int tempByte = 0;

        int total = stPoint + len - 1;

        for (int i = total; i >= stPoint; i--)
        {
            result <<= 8;
            tempByte = src[i];

            if (tempByte < 0)
            {
                tempByte &= 0x7f;
                result += tempByte;
                result += 128;
            } else
                result += tempByte;
        }
        return result;
    }

    /**
     * byte array 값을 long 형으로 변환한다.
     * 
     */
    public static long transBytesToLong(byte[] src, int stPoint, int len)
    {
        long result = 0;
        int tempByte = 0;

        int total = stPoint + len;

        for (int i = stPoint; i < total; i++)
        {
            result <<= 8;
            tempByte = src[i];

            if (tempByte < 0)
            {
                tempByte &= 0x7f;
                result += tempByte;
                result += 128;
            } else
                result += tempByte;
        }
        return result;
    }

    /**
     * 문자형 숫자값을 byte array 로 변환한다.
     * 
     * @param data
     * @param size
     * @return
     */
    public static byte[] transStringNumberToBytes(String data, int size)
    {
        // Logger.debug.println("------------------------
        // transStringNumberToBytes");

        byte[] tempBytes = new byte[size];

        if (data == null)
            return tempBytes;

        byte[] strBytes = data.getBytes();
        int strLen = strBytes.length;

        // Logger.debug.println("------------------------
        // transStringNumberToBytes-- "+size+":"+strLen);

        if (strLen > size)
            strLen = size;

        // 10 9
        int stx = size - strLen;

        if (stx < 0)
        {
            stx = 0;
            strLen = size;
        }

        System.arraycopy(strBytes, 0, tempBytes, stx, strLen);

        if (stx > 0)
        {
            for (int i = 0; i < stx; i++)
            {
                tempBytes[i] = ZERO;
            }
        }

        return tempBytes;
    }

    /**
     * String --> Byte Array
     */
    public static byte[] transStringToBytes(String data, int size)
    {
        return transStringToBytes(data, size, ALIGN_LEFT, SPACE);
    }

    /**
     * String --> Byte Array
     */
    public static byte[] transStringToBytes(String data, int size, int align, byte fillData)
    {
        byte[] tempBytes = new byte[size];

        if (data == null)
            return tempBytes;

        byte[] strBytes = data.getBytes();
        int strLen = strBytes.length;

        if (strLen > size)
            strLen = size;

        if (align == ALIGN_LEFT)
        {

            System.arraycopy(strBytes, 0, tempBytes, 0, strLen);

            if (strLen < size)
            {
                for (int i = strLen; i < size; i++)
                {
                    tempBytes[i] = fillData;
                }
            }
        } else
        {
            int idx = size - strLen;

            System.arraycopy(strBytes, 0, tempBytes, idx, strLen);

            if (strLen < size)
            {
                for (int i = 0; i < idx; i++)
                {
                    tempBytes[i] = fillData;
                }
            }

        }

        return tempBytes;
    }

    /**
     * little-edian 으로 된 숫자값을 big-endian 으로 big-endian 값을 little-endain
     * 
     * @param data
     * @param len
     * @return
     */
    public static int convertEndianType(int data, int len)
    {
        byte[] result = transIntToBytes(data, len);
        byte[] tmp = new byte[len];

        for (int i = 0; i < len; i++)
        {
            tmp[i] = result[len - i - 1];
        }

        return transBytesToInt(tmp, 0, len);

    }

    /**
     * int --> Byte Array
     */
    public static byte[] transIntToBytesOld(int data, int len)
    {
        byte[] result = new byte[len];
        try
        {
            int tempInt = data;
            for (int j = 0; j < len; j++)
            {
                result[(len - 1) - j] = (byte) (tempInt % 256);
                tempInt /= 256;
            }
        } catch (Exception e)
        {
            System.err.println("IN TypeCast : Cast Error!!");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * int 값을 byte array 로 변환
     * 
     * @param data
     * @param len
     * @return
     */
    public static byte[] transIntToBytes(int data, int len)
    {
        byte[] result = new byte[len];
        try
        {
            int tempInt = data;
            for (int j = len - 1; j > -1; j--)
            {
                result[j] = (byte) tempInt;
                // result[(len - 1) - j]= (byte) (tempInt % 256);
                tempInt = tempInt >>> 8;
            }
        } catch (Exception e)
        {
            System.err.println("IN TypeCast : Cast Error!!");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * int 형 값을 little endian byte array로 변환
     * 
     * @param data
     * @param len
     * @return
     */
    public static byte[] transIntToLittleEndianBytes(int data, int len)
    {
        byte[] result = new byte[len];
        try
        {
            int tempInt = data;
            for (int j = 0; j < len; j++)
            {
                result[j] = (byte) tempInt;
                // result[(len - 1) - j]= (byte) (tempInt % 256);
                tempInt = tempInt >>> 8;
            }
        } catch (Exception e)
        {
            System.err.println("IN TypeCast : Cast Error!!");
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args)
    {

        // byte[] result = transIntToLittleEndianBytes(-1, 4);
        byte[] result = transIntToBytes(10, 4);
        System.out.println(Hexa.logFormat(result));

        double x = 1;

        byte[] re = transDoubleToBytes(x, 8);

        System.out.println(Hexa.logFormat(re));

    }

    /**
     * int --> Byte Array
     */
    public static byte[] transLongToBytes(long data, int len)
    {
        byte[] result = new byte[len];
        try
        {
            long tempInt = data;
            for (int j = 0; j < len; j++)
            {
                result[(len - 1) - j] = (byte) (tempInt % 256);
                tempInt /= 256;
            }
        } catch (Exception e)
        {
            System.err.println("IN TypeCast : Cast Error!!");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * double 형태의 숫자르 byte[] 변환 길이(len)는 8로 세팅하는것이 일반적이다.
     * 
     * @param d
     * @param len
     * @return
     */
    public static byte[] transDoubleToBytes(double d, int len)
    {
        long bits = Double.doubleToLongBits(d);
        return transLongToBytes(bits, 8);
    }

    /**
     * byte[] 을 double 로 변환
     * 
     * @param src
     * @param stPoint
     * @param len
     * @return
     */
    public static double transBytesToDouble(byte[] src, int stPoint, int len)
    {
        long result = transBytesToLong(src, stPoint, len);
        return Double.longBitsToDouble(result);
    }

}