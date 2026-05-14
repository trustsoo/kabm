package jdf.framework.core.data.cci;


import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.ResourceException;


/**
 * <b><code>Interaction</code></b>
 * <p>
 * EIS와 연결후 실제 작업을 하는 class
 * </p>
 *
 * @author
 * @version 1.0
 */
public interface Interaction
{
	/**
     * Connection 객체를 얻는다.
     *  
     * @return
	 */
	public Connection getConnection();
	
	
    /**
     * trcode (BLD 명) 와 input DataSet 에 따라 프로세스를 수행
     * 
     * @param spec
     * @param input
     * @return
     * @throws ResourceException
     */
    public DataSet execute(String spec, DataSet input) throws ResourceException;
    
    /**
     * trcode (BLD 명) 와 input DataSet 에 따라 프로세스를 수행하고 결과를 output DataSet 에 넣는다.
     * 
     * @param spec
     * @param input
     * @param output
     * @throws ResourceException
     */
	public void execute(String spec, DataSet input, DataSet output) throws ResourceException;
    
    
    /**
     * Interaction 객체 사용완료후 가상의 종료상태를 만든다.
     * 
     * @throws ResourceException
     */
	public void close() throws ResourceException;
}