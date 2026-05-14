package jdf.framework.core.util.collection;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * <p>
 * Thread Pool내의 Queue에서 Runnable 객체를 담아는 리스트로 캐스팅을 
 * 피하기 위하여 jdk1.2.2내의 LinkedList를 Obejct이 아닌 Runnable 객체만 담을 수 있도록 변경하였습니다.
 * </p>
 *
 * @author
 * @version 1.0
 */

public class RunnableList
{
    protected int modCount = 0;

    private Entry header = new Entry(null, null, null);
    private int size = 0;

    /**
     * Constructs an empty list.
     */
    public RunnableList() {
        header.next = header.previous = header;
    }

    /**
     * Returns the first element in this list.
     *
     * @return the first element in this list.
     */
    public Runnable getFirst() {
        if (size==0)
            throw new NoSuchElementException();

        return header.next.element;
    }

    /**
     * Returns the last element in this list.
     *
     * @return the last element in this list.
     * @throws    NoSuchElementException if this list is empty.
     */
    public Runnable getLast()  {
        if (size==0)
            throw new NoSuchElementException();

        return header.previous.element;
    }

    /**
     * Removes and returns the first element from this list.
     *
     * @return the first element from this list.
     * @throws    NoSuchElementException if this list is empty.
     */
    public Runnable removeFirst() {
        Runnable first = header.next.element;
        remove(header.next);
        return first;
    }

    /**
     * Removes and returns the last element from this list.
     *
     * @return the last element from this list.
     * @throws    NoSuchElementException if this list is empty.
     */
    public Runnable removeLast() {
        Runnable last = header.previous.element;
        remove(header.previous);
        return last;
    }

    /**
     * Inserts the given element at the beginning of this list.
     */
    public void addFirst(Runnable o) {
        addBefore(o, header.next);
    }

    /**
     * Appends the given element to the end of this list.  (Identical in
     * function to the <tt>add</tt> method; included only for consistency.)
     */
    public void addLast(Runnable o) {
        addBefore(o, header);
    }

