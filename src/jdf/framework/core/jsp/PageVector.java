package jdf.framework.core.jsp;

import jdf.framework.core.log.Logger;

import java.util.Collection;
import java.util.Vector;

/**
 * The <code>PageVector</code> class implements a page-readable Vector.
 * @author  JeongHoon Baek
 * @version 1.0, 10/29/99
 * @see Vector
 */
public class PageVector extends Vector
{
	/**
	 * 페이지의 크기
	 * @serial
	 */
	protected int pageCount;

	/**
	 * 현재 페이지의 시작을 가리키는 인덱스
	 * @serial
	 */
	protected int index;

	/**
	 * 페이지의 마지막?
	 * @serial
	 */
	protected boolean startOfPage = true;
	protected boolean endOfPage = false;
	protected int startrow;

	/**
	* 블록의 처음 페이지를 지정
	* ex) 1 2 3 4 5 ==> startpg : 1,
	*     6 7 8 9 10 ==> startpg : 6
	*/
	public int startpg;

	/**
	* 블록의 끝 페이지를 지정
	* ex) 1 2 3 4 5 ==> endpg : 5,
	*     6 7 8 9 10 ==> block : 10
	*/
	public int endpg;

	/**
		 * 페이징 단위(블록)를 지정
		 * ex) 1 2 3 4 5 ==> numNum : 5,
		 *     1 2 3 4 5 6 7 8 9 10 ==> numNum : 10
		 */
	protected int numNum;

	/**
	* 블록 구분자.
	* ex) <br>1 2 3 4 5 || 6 7 8 9 10 <br>
	*     ---------	-----------<br>
	*        1				 2
	*/
	public int block;

	/**
	 * 새로운 페이지벡터 생성(페이지크기 = 20)
	 */
	public PageVector()
	{
		this(20);
	}

	/**
	 * 새로운 페이지벡터 생성
	 * @param	pageCount	페이지의 크기 지정
	 */
	public PageVector(int pageCount)
	{
		this(pageCount, 10);
	}

	/**
	 * 새로운 페이지벡터 생성
	 * @param	pageCount	페이지의 크기 지정
	 */
	public PageVector(int pageCount, int initialCapacity)
	{
		this(pageCount, initialCapacity, 0);
	}

	/**
	 * 새로운 페이지벡터 생성
	 * @param	pageCount	페이지의 크기 지정
	 */
	public PageVector(int pageCount, int initialCapacity, int capacityIncrement)
	{
		super(initialCapacity, capacityIncrement);

		this.pageCount = pageCount;
		index = -pageCount;
	}

	/**
	 * 새로운 페이지벡터 생성
	 * @param	pageCount	페이지의 크기 지정
	 */
	// not use in 1.1.8

	public PageVector(int pageCount, Collection c)
	{
		super(c);

		this.pageCount = pageCount;
		index = -pageCount;
	}

	/**
	 * 페이지당 개수 return
	 * @return	현재 페이지의 원소 개수
	 */
	public int getPageCount()
	{
		return isLastPage() ? (elementCount - index) : pageCount;
	}

	/**
	 * 현재 페이지번호 return
	 * @return	현재 페이지번호
	 */
	public int getPageNo()
	{
		return (index / pageCount) + 1;
	}

	/**
	 * 페이지개수 return
	 * @return	페이지개수
	 */
	public int getMaxPageNo()
	{
		return ((elementCount % pageCount) > 0) ? elementCount / pageCount + 1 : elementCount / pageCount;
	}

	/**
	 * 현재 페이지을 배열로 return(인덱스 변화 없음)
	 * @return	벡터에 저장된 오브젝트 배열
	 */
	// not use 1.1.8
	/*public Object[] getPage()
	{
	   startrow = index ;
		return subList(startrow, endrow ).toArray();
	}*/

	public Collection getPage()
	{
		startrow = index;
		//Logger.debug.println( "start : " + startrow );
		//Logger.debug.println( "end : " + index + getPageCount() );
		return subList(startrow, index + getPageCount());
	}

	/**
	 * 맨 처음 페이지을 return하고 인덱스 증가시킴
	 * @return	벡터에 저장된 오브젝트 배열
	 */
	/*public Object[] firstPage()
	{
		index = 0;
		return getPage();
	}*/

	public Collection getFirstPage()
	{
		index = 0;
		return getPage();
	}

	/**
	 * 이전 페이지을 return하고 인덱스 증가시킴
	 * @return	벡터에 저장된 오브젝트 배열
	 */
	public Collection getPrevPage()
	{
		if (index >= pageCount)
			index -= pageCount;
		else
			index = 0;
		return getPage();
	}

	/**
	 * 다음 페이지을 return하고 인덱스 증가시킴
	 * @return	벡터에 저장된 오브젝트 배열
	 */
	public Collection getNextPage()
	{
		if ((index + pageCount) < elementCount)
		{
			endOfPage = false;
			index += pageCount;
		}
		else
		{
			endOfPage = true;
		}

		return getPage();
	}

