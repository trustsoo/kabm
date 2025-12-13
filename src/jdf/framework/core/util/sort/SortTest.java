package jdf.framework.core.util.sort;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class SortTest
{
	private static int max = 20;		//생성갯수

	public static void main(String[] args) throws Exception
	{
		if (args.length > 0)
			max = Integer.parseInt(args[0]);

		SortTest st = new SortTest();
		//st.testCollection();
		st.testArray();
		st.testArray();
		st.testArray();
		st.testArray();
		st.testArray();
		st.testArray();
	}

	/**
	 * SortHelper: Array 소트
	 * 배열의 경우, 해당 오브젝트에 대한 Comparator을 생성해야만 한다.
	 * 해당 필드의 객체는 Comparable을 구현해야 한다.
	 */
	public void testArray() throws Exception
	{
		Person[] sa = new Person[max];
		for (int i = 0;i < max ; i++ )
		{
			if ( i % 3 == 0)
				sa[i] = new Man( "id" + (int)((Math.random()*1322)%max), (int)((Math.random()*1322)%max) );
			else
				sa[i] = new Woman( "id" + (int)((Math.random()*1322)%max), (int)((Math.random()*1322)%max) );
			
		}

		SortFactory sf = SortFactory.newInstance(new MergeSort());
		sf.setField( new String[] {"id", "age"} );
		
		long time = System.currentTimeMillis();
		
		sf.sort(sa);
		
		System.out.println("Elapsed Time: " + (System.currentTimeMillis() - time));

		display(sa);
	}

	/**
	 * SortHelper: Collection 소트
	 * int, String, float등이 아닌 다른 객체를 기준으로 정렬하고자 한다면,
	 * 해당 필드의 객체는 Comparable을 구현해야 한다.
	 */
	public void testCollection() throws Exception
	{
		Vector vc = new Vector();
		for (int i = 0;i < max ; i++ )
		{
			vc.add(i, new Man( "id" + (int)((Math.random()*1322)%max), (int)((Math.random()*1322)%max) ) );
		}
		
		SortFactory sf = SortFactory.newInstance(new MergeSort());
		sf.setField("age");
		display(vc);

		long time = System.currentTimeMillis();

		sf.sort(vc);

		System.out.println("Elapsed Time: " + (System.currentTimeMillis() - time));
		
		display(vc);
		
	}
	

	public void display(Collection c)
	{
		StringBuffer string = new StringBuffer();
		Iterator it = c.iterator();
		Person person = null;
		while (it.hasNext() )
		{
			person = (Person)it.next();
			string.append( getString(person, "id") + " : " + getString(person, "age") + "\n");
		}
		//System.out.println(string);
	}

	public void display(Object[] c)
	{
		StringBuffer string = new StringBuffer();

		Person person = null;
		for (int i = 0; i < c.length ; i++ )
		{
			person = (Person) c[i];
			string.append( getString(person, "id") + " : " + getString(person, "age") + "\n");
		}
		System.out.println(string);
	}

	/**
     * Bean형태의 Entity의 값들을 문자열로 출력한다.
     *
     * Returns a String that represents the member variables of the sub class.
     * @return a string representation of the receiver
     */
    public String getString(Object o, String fieldName)
	{
	    Class c = o.getClass();
	    
	    
	    try
	    {
	        Field field = c.getField(fieldName);
	        return String.valueOf( field.get(o) );
        }
        catch(Exception e) {}
	    return "";
	}

	private abstract class Person
	{
		public abstract String toString();		
	};

	private class Man extends Person
	{
		public String id;
		public int age;

		public Man(String id, int age){
			this.id = id;
			this.age = age;
		}

		public String toString(){
			return id + " " + age;
		}
		
	};

	private class Woman extends Person
	{
		public String id;
		public int age;

		public Woman(String id, int age){
			this.id = id;
			this.age = age;
		}

		public String toString(){
			return id + " " + age;
		}
		
	};
}