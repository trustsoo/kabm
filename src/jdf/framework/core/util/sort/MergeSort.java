package jdf.framework.core.util.sort;

import java.util.Comparator;

/**
 * <b><code>MergeSort</code></b>
 * <p>
 * MergeSort 정렬을 한다.
 *
 * </p>
 *
 * @author
 * @version 1.0
 */
public class MergeSort implements Sortable
{
	
	public void sort(Object src[], Comparator c)
	{
		sort(src, 0, src.length - 1, c);
	}

	public void sort(Object src[], int low, int high, Comparator c) 
	{
		Object aux[] = (Object[])src.clone();
        mergeSort(aux, src, low, high + 1, c);
	}

	private void mergeSort(Object src[], Object dest[], int low, int high, Comparator c) 
	{
		int length = high - low;

		if (length < 7) {		//7보다 작을때는 삽입정렬
			for (int i=low; i<high; i++)
			for (int j=i; j>low && c.compare(dest[j-1], dest[j])>0; j--)
				swap(dest, j, j-1);
			return;
		}

       int mid = (low + high) / 2;
        mergeSort(dest, src, low, mid, c);
        mergeSort(dest, src, mid, high, c);

        if (c.compare(src[mid-1], src[mid]) <= 0) {
           System.arraycopy(src, low, dest, low, length);
           return;
        }

        for(int i = low, p = low, q = mid; i < high; i++) {
            if (q >= high || p < mid && c.compare(src[p], src[q]) <= 0)
                dest[i] = src[p++];
            else
                dest[i] = src[q++];
        }
    }

	private void swap(Object x[], int a, int b) {
		Object t = x[a];
		x[a] = x[b];
		x[b] = t;
    }
};