	/**
	 * 맨 마지막 페이지을 return하고 인덱스는 맨 마지막 페이지를 가리킴
	 * @return	벡터에 저장된 오브젝트 배열
	 */
	public Collection getLastPage()
	{
		//index = (elementCount / pageCount) * pageCount;

		if (elementCount >= pageCount)
			index = elementCount - (elementCount % pageCount);

		return getNextPage();
	}

	/**
	 * 지정한 페이지 번호에 해당하는 페이지을 return하고 인덱스 증가시킴
	 * @param	ord	페이지번호
	 * @return	벡터에 저장된 오브젝트 배열
	 */
	public Collection getOrdinalPage(int ord)
	{
		index = (pageCount * ord) - pageCount;

		if (index >= elementCount)
			return getLastPage();

		return getPage();
	}

	public void setOrdinalPage(String ord)
	{
		try
		{
			setOrdinalPage(Integer.parseInt(ord));
		}
		catch (Exception ex)
		{
			setOrdinalPage(1);
		}

	}

	public void setOrdinalPage(String ord, int numNum)
	{
		try
		{
			setOrdinalPage(Integer.parseInt(ord), numNum);
		}
		catch (Exception ex)
		{
			setOrdinalPage(1);
		}

	}

	public void setOrdinalPage(int ord)
	{
		setOrdinalPage(ord, 10);
	}

	public void setOrdinalPage(int ord, int numNum)
	{
		index = (pageCount * ord) - pageCount;

		if (numNum < 1)
		{
			this.numNum = 10;
		}
		else
		{
			this.numNum = numNum;
		}

		if (index >= elementCount)
		{
			index = getLastPageNo();
		}
		setBlock(ord); //
	}

	public int getStartRow()
	{
		//Logger.debug.println( "start : " + index );
		return this.index;
	}

	public int getEndRow()
	{

		//Logger.debug.println( "end : " + index + getPageCount() );
		return index + getPageCount();
	}

	/**
	 * 마지막 페이지 체크
	 * @return	boolean (마지막 페이지)
	 */
	public boolean isLastPage()
	{
		return getPageNo() >= getMaxPageNo();
	}

	/**
	 * 첫 페이지 체크
	 * @return	boolean
	 */
	public boolean isFirstPage()
	{
		return (getPageNo() == 1);
	}

	public int getNextPageNo()
	{
		if (getPageNo() < getMaxPageNo())
		{
			return (getPageNo() + 1);
		}
		else
		{
			return getMaxPageNo();
		}
	}

	public int getPrevPageNo()
	{
		if (getPageNo() <= 1)
			return 1;
		else
			return (getPageNo() - 1);
	}

	public int getLastPageNo()
	{
		return getMaxPageNo();
	}

	/**
	* 다음 블록의 시작페이지를 리턴한다.
	* <PRE></PRE>
	*
	* @param none
	*
	* @return int
	*/
	public int getNextPageUnit()
	{
		return (block + 1) * numNum + 1;
	}

	/**
	* 이전 블록의 시작페이지를 리턴한다.
	* <PRE></PRE>
	*
	* @param none
	*
	* @return int
	*/
	public int getPrevPageUnit()
	{
		return (block - 1) * numNum + 1;
	}

	public static Vector copyRange(Vector vec, int fromIndex, int toIndex)
	{
		if (fromIndex < 0 || toIndex < 0 || vec.size() < toIndex || fromIndex > toIndex)
			throw new IndexOutOfBoundsException();

		Vector newvector = new Vector();

		for (int i = fromIndex; i <= toIndex; i++)
		{
			newvector.addElement((Object) vec.elementAt(i));
		}

		return newvector;
	}

	/**
	* 블록을 지정한다.
	* <PRE></PRE>
	*
	* @param int cur_page
	*
	* @return void
	*/
	public void setBlock(int cur_page)
	{
		block = cur_page / numNum;

		if ((cur_page % numNum) == 0)
		{
			block = block - 1; //numNum의 배수일때는 1을 빼준다.
		}

		if (cur_page < numNum)
			block = 0;

	}

	/**
	* 페이지 번호를 생성한다.
	*
	* <PRE></PRE>
	*
	* @param none
	*
	* @return int[] (num)
	* @exception ArrayIndexOutOfBoundsException
	*/
	public int[] Arraynum()
	{
		startpg = (block * numNum) + 1;
		endpg = startpg + numNum;

		//Logger.debug.println(" block: "+block);
		//Logger.debug.println(" startpg: "+startpg);
		//Logger.debug.println(" endpg: "+endpg);

		int[] num = null;
		try
		{
			num = new int[numNum];

			if (getLastPageNo() < numNum)
			{
				startpg = 1;
				endpg = getLastPageNo();
			}
			else
			{
				if (startpg < 1)
				{
					startpg = 1;
					endpg = numNum;
				}
			}

			for (int i = 0; i < num.length; i++)
			{
				num[i] = i + startpg;

				//Logger.debug.println("num["+i+"] : "+  num[i]);
			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			Logger.sys.println(e.toString());
		}
		return num;
	}

}
