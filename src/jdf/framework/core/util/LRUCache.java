/*
 * Copyright (c) 1998-2002 Caucho Technology -- all rights reserved
 *
 * Caucho Technology permits modification and use of this file in
 * source and binary form ("the Software") subject to the Caucho
 * Developer Source License 1.1 ("the License") which accompanies
 * this file.  The License is also available at
 *   http://www.caucho.com/download/cdsl1-1.xtp
 *
 * In addition to the terms of the License, the following conditions
 * must be met:
 *
 * 1. Each copy or derived work of the Software must preserve the copyright
 *    notice and this notice unmodified.
 *
 * 2. Each copy of the Software in source or binary form must include 
 *    an unmodified copy of the License in a plain ASCII text file named
 *    LICENSE.
 *
 * 3. Caucho reserves all rights to its names, trademarks and logos.
 *    In particular, the names "Resin" and "Caucho" are trademarks of
 *    Caucho and may not be used to endorse products derived from
 *    this software.  "Resin" and "Caucho" may not appear in the names
 *    of products derived from this software.
 *
 * This Software is provided "AS IS," without a warranty of any kind. 
 * ALL EXPRESS OR IMPLIED REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED.
 *
 * CAUCHO TECHNOLOGY AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A RESULT OF USING OR
 * DISTRIBUTING SOFTWARE. IN NO EVENT WILL CAUCHO OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE SOFTWARE, EVEN IF HE HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGES.      
 *
 * @author Scott Ferguson
 */

package jdf.framework.core.util;

import java.util.Iterator;

/**
 * Fixed length cache with a LRU replacement policy. If cache items implement CacheListener, they will be informed when
 * they're removed from the cache.
 * 
 * <p>
 * Null keys are not allowed. LRUCache is synchronized.
 */
public class LRUCache
{
	// hash table containing the entries. Its size is twice the capacity
	// so it will always remain at least half empty
	private CacheItem[] entries;

	// maximum allowed entries
	private int capacity;

	// number of items in the cache
	private int size;

	private int mask;

	// head of the LRU list
	private CacheItem head;

	// tail of the LRU list
	private CacheItem tail;

	private static Integer NULL = new Integer(0);
	
	public LRUCache()
	{
		//this(1024);
		this(500);
	}
	
	/**
     * Create the LRU cache with a specific capacity.
     * 
     * @param initialCapacity
     *            minimum capacity of the cache
     */
	public LRUCache(int initialCapacity) {
		int capacity;

		for (capacity = 16; capacity < 2 * initialCapacity; capacity *= 2) {
		}

		entries = new CacheItem[capacity];
		mask = capacity - 1;

		this.capacity = initialCapacity;
	}

	/**
     * Returns the current number of entries in the cache.
     */
	public int size()
	{
		return size;
	}

	/**
     * Clears the cache
     */
	public synchronized void clear()
	{
		size = 0;
		head = null;
		tail = null;
	}

	/**
     * Get an item from the cache and make it most recently used.
     * 
     * @param key
     *            key to lookup the item
     * @return the matching object in the cache
     */
	public Object get(Object key)
	{
		if (key == null)
			key = NULL;

		int hash = key.hashCode() & mask;
		int count = size + 1;

		synchronized (this) {
			for (; count > 0; count--) {
				CacheItem item = entries[hash];

				if (item == null)
					return null;

				if (item.key == key || item.key.equals(key)) {
					updateLru(item);

					return item.value;
				}

				hash = (hash + 1) & mask;
			}
		}

		return null;
	}

	/**
     * Puts a new item in the cache. If the cache is full, remove the LRU item.
     * 
     * @param key
     *            key to store data
     * @param value
     *            value to be stored
     * 
     * @return old value stored under the key
     */
	public Object put(Object key, Object value)
	{
		if (key == null)
			key = NULL;

		// remove LRU items until we're below capacity
		while (size >= capacity) {
			Object o = remove(tail.key);
			//DataSetOneRowMap d = (DataSetOneRowMap)o;
			//System.out.println(">>>>>>>>>>>>>>>>>>>"+d.get("alias") + " is removed.");
		}

		Object oldValue = null;

		int hash = key.hashCode() & mask;
		int count = size + 1;

		synchronized (this) {
			for (; count > 0; count--) {
				CacheItem item = entries[hash];

				// No matching item, so create one
				if (item == null) {
					item = new CacheItem(key, value);
					entries[hash] = item;
					size++;
					item.next = head;
					if (head != null)
						head.prev = item;
					else
						tail = item;
					head = item;

					return null;
				}

				// matching item gets replaced
				if (item.key == key || item.key.equals(key)) {
					updateLru(item);

					oldValue = item.value;
					item.value = value;
					break;
				}

				hash = (hash + 1) & mask;
			}
		}

		return oldValue;
	}

