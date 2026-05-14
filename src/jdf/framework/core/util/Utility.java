/**
 * @(#) Utility.java
 * @(#)
 * Copyright 1999-2000 by  LG-EDS Systems, Inc.,
 * Information Technology Group, Application Architecture Team,
 * Application Infrastructure Part.
 * 236-1, Hyosung-2dong, Kyeyang-gu, Inchun, 407-042, KOREA.
 * All rights reserved.
 *
 * NOTICE !      You can copy or redistribute this code freely,
 * but you should not remove the information about the copyright notice
 * and the author.
 *
 * @author  WonYoung Lee, wyounglee@lgeds.lg.co.kr.
 */

package jdf.framework.core.util;

import jdf.framework.core.log.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.Vector;


/**
 * Java Object 관련 Utility
 * 
 * @author
 *
 */
public final class Utility {

	/**
	 * Don't let anyone instantiate this class
	 */
	private Utility() {
	}

	/**
	 * Object[] 의 복제. Object의 Array 를 복제(clone)하여 새로운 Instance를 만들어 줍니다.
	 * 
	 * @param objects
	 *            java.lang.Object[]
	 * @return java.lang.Object[]
	 * 
	 * @see #clone(Object)
	 */
	public synchronized static Object[] clone(Object[] objects) {
		int length = objects.length;
		Class c = objects.getClass().getComponentType();
		Object array = Array.newInstance(c, length);

		for (int i = 0; i < length; i++) {
			Array.set(array, i, Utility.clone(objects[i]));
		}
		return (Object[]) array;
	}

	/**
	 * Object 의 복제. 일반적으로 <code>java.lang.Object.clone()</code> 함수를 를 사용하여
	 * Object를 복제하면 Object내에 있는 Primitive type을 제외한 Object field들은 복제가 되는 것이 아니라
	 * 같은 Object의 reference를 갖게 된다.<br>
	 * 그러나 이 Method를 사용하면 각 field의 동일한 Object를 새로 복제(clone)하여 준다.
	 * 
	 * @param object
	 *            java.lang.Object
	 * @return java.lang.Object
	 * 
	 * @see #clone(Object[])
	 * 
	 */
	public synchronized static Object clone(Object object) {
		Class c = object.getClass();
		Object newObject = null;
		try {
			newObject = c.newInstance();
		} catch (Exception e) {
			return null;
		}

		Field[] field = c.getFields();
		for (int i = 0; i < field.length; i++) {
			try {
				Object f = field[i].get(object);
				field[i].set(newObject, f);
			} catch (Exception e) {
			}
		}
		return newObject;
	}

	/**
	 * Vector 의 복제. 일반적으로 Vector Object를 clone()을 하면 Vector내의 Element Object는 새로
	 * 생성되는 것이 아니라 동일한 Object에 대한 reference만 새로 생성되기 때문에 같은 Element Object를
	 * reference하는 Vector를 생성하게 된다. 그러나 이 method를 사용하면 Vector내의 모든 Element
	 * Object도 새로 복제하여 준다.
	 * 
	 * @param objects
	 *            java.util.Vector
	 * @return java.util.Vector
	 * 
	 * @see #clone(Object)
	 * @see #clone(Object[])
	 */
	public synchronized static Vector clone(Vector objects) {
		Vector newObjects = new Vector();
		Enumeration e = objects.elements();
		while (e.hasMoreElements()) {
			Object o = e.nextElement();
			newObjects.addElement(Utility.clone(o));
		}
		return newObjects;
	}

