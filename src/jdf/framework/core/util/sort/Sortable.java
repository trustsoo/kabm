package jdf.framework.core.util.sort;

import java.util.*;
/**
 * <b><code>Sortable</code></b>
 * <p>
 * sort 구현
 *
 * </p>
 *
 * @author
 * @version 1.0
 */
public interface Sortable
{
	public void sort(Object src[], Comparator c);
	public void sort(Object src[], int low, int high, Comparator c);
}