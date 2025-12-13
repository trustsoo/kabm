package jdf.framework.core.util.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

/**
 * HashCode로서 integer를 키로 받는다.
 */
public class IntObjectMap 
{
	private transient Entry table[];

    private transient int count;

    private int threshold;

    private float loadFactor;

    private transient int modCount = 0;

    public IntObjectMap(int initialCapacity, float loadFactor) {
		if (initialCapacity < 0)
			throw new IllegalArgumentException("Illegal Initial Capacity: " + initialCapacity);
		if (loadFactor <= 0 || Float.isNaN(loadFactor))
			throw new IllegalArgumentException("Illegal Load factor: " + loadFactor);
		if (initialCapacity==0)
			initialCapacity = 1;

		this.loadFactor = loadFactor;
		table = new Entry[initialCapacity];
		threshold = (int)(initialCapacity * loadFactor);
    }

    public IntObjectMap(int initialCapacity) {
		this(initialCapacity, 0.75f);
    }

    public IntObjectMap() {
		this(11, 0.75f);
    }

    public int size() {
		return count;
    }

    public boolean isEmpty() {
		return count == 0;
    }

    public boolean containsValue(Object value) {
		Entry tab[] = table;

		if (value==null) {
			for (int i = tab.length ; i-- > 0 ;)
				for (Entry e = tab[i] ; e != null ; e = e.next)
					if (e.value==null)
					return true;
		} else {
			for (int i = tab.length ; i-- > 0 ;)
				for (Entry e = tab[i] ; e != null ; e = e.next)
					if (value.equals(e.value))
					return true;
		}

		return false;
    }

    public boolean containsKey(int hash) {
		Entry tab[] = table;
		int index = (hash & 0x7FFFFFFF) % tab.length;
		for (Entry e = tab[index]; e != null; e = e.next)
		{
			if (e.hash==hash)
				return true;
		}

		return false;
    }

    public Object get(int hash) {
		Entry tab[] = table;

		int index = (hash & 0x7FFFFFFF) % tab.length;
		for (Entry e = tab[index]; e != null; e = e.next)
			if (e.hash == hash)
				return e.value;

		return null;
    }

    private void rehash() {
		int oldCapacity = table.length;
		Entry oldMap[] = table;

		int newCapacity = oldCapacity * 2 + 1;
		Entry newMap[] = new Entry[newCapacity];

		modCount++;
		threshold = (int)(newCapacity * loadFactor);
		table = newMap;

		for (int i = oldCapacity ; i-- > 0 ;) {
			for (Entry old = oldMap[i] ; old != null ; ) {
			Entry e = old;
			old = old.next;

			int index = (e.hash & 0x7FFFFFFF) % newCapacity;
			e.next = newMap[index];
			newMap[index] = e;
			}
		}
    }

    public Object put(int hash, Object value) {
		Entry tab[] = table;
		int index = 0;

		index = (hash & 0x7FFFFFFF) % tab.length;
		for (Entry e = tab[index] ; e != null ; e = e.next) {
			if (e.hash == hash) {
				Object old = e.value;
				e.value = value;
				return old;
			}
		}

		modCount++;
		if (count >= threshold) {
			// Rehash the table if the threshold is exceeded
			rehash();

			tab = table;
			index = (hash & 0x7FFFFFFF) % tab.length;
		}

		// Creates the new entry.
		Entry e = new Entry(hash, value, tab[index]);
		tab[index] = e;
		count++;
		return null;
    }

    public Object remove(int hash) {
		Entry tab[] = table;

        int index = (hash & 0x7FFFFFFF) % tab.length;

		for (Entry e = tab[index], prev = null; e != null;
			 prev = e, e = e.next) {
			if (e.hash == hash) {
				modCount++;
				if (prev != null)
					prev.next = e.next;
				else
					tab[index] = e.next;

				count--;
				Object oldValue = e.value;
				e.value = null;
				return oldValue;
			}
		}

		return null;
    }

