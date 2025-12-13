package jdf.framework.core.util.sort;

import java.util.*;
/**
 * <b><code>BubbleSort</code></b>
 * <p>
 * BubbleSort 정렬을 한다.
 *
 * </p>
 *
 * @author
 * @version 1.0
 */
public class BubbleSort implements Sortable
{
	
	public void sort(Object src[], Comparator c)
	{
		sort(src, 0, src.length - 1, c);
	}

	public void sort(Object src[], int low, int high, Comparator c) 
	{
        bubbleSort(src, low, high, c);
	}

	private void bubbleSort(Object src[], int low, int high, Comparator c) 
	{
		int i, j; 
		for (i = low; i<=high; i++ ) 
		{ 
			for (j = i; j <=high; j++ ) 
				if (c.compare(src[i], src[j]) > 0) 
					swap(src, i, j);
		}
    }

	private void swap(Object x[], int a, int b) {
		Object t = x[a];
		x[a] = x[b];
		x[b] = t;
    }
};
