package jdf.framework.logic.util;

import java.io.InputStream;

/**
 * 저장소로부터 URI에 해당하는 바이트를 반환하거나 저장하는 역활을 한다.
 */
public interface DescAccessible
{
    /**
	 * URI로 부터 저장된 바이트를 반환한다.
	 * @param uri
	 * @return byte[]
	 */
	public byte[] read(String uri) throws Exception;
	
	/**
	 * URI로 부터 저장된 바이트를 스트림형태로 반환한다.
	 * @param uri
	 * @return InputStream
	 */
	public InputStream readStream(String uri) throws Exception;
	
	/**
	 * URI로 바이트를 저장한다.
	 * @param uri
	 * @param b
	 */
    public void write(String uri, byte[] b) throws Exception;
	
	/**
	 * URI로 스트링을 저장한다.
	 * @param uri
	 * @param b
	 */
    public void write(String uri, String s) throws Exception;

	/**
	 * URI로 스트림을 저장한다.
	 * @param uri
	 * @param b
	 */
    public void write(String uri, InputStream in) throws Exception;
    
    /**
     * URI에 해당하는 Descriptor가 존재하는지 여부를 확인한다.
     * @param uri
     * @return boolean 존재 여부
     */
    public boolean exists(String uri) throws Exception;
    
    /**
     * 전체 URI의 리스트를 얻어온다.
     * @return String[] URI의 리스트
     */
    public String[] list() throws Exception;
    
	/**
	 * 해당 URI 디렉토리내의 리스트를 얻어온다.
	 * @return String[] URI의 리스트
	 */
	public String[] list(String uri) throws Exception;

}
