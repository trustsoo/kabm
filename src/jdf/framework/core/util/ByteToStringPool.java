/*
 * @(#)ByteToStringPool.java
 *
 * NOTICE !      
 * You can copy or redistribute this code freely except commercial use,
 * If you want to use this program for commercial use, you must contact to me.
 *
 * And, you should not remove the information about the copyright notice 
 * and the author.
 * 
 * @author
 */

package jdf.framework.core.util;

import java.io.UnsupportedEncodingException;

/**
 * <p>
 * String의 변환시 특히, 소켓으로 받은 KSC5601 의 경우, 일반 스트링생성에 비해 수십배에
 * 이르는 성능 차이를 보인다. 증권 수신기의 경우, 매번 들어왔던 byte 배열이 거의 들어오므로
 * 생성된 스트링을 보관후 똑같은 byte 배열의 경우, 다시 반환한다.
 *
 * </p>
 *
 * @author
 * @version 1.0
 */

public final class ByteToStringPool
{
    private final static String ENCODE= "KSC5601";

    private final static int DEFAULT_POOL_SIZE= 1024;

    private LRUCache strPool;
    private LRUCache bytePool;
    private String encoding;

    public ByteToStringPool()
    {
        this(DEFAULT_POOL_SIZE, ENCODE);
    }

    public ByteToStringPool(int size)
    {
        this(DEFAULT_POOL_SIZE, ENCODE);
    }

    public ByteToStringPool(String encoding)
    {
        this(DEFAULT_POOL_SIZE, encoding);
    }

    public ByteToStringPool(int init_size, String encoding)
    {
        this.encoding= encoding;

        strPool= new LRUCache(init_size);
        bytePool= new LRUCache(init_size);
    }

    public void clear()
    {
        strPool.clear();
        bytePool.clear();
    }

    public String getEncoding()
    {
        return encoding;
    }

    /**
     * 해당 byte array에 대하여 생성된 String을 보관후 동일한 byte array가
     * 또 요청시 보관된 String을 반환한다.
     */
    public String get(byte[] bytes, int offset, int length)
    {
        byte[] ch= new byte[length];
        System.arraycopy(bytes, offset, ch, 0, length);

        return get(ch);
    }

    /**
     * 해당 byte array에 대하여 생성된 String을 보관후 동일한 byte array가
     * 또 요청시 보관된 String을 반환한다.
     */
    public String get(byte[] ch)
    {
        try
        {
            int hash= StringHasher.hashChars(ch);
            String str= (String) strPool.get(new Integer(hash));

            if (str == null)
            {
                str= new String(ch, 0, ch.length, encoding);

                strPool.put(new Integer(hash), str);
            }

            return str;

        }
        catch (UnsupportedEncodingException e)
        {
            return null;
        }
    }

    public void put(byte[] ch, String str)
    {
        int hash= StringHasher.hashChars(ch);
        strPool.put(new Integer(hash), str);
    }

    /**
     * 해당 String에 대하여 생성된 byte array를 보관후 동일한 String이
     * 또 요청시 보관된 byte array을 반환한다.
     */
    public byte[] get(String str)
    {
        try
        {
            int hash= str.hashCode();
            byte[] b= (byte[]) bytePool.get(new Integer(hash));

            if (b == null)
            {
                b= str.getBytes(encoding);

                bytePool.put(new Integer(hash), b);
            }

            return b;

        }
        catch (UnsupportedEncodingException e)
        {
            return null;
        }
    }

    public void put(String str, byte[] ch)
    {
        int hash= str.hashCode();
        bytePool.put(new Integer(hash), ch);
    }

    
    

}
