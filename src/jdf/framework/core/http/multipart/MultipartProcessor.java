package jdf.framework.core.http.multipart;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



public interface MultipartProcessor
{

	/**
	 * request �� ó= ����Ǵ� �޽��
	 * object 8�δ� �Ľ̰�� �� jdf.framework.core.data.DataSet �Ǵ� Javabean �� ����.
	 * �ʱ�ȭ �۾��� Ư���� �ʿ��;�� Ư���� ������ �ʿ�� ���.
	 * 
	 */
	public void intialize(Object obj);

	/**
	 * Multipart request ó����, field(parameter)�� �Ѿ�4� d���� ���'�� OutputStream ����ü�� ��ȯ�Ѵ�.
	 * �ϳ��� multipart HTTP request �󿡼� ���� paraemter �� �Ѿ�4� ��� ���� �� �޽�带 ȣ���Ͽ� OutputStream;
	 * ��� �� OutputStream�� ����; �����Ѵ�.
	 * 
	 * @param fieldName �ʵ��
	 * @param filename ���ϸ� (����Ÿ��8�� ��ε��)
	 * @param contentType file content type d��
	 * @return
	 */
	public OutputStream getOutputStream(String fieldName, String filename, String contentType) throws IOException;
	
	
	
	/**
	 * getOutputStream�� �ݴ�� multipart request ó���� ����ģ�� ���; ��n�1� '�� ���8�� 
	 * ����ڴ� getInputStream; ȣ���Ͽ� ����; ��´�. 
	 * ��� �� interface �� �����ϰ��� �ϴ� ���� getOutputStream�� getInputStream�� ���� 
	 * /����8�� ������ �� �ֵ��� �����Ѵ�.
	 * 
	 * @param fieldName �ʵ��
	 * @return InputStream ����ü
	 * @throws IOException IO ó�� �7�� �߻�
	 */
	public InputStream getInputStream(String fieldName) throws IOException;
	

	public void close();

}