	/**
	 * Entity Class의 null string field 초기화.
	 * <p>
	 * Entity class 내에 있는 java.lang.String형의 field는 DB의 Column과 밀접한 연관이 있는 경우가
	 * 많다. 이러한 Entity Field가 특히 GUI의 특정 TextFiled에 표현되어야 하는 경우도 많다. 만약 그 String
	 * Filed가 null일 경우 일일이 검사를 한다는 것은 참으로 답답한 일이 아닐 수 없다.
	 * <p>
	 * 이 method는 여하한의 Object 내에 있는 모든 java.lang.String형의 field 변수 중 null 값으로 된
	 * field를 길이가 0 인 blank string("")으로 초기화 시켜준다. 만약 null이 아닌 정상적인 String이 대입되어
	 * 있으면 강제적으로 trim()를 시켜준다.
	 * <p>
	 * 이 때 trim() 함수는 java.lang.String 의 trim()을 사용하지 않았다.
	 * 
	 * <xmp> Sample Code: public java.util.Vector selectAll() throws Exception {
	 * java.util.Vector list = new Vector(); Statement stmt = null; ResultSet rs
	 * =null; try{ stmt = conn.createStatement(); String query = "select " +
	 * "id, " + "name, " + "desc " + "from THE10 " + "order by id ";
	 * 
	 * rs = stmt.executeQuery(query);
	 * 
	 * while ( rs.next() ) { AdminAuth entity = new AdminAuth(); entity.id =
	 * rs.getString("id"); entity.name = rs.getString("name"); entity.desc =
	 * rs.getString("desc"); Utility.fixNull(entity); list.addElement(entity); } }
	 * finally { try{rs.close();}catch(Exception e){}
	 * try{stmt.close();}catch(Exception e){} } return list; } </xmp
	 * 
	 * @param java.lang.Object
	 *            Object내의 public java.lang.String 형의 member variable에만 영향을 준다.
	 * 
	 * @see #fixNullAll(Object)
	 * @see #trim(String)
	 */
	public synchronized static void fixNull(Object o) {
		if (o == null)
			return;

		Class c = o.getClass();
		if (c.isPrimitive())
			return;

		Field[] fields = c.getFields();
		for (int i = 0; i < fields.length; i++) {
			try {

				Object f = fields[i].get(o);
				Class fc = fields[i].getType();

				if (fc.getName().equals("java.lang.String")) {
					int mod = fields[i].getModifiers();
					if (Modifier.isStatic(mod) && Modifier.isFinal(mod))
						continue;

					if (f == null)
						fields[i].set(o, "");
					else {
						// String item = Utility.trim( (String)f );
						String item = ((String) f).trim();
						fields[i].set(o, item);
					}
				}
			} catch (IllegalAccessException e) {
				Logger.warn.println("fixNull Exception : " + c.getName() + "->"
						+ fields[i].getName());
			}
		}
	}

	/**
	 * Entity Class의 재귀적인 null string field 초기화.
	 * <p>
	 * fixNull() 과 유사한 기능을 하는데, java.lang.String field 뿐만 아니라 Member 변수 중 Array,
	 * Object 가 있으면 재귀적으로 따라 가서 String형을 blank string("")으로 만들어 준다.<br>
	 * 정상적인 String인 경우 trim()을 시켜준다.<br>
	 * 만약 Array나, Vector가 null일 경우 Instance화는 하지 않는다.
	 * 
	 * <p>
	 * 재귀적으로 추적되는 만큼, 부모와 자식간에 서로 양방향 reference를 갖고 있으면 절대 안된다. 아마 Stack
	 * Overflow를 내며 JVM을 내릴 것이다.
	 * 
	 * @param java.lang.Object
	 *            Object내의 public String 형뿐만 아니라, Object[], Vector 등과 같은 public
	 *            Object형 Member Variable에 영향을 준다.
	 * 
	 * @see #fixNull(Object)
	 * @see #trim(String)
	 * 
	 * @author 김형기, 이원영
	 */
	public synchronized static void fixNullAll(Object o) {
		if (o == null)
			return;

		Class c = o.getClass();
		if (c.isPrimitive())
			return;

		if (c.isArray()) {
			int length = Array.getLength(o);
			if (length < 1)
				return;
			for (int i = 0; i < length; i++) {
				Object element = Array.get(o, i);
				Utility.fixNullAll(element);
			}
		} else {
			Field[] fields = c.getFields();
			for (int i = 0; i < fields.length; i++) {
				try {
					Object f = fields[i].get(o);
					Class fc = fields[i].getType();
					if (fc.isPrimitive())
						continue;
					if (fc.getName().equals("java.lang.String")) {
						if (f == null)
							fields[i].set(o, "");
						else {
							// String item = Utility.trim( (String)f );
							String item = ((String) f).trim();
							fields[i].set(o, item);
						}
					} else if (f != null) {
						Utility.fixNullAll(f);
					} else {
					} // Some Object, but it's null.
				} catch (IllegalAccessException e) {
					Logger.warn.println("fixNullAll Exception : " + c.getName()
							+ "->" + fields[i].getName());
				}
			}
		}
	}

