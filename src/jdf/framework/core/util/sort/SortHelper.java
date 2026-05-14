package jdf.framework.core.util.sort;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * <b><code>Sortable</code></b>
 * <p>
 * sort를 하기 위한 중간 매개체 클래스(Strategy)
 * 
 * </p>
 *
 * @author
 * @version 1.0
 */
public class SortHelper
{
	//내림차순
	public static final int ASC = 1;
	//올림차순
	public static final int DESC = -1;

	public static SortHelper helper;

	public synchronized static SortHelper getInstance()
	{
		if (helper == null)
			helper = new SortHelper();

		return helper;
	}

	private void rangeCheck(int arrayLen, int fromIndex, int toIndex) {
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex+")");
        if (fromIndex < 0)
            throw new ArrayIndexOutOfBoundsException(fromIndex);
        if (toIndex > arrayLen)
            throw new ArrayIndexOutOfBoundsException(toIndex);
    }
	
	private void lengthCheck(String[] fieldname, int[] order) throws SortException
	{
		if (fieldname.length != order.length)
		{
			throw new SortException("No Matching Field & Order Array Length.");
		}
	}

	
	/**
	 * 배열과 소트클래스, Comparator를 받아 소트한다.
	 * @param src 소트하고 싶은 배열
	 * @param fromIndex 시작인덱스
	 * @param toIndex 끝인덱스
	 * @param c 비교의 기준이 되는 Comparator
	 * @param s 소트방식 클래스
	 */
	public void sort(Object[] src, int fromIndex, int toIndex, Comparator c, Sortable s) {
        rangeCheck(src.length, fromIndex, toIndex);
        
		s.sort(src, fromIndex, toIndex, c);
    }

	/**
	 * 배열과 소트클래스, Comparator를 받아 소트한다.
	 * @param src 소트하고 싶은 배열
	 * @param fromIndex 시작인덱스
	 * @param toIndex 끝인덱스
	 * @param c 비교의 기준이 되는 Comparator
	 * @param s 소트방식 클래스
	 * @return Object[]
	 */
	public Object[] sortClone(Object[] src, int fromIndex, int toIndex, Comparator c, Sortable s) {
        rangeCheck(src.length, fromIndex, toIndex);
        
		Object clone[] = (Object[])src.clone();

		sort(clone, fromIndex, toIndex, c, s);
		
		return clone;
    }
	
	/**
	 * 배열과 소트클래스, Comparator를 받아 소트한다.
	 * @param src 소트하고 싶은 배열
	 * @param fromIndex 시작인덱스
	 * @param toIndex 끝인덱스
	 * @param fieldname 비교의 기준이 되는 Object의 fieldname
	 * @param order 정렬 (올림, 내림)
	 * @param s 소트방식 클래스
	 */
	public void sort(Object[] src, int fromIndex, int toIndex, String fieldname, int order, Sortable s) throws NoSuchFieldException, Exception
	{
        rangeCheck(src.length, fromIndex, toIndex);

		Comparator comp = new ObjectComparator(fieldname, order, null);
		
		s.sort(src, fromIndex, toIndex, comp);
    }

	/**
	 * 배열과 소트클래스, Comparator를 받아 소트한다.
	 * @param src 소트하고 싶은 배열
	 * @param fromIndex 시작인덱스
	 * @param toIndex 끝인덱스
	 * @param fieldname 비교의 기준이 되는 Object의 fieldname
	 * @param order 정렬 (올림, 내림)
	 * @param s 소트방식 클래스
	 */
	public Object[] sortClone(Object[] src, int fromIndex, int toIndex, String fieldname, int order, Sortable s) throws NoSuchFieldException, Exception
	{
        rangeCheck(src.length, fromIndex, toIndex);
		Object clone[] = (Object[])src.clone();
        
		sort(clone, fromIndex, toIndex, fieldname, order, s);

		return clone;
    }
	

	/**
	 * 배열과 소트클래스, 객체의 필드를 정의한 String배열를 받아 필드를 정렬 기준으로 소트한다.
	 * @param src 소트하고 싶은 배열
	 * @param fromIndex 시작인덱스
	 * @param toIndex 끝인덱스
	 * @param fieldname 비교의 기준이 되는 Object의 fieldname
	 * @param order 정렬 (올림, 내림)
	 * @param s 소트방식 클래스
	 */
	public void sort(Object[] src, int fromIndex, int toIndex, String[] fieldname, int[] order, Sortable s) throws NoSuchFieldException, Exception
	{
        lengthCheck(fieldname, order);
		rangeCheck(src.length, fromIndex, toIndex);
		
		Comparator comp = new ObjectComparator(fieldname[fieldname.length - 1], order[order.length - 1], null);

		Comparator nextcomp = comp;
		Comparator comp0 = null;
		for (int i = fieldname.length - 2; i > -1; i--){
			comp0 = new ObjectComparator(fieldname[i], order[i], nextcomp);
			nextcomp = comp0;
		}

		
		s.sort(src, fromIndex, toIndex, comp0);
    }

	/**
	 * 배열과 소트클래스, 객체의 필드를 정의한 String배열를 받아 필드를 정렬 기준으로 소트한다.
	 * @param src 소트하고 싶은 배열
	 * @param fromIndex 시작인덱스
	 * @param toIndex 끝인덱스
	 * @param fieldname 비교의 기준이 되는 Object의 fieldname
	 * @param order 정렬 (올림, 내림)
	 * @param s 소트방식 클래스
	 */
	public Object[] sortClone(Object[] src, int fromIndex, int toIndex, String[] fieldname, int[] order, Sortable s) throws NoSuchFieldException, Exception
	{
        rangeCheck(src.length, fromIndex, toIndex);
		Object clone[] = (Object[])src.clone();
		
		sort(clone, fromIndex, toIndex, fieldname, order, s);

		return clone;
    }



	
	/**
	 * Collection 객체와 소트클래스, Collection이 포함한 객체의 필드를 받아 필드를 기준으로 소트한다.
	 * @param src 소트하고 싶은 Collection
	 * @param src 소트하고 싶은 Object의 field
	 * @param fromIndex 시작인덱스
	 * @param toIndex 끝인덱스
	 * @param s 소트방식 클래스
	 */
	public void sort(Collection src, int fromIndex, int toIndex, String fieldname, int order, Sortable s) throws NoSuchFieldException, Exception
	{
        Object[] temp = src.toArray();
		sort(temp, fromIndex, toIndex, fieldname, order, s);

		src.clear();

		
		if (temp.length % 2 == 0)
		{
			for (int i = 0;i < temp.length ; i+=2 )
			{
				src.add(temp[i]);
				src.add(temp[i+1]);
			}
		}else{
			for (int i = 0;i < temp.length-1 ; i+=2 )
			{
				src.add(temp[i]);
				src.add(temp[i+1]);
			}
			src.add(temp[temp.length-1]);
		}

    }
	
	
	/**
	 * Collection 객체와 소트클래스, Collection이 포함한 객체의 필드를 받아 필드를 기준으로 소트한다.
	 * @param src 소트하고 싶은 Collection
	 * @param field 소트하고 싶은 Object의 field
	 * @param fromIndex 시작인덱스
	 * @param toIndex 끝인덱스
	 * @param s 소트방식 클래스
	 * @return Collection
	 */
	public Collection sortClone(Collection src, int fromIndex, int toIndex, String fieldname, int order, Sortable s) throws NoSuchFieldException, Exception
	{
		Collection clone = (Collection)src.getClass().newInstance();
		clone.addAll(src);

		sort(clone, fromIndex, toIndex, fieldname, order, s);
		return clone;
	}
	

	/**
	 * Collection 객체와 소트클래스, Collection이 포함한 객체의 필드를 정의한 String배열를 받아 필드를 order배열에 기준으로 소트한다.
	 * @param src 소트하고 싶은 Collection
	 * @param field 소트하고 싶은 Object의 field를 정의한 String 배열
	 * @param fromIndex 시작인덱스
	 * @param toIndex 끝인덱스
	 * @param s 소트방식 클래스
	 * @param s 정렬을 정의한 int배열(ASC, DESC)
	 * @return Collection
	 */
	public void sort(Collection src, int fromIndex, int toIndex, String[] fieldname, int[] order, Sortable s) throws NoSuchFieldException, Exception
	{
		Object[] temp = src.toArray();
		sort(temp, fromIndex, toIndex, fieldname, order, s);
		src.clear();

		
		if (temp.length % 2 == 0)
		{
			for (int i = 0;i < temp.length ; i+=2 )
			{
				src.add(temp[i]);
				src.add(temp[i+1]);
			}
		}else{
			for (int i = 0;i < temp.length-1 ; i+=2 )
			{
				src.add(temp[i]);
				src.add(temp[i+1]);
			}
			src.add(temp[temp.length-1]);
		}

    }

	/**
	 * Collection 객체와 소트클래스, Collection이 포함한 객체의 필드를 받아 필드를 기준으로 소트한다.
	 * @param src 소트하고 싶은 Collection
	 * @param field 소트하고 싶은 Object의 field
	 * @param fromIndex 시작인덱스
	 * @param toIndex 끝인덱스
	 * @param s 소트방식 클래스
	 * @return Collection
	 */
	public Collection sortClone(Collection src, int fromIndex, int toIndex, String[] fieldname, int[] order, Sortable s) throws NoSuchFieldException, Exception
	{
		Collection clone = (Collection)src.getClass().newInstance();
		clone.addAll(src);

		sort(clone, fromIndex, toIndex, fieldname, order, s);
		return clone;
	}



	private class ObjectComparator implements Comparator
	{
		private String fieldname;		// 해당 멤버 필드

		private int order = 1;		// 정렬 방식(양수: 내림차순(디폴트), 음수: 올림차순)

		private Comparator nextComparator;

		public ObjectComparator(String fieldname, int order, Comparator nextComparator)  throws NoSuchFieldException
		{
			this.fieldname = fieldname;
			
			this.order = order;
			this.nextComparator = nextComparator;
		}

		public int compare( Object o1, Object o2 )
		{
			int result = 0;
			
			try
			{
				Comparable f1 = getComparable(o1, fieldname);
				Comparable f2 = getComparable(o2, fieldname);
				
				result = f1.compareTo(f2) * order;
			}
			catch (IllegalAccessException e)
			{
				result = 0;
			}catch (Exception e){
				e.printStackTrace();
				result = 0;
			}

			if (nextComparator != null && result == 0)
				result = nextComparator.compare(o1, o2);

			return result;
		};

		private Comparable getComparable(Object o, String fieldname) throws IllegalAccessException, NoSuchFieldException
		{
			Class c = o.getClass();
	    
			Field field = getField(c);
			return (Comparable) field.get(o);
		}

		private Map map = new HashMap();
		
		private Field getField(Class c) throws NoSuchFieldException
		{
			/*
				필드명은 고려하지 않고, 클래스로만 Key로 사용하여 Field를 
				구별한 이유는 이미, ObjectComparator 객체를 생성할때, fieldname별로
				ObjectComparator 객체가 따로 생성되기때문에, 클래스로만 Key를 사용하였다.
			*/

			Field f = (Field) map.get(c);
			if (f == null)
			{
				f = c.getField(fieldname);
				map.put(c, f);
			}
			return f;
		}
		
	}
}