package jdf.framework.core.util;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * <p>
 * 문자열,숫자,byte, byte array등의 데이타를 StringBuffer와 같이 계속 .append()한 다음 간편하게 byte
 * array로 바꾸기 위한 Class.
 * </p>
 * 
 * @author
 * @version 1.0
 */

public class ByteArrayBuffer
{

    private ByteArrayOutputStream stream;

    /**
     * 기본 버퍼길이(1000)를 가지고 이 객체를 생성한다.
     * 
     */
    public ByteArrayBuffer()
    {
        stream = new ByteArrayOutputStream();

    }

    /**
     * 입력받은 길이를 가지고 이 객체를 생성한다.
     * 
     */
    public ByteArrayBuffer(int limitSize)
    {
        stream = new ByteArrayOutputStream(limitSize);
    }

    /**
     * byte 값을 추가한다.
     * 
     * @param content
     * @return
     */
    public ByteArrayBuffer append(byte content)
    {
        byte[] temp = new byte[1];
        temp[0] = content;

        return append(temp, 0, 1);
    }

    /**
     * byte[] 값을 추가한다.
     * 
     * @param content
     * @return
     */
    public ByteArrayBuffer append(byte[] content)
    {
        return append(content, 0, content.length);
    }

    /**
     * String 을 추가한다.
     * 
     * @param content
     * @return
     */
    public ByteArrayBuffer append(String content)
    {
        return append(content.getBytes(), 0, content.length());
    }

    
    /**
     * 숫자값을 문자형으로 변환후 byte[] 로 추가한다.
     * @param content
     * @return
     */
    public ByteArrayBuffer append(int content)
    {
        String temp = String.valueOf(content);

        return append(temp.getBytes(), 0, temp.length());
    }

    /**
     * byte[] 의 값을 특정사이즈 만큼만 추가한다.
     * 
     * @param content
     * @param size
     * @return
     */
    public ByteArrayBuffer append(byte[] content, int size)
    {
        return append(content, 0, size);
    }

    /**
     * byte[] 의 값을 특정시작점부터 특정 길이까지만 추가한다.
     * 
     * @param content
     * @param off
     * @param size
     * @return
     */
    public ByteArrayBuffer append(byte[] content, int off, int size)
    {
        stream.write(content, off, size);
        return this;
    }

    /**
     * 쌓아놓은 데이타를 byte array로 바꾸어 반환한다.
     * 
     * @return byteArray
     */
    public byte[] getBytes()
    {
        return stream.toByteArray();
    }

    /**
     * 쌓아놓은 데이타를 문자열로 바꾸어 반환한다.
     * 
     * @return 문자열
     */
    public String toString()
    {

        return new String(getBytes());
    }

    /**
     * 쌓아놓은 데이타를 OutputStream으로 가져온다.
     * 
     * @return OutputStream
     */
    public OutputStream getOutputStream()
    {
        return stream;
    }

}
