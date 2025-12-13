package jdf.framework.core.util.sort;

//QuickSort with median-of-three partitioning

import java.util.Comparator;
/**
 * <b><code>M3PQuickSort</code></b>
 * <p>
 * QuickSort 정렬을 한다.(QuickSort 변형)
 *
 * </p>
 *
 * @author
 * @version 1.0
 */
public class M3PQuickSort implements Sortable
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

		int size = right - left + 1;

		if (size <= 3) // manual sort if small
			manualSort(src, left, right, c);
		else // quicksort if large
			{
			Object median = medianOf3(src, left, right, c);
			int partition = partitionIt(src, left, right, median, c);
			quickSort(src, left, partition - 1, c);
			quickSort(src, partition + 1, right, c);
		}
	}

	public int partitionIt(Object[] src, int left, int right, Object pivot, Comparator c)
	{
		int leftPtr = left; // right of first elem
		int rightPtr = right - 1; // left of pivot

		while (true)
		{
			while (c.compare(src[++leftPtr], pivot) < 0); // find bigger
			while (c.compare(src[--rightPtr], pivot) > 0); // find smaller
			if (leftPtr >= rightPtr) // if pointers cross,
				break; //    partition done
			else // not crossed, so
				swap(src, leftPtr, rightPtr); // swap elements
		} // end while(true)

		swap(src, leftPtr, right - 1); // restore pivot  //***
		return leftPtr; // return pivot location
	}

	public Object medianOf3(Object[] src, int left, int right, Comparator c)
	{
		int center = (left + right) / 2;

		// order left & center
		if (c.compare(src[left], src[center]) > 0)
			swap(src, left, center);

		// order left & right
		if (c.compare(src[left], src[right]) > 0)
			swap(src, left, right);

		// order center & right
		if (c.compare(src[center], src[right]) > 0)
			swap(src, center, right);

		swap(src, center, right - 1); // put pivot on right
		return src[right - 1]; // return median value
	}

	public void manualSort(Object[] src, int left, int right, Comparator c)
	{
		int size = right - left + 1;
		if (size <= 1)
			return; // no sort necessary
		if (size == 2)
		{ // 2-sort left and right
			if (c.compare(src[left], src[right]) > 0)
				swap(src, left, right);
			return;
		}
		else // size==3
			{ // 3-sort left, center (right-1) & right
			if (c.compare(src[left], src[right - 1]) > 0)
				swap(src, left, right - 1); // left, center
			if (c.compare(src[left], src[right]) > 0)
				swap(src, left, right); // left, right
			if (c.compare(src[right - 1], src[right]) > 0)
				swap(src, right - 1, right); // center, right
		}
	}

	private void swap(Object x[], int a, int b)
	{
		Object t = x[a];
		x[a] = x[b];
		x[b] = t;
	}
};
