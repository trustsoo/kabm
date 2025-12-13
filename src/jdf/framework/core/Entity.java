package jdf.framework.core;



import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;


/**
 * <b><code>Entity</code></b>
 * <p>
 * 의미있는 정보를 가지는 가장 작은 단위를 나타내는 Class
 * </p>
 *
 * @author  WonYoung Lee, wyounglee@lgeds.lg.co.kr.
 * @version 1.0
 */
 

public abstract class Entity implements java.io.Serializable {

    /**
     * EntityData default constructor
     */
    public Entity() {
        super();
    }

    /**
     * Entity의 값들을 문자열로 출력한다.
     * Returns a String that represents the member variables of the sub class.
     * @return a string representation of the receiver
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
            
        Class c = this.getClass();
        String fullname = c.getName();
            
        buf.append(fullname + ":{");
        Field[] fields = c.getFields();
        
        if (fields.length == 0)
            return BeantoString();
        
        for (int i=0 ; i<fields.length; i++) {
            try {
                if ( i != 0 ) buf.append(',');
                buf.append(fields[i].getName() + '=');
                Object f = fields[i].get(this);
                Class fc = fields[i].getType();
                if(fc.getComponentType() != Byte.TYPE){
                    if ( fc.isArray() ) {
                        buf.append('[');
                        int length = Array.getLength(f);
                        for(int j=0; j<length ;j++){
                            if ( j != 0 ) buf.append(',');
                            Object element = Array.get(f, j);
                            buf.append(element.toString());
                        }
                        buf.append(']');
                    }
                    else    
                        buf.append(f.toString());
                }
            }
            catch(Exception e) {}
        }
        buf.append('}');
        return buf.toString();
    }
    
    
    
    
    
    private String BeantoString()
    {
        StringBuffer buf = new StringBuffer();
        Method[] me = getGetMethods();
        String propertyName;
        
        for (int i=0 ; i<me.length; i++) 
        {
            try {
                        
                propertyName = me[i].getName();
                
                if ( i != 0 ) buf.append(',');
                buf.append( methodNameToPropertyName(propertyName) + '=');
                
                
                Object f = me[i].invoke(this,null);
                Class fc = me[i].getReturnType();
                
                if(fc.getComponentType() != Byte.TYPE){
	                if ( fc.isArray() ) {
	                    buf.append('[');
	                    int length = Array.getLength(f);
	                    for(int j=0; j<length ;j++){
	                        if ( j != 0 ) buf.append(',');
	                        Object element = Array.get(f, j);
	                        buf.append(element.toString());
	                    }
	                    buf.append(']');
	                }
	                else    
	                    buf.append(String.valueOf(f));
                }
            }
            catch(Exception e) {}
        }
        
        return buf.toString();
        
    }
    
    
    
    
    private Method[] getGetMethods()
    {
    
        Method[] me = this.getClass().getMethods();
        
        ArrayList list = new ArrayList();
        
        String methodName;
        for(int j=9; j< me.length; j++)
        {
            methodName = me[j].getName();
            if( methodName.startsWith("get") )
                list.add( me[j]);
        }
        
        return (Method[]) list.toArray(new Method[] {});
    }
    
    
    private static String methodNameToPropertyName(String methodName)
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
        if (Character.isUpperCase(ch) &&
            (methodName.length() == 1 ||
             Character.isLowerCase(methodName.charAt(1)))) {
          methodName = Character.toLowerCase(ch) + methodName.substring(1);
        }
        
        return methodName;
    }

    

	
}