    public void clear() {
		Entry tab[] = table;
		modCount++;
		for (int index = tab.length; --index >= 0; )
			tab[index] = null;
		count = 0;
    }

    // Views

    public Iterator getKeyIterator() {
		return getHashIterator(KEYS);
	}

    public Iterator getValuesIterator() {
		return getHashIterator(VALUES);
	}

    private Iterator getHashIterator(int type) {
		if (count == 0) {
			return emptyHashIterator;
		} else {
			return new HashIterator(type);
		}
    }

    /**
     * IntObjectMap collision list entry.
     */
    private static class Entry {
		int hash;
		Object value;
		Entry next;

		Entry(int hash, Object value, Entry next) {
			this.hash = hash;
			this.value = value;
			this.next = next;
		}

		protected Object clone() {
			return new Entry(hash, value, (next==null ? null : (Entry)next.clone()));
		}

		// Entry Ops 

		public Object getValue() {
			return value;
		}

		public Object setValue(Object value) {
			Object oldValue = this.value;
			this.value = value;
			return oldValue;
		}

		public boolean equals(Object o) {
			if (!(o instanceof Entry))
				return false;
			Entry e = (Entry)o;

			return (value==null ? e.getValue()==null : value.equals(e.getValue()) );
		}

		public int hashCode() {
			return hash ^ (value==null ? 0 : value.hashCode());
		}

		public String toString() {
			return hash+"="+value;
		}
    }

    // Types of Iterators
    private static final int KEYS = 0;
    private static final int VALUES = 1;
    private static final int ENTRIES = 2;

    private static EmptyHashIterator emptyHashIterator = new EmptyHashIterator();
					     
    private static class EmptyHashIterator implements Iterator {
	
		EmptyHashIterator() {
			
		}

		public boolean hasNext() {
			return false;
		}

		public Object next() {
			throw new NoSuchElementException();
		}
		
		public void remove() {
			throw new IllegalStateException();
		}

	}			
				
	private class HashIterator implements Iterator {
		Entry[] table = IntObjectMap.this.table;
		int index = table.length;
		Entry entry = null;
		Entry lastReturned = null;
		int type;

		/**
		 * The modCount value that the iterator believes that the backing
		 * MemberList should have.  If this expectation is violated, the iterator
		 * has detected concurrent modification.
		 */
		private int expectedModCount = modCount;

		HashIterator(int type) {
			this.type = type;
		}

		public boolean hasNext() {
			Entry e = entry;
			int i = index;
			Entry t[] = table;
			/* Use locals for faster loop iteration */
			while (e == null && i > 0)
			e = t[--i];
			entry = e;
			index = i;
			return e != null;
		}

		public Object next() {
			if (modCount != expectedModCount)
			throw new ConcurrentModificationException();

			Entry et = entry;
			int i = index;
			Entry t[] = table;

			/* Use locals for faster loop iteration */
			while (et == null && i > 0) 
			et = t[--i];

			entry = et;
			index = i;
			if (et != null) {
			Entry e = lastReturned = entry;
			entry = e.next;
			return type == KEYS ? new Integer(e.hash) : (type == VALUES ? e.value : e);
			}
			throw new NoSuchElementException();
		}

		public void remove() 
		{
			if (lastReturned == null)
				throw new IllegalStateException();
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();

			Entry[] tab = IntObjectMap.this.table;
			int index = (lastReturned.hash & 0x7FFFFFFF) % tab.length;

			for (Entry e = tab[index], prev = null; e != null; prev = e, e = e.next) 
			{
				if (e == lastReturned) {
					modCount++;
					expectedModCount++;
					if (prev == null)
						tab[index] = e.next;
					else
						prev.next = e.next;
					count--;
					lastReturned = null;
					return;
				}
			}

			throw new ConcurrentModificationException();
		}
    }

    int capacity() {
        return table.length;
    }

    float loadFactor() {
        return loadFactor;
    }
}