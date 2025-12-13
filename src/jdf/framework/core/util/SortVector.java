package jdf.framework.core.util;

// SortVector.java

import java.util.Collection;
import java.util.Vector;

/**
 * <b><code>SortVector</code></b>
 * <p>
 * Vector가 담고있는 객체의 특정 필드를 기준으로 정렬시켜준다.
 * 
 * </p>
 * 
 * @author
 * @version 1.0
 */

public class SortVector extends Vector
{

	/**
     * 
     */
	private static final long serialVersionUID = 6766935256217407225L;

	public final static String INT = "INT";

	public final static String DOUBLE = "DOUBLE";

	public final static String FLOAT = "FLOAT";

	public final static String STRING = "STRING";

	public final static String ORDER = "ORDER";

	public final static String DESC = "DESC";

	public SortVector() {
	}

	public void sort()
	{
		stringSort(0, size() - 1);
	}

	public SortVector(int initialCapacity, int capacityIncrement) {
		super(initialCapacity, capacityIncrement);
	}

	public SortVector(Collection c) {
		super(c);
	}

	public void sort(String field) throws NoSuchFieldException, IllegalAccessException
	{
		classFieldObjectOrder(0, size() - 1, field);
	}

	public void sort(String flag, String field, String ord) throws NoSuchFieldException, IllegalAccessException
	{
		if (flag.equals(INT)) {

			if (ord.equals(ORDER))
				classFieldIntOrder(0, size() - 1, field);
			else
				classFieldIntDesc(0, size() - 1, field);

		} else if (flag.equals(DOUBLE)) {

			if (ord.equals(ORDER))
				classFieldDoubleOrder(0, size() - 1, field);
			else
				classFieldDoubleDesc(0, size() - 1, field);

		} else if (flag.equals(FLOAT)) {

			if (ord.equals(ORDER))
				classFieldFloatOrder(0, size() - 1, field);
			else
				classFieldFloatDesc(0, size() - 1, field);

		} else {

			if (ord.equals(ORDER))
				classFieldObjectOrder(0, size() - 1, field);
			else
				classFieldObjectDesc(0, size() - 1, field);

		}
	}

	private void stringSort(int left, int right)
	{
		if (right > left) {
			Object obj = elementAt(right);
			int i = left - 1;
			int j = right;
			while (true) {
				while (lessThan(elementAt(++i), obj))
					;
				while (j > 0)
					if (lessThanOrEqual(elementAt(--j), obj))
						break;
				if (i >= j)
					break;
				swap(i, j);
			}
			swap(i, right);
			stringSort(left, i - 1);
			stringSort(i + 1, right);
		}
	}

	private void classFieldIntOrder(int left, int right, String field) throws NoSuchFieldException,
			IllegalAccessException
	{
		if (right > left) {
			int obj = elementAt(right).getClass().getField(field).getInt(elementAt(right));
			int i = left - 1;
			int j = right;
			while (true) {
				while (elementAt(++i).getClass().getField(field).getInt(elementAt(i)) < obj)
					;
				while (j > 0)
					if (elementAt(--j).getClass().getField(field).getInt(elementAt(j)) <= obj)

						break;
				if (i >= j)
					break;
				swap(i, j);
			}
			swap(i, right);
			classFieldIntOrder(left, i - 1, field);
			classFieldIntOrder(i + 1, right, field);
		}
	}

	private void classFieldIntDesc(int left, int right, String field) throws NoSuchFieldException,
			IllegalAccessException
	{
		if (right > left) {
			int obj = elementAt(right).getClass().getField(field).getInt(elementAt(right));
			int i = left - 1;
			int j = right;
			while (true) {
				while (elementAt(++i).getClass().getField(field).getInt(elementAt(i)) > obj)
					;
				while (j > 0)
					if (elementAt(--j).getClass().getField(field).getInt(elementAt(j)) >= obj)

						break;
				if (i >= j)
					break;
				swap(i, j);
			}
			swap(i, right);
			classFieldIntDesc(left, i - 1, field);
			classFieldIntDesc(i + 1, right, field);
		}
	}

	private void classFieldDoubleOrder(int left, int right, String field) throws NoSuchFieldException,
			IllegalAccessException
	{
		if (right > left) {
			double obj = elementAt(right).getClass().getField(field).getDouble(elementAt(right));
			int i = left - 1;
			int j = right;
			while (true) {
				while (elementAt(++i).getClass().getField(field).getDouble(elementAt(i)) < obj)
					;
				while (j > 0)
					if (elementAt(--j).getClass().getField(field).getDouble(elementAt(j)) <= obj)
						break;
				if (i >= j)
					break;
				swap(i, j);
			}
			swap(i, right);
			classFieldDoubleOrder(left, i - 1, field);
			classFieldDoubleOrder(i + 1, right, field);
		}
	}

