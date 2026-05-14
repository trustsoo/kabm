package jdf.framework.core.util;

/**
 * <b><code>SmartStringArray</code></b>
 * <p>
 * 일반 구분자로 구분되 문자열을 String Array로 변환하거나, 반대의 경우도 수행한다.
 * 
 * </p>
 * 
 * @author
 * @version 1.0
 */

public class SmartStringArray
{

	int sp = 0; // "stack pointer" to keep track of position in the array

	private String[] array;

	private int growthSize;

	public SmartStringArray() {
		this(1024);
	}

	public SmartStringArray(int initialSize) {
		this(initialSize, (int) (initialSize / 4));
	}

	public SmartStringArray(int initialSize, int growthSize) {
		this.growthSize = growthSize;
		array = new String[initialSize];
	}

	public void add(String i)
	{
		if (sp >= array.length) { // time to grow!
			String[] tmpArray = new String[array.length + growthSize];
			System.arraycopy(array, 0, tmpArray, 0, array.length);
			array = tmpArray;
		}
		array[sp] = i;
		sp += 1;
	}

	public String[] toArray()
	{
		String[] trimmedArray = new String[sp];
		System.arraycopy(array, 0, trimmedArray, 0, trimmedArray.length);
		return trimmedArray;
	}

	/**
	 * 문자열을 정의된 구분자에 의해 배열로 반환한다.
	 * 
	 * @param token
	 *            구분자
	 * @param string
	 *            배열로 변환하고자 하는 문자
	 * @return string[] 배열
	 */
	public static String[] split(String token, String string)
	{

		if (string == null || string.length() == 0)
			return new String[] {};

		SmartStringArray ssa = new SmartStringArray();

		int previousLoc = 0;
		int loc = string.indexOf(token, previousLoc);

		if (loc != -1) {

			do {
				ssa.add(string.substring(previousLoc, loc));
				previousLoc = (loc + token.length());
				loc = string.indexOf(token, previousLoc);
			} while ((loc != -1) && (previousLoc < string.length()));

			ssa.add(string.substring(previousLoc));

		} else
			ssa.add(string);

		return (ssa.toArray());
	}

	/**
	 * <pre>
	 *  &quot;001&quot;,&quot;name&quot;,&quot;apple,tomato&quot; 을 구분하기 위해
	 *  token 은 ,
	 *  skipBlockStr 은 &quot; 가 된다.
	 *  
	 * 
	 * </pre>
	 * 
	 * @param token
	 * @param string
	 * @param skipBlockStr
	 * @return
	 */
	public static String[] split(String token, String string, String skipBlockStr)
	{
		string = StringFormater.replaceStr(string, (skipBlockStr + skipBlockStr), skipBlockStr);
		
		if (skipBlockStr == null)
			return split(token, string);

		if (string == null || string.length() == 0)
			return new String[] {};

		SmartStringArray ssa = new SmartStringArray();

		int previousLoc = 0;
		int loc = string.indexOf(token, previousLoc);

		if (loc != -1) {
			String preTmpStr = null;

			do {
				String tmp = string.substring(previousLoc, loc);
				
				int s = tmp.indexOf(skipBlockStr);
				
				
				
				if (s == 0) {
					int e = tmp.lastIndexOf(skipBlockStr);
					if (e == tmp.length()-1 && e!=s) {
						ssa.add(tmp.substring(s + 1, e));
						preTmpStr = null;
					} else if (preTmpStr != null) {
						ssa.add(preTmpStr +token+ tmp.substring(0, s));
						preTmpStr = null;
					}
					else {
						preTmpStr = tmp.substring(s + 1);
					}

				} else if (preTmpStr != null) {
					if(s==tmp.length()-1) {
						ssa.add(preTmpStr + token+tmp.substring(0, s));
						preTmpStr=null;
					}
					else {
						preTmpStr = preTmpStr + token+tmp;
					}
					
				} else {
					ssa.add(tmp);
					preTmpStr = null;
				}

				previousLoc = (loc + token.length());
				loc = string.indexOf(token, previousLoc);
			} while ((loc != -1) && (previousLoc < string.length()));

			String tmp = string.substring(previousLoc);
			
			
			
			if (preTmpStr != null) {
				int e = tmp.lastIndexOf(skipBlockStr);
				if (e > 0) {
					tmp = tmp.substring(0, e);
					
				}
				else if(e==0)
					tmp="";
				tmp = preTmpStr + token+tmp;
			}
			else {
				int s = tmp.indexOf(skipBlockStr);
				if(s==0) {
					int e = tmp.lastIndexOf(skipBlockStr);
					if(e>s) {
						tmp = tmp.substring(s+1, e);
					}
				}
			}
			ssa.add(tmp);

		} else {
			ssa.add(string);
			System.out.println(" 3 "+string);
		}

		return (ssa.toArray());
	}

	/**
	 * 배열을 구분자로 구분된 문자열로 반환한다.
	 * 
	 * @param token
	 *            구분자
	 * @param string
	 *            배열
	 * @return string 구분자가 들어간 문자열
	 */
	public static String join(String token, String[] strings)
	{

		if (strings == null || strings.length == 0)
			return "";

		StringBuffer sb = new StringBuffer();

		for (int x = 0; x < (strings.length - 1); x++) {
			sb.append(strings[x]);
			sb.append(token);
		}
		sb.append(strings[strings.length - 1]);

		return (sb.toString());
	}

	public static void main(String[] args)
	{

		String[] tmp = split(",", "");
		System.out.println(tmp.length);

	}

}