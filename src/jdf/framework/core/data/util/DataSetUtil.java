package jdf.framework.core.data.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.ExternalData;
import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.logic.spi.management.BLContextFactory;
import jdf.framework.logic.spi.parser.DataSetParser;
import jdf.framework.logic.spi.parser.Protocol;
import jdf.framework.logic.spi.parser.ProtocolFactory;


/**
 * DataSet 변환관련 Utility 주로 Java Beans 과 DataSet간의 변환으로 다루는 class
 * 
 * 
 * @author
 * @since 3.1
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 * Templates
 */
public class DataSetUtil
{

	private static BLContextFactory bl_factory = BLContextFactory.getInstance();

	/**
	 * Java beans의 값을 DataSet으로 옮겨준다.
	 * 
	 * @param bean
	 * @param dataset
	 */
	public static void toDataSet(Object bean, DataSet dataset)
	{
		Class c = bean.getClass();

		try {
			BeanInfo info = Introspector.getBeanInfo(c);

			PropertyDescriptor[] props = info.getPropertyDescriptors();

			for (int i = 0; i < props.length; i++) {
				String propertyName = props[i].getName();
				Object val = props[i].getReadMethod().invoke(bean, new Object[] {});

				dataset.put(propertyName, val);
			}
		} catch (IntrospectionException ie) {

		} catch (IllegalAccessException ia) {

		} catch (InvocationTargetException ite) {

		} catch (NullPointerException npe) {

		}

	}

	/**
	 * DataSet의 값을 Java Beans로 옮겨준다.
	 * 
	 * 
	 * @param dataset
	 * @param bean
	 */
	public static void toBean(DataSet dataset, Object bean)
	{
		Class c = bean.getClass();

		try {
			BeanInfo info = Introspector.getBeanInfo(c);

			PropertyDescriptor[] props = info.getPropertyDescriptors();

			for (int i = 0; i < props.length; i++) {
				String propertyName = props[i].getName();
				Object val = dataset.get(propertyName);
				props[i].getWriteMethod().invoke(bean, new Object[] { val });

				dataset.put(propertyName, val);
			}
		} catch (IntrospectionException ie) {

		} catch (IllegalAccessException ia) {

		} catch (InvocationTargetException ite) {

		} catch (NullPointerException npe) {

		}
	}

	/**
	 * DataSet bytearray 로 변환
	 * 
	 * 
	 * @param trcode
	 * @param ds
	 * @return
	 * @throws ResourceException
	 */
	public static byte[] toByteArray(String trcode, DataSet ds) throws ResourceException
	{
		IOSchema schema = bl_factory.getIOSchema(trcode);

		Protocol protocol = ProtocolFactory.getProtocol();

		DataSetParser dataParser = protocol.getParser();
		dataParser.setClientMode();

		ExternalData bodydata = dataParser.transExternalData(schema, ds);

		return bodydata.toByteArray();

	}

}