    /**
     * Returns <tt>true</tt> if this list contains the specified element.
     * More formally, returns <tt>true</tt> if and only if this list contains
     * at least one element <tt>e</tt> such that <tt>(o==null ? e==null
     * : o.equals(e))</tt>.
     *
     * @param o element whose presence in this list is to be tested.
     * @return <tt>true</tt> if this list contains the specified element.
     */
    public boolean contains(Runnable o) {
        return indexOf(o) != -1;
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list.
     */
    public int size() {
        return size;
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param o element to be appended to this list.
     * @return <tt>true</tt> (as per the general contract of
     * <tt>Collection.add</tt>).
     */
    public boolean add(Runnable o) {
        addBefore(o, header);
            return true;
    }

    /**
     * Removes the first occurrence of the specified element in this list.  If
     * the list does not contain the element, it is unchanged.  More formally,
     * removes the element with the lowest index <tt>i</tt> such that
     * <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt> (if such an
     * element exists).
     *
     * @param o element to be removed from this list, if present.
     * @return <tt>true</tt> if the list contained the specified element.
     */
    public boolean remove(Runnable o) {
        if (o==null) {
            for (Entry e = header.next; e != header; e = e.next) {
                if (e.element==null) {
                    remove(e);
                    return true;
                }
            }
        } else {
            for (Entry e = header.next; e != header; e = e.next) {
                if (o.equals(e.element)) {
                    remove(e);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes all of the elements from this list.
     */
    public void clear() {
        modCount++;
        header.next = header.previous = header;
        size = 0;
    }


    // Positional Access Operations

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of element to return.
     * @return the element at the specified position in this list.
     * 
     * @throws IndexOutOfBoundsException if the specified index is is out of
     * range (<tt>index &lt; 0 || index &gt;= size()</tt>).
     */
    public Runnable get(int index) {
        return entry(index).element;
    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element.
     *
     * @param index index of element to replace.
     * @param element element to be stored at the specified position.
     * @return the element previously at the specified position.
     * @throws IndexOutOfBoundsException if the specified index is out of
     *        range (<tt>index &lt; 0 || index &gt;= size()</tt>).
     */
    public Runnable set(int index, Runnable element) {
        Entry e = entry(index);
        Runnable oldVal = e.element;
        e.element = element;
        return oldVal;
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any
     * subsequent elements to the right (adds one to their indices).
     *
     * @param index index at which the specified element is to be inserted.
     * @param element element to be inserted.
     * 
     * @throws IndexOutOfBoundsException if the specified index is out of
     *        range (<tt>index &lt; 0 || index &gt; size()</tt>).
     */
    public void add(int index, Runnable element) {
        addBefore(element, (index==size ? header : entry(index)));
    }

    /**
     * Removes the element at the specified position in this list.  Shifts any
     * subsequent elements to the left (subtracts one from their indices).
     * Returns the element that was removed from the list.
     *
     * @param index the index of the element to removed.
     * @return the element previously at the specified position.
     * 
     * @throws IndexOutOfBoundsException if the specified index is out of
     *        range (<tt>index &lt; 0 || index &gt;= size()</tt>).
     */
    public Runnable remove(int index) {
        Entry e = entry(index);
        remove(e);
        return e.element;
    }

    /**
     * Return the indexed entry.
     */
    private Entry entry(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: "+index+ ", Size: "+size);
        Entry e = header;
        if (index < size/2) {
            for (int i = 0; i <= index; i++)
                e = e.next;
        } else {
            for (int i = size; i > index; i--)
                e = e.previous;
        }
        return e;
    }


    // Search Operations

    /**
     * Returns the index in this list of the first occurrence of the
     * specified element, or -1 if the MemberList does not contain this
     * element.  More formally, returns the lowest index i such that
     * <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt>, or -1 if
     * there is no such index.
     *
     * @param o element to search for.
     * @return the index in this list of the first occurrence of the
     *         specified element, or -1 if the list does not contain this
     *         element.
     */
    public int indexOf(Runnable o) {
        int index = 0;
        if (o==null) {
            for (Entry e = header.next; e != header; e = e.next) {
                if (e.element==null)
                    return index;
                index++;
            }
        } else {
            for (Entry e = header.next; e != header; e = e.next) {
                if (o.equals(e.element))
                    return index;
                index++;
            }
        }
        return -1;
    }

    /**
     * Returns the index in this list of the last occurrence of the
     * specified element, or -1 if the list does not contain this
     * element.  More formally, returns the highest index i such that
     * <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt>, or -1 if
     * there is no such index.
     *
     * @param o element to search for.
     * @return the index in this list of the last occurrence of the
     *         specified element, or -1 if the list does not contain this
     *         element.
     */
    public int lastIndexOf(Runnable o) {
        int index = size;
        if (o==null) {
            for (Entry e = header.previous; e != header; e = e.previous) {
                index--;
                if (e.element==null)
                    return index;
            }
        } else {
            for (Entry e = header.previous; e != header; e = e.previous) {
                index--;
                if (o.equals(e.element))
                    return index;
            }
        }
        return -1;
    }

    /**
     * Returns a list-iterator of the elements in this list (in proper
     * sequence), starting at the specified position in the list.
     * Obeys the general contract of <tt>MemberList.listIterator(int)</tt>.<p>
     *
     * The list-iterator is <i>fail-fast</i>: if the list is structurally
     * modified at any time after the Iterator is created, in any way except
     * through the list-iterator's own <tt>remove</tt> or <tt>add</tt>
     * methods, the list-iterator will throw a
     * <tt>ConcurrentModificationException</tt>.  Thus, in the face of
     * concurrent modification, the iterator fails quickly and cleanly, rather
     * than risking arbitrary, non-deterministic behavior at an undetermined
     * time in the future.
     *
     * @param index index of first element to be returned from the
     *          list-iterator (by a call to <tt>next</tt>).
     * @return a ListIterator of the elements in this list (in proper
     *         sequence), starting at the specified position in the list.
     * @throws    IndexOutOfBoundsException if index is out of range
     *        (<tt>index &lt; 0 || index &gt; size()</tt>).
     * @see List#listIterator(int)
     */
    public Runnarator listIterator(int index) {
        return new ListItr(index);
    }

    private class ListItr implements Runnarator
    {
        private Entry lastReturned = header;
        private Entry next;
        private int nextIndex;
        private int expectedModCount = modCount;

        ListItr(int index) {
            if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: "+index+
                                ", Size: "+size);
            if (index < size/2) {
            next = header.next;
            for (nextIndex=0; nextIndex<index; nextIndex++)
                next = next.next;
            } else {
            next = header;
            for (nextIndex=size; nextIndex>index; nextIndex--)
                next = next.previous;
            }
        }

        public boolean hasNext() {
            return nextIndex != size;
        }

        public Runnable next() {
            checkForComodification();
            if (nextIndex == size)
                throw new NoSuchElementException();

            lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.element;
        }

        public boolean hasPrevious() {
            return nextIndex != 0;
        }

        public Runnable previous() {
            if (nextIndex == 0)
            throw new NoSuchElementException();

            lastReturned = next = next.previous;
            nextIndex--;
            checkForComodification();
            return lastReturned.element;
        }

        public int nextIndex() {
            return nextIndex;
        }

        public int previousIndex() {
            return nextIndex-1;
        }

        public void remove() {
            RunnableList.this.remove(lastReturned);
            if (next==lastReturned)
                    next = lastReturned.next;
                else
            nextIndex--;
            lastReturned = header;
            expectedModCount++;
        }

        public void set(Runnable o) {
            if (lastReturned == header)
            throw new IllegalStateException();
            checkForComodification();
            lastReturned.element = o;
        }

        public void add(Runnable o) {
            checkForComodification();
            lastReturned = header;
            addBefore(o, next);
            nextIndex++;
            expectedModCount++;
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    private static class Entry {
        Runnable element;
        Entry next;
        Entry previous;

        Entry(Runnable element, Entry next, Entry previous) {
            this.element = element;
            this.next = next;
            this.previous = previous;
        }
    }

    private Entry addBefore(Runnable o, Entry e) {
        Entry newEntry = new Entry(o, e, e.previous);
        newEntry.previous.next = newEntry;
        newEntry.next.previous = newEntry;
        size++;
        modCount++;
        return newEntry;
    }

    private void remove(Entry e) {
        if (e == header)
            throw new NoSuchElementException();

        e.previous.next = e.next;
        e.next.previous = e.previous;
        size--;
        modCount++;
    }

    /**
     * Returns an array containing all of the elements in this list
     * in the correct order.
     *
     * @return an array containing all of the elements in this list
     *         in the correct order.
     */
    public Runnable[] toArray() {
        Runnable[] result = new Runnable[size];
        int i = 0;
        for (Entry e = header.next; e != header; e = e.next)
            result[i++] = e.element;
        return result;
    }

    /**
     * Returns an array containing all of the elements in this list in
     * the correct order.  The runtime type of the returned array is that of
     * the specified array.  If the list fits in the specified array, it
     * is returned therein.  Otherwise, a new array is allocated with the
     * runtime type of the specified array and the size of this list.<p>
     *
     * If the list fits in the specified array with room to spare
     * (i.e., the array has more elements than the list),
     * the element in the array immediately following the end of the
     * collection is set to null.  This is useful in determining the length
     * of the list <i>only</i> if the caller knows that the list
     * does not contain any null elements.
     *
     * @param a the array into which the elements of the list are to
     *      be stored, if it is big enough; otherwise, a new array of the
     *      same runtime type is allocated for this purpose.
     * @return an array containing the elements of the list.
     * @throws ArrayStoreException if the runtime type of a is not a
     *         supertype of the runtime type of every element in this list.
     */
    public Runnable[] toArray(Runnable a[]) {
        if (a.length < size)
            a = (Runnable[])java.lang.reflect.Array.newInstance(
                                a.getClass().getComponentType(), size);
        int i = 0;
        for (Entry e = header.next; e != header; e = e.next)
            a[i++] = e.element;

        if (a.length > size)
            a[size] = null;

        return a;
    }

}
