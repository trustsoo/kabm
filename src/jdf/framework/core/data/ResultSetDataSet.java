package jdf.framework.core.data;

import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.core.data.schema.StoredQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * 
 * 
 * @author
 * 
 */
public class ResultSetDataSet extends DataSet
{

	/**
     * 
     */
	private static final long serialVersionUID = -7596281408591122091L;

	/**
     * bld에서 이 객체를 사용하려면 bld property 에 dbms:pre-fetch 값을 false로 해야 한다.
     * 
     */
	public final static String PREFETCH_PROPERTY = "dbms:pre-fetch";

	private ResultSet rs;

	private Statement stmt;

	// resultset next() 함수를 부른 상태인가?
	boolean isFetchEnable = false;

	private String[] getFetchColumnArray;

	/**
     * 
     * 
     */
	public ResultSetDataSet(String name) {
		super(name);

	}

	/**
     * default creator
     * 
     * @param rs
     */
	public ResultSetDataSet(ResultSet rs) {
		setResultSet(rs);
	}

	/**
     * 
     * @param stmt
     */
	public void setStatement(Statement stmt)
	{
		this.stmt = stmt;
	}

	/**
     * 
     * @param rs
     */
	public void setResultSet(ResultSet rs)
	{
		this.rs = rs;

	}

	/**
     * 
     */
	public void setIOSchema(IOSchema schema, int type)
	{
		super.setIOSchema(schema, type);

		if (type == IOSchema.OUT) {
			StoredQuery[] queryArray = schema.getProcessor().getStoredQueryArray();

			if (queryArray.length > 0) {
				// bld의 <getProperties>에 정의된 field 목록을 가져온다.
				this.getFetchColumnArray = queryArray[0].getOutputFieldNames();
			}

		}
	}

	/**
     * 
     * @param bldFieldName
     * @return
     */
	private int getFetchColumnIndex(String bldFieldName)
	{
		if (this.getFetchColumnArray != null) {

			for (int i = 0; i < this.getFetchColumnArray.length; i++) {
				if (this.getFetchColumnArray[i].equals(bldFieldName))
					return i + 1;
			}
		}
		// 조건을 만족하는 것이 없으면 -1 return
		return -1;

	}

	/**
     * 
     */
	public Object get(Object key, int seq)
	{
		// resultset 에서 fetch 할수있으면 먼저가져온다.
		Object result = getFetchData(key, seq);

		return result;
	}

	/**
     * 
     */
	public String getText(Object key, int seq)
	{
		return (String) getFetchData(key, seq);

	}

	/**
     * 
     * 
     * @param key
     * @param seq
     * @return
     */
	private Object getFetchData(Object key, int seq)
	{
		Object result = null;

		if (isFetchEnable) {

			int columnIdx = getFetchColumnIndex(key.toString());
			try {
				// 순서를 찾지 못했으면 그냥 필드 이름으로 찾는다.
				if (columnIdx == -1) {
					result = rs.getString(key.toString());
				} else {
					result = rs.getString(columnIdx);
				}
			} catch (SQLException sqle) {

			}

		}

		return result;
	}

	private Object baseKeyObject = null;

	private int count = 0;

	/**
     * 
     */
	public int getCount(Object key)
	{
		if (baseKeyObject == null)
			baseKeyObject = key;

		// 처음 검사하는 필드에 대해서만 올바른 갯수정보를 주고
		// 그외는 다 무시.
		// XmlTransformerImpl 참조
		if (!baseKeyObject.equals(key))
			return 0;

		// this.getIOSchema().getProcessor().getStoredQueryByName("wer")

		// int size = super.getCount(key);

		// System.out.println(key +" "+size);

		try {
			if (rs.next()) {
				isFetchEnable = true;
				// System.out.print(size);

				return ++count;
			} else {
				isFetchEnable = false;
				// System.out.print(">" + size + "<");
			}
		} catch (SQLException sqle) {

			System.err.println(sqle.toString());
		}

		return count;

	}

	/**
     * 이 객체사용자 최종적으로 이 메쏘드를 호출해야 한다.
     * 
     */
	public void clear()
	{
		super.clear();

		try {
			if (rs != null)
				rs.close();
		} catch (SQLException sqle) {

		}
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException sqle) {

		}
	}

}