	/**
     * Put item at the head of the lru list. This is always called while synchronized.
     */
	private void updateLru(CacheItem item)
	{
		CacheItem prev = item.prev;
		CacheItem next = item.next;

		if (prev != null) {
			prev.next = next;

			item.prev = null;
			item.next = head;
			head.prev = item;
			head = item;

			if (next != null)
				next.prev = prev;
			else
				tail = prev;
		}
	}

	/**
     * Remove the last item in the LRU
     */
	public boolean removeTail()
	{
		CacheItem last = tail;

		if (last == null)
			return false;
		else {
			remove(last.key);
			return true;
		}
	}

	/**
     * Removes an item from the cache
     * 
     * @param key
     *            the key to remove
     * 
     * @return the value removed
     */
	public Object remove(Object key)
	{
		if (key == null)
			key = NULL;

		int hash = key.hashCode() & mask;
		int count = size + 1;

		Object value = null;

		synchronized (this) {
			for (; count > 0; count--) {
				CacheItem item = entries[hash];

				if (item == null)
					return null;

				if (item.key == key || item.key.equals(key)) {
					entries[hash] = null;
					size--;

					CacheItem prev = item.prev;
					CacheItem next = item.next;

					if (prev != null)
						prev.next = next;
					else
						head = next;

					if (next != null)
						next.prev = prev;
					else
						tail = prev;

					// Shift colliding entries down
					for (int i = 1; i <= count; i++) {
						int nextHash = (hash + i) & mask;
						CacheItem nextItem = entries[nextHash];
						if (nextItem == null)
							break;

						entries[nextHash] = null;
						refillEntry(nextItem);
					}

					value = item.value;
					break;
				}

				hash = (hash + 1) & mask;
			}
		}

		if (count < 0)
			throw new RuntimeException("internal cache error");

		return value;
	}

	/**
     * Put the item in the best location available in the hash table.
     */
	private void refillEntry(CacheItem item)
	{
		int baseHash = item.key.hashCode();

		for (int count = 0; count < size + 1; count++) {
			int hash = (baseHash + count) & mask;

			if (entries[hash] == null) {
				entries[hash] = item;
				return;
			}
		}
	}

	/**
     * Returns the keys stored in the cache
     */
	public Iterator keys()
	{
		KeyIterator iter = new KeyIterator();
		iter.init(this);
		return iter;
	}

	/**
     * Returns keys stored in the cache using an old iterator
     */
	public Iterator keys(Iterator oldIter)
	{
		KeyIterator iter = (KeyIterator) oldIter;
		iter.init(this);
		return iter;
	}

	/**
     * Returns the values in the cache
     */
	public Iterator values()
	{
		ValueIterator iter = new ValueIterator();
		iter.init(this);
		return iter;
	}

	public Iterator values(Iterator oldIter)
	{
		ValueIterator iter = (ValueIterator) oldIter;
		iter.init(this);
		return iter;
	}

	/**
     * A cache item
     */
	static class CacheItem
	{
		CacheItem prev;

		CacheItem next;

		Object key;

		Object value;

		CacheItem(Object key, Object value) {
			this.key = key;
			this.value = value;
		}
	}

	/**
     * Iterator of cache keys
     */
	static class KeyIterator implements Iterator
	{
		CacheItem item;

		void init(LRUCache cache)
		{
			item = cache.head;
		}

		public boolean hasNext()
		{
			return item != null;
		}

		public Object next()
		{
			Object value = item.key;

			item = item.next;

			return value;
		}

		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}

	/**
     * Iterator of cache values
     */
	static class ValueIterator implements Iterator
	{
		CacheItem item;

		void init(LRUCache cache)
		{
			item = cache.head;
		}

		public boolean hasNext()
		{
			return item != null;
		}

		public Object next()
		{
			Object value = item.value;

			item = item.next;

			return value;
		}

		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
}