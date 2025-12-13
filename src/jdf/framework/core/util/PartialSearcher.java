package jdf.framework.core.util;

import java.util.Iterator;
import java.util.Map;

/**
 * Map의 키를 부분적으로 검색해 해당 Key list를 리턴한다.
 * 
 * 
 * @author
 * @version 1.0 2001/05/01
 * 
 * @see jdf.framework.core.manageobject.dbms.SQLQueryImpl
 * @see jdf.framework.core.manageobject.ExpressionBuilder
 * 
 */

public class PartialSearcher
{
    Map hash;

    String[] sortedArray;

   

    /**
     * 기본생성자
     * 
     * @param h
     */
    public PartialSearcher(Map h)
    {
        hash = h;
        createSortedArray();
    }

    /**
     * 해당 문자열과 일치하는 결과값을 배열로 반환
     * 
     * @param s
     * @return
     */
    public Object[] match(String s)
    {

        int startIdx = binarySearch(sortedArray, s, 0, sortedArray.length - 1);
        int endIdx = binarySearch(sortedArray, s + '\uFFFF', 0, sortedArray.length - 1);

        Object[] objs = new Object[endIdx - startIdx];
        for (int i = startIdx; i < endIdx; i++)
            objs[i - startIdx] = sortedArray[i];
        return objs;

    }

    /**
     * 
     *
     */
    public void createSortedArray()
    {
        sortedArray = new String[hash.size()];

        Iterator keys = hash.keySet().iterator();

        for (int k = 0; keys.hasNext(); k++)
            sortedArray[k] = (String) keys.next();

        quicksort(sortedArray, 0, sortedArray.length - 1);
    }

    /**
     * binary 검색법에 따라 데이터 검색
     * 
     * 
     * @param arr
     * @param elem
     * @param fromIndex
     * @param toIndex
     * @return
     */
    public static int binarySearch(String[] arr, String elem, int fromIndex, int toIndex)
    {
        int mid, cmp;
        while (fromIndex <= toIndex)
        {
            mid = (fromIndex + toIndex) >>> 1;
            if ((cmp = arr[mid].compareTo(elem)) < 0)
                fromIndex = mid + 1;
            else if (cmp > 0)
                toIndex = mid - 1;
            else
                return mid;
        }
        return fromIndex;
    }

    /**
     * quicksort 방식에 따라 데이터 검색
     * 
     * @param arr
     * @param lo
     * @param hi
     */
    public void quicksort(String[] arr, int lo, int hi)
    {
        if (lo >= hi)
            return;

        int mid = (lo + hi) >>> 1;
        String tmp;
        String middle = arr[mid];

        if (arr[lo].compareTo(middle) > 0)
        {
            arr[mid] = arr[lo];
            arr[lo] = middle;
            middle = arr[mid];
        }

        if (middle.compareTo(arr[hi]) > 0)
        {
            arr[mid] = arr[hi];
            arr[hi] = middle;
            middle = arr[mid];

            if (arr[lo].compareTo(middle) > 0)
            {
                arr[mid] = arr[lo];
                arr[lo] = middle;
                middle = arr[mid];
            }
        }

        int left = lo + 1;
        int right = hi - 1;

        if (left >= right)
            return;

        for (;;)
        {
            while (arr[right].compareTo(middle) > 0)
            {
                right--;
            }

            while (left < right && arr[left].compareTo(middle) <= 0)
            {
                left++;
            }

            if (left < right)
            {
                tmp = arr[left];
                arr[left] = arr[right];
                arr[right] = tmp;
                right--;
            } else
            {
                break;
            }
        }

        quicksort(arr, lo, left);
        quicksort(arr, left + 1, hi);
    }
}