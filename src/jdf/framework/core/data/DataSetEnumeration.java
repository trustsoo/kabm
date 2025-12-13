package jdf.framework.core.data;

import java.util.Enumeration;

/**
 * 
 * @author
 *
 */
public class DataSetEnumeration implements Enumeration {
	
	private DataSet ds;
	private int maxSize=0;
	private int crntIdx=0;
	
	public DataSetEnumeration(DataSet ds)
	{
		this.ds=ds;
		this.maxSize = ds.getMaxDataSize();
	}
	

	public boolean hasMoreElements() {
		if(this.maxSize>this.crntIdx) {
			return true;
		}
		
		return false;
	}

	public Object nextElement() {
		if(this.maxSize>this.crntIdx) {
		    Object result = new DataSetOneRowMap(this.ds, this.crntIdx);
			this.crntIdx++;
			return result;
		}
		return null;

	}

}