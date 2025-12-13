package jdf.framework.core;

/**
 * <b><code>Configurable</code></b>
 * <p>
 * 설정의 파라미터를 받기 위한 기본 인터페이스
 * </p>
 * 
 * @author
 * @version 1.0
 */
public interface Configurable
{

	/**
	 * 
	 * @uml.property name="configInfo"
	 */
	public void setConfigInfo(Config info);

	/**
	 * 
	 * @uml.property name="configInfo"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	public Config getConfigInfo();

}
