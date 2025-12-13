package jdf.framework.core.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Java Beans 의 데이터 입/출력 관련 Utility 성 class
 * 
 * @author
 * 
 */
public class BeanUtil
{

	/**
	 * Configures a single bean property.
	 * 
	 * @param obj
	 *            the bean to configure
	 * @param config
	 *            the configuration node (for error messages)
	 * @param name
	 *            the property name
	 * @param value
	 *            the property name
	 */
	public static void setBeanProperty(Object obj, String name, String value) throws RuntimeException
	{
		try {
			BeanInfo info = Introspector.getBeanInfo(obj.getClass());

			setBeanProperty(obj, name, value, info);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	/**
	 * Configures a single bean property.
	 * 
	 * @param obj
	 *            the bean to configure
	 * @param name
	 *            the property name
	 * @param value
	 *            the property name
	 * @param info
	 *            the bean's introspected info
	 * @param config
	 *            the configuration node (for error messages)
	 */
	public static void setBeanProperty(Object obj, String name, String value, BeanInfo info) throws RuntimeException
	{
		if (name == null || value == null)
			throw new RuntimeException("unknown name");

		try {
			name = configToBeanName(name);

			PropertyDescriptor[] pds = info.getPropertyDescriptors();
			PropertyDescriptor pd = null;
			for (int i = 0; i < pds.length; i++) {
				if (pds[i].getName().equals(name)) {
					pd = pds[i];
					break;
				}
			}

			if (pd == null || pd.getWriteMethod() == null) {
				for (int i = 0; i < pds.length; i++) {
					if (pds[i].getName().equalsIgnoreCase(name)) {
						pd = pds[i];
						break;
					}
				}
			}

			if (pd == null || pd.getWriteMethod() == null) {
				setBeanPropertyMethod(obj, name, value);
				return;
			}

			Method method = pd.getWriteMethod();
			Class type = method.getParameterTypes()[0];
			String typeName = type.getName();

			if (typeName.equals("boolean") || typeName.equals("java.lang.Boolean"))
				method.invoke(obj, new Object[] { Boolean.valueOf(value) });
			else if (typeName.equals("int") || typeName.equals("java.lang.Integer"))
				method.invoke(obj, new Object[] { new Integer(value) });
			else if (typeName.equals("double") || typeName.equals("java.lang.Double"))
				method.invoke(obj, new Object[] { new Double(value) });
			else if (typeName.equals("long") || typeName.equals("java.lang.Long"))
				method.invoke(obj, new Object[] { new Long(value) });
			else
				method.invoke(obj, new Object[] { value });
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	/**
	 * Translates a configuration name to a bean name.
	 * 
	 * <pre>
	 * foo-bar maps to fooBar
	 * </pre>
	 */
	private static String configToBeanName(String name)
	{
		StringBuffer cb = new StringBuffer();

		for (int i = 0; i < name.length(); i++) {
			char ch = name.charAt(i);

			if (ch == '-')
				cb.append(Character.toUpperCase(name.charAt(++i)));
			else
				cb.append(ch);
		}

		return cb.toString();
	}

	/*
	 * Sets the property for a translated bean method.
	 * 
	 * @param obj the bean to configure @param name the property name @param value the property value @param node the
	 * configure node (for errors)
	 */
	private static void setBeanPropertyMethod(Object obj, String name, String value) throws Exception
	{
		Method[] methods = obj.getClass().getMethods();

		Method method = getMethod(methods, "setProperty");
		if (method == null)
			method = getMethod(methods, "setAttribute");
		if (method == null)
			method = getMethod(methods, "put");
		if (method == null)
			method = getMethod(methods, "set");

		if (method == null)
			throw new RuntimeException("can't set property " + name);

		method.invoke(obj, new Object[] { name, value });
	}

	/**
	 * Returns the method matching the name.
	 */
	static private Method getMethod(Method[] methods, String name)
	{
		Method method = null;
		for (int i = 0; i < methods.length; i++) {
			method = methods[i];

			if (!Modifier.isPublic(method.getModifiers()))
				continue;

			if (method.getName().equals(name))
				return method;
		}

		return null;
	}

	/**
	 * Returns a set method matching the property name.
	 */
	public static Method getSetMethod(Class cl, String propertyName)
	{
		String setName = "set" + propertyNameToMethodName(propertyName);

		Method[] methods = cl.getMethods();
		for (int i = methods.length - 1; i >= 0; i--) {
			if (methods[i].getName().equals(setName) && methods[i].getParameterTypes().length == 1
					&& methods[i].getReturnType().equals(void.class))
				return methods[i];
		}

		return null;
	}

	/**
	 * Returns a get method matching the property name.
	 */
	public static Method getGetMethod(Class cl, String propertyName)
	{
		String getName = "get" + propertyNameToMethodName(propertyName);
		String isName = "is" + propertyNameToMethodName(propertyName);

		Method[] methods = cl.getMethods();
		for (int i = methods.length - 1; i >= 0; i--) {
			if (methods[i].getParameterTypes().length != 0 || methods[i].getReturnType().equals(void.class)) {
			} else if (methods[i].getName().equals(getName))
				return methods[i];
			else if (methods[i].getName().equals(isName) && methods[i].getReturnType().equals(boolean.class))
				return methods[i];
		}

		return null;
	}

	/**
	 * Converts a user's property name to a bean method name.
	 * 
	 * @param propertyName
	 *            the user property name
	 * @return the equivalent bean method name
	 */
	public static String propertyNameToMethodName(String propertyName)
	{
		char ch = propertyName.charAt(0);
		if (Character.isLowerCase(ch))
			propertyName = Character.toUpperCase(ch) + propertyName.substring(1);

		return propertyName;
	}

	/**
	 * Converts a user's property name to a bean method name.
	 * 
	 * @param methodName
	 *            the method name
	 * @return the equivalent property name
	 */
	public static String methodNameToPropertyName(String methodName)
	{
		if (methodName.startsWith("get"))
			methodName = methodName.substring(3);
		else if (methodName.startsWith("set"))
			methodName = methodName.substring(3);
		else if (methodName.startsWith("is"))
			methodName = methodName.substring(2);
		else
			return null;

		if (methodName.length() == 0)
			return null;

		char ch = methodName.charAt(0);
		if (Character.isUpperCase(ch) && (methodName.length() == 1 || Character.isLowerCase(methodName.charAt(1)))) {
			methodName = Character.toLowerCase(ch) + methodName.substring(1);
		}

		return methodName;
	}

	/**
	 * Java Beans에 값을 세팅한다.
	 * 
	 * @param obj
	 * @param name
	 * @param value
	 * @throws RuntimeException
	 */
	public static void setBeanProperty(Object obj, String name, Object value) throws RuntimeException
	{
		try {
			BeanInfo info = Introspector.getBeanInfo(obj.getClass());

			setBeanProperty(obj, name, value, info);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	/**
	 * Java Beans 에 값을 세팅한다.
	 * 
	 * @param obj
	 * @param name
	 * @param value
	 * @param info
	 * @throws RuntimeException
	 */
	public static void setBeanProperty(Object obj, String name, Object value, BeanInfo info) throws RuntimeException
	{
		if (name == null || value == null)
			throw new RuntimeException("unknown name");

		try {

			Method method = getSetMethod(obj.getClass(), name);

			method.invoke(obj, new Object[] { value });

		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	/**
	 * 해당 class 및 필드의 객체 type 이름을 가져온다.
	 * 
	 * @param c
	 * @param name
	 * @return
	 */
	public static String getTypeName(Class c, String name)
	{

		Method me = getSetMethod(c, name);

		Class type = me.getParameterTypes()[0];
		return type.getName();
	}

	/**
	 * Java Beans 에 값을 세팅한다.
	 * 
	 * @param obj
	 * @param name
	 * @param value
	 * @throws RuntimeException
	 */
	public static void setProperty(Object obj, String name, Object value) throws RuntimeException
	{
		if (name == null || value == null)
			throw new RuntimeException("unknown name");

		Method me = getSetMethod(obj.getClass(), name);

		if (me == null)
			throw new RuntimeException(name + "property not exist.");

		try {
			me.invoke(obj, new Object[] { value });
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	/**
	 * Java Beans 에 세팅된 값을 가져온다.
	 * 
	 * @param obj
	 * @param name
	 * @return
	 * @throws RuntimeException
	 */
	public static Object getProperty(Object obj, String name) throws RuntimeException
	{
		if (name == null)
			throw new RuntimeException("unknown name");

		Method me = getGetMethod(obj.getClass(), name);

		if (me == null)
			throw new RuntimeException(name + "property not exist.");

		try {
			return me.invoke(obj, new Object[] {});
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	private static Hashtable sHash = new Hashtable(); // set Method hashing

	private static Hashtable gHash = new Hashtable(); // get Method hashing

	/**
	 * get으로 시작하는 메쏘드를 배열로 반환한다.
	 * 
	 * @param c
	 * @return
	 */
	public static Method[] getGetMethods(Class c)
	{

		Method[] me = (Method[]) gHash.get(c);

		if (me == null) {
			// me = c.getMethods();
			me = c.getDeclaredMethods();

			ArrayList list = new ArrayList();

			String methodName;
			for (int j = 0; j < me.length; j++) // <-- 왜 9로 했을까?
			{
				methodName = me[j].getName();
				if (methodName.startsWith("get"))
					list.add(me[j]);
			}

			me = (Method[]) list.toArray(new Method[] {});

			gHash.put(c, me);
		}

		return me;

	}

	/**
	 * set 으로 시작하는 메쏘드를 배열로 반환한다.
	 * 
	 * @param c
	 * @return
	 */
	public static Method[] getSetMethods(Class c)
	{
		Method[] me = (Method[]) sHash.get(c);

		if (me == null) {
			// me = c.getMethods();
			me = c.getDeclaredMethods();

			ArrayList list = new ArrayList();

			String methodName;
			for (int j = 0; j < me.length; j++) // <-- 왜 9로 했을까?
			{
				methodName = me[j].getName();
				if (methodName.startsWith("set"))
					list.add(me[j]);
			}

			me = (Method[]) list.toArray(new Method[] {});

			sHash.put(c, me);
		}

		return me;
	}

}
