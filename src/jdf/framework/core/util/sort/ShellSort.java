package jdf.framework.core.util.sort;

import java.util.Comparator;

/**
 * <b><code>ShellSort</code></b>
 * <p>
 * Shell 정렬을 한다.
 *
 * </p>
 *
 * @author
 * @version 1.0
 */
public class ShellSort implements Sortable
{
	
	public void sort(Object src[], Comparator c)
	{
		sort(src, 0, src.length - 1, c);
	}

	public void sort(Object src[], int low, int high, Comparator c) 
	{
        shellSort(src, low, high, c);
	}

	private void shellSort(Object src[], int low, int high, Comparator c)
	{
		int inner, outer;
		Object temp;

		int h = low + 1;                     // find initial value of h
		while(h <= (high + 1) /3)
		h = h*3 + 1;                // (1, 4, 13, 40, 121, ...)

		while(h>0)                     // decreasing h, until h=1
		{
			// h-sort the file
			for(outer = h; outer<(high + 1); outer++)
			{
				temp = src[outer];
				inner = outer;
				
				// one subpass (eg 0, 4, 8)
				while(inner > h-1 && c.compare(src[inner-h], temp) >=  0)
				{
					src[inner] = src[inner-h];
					inner -= h;
				}

				src[inner] = temp;
			}  // end for
			h = (h-1) / 3;              // decrease h
		}  // end while(h>0)
	}  // end shellSort()
};
