package jdf.framework.core.util;

import jdf.framework.core.Entity;
import jdf.framework.core.data.DataSet;
import jdf.framework.core.io.FormatedEntity;
import jdf.framework.core.log.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class EntityUtil
{
	private static final String LOG_ID="<at:EntityUtil> ";
    public EntityUtil()
    {
    }

    public static FormatedEntity trans(FormatedEntity array[])
    {
        EntityArrayWrapper x = new EntityArrayWrapper();
        if(array != null)
        {
            //x.size = array.length;
            x.atom = array;
        }
        return x;
    }

    public static FormatedEntity trans(List<FormatedEntity> list)
    {
        FormatedEntity array[] = (FormatedEntity[])list.toArray(new FormatedEntity[0]);
        EntityArrayWrapper x = new EntityArrayWrapper();
        if(list != null)
        {
            //x.size = array.length;
            x.atom = array;
        }
        return x;
    }

    public static FormatedEntity trans(FormatedEntity header, FormatedEntity array[])
    {
        HeaderAndEntityArrayWrapper x = new HeaderAndEntityArrayWrapper();
        x.header = header;
        if(array != null)
        {
            //x.size = array.length;
            x.atom = array;
        }
        return x;
    }

    public static FormatedEntity trans(FormatedEntity header, List list)
    {
        FormatedEntity array[] = (FormatedEntity[])list.toArray(new FormatedEntity[0]);
        HeaderAndEntityArrayWrapper x = new HeaderAndEntityArrayWrapper();
        x.header = header;
        if(list != null)
        {
            //x.size = array.length;
            x.atom = array;
        }
        return x;
    }
    
    
    /**
     * Entity간에 복사를 한다.
     * <pre>source Entity보다 target Entity가 필드의 개수가 작아야 하며
     * target Entity의 필드는 source Entity에 같은 type, name으로 같은 순서대로
     * 존재하여야 한다.</pre>
     * @author advan94
     * @param src
     * @param target
     * @param length ( target Entity의 원하는 필드 개수)
     * @throws Exception
     */
    public static void convertEntity(Entity src, Entity target, int length) throws Exception
    {
        Class ori_c = src.getClass();
        Field[] ori_field = ori_c.getFields();        
        
        Class c = target.getClass();
        Field[] field = c.getFields();
        try
        {
            for(int idx=0; idx<length; idx++)
            {                
                //Object o = ori_field[idx].get(src);                
                Class fieldtype= field[idx].getType();             
                if(fieldtype == String.class)
                {
                    field[idx].set(target, ori_field[idx].get(src).toString());
                }
                else if(fieldtype == Integer.TYPE)
                {
                    field[idx].setInt(target, Integer.parseInt(ori_field[idx].get(src).toString()));
                }
                else if(fieldtype == Double.TYPE)
                {
                    field[idx].setDouble(target, Double.valueOf(ori_field[idx].get(src).toString()).doubleValue());
                }                
            }
        } catch(Exception ex)
        {
            System.out.println(LOG_ID+"converEntity Failed! : "+ex.toString());
        	throw ex;
        }
    }
    
    
    
    public static void convertEntity(Entity src, Entity target) throws Exception
    {
        Class ori_c = src.getClass();
        Field[] ori_field = ori_c.getFields();
        
        Class target_c = target.getClass();
        Field[] target_field = target_c.getFields();
        
        try
        {
            
            for(int idx=0; idx<target_field.length; idx++)
            {               
                Class target_filedType = target_field[idx].getType();
                String target_filedName = target_field[idx].getName();
                
                for(int kdx=0; kdx<ori_field.length; kdx++)
                {                   
                    String ori_filedName = ori_field[idx].getName();
                    
                    
                    if(target_filedName.equals(ori_filedName))
                    {
                        if(target_filedType == String.class)
                        {
                            target_field[idx].set(target, ori_field[idx].get(src).toString());
                        }
                        else if(target_filedType == Integer.TYPE)
                        {
                            target_field[idx].setInt(target, Integer.parseInt(ori_field[idx].get(src).toString()));
                        }
                        else if(target_filedType == Double.TYPE)
                        {
                            target_field[idx].setDouble(target, Double.valueOf(ori_field[idx].get(src).toString()).doubleValue());
                        }    
                    }
                }                
            }
        } catch(Exception ex)
        {
        	System.out.println(LOG_ID+"converEntity Failed! : "+ex.toString());
            throw ex;
        }
        
    }
    
    
    public static void dataSetToFormatedEntity(DataSet data, int size, FormatedEntity[] entity) throws Exception
	{
		
		//List list = new ArrayList();
		Iterator<Object> ite = data.keySet().iterator();
		
		List<Object> keys  = new ArrayList();
		while(ite.hasNext())
		{
			keys.add(ite.next());			
		}
		
		
		for(int idx=0; idx<size; idx++)
		{			
			Class<?> c = entity[idx].getClass();
			
			for(int kdx=0; kdx<keys.size(); kdx++)
			{
				try
				{
					String fname = (String) keys.get(kdx);
					Field field = c.getField(fname);				
					Class<?> fieldtype= field.getType();
					
					if(fieldtype == String.class)
	                {
	                    field.set(entity[idx], data.get(keys.get(kdx), idx).toString());
	                }
	                else if(fieldtype == Integer.TYPE)
	                {	
	                	field.setInt(entity[idx], Integer.parseInt(data.get(keys.get(kdx), idx).toString()));
	                }
	                else if(fieldtype == Double.TYPE)
	                {
	                    field.setDouble(entity[idx], Double.valueOf(data.get(keys.get(kdx), idx).toString()).doubleValue());
	                }				
				} catch(NoSuchFieldException nex)
				{
					Logger.warn.println(LOG_ID+"class : "+c.getName()+" : "+nex.toString());
				}
			}
		}
	}
    
    public static void dataSetToFormatedEntity(DataSet data, FormatedEntity mainEntity, FormatedEntity subEntity) throws Exception
    {
    	
    	/*if(subEntity == null)
    	{
    		Logger.debug.println(">>>>>>>>>>>>>>>>>"+data);
    	}*/
    	
    	Iterator<Object> ite = data.keySet().iterator();
		
		List<Object> keys  = new ArrayList<>();
		while(ite.hasNext())
		{
			keys.add(ite.next());			
		}
		
		FormatedEntity entity = null;
		
		if(subEntity == null)
			entity = mainEntity;
		else
			entity = subEntity;
		
		
		Class<?> c = entity.getClass();
		Field[] fields = c.getFields();
		
		for(int idx=0; idx<fields.length; idx++)
		{	
			Field field = fields[idx];			
			Class<?> fieldtype= field.getType();			
			
			try
			{
			
				if(fieldtype.getSuperclass() == FormatedEntity.class)
				{
					FormatedEntity child = null;
					
					try
					{	
						// 재귀호출 시 무한루프를 방지하기 위해
						if(field.get(entity) == null)
						{
							child = (FormatedEntity)fieldtype.newInstance(); 
						}else{
							child = (FormatedEntity)field.get(entity);
						}
						
					} catch(Exception ex)
					{
						child = (FormatedEntity)fieldtype.newInstance();
					}
					
					// 재귀호출 시 무한루프를 방지하기 위해
					if( child != null)		            	
						dataSetToFormatedEntity(data, entity, child);
					
	            	field.set(entity, child);
				}
				
				for(int kdx=0; kdx<keys.size(); kdx++)
				{
					try
					{
						String fname = (String) keys.get(kdx);				
						
						if(field.getName().equals(fname))
						{
							if(fieldtype == String.class)
				            {
								//Logger.debug.println("================>"+keys.get(kdx)+":"+data.get(keys.get(kdx)).toString());
								
				                field.set(entity, data.get(keys.get(kdx)).toString());
				            }
				            else if(fieldtype == Integer.TYPE)
				            {	
				            	field.setInt(entity, Integer.parseInt(data.get(keys.get(kdx)).toString()));
				            }
				            else if(fieldtype == Double.TYPE)
				            {
				                field.setDouble(entity, Double.valueOf(data.get(keys.get(kdx)).toString()).doubleValue());
				            }
						}
					} catch(Exception ex)
					{
						Logger.warn.println(LOG_ID+"(2)"+ex.toString());
					}
					
				}
				
				
			} catch(Exception ex)
			{
				Logger.warn.println(LOG_ID+"(1)"+ex.toString());
			}
			//System.out.println( c.getName() + ":" + idx);
		}		
    }
    
    
    public static void dataSetToFormatedEntity(DataSet data, FormatedEntity entity) throws Exception
	{
		
		//List list = new ArrayList();
		Iterator<Object> ite = data.keySet().iterator();
		
		List<Object> keys  = new ArrayList<>();
		while(ite.hasNext())
		{
			keys.add(ite.next());			
		}
		
		
		
		Class<?> c = entity.getClass();
		
		for(int kdx=0; kdx<keys.size(); kdx++)
		{
			try
			{
				String fname = (String) keys.get(kdx);
				Field field = c.getField(fname);				
				Class<?> fieldtype= field.getType();
				
				if(fieldtype == String.class)
	            {
	                field.set(entity, data.get(keys.get(kdx)).toString());
	            }
	            else if(fieldtype == Integer.TYPE)
	            {	
	            	field.setInt(entity, Integer.parseInt(data.get(keys.get(kdx)).toString()));
	            }
	            else if(fieldtype == Double.TYPE)
	            {
	                field.setDouble(entity, Double.valueOf(data.get(keys.get(kdx)).toString()).doubleValue());
	            } else if(fieldtype == FormatedEntity.class)
	            {
	            	dataSetToFormatedEntity(data, (FormatedEntity)fieldtype.newInstance());
	            	
	            }
			} catch(NoSuchFieldException nex)
			{
				//Logger.warn.println(LOG_ID+nex.toString());
			}
			
		}
	}
    
    public static DataSet convertDataSet(Entity src) throws Exception
    {
    	Class<?> ori_c = src.getClass();
        Field[] ori_field = ori_c.getFields();
        DataSet data = new DataSet();        
        try
        {       
            for(int idx=0; idx<ori_field.length; idx++)
            {                   
                String ori_filedName = ori_field[idx].getName();
                Object o = ori_field[idx].get(src);
                if(o == null)
                	data.put(ori_filedName, "");
                else
                	data.put(ori_filedName, ori_field[idx].get(src).toString());
            }                
            
        } catch(Exception ex)
        {
        	System.out.println(LOG_ID+"converEntity Failed! : "+ex.toString());
            throw ex;
        }
        return data;
    }
    
    
    public static void convertDataSet(FormatedEntity src, DataSet data) throws Exception
    {
    	
    	//Logger.debug.println("src input"+src.toString());
    	
    	Class<?> ori_c = src.getClass();
        Field[] ori_field = ori_c.getFields();
        
        if(data == null) data = new DataSet();
        
        try
        {       
            for(int idx=0; idx<ori_field.length; idx++)
            {                   
                String ori_filedName = ori_field[idx].getName();
                Class<?> fieldtype = ori_field[idx].getType();
                if(fieldtype.getSuperclass() == FormatedEntity.class)
                {
                	FormatedEntity child = null;
                	try
                	{
                		child = (FormatedEntity)ori_field[idx].get(src);
                	} catch(Exception ex)
                	{                		
                	}
                	
                	if(child == null) child = (FormatedEntity)fieldtype.newInstance();
                	
                	convertDataSet(child, data);
                } else if (fieldtype.isArray())
                {
                	Class entity = fieldtype.getComponentType();
                	
                	/*FormatedEntity child = null;                    
                    try 
                    {	
                        child = (FormatedEntity) entity.newInstance();
                    } catch(ClassCastException ccex) 
				    {
				        throw new ParsingException(entity.getName()+" type 배열은 아직 지원하지 않는 타입입니다");
				    }
                    
                    int childLen = FormatedEntity.getArraySum( child.getFieldsLength() ); //자식의 길이를 구한다.*/
                    int arrayNum = 0;                    
                    
                    try
                    {
                        FormatedEntity[] arryChild = (FormatedEntity[]) ori_field[idx].get( src );
                        arrayNum = arryChild.length;
                        
                        for(int kdx=0; kdx<arrayNum; kdx++)
                        {
                        	FormatedEntity atom = null;
                        	
                        	try
                        	{
                        		atom = (FormatedEntity)arryChild[kdx];//(FormatedEntity)fieldtype.newInstance();
                        	} catch(Exception ex)
                        	{
                        		throw ex;
                        	}
                        	
                        	if(atom == null) atom = arryChild[kdx].getClass().newInstance();
                        	
                        	convertDataSet(atom, data);
                        }
                    }
                    catch(Exception eee)
                    {
                    	eee.printStackTrace();
                    }
                }else
                {
                	Object o = ori_field[idx].get(src);
                	if(o == null)
                		data.add(ori_filedName, "");
                	else
                		data.add(ori_filedName, ori_field[idx].get(src).toString());
                }
            }                
            
        } catch(Exception ex)
        {
        	Logger.err.println(LOG_ID+"converEntity Failed! : "+ex.toString());
            throw ex;
        }
        
    }
    
    /** FormatedEntity 구조체를 key를 어트리뷰트로 하는 XML문서로 변경한다.
     * @param entity
     * @param xpath
     * @return
     * @throws Exception
     */
    public static String convertFormatedEntityToXML(FormatedEntity entity, String xpath) throws Exception
	{
		DataSet a = new DataSet();
		DataSet b = new DataSet();
		String result = null;
		try 
		{
			EntityUtil.convertDataSet(entity, a);
			Iterator ite = a.keySet().iterator();
			while(ite.hasNext())
			{					
				String key = (String)ite.next();					
				b.put("/"+xpath+"/"+key, a.getText(key)); 
			}
			result = b.getXmlString();
		} catch (Exception e) 
		{		
			throw e;
		}	
		return result;
		
	}
    
    
    
    /**
     * Entity간에 복사를 한다.
     * <pre>source Entity보다 target Entity가 필드의 개수가 작아야 하며
     * target Entity의 필드는 source Entity에 같은 type, name으로 같은 순서대로
     * 존재하여야 한다.</pre>
     * @author advan94
     * @param src
     * @param target
     * @return Entity
     * @throws Exception
     */
    /*public static Entity convertEntity(Entity src, Entity target) throws Exception
    {
        Class ori_c = src.getClass();
        Field[] ori_field = ori_c.getFields();        
        
        Class c = target.getClass();
        Field[] field = c.getFields();
        try
        {
            for(int idx=0; idx<field.length; idx++)
            {                
                Object o = ori_field[idx].get(src);                
                Class fieldtype= field[idx].getType();             
                if(fieldtype == String.class)
                {
                    field[idx].set(target, ori_field[idx].get(src).toString());
                }
                else if(fieldtype == Integer.TYPE)
                {
                    field[idx].setInt(target, Integer.parseInt(ori_field[idx].get(src).toString()));
                }
                else if(fieldtype == Double.TYPE)
                {
                    field[idx].setDouble(target, Double.valueOf(ori_field[idx].get(src).toString()).doubleValue());
                }                
            }
        } catch(Exception ex)
        {
            Logger.err.println("converEntity Failed! : "+ex.toString());
            throw ex;
        }
        return target;
    }*/
    
    
    
    
}
