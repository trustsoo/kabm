package jdf.framework.core.util;

import jdf.framework.core.log.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Hashtable;
import java.util.Properties;


/**
 * java.lang.reflect.* 의 속도문제를 어느정도 해결하고자 하는 Class Class에서 getFields() method의
 * 느린문제를 cache를 통해서 어느정도 해결한다. 
 * [수정사항] IBM의 JDK의 경우, 필드를 가져올때,
 * 순서가 거꾸로 반환이 된다. 따라서, JAVA Vendor를 판별하여, 정상적으로 동작할수 있게 정형화 시킴
 */
public final class ReflectUtil
{

    private final static Hashtable fHash = new Hashtable(); // Field hashing

    //private final static Hashtable dfHash = new Hashtable(); // Field hashing

    private static String JAVA_VENDOR;
    
    private static boolean isReverse = false;

    static
    {
        Properties p = System.getProperties();
        JAVA_VENDOR = p.getProperty("java.vendor").toLowerCase();
        
        // IBM JDK 는 이상하다... 필드가 꺼구로 나오다니...
        if (JAVA_VENDOR.equals("ibm corporation"))
            isReverse = true;
        
        
        if( "false".equals( p.getProperty("autoFieldReverse")) )
            isReverse=false;
        
        
        Logger.info.println("<jdf.framework.core.util.ReflectUtil> Field 순서 변환 "+ isReverse);
    }

    private static Field[] normalize(Field[] fields)
    {
        // IBM의 JVM의 경우, 필드들의 순서가 거꾸 반환된다.
        if (isReverse)
        {
            Field[] newfields = new Field[fields.length];

            for (int i = 0; i < newfields.length; i++)
            {
                newfields[i] = fields[fields.length - i - 1];
            }

            return newfields;
        }

        return fields;
    }

    public static Field[] getFields(Class c)
    {
        Field[] field = (Field[]) fHash.get(c);

        if (field == null)
        {
            field = normalize(c.getFields());

            fHash.put(c, field);
        }

        return field;
    }

    private static Hashtable fHash2 = new Hashtable();

    /**
     * class의 field 명에 해당하는 Field 반환
     * 
     * @param c
     * @param fieldName
     * @param isCache
     * @return
     */
    public static Field getField(Class c, String fieldName, boolean isCache)
    {
        try
        {
            String key = c.getClass().getName() + fieldName;
            Field f = null;
            
            if(isCache)
                f = (Field) fHash2.get(key);
            if (f == null)
            {
                Field[] fields = getFields(c);

                for (int i = 0; i < fields.length; i++)
                {

                    if (fieldName.equals(fields[i].getName()))
                    {
                        f = fields[i];
                        break;
                    }

                }
                if (isCache && f != null)
                    fHash2.put(key, f);
            }
            return f;
        } catch (Exception e)
        {
            return null;
        }
    }
    
    public static Field getField(Class c, String fieldName)
    {
        return getField(c, fieldName, true);

    }

    /**
     * Class의 field array 반환
     * 
     * @param c
     * @return
     */
    public static Field[] getDeclaredFields(Class c)
    {
        return getFields(c);
        /*
        Field[] field = (Field[]) dfHash.get(c);
        

        if (field == null)
        {
            field = normalize(c.getDeclaredFields());
            dfHash.put(c, field);
        }

        return field;
        */
    }

    /**
     * 익명의 오브젝트의 메소드를 실행시킨다. Mathod객체의 invoke를 쓰기 쉽게 만들었다.
     * 
     * 예) ReflectionUtil.invoke( obj, "println", new Object[]{new
     * String("haha")} );
     * 
     * @param obj
     *            실행시키고자 하는 Object 객체
     * @param methodName
     *            메소드명
     * @param params
     *            메소드에 넘겨질 Object 배열
     * 
     */
    public static Object invoke(Object obj, String methodName, Object[] params) throws InvocationTargetException,
            IllegalAccessException
    {
        try
        {
            Method[] methods = obj.getClass().getMethods();

            Method method = getMethod(methods, methodName);

            Object result = null;

            if (method != null)
            {
                result = method.invoke(obj, params);
            }
            else
            	throw new IllegalAccessException(methodName+" method is not exist");

            return result;
        } catch (InvocationTargetException e)
        {
            throw e;
        } catch (IllegalAccessException e)
        {
            throw e;
        }
    }

    /**
     * 
     * @param methods
     * @param name
     * @return
     */
    private static Method getMethod(Method[] methods, String name)
    {
        Method method = null;
        for (int i = 0; i < methods.length; i++)
        {
            method = methods[i];
            if (!Modifier.isPublic(method.getModifiers()))
                continue;
            if (method.getName().equals(name))
                return method;
        }
        return null;
    }

}