	private void classFieldDoubleDesc(int left, int right, String field) throws NoSuchFieldException,
			IllegalAccessException
	{
		if (right > left) {
			double obj = elementAt(right).getClass().getField(field).getDouble(elementAt(right));
			int i = left - 1;
			int j = right;
			while (true) {
				while (elementAt(++i).getClass().getField(field).getDouble(elementAt(i)) > obj)
					;
				while (j > 0)
					if (elementAt(--j).getClass().getField(field).getDouble(elementAt(j)) >= obj)
						break;
				if (i >= j)
					break;
				swap(i, j);
			}
			swap(i, right);
			classFieldDoubleDesc(left, i - 1, field);
			classFieldDoubleDesc(i + 1, right, field);
		}
	}

	private void classFieldFloatOrder(int left, int right, String field) throws NoSuchFieldException,
			IllegalAccessException
	{
		if (right > left) {
			float obj = elementAt(right).getClass().getField(field).getFloat(elementAt(right));
			int i = left - 1;
			int j = right;
			while (true) {
				while (elementAt(++i).getClass().getField(field).getFloat(elementAt(i)) < obj)
					;
				while (j > 0)
					if (elementAt(--j).getClass().getField(field).getFloat(elementAt(j)) <= obj)

						break;
				if (i >= j)
					break;
				swap(i, j);
			}
			swap(i, right);
			classFieldFloatOrder(left, i - 1, field);
			classFieldFloatOrder(i + 1, right, field);
		}
	}

	private void classFieldFloatDesc(int left, int right, String field) throws NoSuchFieldException,
			IllegalAccessException
	{
		if (right > left) {
			float obj = elementAt(right).getClass().getField(field).getFloat(elementAt(right));
			int i = left - 1;
			int j = right;
			while (true) {
				while (elementAt(++i).getClass().getField(field).getFloat(elementAt(i)) > obj)
					;
				while (j > 0)
					if (elementAt(--j).getClass().getField(field).getFloat(elementAt(j)) >= obj)

						break;
				if (i >= j)
					break;
				swap(i, j);
			}
			swap(i, right);
			classFieldFloatDesc(left, i - 1, field);
			classFieldFloatDesc(i + 1, right, field);
		}
	}

	private void classFieldObjectOrder(int left, int right, String field) throws NoSuchFieldException,
			IllegalAccessException
	{
		if (right > left) {
			Object obj = elementAt(right).getClass().getField(field).get(elementAt(right));
			int i = left - 1;
			int j = right;
			while (true) {
				while (lessThan(elementAt(++i).getClass().getField(field).get(elementAt(i)), obj))
					;
				while (j > 0)
					if (lessThanOrEqual(elementAt(--j).getClass().getField(field).get(elementAt(j)), obj))
						break;
				if (i >= j)
					break;
				swap(i, j);
			}
			swap(i, right);
			classFieldObjectOrder(left, i - 1, field);
			classFieldObjectOrder(i + 1, right, field);
		}
	}

	private void classFieldObjectDesc(int left, int right, String field) throws NoSuchFieldException,
			IllegalAccessException
	{
		if (right > left) {
			Object obj = elementAt(right).getClass().getField(field).get(elementAt(right));
			int i = left - 1;
			int j = right;
			while (true) {
				while (lessThan(obj, elementAt(++i).getClass().getField(field).get(elementAt(i))))
					;
				while (j > 0)
					if (lessThanOrEqual(obj, elementAt(--j).getClass().getField(field).get(elementAt(j))))
						break;
				if (i >= j)
					break;
				swap(i, j);
			}
			swap(i, right);
			classFieldObjectDesc(left, i - 1, field);
			classFieldObjectDesc(i + 1, right, field);
		}
	}

	private void swap(int i, int j)
	{
		Object temp = elementAt(i);
		setElementAt(elementAt(j), i);
		setElementAt(temp, j);
	}

	private boolean lessThan(Object l, Object r)
	{
		return ((String) l).compareTo((String) r) < 0;
	}

	private boolean lessThanOrEqual(Object l, Object r)
	{
		return ((String) l).compareTo((String) r) <= 0;
	}

}