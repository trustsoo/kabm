package jdf.framework.core.util.encrypt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.BitSet;

public final class ArrayByteList implements Serializable {

	private transient byte[] elements;

	private int size;
	
	private static final long serialVersionUID = -6250350905005960078L;
		
	private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	
	public ArrayByteList() {
		this(64);
	}

	public ArrayByteList(int initialCapacity) {
		elements = new byte[initialCapacity];
		size = 0;
	}

	public ArrayByteList(byte[] elems) {
		elements = elems;
		size = elems.length;
	}

	public ArrayByteList(ByteBuffer elems) {
		this(elems.remaining());
		add(elems);
	}

	public ArrayByteList(CharSequence str, Charset charset) {
		this(getCharset(charset).encode(CharBuffer.wrap(str)));
	}

	public ArrayByteList add(byte elem) {
		if (size == elements.length) ensureCapacity(size + 1);
		elements[size++] = elem;
		return this;
		// equally correct alternative impl: insert(size, elem);
	}

	public ArrayByteList add(byte[] elems, int offset, int length) {
		if (offset < 0 || length < 0 || offset + length > elems.length) 
			throw new IndexOutOfBoundsException("offset: " + offset + ", length: " + length + ", elems.length: " + elems.length);
		ensureCapacity(size + length);
		System.arraycopy(elems, offset, this.elements, size, length);
		size += length;
		return this;
		// equally correct alternative impl: replace(size, size, elems, offset, length);
	}

	public ArrayByteList add(ArrayByteList elems) {
		replace(size, size, elems);
		return this;
	}
	
	public ArrayByteList add(ByteBuffer elems) {
		int length = elems.remaining();
		ensureCapacity(size + length);
		elems.get(this.elements, size, length);
		size += length;
		return this;
		// equally correct alternative impl: replace(size, size, elems, elems.remaining());
	}
	
	public ArrayByteList add(CharSequence str, Charset charset) {
		return add(getCharset(charset).encode(CharBuffer.wrap(str)));
	}

	public ArrayByteList add(InputStream elems) throws IOException {
		// Note that our algo is correct and efficient even if
		// the input stream implements available() in weird or buggy ways.
		try {
			ensureCapacity(size + 1 + Math.max(0, elems.available()));
			int n;
			while ((n = elems.read(elements, size, elements.length - size)) >= 0) {
				size += n;
				// increasingly make room for next read (and defensively 
				// ensure we don't spin loop, attempting to read zero bytes per iteration)
				ensureCapacity(size + Math.max(1, elems.available()));
			}
		} 
		finally {
			if (elems != null) elems.close();
		}
		return this;
	}

	public ArrayByteList add(ReadableByteChannel elems) throws IOException {
		try {
			int remaining = 8192;
			if (elems instanceof FileChannel) { // we can be more efficient
				long rem = ((FileChannel) elems).size() - ((FileChannel) elems).position();
				if (size + 1 + rem > Integer.MAX_VALUE) throw new IllegalArgumentException("File channel too large (2 GB limit exceeded)");
				remaining = (int) rem;
			}
			ensureCapacity(size + 1 + remaining);
			int n;
			while ((n = elems.read(ByteBuffer.wrap(elements, size, elements.length - size))) >= 0) {
				size += n;
				// increasingly make room for next read (and defensively 
				// ensure we don't spin loop, attempting to read zero bytes per iteration)
				ensureCapacity(size + 1);
			}
		}
		finally {
			if (elems != null) elems.close();
		}
		return this;
	}
	
	public byte[] asArray() {
		return elements;
	}

	public ByteBuffer asByteBuffer() {
		return ByteBuffer.wrap(elements, 0, size);
	}

	public OutputStream asOutputStream() {
		return new OutputStream() {			
			public void write(int b) {
				add((byte) b);
			}
			public void write(byte b[], int off, int len) {
				add(b, off, len);
			}
		};
	}
	
	public int binarySearch(byte key) {
		int low = 0;
		int high = size - 1;

		while (low <= high) {
			int mid = (low + high) >> 1; // >> 1 is equivalent to divide by 2
			byte midVal = elements[mid];

			if (midVal < key)
				low = mid + 1;
			else if (midVal > key)
				high = mid - 1;
			else
				return mid; // key found
		}
		return -(low + 1); // key not found.
	}

	public ArrayByteList clear() {
		size = 0;
		return this;
		// equally correct alternative impl: remove(0, size);
	}
		
	public ArrayByteList copy() {
		return new ArrayByteList(toArray());
	}

	public boolean equals(Object otherObj) { 
		if (this == otherObj) return true;
		if (!(otherObj instanceof ArrayByteList)) return false;
		ArrayByteList other = (ArrayByteList) otherObj;
		if (size != other.size) return false;
		return indexOf(0, size, other) >= 0;
	}

