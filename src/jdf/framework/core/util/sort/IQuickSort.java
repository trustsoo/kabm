package jdf.framework.core.util.sort;

//demonstrates quick sort; uses insertion sort for cleanup
//원소가 10이하이면 insertionSort 방법으로 소팅
import java.util.Comparator;

/**
 * <b><code>IQuickSort</code></b>
 * <p>
 * QuickSort 정렬을 한다.(QuickSort 변형)
 *
 * </p>
 *
 * @author
 * @version 1.0
 */
public class IQuickSort implements Sortable
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

		if (size < 10) // insertion sort if small
			insertionSort(src, left, right, c);
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

	public void insertionSort(Object[] src, int left, int right, Comparator c)
	{
		int in, out;
		//  sorted on left of out
		for (out = left + 1; out <= right; out++)
		{
			Object temp = src[out]; // remove marked item
			in = out; // start shifts at out
			// until one is smaller,
			while (in > left && c.compare(src[in - 1], temp) >= 0)
			{
				src[in] = src[in - 1]; // shift item to right
				--in; // go left one position
			}
			src[in] = temp; // insert marked item
		}
	}

	public void insertionSort(int left, int right)
	{

	} // end insertionSort()

	private void swap(Object x[], int a, int b)
	{
		Object t = x[a];
		x[a] = x[b];
		x[b] = t;
	}
};