	/**
	 * 두 객체사이에서 동일 필드의 값을 일치 시킨다.
	 * 
	 * @param source
	 *            소스 객체
	 * @param target
	 *            타켓 객체
	 */
	public static void matchFieldValue(Object source, Object target)
			throws IllegalAccessException {
		Class sc = source.getClass();
		Field[] sfields = ReflectUtil.getDeclaredFields(sc); // sc.getFields();

		Class tc = target.getClass();
		Field[] tfields = ReflectUtil.getDeclaredFields(tc); // tc.getFields();

		for (int i = 0; i < sfields.length; i++) {
			for (int j = 0; j < tfields.length; j++) {
				if (sfields[i].getName().equals(tfields[j].getName())
						&& sfields[i].getClass() == tfields[j].getClass()) {
					tfields[j].set(target, sfields[i].get(source));
					break;
				}
			}
		}
	}

	/**
	 * Remove special white space from both ends of this string.
	 * <p>
	 * All characters that have codes less than or equal to
	 * <code>'&#92;u0020'</code> (the space character) are considered to be
	 * white space.
	 * <p>
	 * java.lang.String의 trim()과 차이점은 일반적인 white space만 없애는 것이 아니라 위에서와 같은 특수한
	 * blank도 짤라 준다.<br>
	 * 이 소스는 IBM HOST와 데이타를 주고 받을 때 유용하게 사용했었다. 일반적으로 많이 쓰이지는 않을 것이다.
	 * 
	 * @param java.lang.String
	 * @return java.lang.String this string, with white space removed from the
	 *         front and end.
	 */

	public static synchronized String trim(String s) {
		int st = 0;
		char[] val = s.toCharArray();
		int count = val.length;
		int len = count;

		while ((st < len) && ((val[st] <= ' ') || (val[st] == '\t')))
			st++;
		while ((st < len) && ((val[len - 1] <= ' ') || (val[len - 1] == '\t')))
			len--;
		// while ((st < len) && ((val[len - 1] <= ' ') || (val[len-1] == ' ')))
		// len--;

		return ((st > 0) || (len < count)) ? s.substring(st, len) : s;
	}

	/**
	 * 배열형태의 String 에서 좌우 여백및 tab을 제거한 String array를 반납한다.
	 * 
	 * @param java.lang.String[]
	 * @return java.lang.String[]
	 */
	public static String[] trim(String[] src) {
		if (src == null)
			return null;

		for (int i = 0; i < src.length; i++) {
			src[i] = trim(src[i]);
		}

		return src;

	}
	
	public static String getStackTrace(Throwable e) 
    {
    	ByteArrayOutputStream bos = null;
    	PrintWriter writer = null;    	
    	try
		{
    		bos = new ByteArrayOutputStream();
    		writer = new PrintWriter(bos);
    		e.printStackTrace(writer);
    		
		} catch(Exception ex)
		{
		} finally
		{
			writer.flush();
			writer.close();			
		}
		return bos.toString();
	}
	
	public static void main(String arg[]) {

		String test = " tewst asdfa \t ";

		test = trim(test);

		System.out.println("-" + test + "-");
	}

}
