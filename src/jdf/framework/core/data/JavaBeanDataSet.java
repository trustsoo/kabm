package jdf.framework.core.data;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 일반적인 Java Bean(POJO) 객체를 이용하여 DataSet 을 구현한 객체, 즉 실제 데이터가 read/write 되는것은
 * Java Bean 객체에 대해서 access 가 일어나고, DataSet 은 일종의 interface 만을 제공한다.
 * 
 * @author
 * 
 */
public class JavaBeanDataSet extends DataSet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Class<?> storeClass;

	private List<Object> objectList = new ArrayList<>();


	private Object primaryStoreObject = null;

	// java.beans.PropertyDescriptor 정보가 들어간다.
	private ConcurrentMap<String, PropertyDescriptor> objectPropertiesInfo = new ConcurrentHashMap<>();

	/**
	 * 기본 생성자
	 * 
	 * @param storeClass
	 */
	public JavaBeanDataSet(Class<?> storeClass) {
		this.storeClass = storeClass;

		initBeanInfo();
	}

	/**
	 * 기본생성자
	 * 
	 * @param storeObj
	 */
	public JavaBeanDataSet(Object storeObj) {

		// 리스트 형태이면 바로 설정한다.
		if (storeObj instanceof List) {
			this.objectList = (List) storeObj;
			// 첫번째것을 primary store object으로 사용
			this.primaryStoreObject = this.objectList.get(0);

			if (this.primaryStoreObject == null)
				throw new IllegalArgumentException("list 형태안에 기본객체가 없습니다.");

			this.objectList.remove(0);

			this.storeClass = this.primaryStoreObject.getClass();

			// 이짓을 왜하냐하면....
			// List 인 경우 검색결과가 없어도 list 에 검색된건수가 1가 있는것으로 나와서...
			this.primaryStoreObject = null;

		}
		// 일반 단일 객체인 경우
		else {
			this.primaryStoreObject = storeObj;
			this.storeClass = this.primaryStoreObject.getClass();
		}

		initBeanInfo();

	}

	/**
	 * 
	 * @throws IntrospectionException
	 */
	private void initBeanInfo() throws IllegalArgumentException {
		try {
			BeanInfo info = Introspector.getBeanInfo(this.storeClass);

			PropertyDescriptor[] props = info.getPropertyDescriptors();

			// 매번 property 에 해당하는 Method를 찾지 않기 위하여
			for (int i = 0; i < props.length; i++) {
				this.objectPropertiesInfo.put(props[i].getName(), props[i]);
			}
		} catch (IntrospectionException ie) {
			throw new IllegalArgumentException(ie.toString());
		}

	}

	/**
	 * 새로운 객체를 생성한다.
	 * 
	 * @return
	 */
	private Object createNewInstance() {

		try {
			return this.storeClass.newInstance();
		} catch (java.lang.InstantiationException ie) {
			ie.printStackTrace();
			return null;
		} catch (java.lang.IllegalAccessException iae) {
			iae.printStackTrace();
			return null;
		}
	}

	/**
	 * 해당순서에 해당하는 객체를 returng 한다.
	 * 
	 * @param seq
	 * @return
	 */
	private Object getObject(int seq) {
		Object tmp = null;

		if (seq < this.objectList.size()) {
			tmp = this.objectList.get(seq);
		} else if (seq == this.objectList.size()) {

			if (seq == 0) {
				if (this.primaryStoreObject == null)
					this.primaryStoreObject = createNewInstance();

				tmp = this.primaryStoreObject;
			} else
				tmp = createNewInstance();

			this.objectList.add(tmp);
		}

		return tmp;
	}

	/**
	 * 데이터를 POJO에 세팅한다.
	 * 
	 */
	public Object put(Object key, Object val, int seq)
			throws IllegalArgumentException {
		Object bean = getObject(seq);

		PropertyDescriptor prop = this.objectPropertiesInfo.get(key);

		if (prop == null)
			return super.put(key, val, seq);

		try {
			Method m = prop.getWriteMethod();
			if (m != null)
				setValue(bean, m, val);
			// 객체에 property 가 없는 경우 발생
			else
				throw new IllegalArgumentException(key
						+ " property is not writable");

		} catch (InvocationTargetException ite) {
			ite.printStackTrace();
		} catch (IllegalAccessException iae) {
			iae.printStackTrace();
		}

		return bean;
	}

	/**
	 * method 에 값을 세팅한다.
	 * 
	 * @param bean
	 * @param m
	 * @param val
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private void setValue(Object bean, Method m, Object val)
			throws InvocationTargetException, IllegalAccessException {
		try {
			m.invoke(bean, new Object[] { val });
		}
		// 타입이 맞지 않는 exception이 발생하면, 그때 체크
		catch (java.lang.IllegalArgumentException iae) {

			Object setValue = null;

			Class paramType = m.getParameterTypes()[0];

			if (paramType == val.getClass())
				setValue = val;
			else if (paramType == String.class)
				setValue = val.toString();
			else if (paramType == int.class || paramType == Integer.class)
				setValue = new Integer(val.toString());
			else if (paramType == float.class || paramType == Float.class)
				setValue = new Float(val.toString());
			else if (paramType == double.class || paramType == Double.class)
				setValue = new Double(val.toString());
			else if (paramType == long.class || paramType == Long.class)
				setValue = new Long(val.toString());
			else if (paramType == boolean.class || paramType == Boolean.class)
				setValue = new Boolean(val.toString());
			else if (val instanceof JavaBeanDataSet) {

				JavaBeanDataSet jds = (JavaBeanDataSet) val;

				Class paramTyp = m.getParameterTypes()[0];

				if (paramTyp.isArray()) {
					setValue = jds.toList().toArray();
				} else if (paramTyp == List.class) {
					setValue = jds.toList();
				} else {
					setValue = jds.getObject();
				}
			}

			m.invoke(bean, new Object[] { setValue });
		}
	}

	/**
	 * 데이터를 조회
	 * 
	 */
	public Object get(Object key, int seq) {
		Object bean = getObject(seq);

		PropertyDescriptor prop = this.objectPropertiesInfo.get(key);

		if (prop == null)
			return super.get(key, seq);

		try {

			Method m = prop.getReadMethod();
			if (m != null)
				return m.invoke(bean, new Object[] {});
			// 객체에 property 가 없는 경우 발생
			else
				throw new IllegalArgumentException(key
						+ " property is not writable");

		} catch (InvocationTargetException ite) {
			ite.printStackTrace();
		} catch (IllegalAccessException iae) {
			iae.printStackTrace();
		}

		return NULL;

	}

	/**
	 * key Object를 세팅된 순서대로 되어있는 List를 반납한다.
	 * 
	 * @return
	 */
	public ArrayList<Object> getKeyObjectList() {
		Set<String> s = this.objectPropertiesInfo.keySet();

		Iterator<String> i = s.iterator();

		ArrayList<Object> result = new ArrayList<>();

		while (i.hasNext()) {
			result.add(i.next());
		}

		return result;
	}

	/**
	 * 기본 String 형태로 return 한다.
	 */
	public String getText(Object key, int seq) {
		Object result = get(key, seq);

		try {
			return result.toString();
		} catch (NullPointerException e) {

		}
		return NULL;
	}

	/**
	 * 총 객체수를 구한다.
	 */
	public int getCount(Object key) {
		return this.objectList.size();
	}

	/**
	 * 최초객체를 return 한다.
	 * 
	 * @return
	 */
	public Object getObject() {
		return this.objectList.get(0);
	}

	/**
	 * 객체 리스트를 return 한다.
	 * 
	 * @return
	 */
	public List toList() {
		return this.objectList;
	}

	/**
	 * Enumeration 형태로 객체를 return
	 */
	public Enumeration toEnumeration() {
		return Collections.enumeration(this.objectList);
	}

	/**
	 * 데이터의 최대 row 수를 얻는다.
	 */
	public int getMaxDataSize() {
		return this.objectList.size();
	}

}