	public void ensureCapacity(int minCapacity) {
		if (minCapacity > elements.length) {
			int newCapacity = Math.max(minCapacity, (elements.length * 3) / 2 + 1);
			elements = subArray(0, size, newCapacity);
		}
	}

	public int findReplace(int from, int to, ArrayByteList pattern, ArrayByteList replacement) {
		checkRange(from, to);
		if (pattern.size == 0) throw new IllegalArgumentException("pattern size must be > 0");
		int n = 0;
		while ((from = indexOf(from, to, pattern)) >= 0) {
			if (pattern != replacement) { // do more than just counting matches
				replace(from, from + pattern.size, replacement);
				to += replacement.size - pattern.size;
			}
			from += replacement.size;
			n++;
		}
		return n;
	}
	
	public byte get(int index) {
		checkIndex(index);
		return elements[index];
	}

	public int hashCode() {
		int hashCode = 1;
		byte[] elems = elements;
		for (int i = size; --i >= 0; )
			hashCode = 31*hashCode + elems[i];
		return hashCode;
	}

	public int indexOf(int from, int to, ArrayByteList subList) {
		// brute-force algorithm, but very efficiently implemented
		checkRange(from, to);
		byte[] elems = elements;
		byte[] subElems = subList.elements;
		
		int subsize = subList.size;
		to -= subsize;
		while (from <= to) {
			int i = subsize;
			int j = from + subsize;
			while (--i >= 0 && elems[--j] == subElems[i]) { // compare from right to left
				;
			}
			if (i < 0) return from; // found
			from++;
		}
		return -1; // not found
	}
	
	public int indexOf(int from, int to, byte elem) {
		checkRange(from, to);
		byte[] elems = elements;
		for (int i = from; i < to; i++) {
			if (elem == elems[i]) return i; //found
		}
		return -1; //not found
	}
	
	public int lastIndexOf(int from, int to, ArrayByteList subList) {
		// brute-force algorithm, but very efficiently implemented
		checkRange(from, to);
		byte[] elems = elements;
		byte[] subElems = subList.elements;
		
		int subsize = subList.size;
		from += subsize;
		while (from <= to) {
			int i = subsize;
			int j = to;
			while (--i >= 0 && elems[--j] == subElems[i]) { // compare from right to left
				;
			}
			if (i < 0) return to - subsize; // found
			to--;
		}
		return -1; // not found
	}
	
	public void remove(int from, int to) {
		shrinkOrExpand(from, to, 0);
		// equally correct alternative impl: replace(from, to, 0, 0); 
	}
	
	public boolean removeAll(ArrayByteList other) {
		// efficient implementation: O(N)
		if (size == 0 || other.size() == 0) return false; //nothing to do
		
		BitSet bitSet = new BitSet(256);
		for (int i = 0; i < other.size; i++) {
			bitSet.set(128 + other.elements[i]);
		}
		
		int j = 0;
		for (int i = 0; i < size; i++) {
			if (! bitSet.get(128 + elements[i])) {
				elements[j++] = elements[i];
			}
		}

		boolean modified = (j != size);
		size = j;
		return modified;
	}

	private void shrinkOrExpand(int from, int to, int replacementSize) {
		checkRange(from, to);
		int diff = replacementSize - (to - from);
		if (diff != 0) {
			ensureCapacity(size + diff);
			if (size - to > 0) { // check is for performance only (arraycopy is native method)
				// diff > 0 shifts right, diff < 0 shifts left
				System.arraycopy(elements, to, elements, to + diff, size - to);
			}
			size += diff;
		}
	}
	
	public void replace(int from, int to, byte[] replacement, int offset, int length) {
		if (offset < 0 || length < 0 || offset + length > replacement.length) 
			throw new IndexOutOfBoundsException("offset: " + offset + ", length: " + length + ", replacement.length: " + replacement.length);
		shrinkOrExpand(from, to, length);
		System.arraycopy(replacement, offset, this.elements, from, length);
	}
	
	public void replace(int from, int to, ArrayByteList replacement) {
		shrinkOrExpand(from, to, replacement.size);
		System.arraycopy(replacement.elements, 0, this.elements, from, replacement.size);
	}
	
	public void replace(int from, int to, ByteBuffer replacement, int replacementSize) {
		if (replacementSize < 0 || replacementSize > replacement.remaining()) 
			throw new IndexOutOfBoundsException("replacementSize: " + replacementSize);
		shrinkOrExpand(from, to, replacementSize);
		replacement.get(this.elements, from, replacementSize);
	}
	
