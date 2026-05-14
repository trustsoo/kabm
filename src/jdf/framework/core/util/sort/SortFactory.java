package jdf.framework.core.util.sort;

import java.util.Collection;
import java.util.Comparator;

/**
 * <b><code>SortFactory</code></b>
 * <p>
 * 원하는 소트알고리즘에 의하여, 클래스 아이템을 소트 시킨다.
 * 아이템이 되는 클래스는 동일하지 않아도 되며,
 * 해당 되는 멤버 필드가 필드명과 필드타입만 같으면 소트시킬 수 있다.
 * 포퍼먼스는 머지소트방식을 선택하였을시, 십만건당, 13초정도의 시간이 소요된다.
 * [사용법] Comparator가 지정되었을때,
 * <pre>
 * SortFactory sf = SortFactory.newInstance(new MergeSort());
 * sf.setComparator(new xxx());
 * sf.setOrder(SortFactory.ASC);
 * sf.sort(obj);
 * </pre>
 *
 * @author
 * @version 1.0
 */
public class SortFactory
{
	//내림차순
	public static final int ASC = 1;
	//올림차순
	public static final int DESC = -1;


	private Sortable sortable = null;

	private Comparator comparator = null;

	private int order = 1;

	private int[] orders;

	private String field = null;

	private String[] fields = null;
	
	/**
	 * 소트 알고니즘을 선택한다.
	 * 예: Merge소트 <code>MergeSort</code>, Quick소트 <code>QuickSort</code>
	 */
	public static SortFactory newInstance(Sortable sortable)
	{
		SortFactory sf = new SortFactory(sortable);

		return sf;
	}

	private SortFactory(Sortable sortable)
	{
		this.sortable = sortable;
	}
	
	/**
	 * <code>Comparator</code>를 설정한다.
	 * 현재는 Object 배열만 해당된다.
	 * @param comparator
	 */
	public void setComparator(Comparator comparator)
	{
		field = null;
		fields = null;

		this.comparator = comparator;
	}
	
	/**
	 * 정렬 방식을 설정한다.
	 * 내림차순 : SortFactory.ASC (1)
	 * 올림차순 : SortFactory.DESC (-1)
	 * @param order
	 */
	public void setOrder(int order)
	{
		this.order = (order > 0) ? ASC : DESC;
	}
	
	/**
	 * 필드가 여러개일 경우, 다중으로 각 필드에 해당하는 정렬 방식을 설정한다.
	 * 콜렉션일 경우만 해당된다.
	 * 또한, 필드의 인덱스 수는 setField에서 설정한 field 배열의 인덱수와 일치해야 한다.
	 * 내림차순 : SortFactory.ASC
	 * 올림차순 : SortFactory.DESC
	 * @param orders
	 */
	public void setOrder(int[] orders)
	{
		this.orders = orders;
	}

	/**
	 * 정렬할 필드를 설정한다.
	 * 콜렉션일 경우만 해당된다.
	 * 내림차순 : SortFactory.ASC
	 * 올림차순 : SortFactory.DESC
	 * @param orders
	 */
	public void setField(String field)
	{
		fields = null;
		comparator = null;

		this.field = field;
	}

	/**
	 * 필드가 여러개일 경우, 정렬할 필드들을 설정한다.
	 * 콜렉션일 경우만 해당된다.
	 * 내림차순 : SortFactory.ASC
	 * 올림차순 : SortFactory.DESC
	 * @param orders
	 */
	public void setField(String[] fields)
	{
		field = null;
		comparator = null;

		this.fields = fields;
	}


	/**
	 * 해당 Object 배열을 소트한다.
	 */
	public void sort(Object[] src) throws Exception
	{
		SortHelper helper = SortHelper.getInstance();

		if (comparator != null)
		{
			helper.sort(src, 0, src.length - 1, comparator, sortable);
		}

		else if (field != null)
		{
			helper.sort(src, 0, src.length - 1, field, order, sortable);
		}

		else if (fields != null && orders == null)
		{
			orders = new int[fields.length];
			for (int i = 0;i < fields.length;i++ )
			{
				orders[i] = ASC;
			}

			helper.sort(src, 0, src.length - 1, fields, orders, sortable);
		}
		
		else if (fields != null && orders != null)
		{
			helper.sort(src, 0, src.length - 1, fields, orders, sortable);
		}
		
		else
		{
			throw new SortException("Comparator 또는 Field가 설정되지 않았습니다.");
		}
	}
	
	/**
	 * 해당 Collection 을 배열을 소트한다.
	 */
	public void sort(Collection src) throws Exception
	{
		SortHelper helper = SortHelper.getInstance();

		if (field != null)
		{
			helper.sort(src, 0, src.size() - 1, field, order, sortable);
		}
		
		else if (fields != null && orders == null)
		{
			orders = new int[fields.length];
			for (int i = 0;i < fields.length;i++ )
			{
				orders[i] = ASC;
			}

			helper.sort(src, 0, src.size() - 1, fields, orders, sortable);
		}

		else if (fields != null && orders != null)
		{
			helper.sort(src, 0, src.size() - 1, fields, orders, sortable);
		}

		else
		{
			throw new SortException("Field[] 또는 Field가 설정되지 않았습니다.");
		}
	}

	/**
	 * 해당 Object 배열을 소트하여, 새로운 복사본을 반환한다.
	 */
	public Object[] sortClone(Object[] src) throws Exception
	{
		SortHelper helper = SortHelper.getInstance();

		if (comparator != null)
		{
			return helper.sortClone(src, 0, src.length - 1, comparator, sortable);
		}

		else if (field != null)
		{
			return helper.sortClone(src, 0, src.length - 1, field, order, sortable);
		}

		else if (fields != null && orders == null)
		{
			orders = new int[fields.length];
			for (int i = 0;i < fields.length;i++ )
			{
				orders[i] = ASC;
			}

			return helper.sortClone(src, 0, src.length - 1, fields, orders, sortable);
		}
		
		else if (fields != null && orders != null)
		{
			return helper.sortClone(src, 0, src.length - 1, fields, orders, sortable);
		}
		
		else
		{
			throw new SortException("Comparator 또는 Field가 설정되지 않았습니다.");
		}
	}

	/**
	 * 해당 Collection 을 소트하여, 새로운 복사본을 반환한다.
	 */
	public Collection sortClone(Collection src) throws Exception
	{
		SortHelper helper = SortHelper.getInstance();

		if (field != null)
		{
			return helper.sortClone(src, 0, src.size() - 1, field, order, sortable);
		}
		
		else if (fields != null && orders == null)
		{
			orders = new int[fields.length];
			for (int i = 0;i < fields.length;i++ )
			{
				orders[i] = ASC;
			}

			return helper.sortClone(src, 0, src.size() - 1, fields, orders, sortable);
		}

		else if (fields != null && orders != null)
		{
			return helper.sortClone(src, 0, src.size() - 1, fields, orders, sortable);
		}

		else
		{
			throw new SortException("Field[] 또는 Field가 설정되지 않았습니다.");
		}	
	}
}