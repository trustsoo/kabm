package jdf.framework.core.data;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author
 *
 */
public class DataSetOneRowMap implements Map {
	
	private DataSet ds;
	private int seq;
	
	/**
	 * 
	 * @param ds
	 * @param seq
	 */
	public DataSetOneRowMap(DataSet ds, int seq)
	{
		this.ds=ds;
		this.seq=seq;
	}

	public void clear() {
		return;
	}

	public boolean containsKey(Object key) {
		return this.ds.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return this.ds.containsValue(value);
	}

	public Set entrySet() {
		return null;
	}

	public Object get(Object key) {
		return this.ds.get(key, this.seq);
	}

	public boolean isEmpty() {
		return this.ds.isEmpty();
	}

	public Set keySet() {
		return this.ds.keySet();
	}

	public Object put(Object key, Object value) {
		return this.ds.put(key, value, this.seq);
	}

	public void putAll(Map t) {
		this.ds.putAll(t);

	}

	public Object remove(Object key) {
		return this.ds.remove(key);
	}

	public int size() {
		return this.ds.size();
	}

	public Collection values() {
		return this.ds.values();
	}

}