	public void replace(int from, int to, byte replacement, int replacementSize) {
		checkSize(replacementSize);
		shrinkOrExpand(from, to, replacementSize);
		while (replacementSize-- > 0) elements[from++] = replacement;
		//java.util.Arrays.fill(this.elements, from, from + replacementSize, replacement);
	}
	
	public boolean retainAll(ArrayByteList other) {
		// efficient implementation: O(N)
		if (size == 0) return false;
		if (other.size() == 0) {
			size = 0;
			return true;
		}

		BitSet bitSet = new BitSet(256);
		for (int i = 0; i < other.size; i++) {
			bitSet.set(128 + other.elements[i]);
		}
		
		int j = 0;
		for (int i = 0; i < size; i++) {
			if (bitSet.get(128 + elements[i]))
				elements[j++] = elements[i];
		}

		boolean modified = (j != size);
		size = j;
		return modified;
	}
	
	public void rotate(int from, int to, int distance) {
		checkRange(from, to);
		int length = to - from;
		if (length == 0) return;
		distance = distance % length;
		if (distance < 0) distance += length;
		if (distance == 0) return;

		byte[] elems = elements;		
		for (int nMoved = 0; nMoved != length; from++) {
			byte displaced = elems[from];
			int i = from;
			do {
				i += distance;
				if (i >= to) i -= length;
				
				byte tmp = elems[i];
				elems[i] = displaced;
				displaced = tmp;

				nMoved++;
			} while (i != from);
		}
    }

	public void set(int index, byte element) {
		checkIndex(index);
		elements[index] = element;
		// equally correct alternative impl: replace(index, index+1, element, 1); 
	}

	public int size() {
		return size;
	}

	public void sort(boolean removeDuplicates) {
		if (size <= 60) { // heuristic threshold according to benchmarks (theory: N*logN = 2*256)
			java.util.Arrays.sort(elements, 0, size);
			if (removeDuplicates) removeDuplicates();
		}
		else {
			countSort(removeDuplicates);
		}
	}
	
	private void countSort(boolean removeDuplicates) {
		final int min = Byte.MIN_VALUE;
		final int max = Byte.MAX_VALUE;
		int[] counts = new int[max - min + 1]; // could use BitSet if removeDuplicates
		for (int i = size; --i >= 0; ) {
			counts[elements[i] - min]++;
		}
		
		int j = 0;
		for (int i = min; i <= max; i++) { 
			int k = counts[i - min];
			if (removeDuplicates && k > 1) k = 1;
			while (--k >= 0) {
				elements[j++] = (byte) i;
			}
		}
		size = j;
	}

	private void removeDuplicates() {
		int i = 0;
		int j = 0;
		while (j < size) {
			byte elem = elements[j++];
			elements[i++] = elem;
			while (j < size && elements[j] == elem) j++; // skip duplicates
		}
		size = i;
	}
	
	private byte[] subArray(int from, int length, int capacity) {
		byte[] subArray = new byte[capacity];
		System.arraycopy(elements, from, subArray, 0, length);
		return subArray;
	}

	public ArrayByteList subList(int from, int to) {
		checkRange(from, to);
		return new ArrayByteList(subArray(from, to - from, to - from));
	}

	public byte[] toArray() {
		return subArray(0, size, size);
	}

	public String toString() {
		//return toList().toString();
		StringBuffer buf = new StringBuffer(4*size);
		buf.append("[");
		for (int i = 0; i < size; i++) {
			buf.append(elements[i]);
			if (i < size-1) buf.append(", ");
		}
		buf.append("]");
		return buf.toString();
	}

	public String toString(Charset charset) {
		return toString(0, size, charset);
	}

	public String toString(int from, int to, Charset charset) {
		checkRange(from, to);
		return getCharset(charset).decode(ByteBuffer.wrap(this.elements, from, to-from)).toString();
	}

	public void trimToSize() {
		if (elements.length > size) {
			elements = subArray(0, size, size);
		}
	}

	private void checkIndex(int index) {
		if (index >= size || index < 0)
			throw new IndexOutOfBoundsException("index: " + index
						+ ", size: " + size);
	}

	private void checkRange(int from, int to) {
		if (from < 0 || from > to || to > size)
			throw new IndexOutOfBoundsException("from: " + from + ", to: "
						+ to + ", size: " + size);
	}

	private void checkSize(int newSize) {
		if (newSize < 0)
			throw new IndexOutOfBoundsException("newSize: " + newSize);
	}
	
	private static Charset getCharset(Charset charset) {
		return charset == null ? DEFAULT_CHARSET : charset;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeInt(elements.length);
		out.write(elements, 0, size);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		elements = new byte[in.readInt()];
		in.readFully(elements, 0, size);
	}
}
