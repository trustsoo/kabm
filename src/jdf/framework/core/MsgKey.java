package jdf.framework.core;

/**
 * <b><code>MessageBox</code></b>
 * <p>
 * CODE -> 메세지로 변환하기 위한 Class
 * </p>
 * 
 * 
 * @author
 * @version 1.0
 */
public class MsgKey
{
	String key1;	//langcode(iso 2char)

	String key2;	//groupcode
	
	String key3;	//code
	
	String toStr;

	public MsgKey(String key1, String key2, String key3) {
		this.key1 = key1;
		this.key2 = key2;
		this.key3 = key3;
		toStr = key1 + "/" + key2 + "/" + key3;
	}

	public int hashCode()
	{
		return key1.hashCode() + key2.hashCode() +  key3.hashCode();
	}

	public String toString()
	{
		return this.toStr;
	}

	public boolean equals(Object obj)
	{
		// System.out.println("this:"+toString()+" obj:"+obj.toString());

		String tmp1 = toString();
		String tmp2 = null;
		if (obj != null)
			tmp2 = obj.toString();

		if (tmp1 != null && tmp1.equals(tmp2))
			return true;
		else if (tmp1 == null && tmp2 == null)
			return true;
		else
			return false;
	}
}