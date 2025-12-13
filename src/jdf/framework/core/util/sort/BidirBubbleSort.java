package jdf.framework.core.util.sort;

import java.util.*;
/**
 * <b><code>BidirBubbleSort</code></b>
 * <p>
 * BidirBubbleSort 정렬을 한다.(Bubble 소트 변형)
 *
 * </p>
 *
 * @author
 * @version 1.0
 */
public class BidirBubbleSort implements Sortable
{
	
	public void sort(Object src[], Comparator c)
	{
		sort(src, 0, src.length - 1, c);
	}

	public void sort(Object src[], int low, int high, Comparator c) 
	{
        bidirbubbleSort(src, low, high, c);
	}

	private void bidirbubbleSort(Object src[], int low, int high, Comparator c) 
	{
		int limit = high + 1;
		int st = low - 1;
		while (st < limit) {
			st++;
			limit--;
			boolean swapped = false;
			
			for (int j = st; j < limit; j++) {
				if (c.compare(src[j], src[j+1]) > 0) {
					swap(src, j, j+1);
					swapped = true;
				}
			}
			
			if (!swapped)
				return;
			else
				swapped = false;

			for (int j = limit; --j >= st;) {
				if (c.compare(src[j], src[j+1]) > 0) {
					swap(src, j, j+1);
					swapped = true;
				}
			}

			if (!swapped) {
				return;
			}
		}
    }

	private void swap(Object x[], int a, int b) {
		Object t = x[a];
		x[a] = x[b];
		x[b] = t;
    }
}