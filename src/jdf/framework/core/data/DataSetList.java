package jdf.framework.core.data;

import java.util.ArrayList;

/**
 * DataSet의 List 형태
 * 
 * @author
 * 
 */
public class DataSetList extends ArrayList<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4456712016142821618L;

	// 기준 DataSet
	private DataSet ds;

	// 사이즈 체크용 기준 key
	private Object baseKey;

	/**
	 * 기본 생성자
	 * 
	 * @param ds
	 *            DataSet
	 */
	public DataSetList(DataSet ds) {
		super();
		this.ds = ds;
	}

	/**
	 * 생성자
	 * @param ds DataSet
	 * @param baseKey 기준key
	 */
	public DataSetList(DataSet ds, Object baseKey) {
		super();
		this.ds = ds;
		this.baseKey = baseKey;
	}

	/**
	 * 최대 갯수를 return 한다.
	 * 
	 */
	public Object get(int idx) {
		return new DataSetOneRowMap(this.ds, idx);
	}

	
	/**
	 * 저장된 객체의 길이정보를 return
	 */
	public int size() {
		if (this.baseKey == null)
			return this.ds.getMaxDataSize();
		else
			return this.ds.getCount(this.baseKey);
	}

}