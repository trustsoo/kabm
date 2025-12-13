package jdf.framework.core.util.sort;

import java.util.Comparator;
/**
 * <b><code>QuickSort</code></b>
 * <p>
 * QuickSort 정렬을 한다.
 *
 * </p>
 *
 * @author
 * @version 1.0
 */
public class QuickSort implements Sortable
{
	public void sort(Object src[], Comparator c)
	{
		sort(src, 0, src.length - 1, c);
	}

	public void sort(Object src[], int low, int high, Comparator c)
	{
		quickSort(src, low, high, c);
	}

	private void quickSort(Object[] src, int left, int right, Comparator c)
	{
		if (right > left)
		{
			int i = left - 1;
			int j = right;
			while (true)
			{
				while (c.compare(src[++i], src[right]) < 0);
				while (j > 0)
					if (c.compare(src[--j], src[right]) <= 0)
						break;
				if (i >= j)
					break;
				swap(src, i, j);
			}
			swap(src, i, right);
			quickSort(src, left, i - 1, c);
			quickSort(src, i + 1, right, c);
		}
	}

	private void swap(Object x[], int a, int b)
	{
		Object t = x[a];
		x[a] = x[b];
		x[b] = t;
	}
};