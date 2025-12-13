package jdf.framework.core.naming.server;

/** * <description>  *       *   @see <related> *   @author $Author:$ *   @version $Revision:$ */
public interface MainMBean
{

	/**
	 * 
	 * @uml.property name="port"
	 */
	// Constants -----------------------------------------------------
	// Public --------------------------------------------------------
	public void setPort(int p);

	/**
	 * 
	 * @uml.property name="port"
	 */
	public int getPort();

	/**
	 * 
	 * @uml.property name="logging"
	 */
	public void setLogging(boolean l);

	/**
	 * 
	 * @uml.property name="logging"
	 */
	public boolean isLogging();

   public void start()
      throws Exception;
   
   public void stop();
}