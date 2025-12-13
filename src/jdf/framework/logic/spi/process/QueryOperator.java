/*
 * Created on 2003-11-05
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jdf.framework.logic.spi.process;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.opencsv.CSVWriter;
import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.ResultSetDataSet;
import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.Field;
import jdf.framework.core.data.schema.FieldType;
import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.core.data.schema.StoredQuery;
import jdf.framework.core.data.schema.format.Formatter;
import jdf.framework.core.data.schema.format.UnEscapeHtmlFormatter;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.core.util.StringFormater;
import jdf.framework.core.util.encrypt.CipherUtil;
import jdf.framework.core.util.encrypt.KISA_SHA256;
import jdf.framework.logic.transform.XlsFormat;



/**
 * 실제 SQL Query 문을 수행하기 위한 class
 * 
 * 
 * @author
 * @version 1.0
 * @since 2003-11-05 오전 11:49:23
 * 
 */
public class QueryOperator extends StoredQuery
{
	private final static String LOG_ID = "<l:QueryOperator> ";

	// DB측 encodign
	private String dbEncoding = null;

	// VM Encoding
	private static String vmEncoding = null;
	
	private static boolean isUnknownCharaterLength = false;
	
	static {
		try {
			String tmp = null;
			// String tmp =
			// Configuration.lookup("/resource/anylogic").getString("dbEncoding");
			// if (tmp != null && tmp.length() > 0)
			// dbEncoding = tmp;

			tmp = Configuration.lookup("/resource/anylogic").getString("vmEncoding");
			if (tmp != null && tmp.length() > 0)
				vmEncoding = tmp;

		} catch (Exception e) {
		}
		
		try
		{
			isUnknownCharaterLength = Configuration.lookup("/resource/anylogic").getBoolean("isUnknownCharaterLength");
		} catch(Exception ex)
		{
			isUnknownCharaterLength = false;
		}

	}

	/**
     * 기본생성자
     * 
     */
	public QueryOperator() {
	}

	/**
     * DB의 encoding을 세팅한다.
     * 
     * @param enc
     */
	public QueryOperator(String enc) {
		dbEncoding = enc;

		//Logger.debug.println(LOG_ID + "QueryOperator encoding:" + enc);
	}

	/**
     * ResultSet 결과값으로 부터 데이터를 가져오는 경우 DB의 encoding에서 VM encoding으로 변환해서 가져온다.
     * 
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
	protected String selectString(String str) throws UnsupportedEncodingException
	{
		if (str == null)
			return null;
		if (dbEncoding != null) {

			return new String(str.getBytes(dbEncoding), vmEncoding);
		} else
			return str;
	}

	/**
     * DBMS 로 query 나 값을 세팅하는 경우 String을 VM encoding에서 DB 측 encoding으로 변환한다.
     * 
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
	protected String inputString(String str)
	{
		if (str == null)
			return null;
		if (dbEncoding != null) {
			try {
				return new String(str.getBytes(vmEncoding), dbEncoding);

			} catch (UnsupportedEncodingException ue) {
				return str;
			}

		} else
			return str;
	}

	/**
     * SQL query문에서 '$변수명' 부분을 치환시킨다.
     * 
     * 
     */
	protected String replaceSQL(String query, DataSet optData)
	{
		//query = replaceSQLandTrans(query, optData);

		if (query == null || query.indexOf("$") < 0 || optData == null)
			return query;

		// Iterator keys = optData.keySet().iterator();

		Block[] blocks = new Block[] {};
		
		String prefix = "input.";

		if (optData.getIOSchema() != null) {
			if (optData.getInOutType() == IOSchema.IN) {
				blocks = optData.getIOSchema().getInputBlocks();
			} else {
				blocks = optData.getIOSchema().getOutputBlocks();
				prefix = "output.";
			}

		}
		for (int i = 0; i < blocks.length; i++) {
			Field[] fields = blocks[i].getFields();
			for (int j = 0; j < fields.length; j++) {
				// System.out.println(" **** "+j);
				String key = fields[j].getName();
				// System.out.println(" **** "+key);
				Object tmp = optData.get(key);
				if (tmp != null) {
					String x = tmp.toString();
					query = StringFormater.replaceStr(query, "$" + key, x);
					query = StringFormater.replaceStr(query, "${" + key+"}", x);
					query = StringFormater.replaceStr(query, "${"+prefix+ key+"}", x);
				}

				// System.out.println("END **** "+j);
			}

		}

		return query;
	}

	/**
     * &변수는 '를 ''로 바꾸어 준다.
     * 
     * 
     * @param query
     * @param optData
     * @return
     */
	protected String replaceSQLandTrans(String query, DataSet optData)
	{
		if (query == null || query.indexOf("#") < 0 || optData == null)
			return query;

		// Iterator keys = optData.keySet().iterator();

		Block[] blocks = new Block[] {};

		if (optData.getIOSchema() != null) {
			if (optData.getInOutType() == IOSchema.IN) {
				// System.out.println(" ----------> IN");
				blocks = optData.getIOSchema().getInputBlocks();
				// System.out.println(" ----------> IN2");
			} else {
				// System.out.println(" ----------> OUT");
				blocks = optData.getIOSchema().getOutputBlocks();
				// System.out.println(" ----------> OUT2");
			}
		}
		for (int i = 0; i < blocks.length; i++) {
			Field[] fields = blocks[i].getFields();
			for (int j = 0; j < fields.length; j++) {
				// System.out.println(" **** "+j);
				String key = fields[j].getName();
				// System.out.println(" **** "+key);
				Object tmp = optData.get(key);
				if (tmp != null) {
					String x = tmp.toString();
					x = StringFormater.replaceStr(x, "'", "''");
					query = StringFormater.replaceStr(query, "#" + key, x);
				}

				// System.out.println("END **** "+j);
			}

		}

		return query;
	}

	/**
     * 
     * @param query
     * @param optData
     * @return
     */
	/*
     * protected String replaceSQL_oldVersion(String query, DataSet optData) { if (query == null || query.indexOf("$") <
     * 0 || optData == null) return query;
     * 
     * Iterator keys = optData.keySet().iterator();
     * 
     * for (int k = 0; keys.hasNext(); k++) {
     * 
     * String key = keys.next().toString(); // System.out.println("2--> "+key); // System.out.println("3-->
     * "+optData.get(key));
     * 
     * query = StringFormater.replaceStr(query, "$" + key, optData.get(key).toString()); }
     * 
     * return query; }
     */

	/**
     * PreparedStatement 를 통해 ? 부분에 값을 세팅한다.
     * 
     * 
     * 
     * @param conn
     * @param pstmt
     * @param input
     * @param output
     * @param fieldNames
     * @param schema
     * @throws SQLException
     */
	protected boolean setValue(Connection conn, PreparedStatement pstmt, DataSet input, DataSet output,
			String[] fieldNames, int seq, IOSchema schema) throws SQLException
	{

		boolean isNext = false;

		boolean isLogEnable = Logger.sql.isPrintMode();
		
		String newSQLQuery = null;
		
		boolean isBindingLog = false;
		
		try
		{
			String logMode = schema.getProcessor().getProperty("sqlLogMode");
			String isPrintSchema = schema.getProcessor().getProperty("isPrint");
			
			if(isPrintSchema != null && "N".equals(isPrintSchema))
				isLogEnable = false;			
			
			if(isLogEnable && logMode != null && "bind".equals(logMode))
			{
				isBindingLog = true;
				
				if (isDanymicQuery()) {
					newSQLQuery = replaceSQL(getSqlQuery(), input);
					newSQLQuery = replaceSQL(newSQLQuery, output);

				} else
					newSQLQuery = getSqlQuery();
				
			}
			
		} catch(Exception ex)
		{			
		}
		
		// debug
		/*
         * if (input == null) Logger.debug.println("input nuylll");
         * 
         * if (output == null) Logger.debug.println("output null");
         * 
         * if (fieldNames == null) Logger.debug.println("fieldnames null");
         * 
         */
		String name = null;
		String data = "";

		try {
			for (int i = 0; i < fieldNames.length; i++) {
				int dataLen = 0;
				name = fieldNames[i];

				if (name == null)
					continue;

				Field field = null;
				DataSet source = null;

				/*
                 * if (input.containsKey(name)) { //Logger.debug.println(LOG_ID+"setValue * "+name+"|"); field =
                 * input.getField(name); source = input; } else { //Logger.debug.println(LOG_ID+"setValue "+name+"|");
                 * field = output.getField(name); source = output; }
                 * 
                 * if (field == null) { field = schema.getInputField(name);
                 * 
                 * if (field == null) { field = schema.getOutputField(name);
                 * 
                 * if (field == null) throw new SQLException(name + "의 키값은 input/output에 존재하지 않습니다"); } }
                 */

				field = schema.getInputField(name);
				source = input;

				if (field == null) {
					field = schema.getOutputField(name);
					source = output;

					if (field == null)
						throw new SQLException(name + "의 키값은 input/output에 존재하지 않습니다");
				}
				
				
				// data= "";

				source.unfixNull();

				Object result = source.get(name, seq);
				if (result == null)
					result = field.getDefaultValue();

				if (result != null)
					data = result.toString();
				else
					data = null;

				if (i == 0 && source.getCount(name) - 1 > seq)
					isNext = true;
				
				
				String encryptType = field.getProperty("encryption");
				if(data != null && encryptType != null )
				{					
					if(encryptType.equals("aria"))
					{
						String plainKey =  input.getText("ENCRYPTION_KEY");
											
						if(plainKey == null || "".equals(plainKey))
						{
							Logger.warn.println(LOG_ID+" input ENCRYPTION_KEY is not found...");
						} else
						{					
							int plainKeyLen = plainKey.length();
							byte[] encryptKeyBytes = new byte[32];
							
							KISA_SHA256.SHA256_Encrpyt( plainKey.getBytes(), plainKeyLen, encryptKeyBytes );					
							
							data = CipherUtil.hexToString(CipherUtil.encode(encryptKeyBytes, data.getBytes("utf-8")));
							Logger.debug.println(LOG_ID+"input aria encrypt Data :"+data );
						}
					} else if(encryptType.equals("sha256"))
					{
						byte[] encryptBytes = new byte[32];
						KISA_SHA256.SHA256_Encrpyt( data.getBytes("utf-8"), data.getBytes("utf-8").length, encryptBytes );
						data = CipherUtil.hexToString(encryptBytes);
						Logger.debug.println(LOG_ID+"input sha256 encrypt Data :"+data );
					}
				}
				
				
				
				if (data != null)
					dataLen = data.length();
				/*
                 * try {
                 * 
                 * data = source.get(name, seq).toString();
                 * 
                 * 
                 * if(i==0 && source.getCount(name)-1>seq) isNext = true; } catch (NullPointerException ne) { Object
                 * defaultVal = field.getDefaultValue(); input.put(name, defaultVal);
                 * 
                 * //data = field.getDefaultValue().toString(); //input.put(name, data); }
                 */
				source.fixNull();

				int idx = i + 1;

				int fieldType = field.getType();
				// String filedName = field.getName();

				switch (fieldType) {

				case FieldType.STRING:

					data = inputString(data);
					
					int len = data == null ? 0 : data.getBytes().length;

					if (len < 1200)
						pstmt.setString(idx, data);
					else {
						java.io.StringReader reader = new java.io.StringReader(data);
						if(!isUnknownCharaterLength)
							pstmt.setCharacterStream(idx, reader, len);
						else
							pstmt.setCharacterStream(idx, reader);

					}

					if (isLogEnable)
					{
						if(!isBindingLog)
							Logger.sql.println(idx + ":String = " + data);
						else
						{
							try
							{
								if(data != null)
								{
									data = data.replaceAll("[$]", "__\t__");
									newSQLQuery = newSQLQuery.replaceFirst("\\?",  "'"+data+"'");
									newSQLQuery = newSQLQuery.replaceAll("__\t__", "\\$");
								} else
								{
									newSQLQuery = newSQLQuery.replaceFirst("\\?",  "'"+data+"'");
								}
							} catch(Exception ex)
							{
								Logger.warn.println(LOG_ID+"print log query error.("+schema.getName()+"("+input.getText("cmd")+"))"+ ex.toString());
							}
							
						}
					}
					
					
					break;

				case FieldType.BLOB:					
					byte[] buff = (byte[])result;
					ByteArrayInputStream bais = new ByteArrayInputStream(buff);
					pstmt.setBinaryStream(idx, bais, buff.length);
					//pstmt.setAsciiStream(idx, bais, buff.length);
					if (isLogEnable)
					{
						if(!isBindingLog)
							Logger.sql.println(idx + ":Blob");
						else
							newSQLQuery = newSQLQuery.replaceFirst("\\?",  ":Blob");
					}
					
					
					/*
					 * ByteArrayInputStream bi = (ByteArrayInputStream)result;
					pstmt.setBinaryStream(idx, bi, bi.available());
					
					InputStream in = (InputStream)result;
					byte[] buffer = StreamUtil.getBytes(in);
					pstmt.setBytes(idx, buffer);
					//pstmt.setAsciiStream(idx, bais, buff.length);
					//pstmt.setBinaryStream(idx, in);		
					
					if (isLogEnable)
					{
						if(!isBindingLog)
							Logger.sql.println(idx + ":Blob");
						else
							newSQLQuery = newSQLQuery.replaceFirst("\\?",  ":Blob");
					}
					 * 
					 * */
					
					break;

				case FieldType.CLOB:

					Clob x = (Clob) result;
					pstmt.setClob(idx, x);

					if (isLogEnable)
					{
						if(!isBindingLog)
							Logger.sql.println(idx + ":Clob");
						else
							newSQLQuery = newSQLQuery.replaceFirst("\\?",  ":Clob");
					}
					
					
					break;

				case FieldType.INTEGER:

					if (dataLen == 0) {
						pstmt.setNull(idx, Types.INTEGER);

					} else {
						pstmt.setInt(idx, Integer.parseInt(data));
					}
					if (isLogEnable)
					{
						if(!isBindingLog)
							Logger.sql.println(idx + ":int = " + data);
						else
							newSQLQuery = newSQLQuery.replaceFirst("\\?",  data);
					}
					
					
					break;

				case FieldType.FLOAT:
					if (dataLen == 0) {
						pstmt.setNull(idx, Types.FLOAT);

					} else {
						pstmt.setFloat(idx, Float.parseFloat(data));
					}
					if (isLogEnable)
					{
						if(!isBindingLog)
							Logger.sql.println(idx + ":float = " + data);
						else
							newSQLQuery = newSQLQuery.replaceFirst("\\?",  data);
					}
					
					
					
					break;

				case FieldType.DOUBLE:
					if (dataLen == 0) {
						pstmt.setNull(idx, Types.DOUBLE);

					} else {
						pstmt.setDouble(idx, Double.parseDouble(data));
					}
					if (isLogEnable)
					{
						if(!isBindingLog)
							Logger.sql.println(idx + ":double = " + data);
						else
							newSQLQuery = newSQLQuery.replaceFirst("\\?",  data);
					}
					
					
					
					break;

				case FieldType.LONG:
					if (dataLen == 0) {
						pstmt.setNull(idx, Types.DECIMAL);

					} else {
						pstmt.setLong(idx, Long.parseLong(data));
					}
					if (isLogEnable)
					{
						if(!isBindingLog)
							Logger.sql.println(idx + ":long = " + data);
						else
							newSQLQuery = newSQLQuery.replaceFirst("\\?",  data);
					}
					
					
					
					break;

				default:
					pstmt.setObject(idx, data);
					if (isLogEnable)
					{
						if(!isBindingLog)
							Logger.sql.println(idx + ":Object = " + data);
						else
							newSQLQuery = newSQLQuery.replaceFirst("\\?",  data);
					}
					
					
					
					break;
				}

			}
			
			String Qid = "TR:" + schema.getName() + " ID:" + this.getName();
			
			if(newSQLQuery != null)
				Logger.sql.println(Qid+"\n"+newSQLQuery);
			

		}

		catch (NumberFormatException nfe) {
			String msg = "setValue NumberFormatErr [" + name + "=" + data + "]";
			Logger.err.println(LOG_ID + msg);
			throw new SQLException(msg);
		} catch (NullPointerException npe) {
			String msg = "setValue NullPointerException [" + name + "=" + data + "]";
			Logger.err.println(LOG_ID + msg);
			throw new SQLException(msg);
		}

		catch (Exception ee) {
			String msg = "setValue Exception [" + name + "=" + data + "] " + ee.toString();
			Logger.err.println(LOG_ID + msg, ee);
			throw new SQLException(msg);
		}

		return isNext;
	}

	/**
     * ResultSet으로 부터 해당 fieldType으로 idx 번째 값을 찾아 return 한다.
     * 
     * @param rset
     * @param fieldType
     * @param idx
     * @return
     * @throws SQLException
     * @throws IOException
     */
	protected Object getValue(ResultSet rset, int fieldType, int idx) throws SQLException, IOException
	{
		Object value = null;
		switch (fieldType) {

		case FieldType.STRING:
			value = selectString(rset.getString(idx));
			break;

		case FieldType.BYTE_ARRAY:
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			BufferedInputStream inStream = new BufferedInputStream(rset.getBinaryStream(idx));

			byte buffer[] = new byte[16 * 1024];
			int bytesRead;

			while ((bytesRead = inStream.read(buffer)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}
			inStream.close();

			value = bos.toByteArray();

			break;

		case FieldType.BLOB:

			/*
             * InputStream input = rset.getAsciiStream(idx); ByteArrayOutputStream out = new ByteArrayOutputStream();
             * while (input.available() > 0) { int size = input.available(); //Logger.debug.println("size = " + size);
             * byte[] buf = new byte[size]; input.read(buf); out.write(buf); } byte[] result = out.toByteArray();
             */
			Blob content = rset.getBlob(idx);
			BufferedInputStream bis = new BufferedInputStream(content.getBinaryStream());			
			int readSize = (int) content.length();
			byte[] buf = new byte[readSize];
			int readSize2 = bis.read(buf);
			bis.close();

			if (readSize2 != readSize)
				throw new IOException("read size error");

			// value = new String(result, 0, result.length, "KSC5601");
			value = buf;

			break;

		case FieldType.CLOB:
			java.sql.Clob clob = rset.getClob(idx);

			if (clob != null) {

				Reader reader = clob.getCharacterStream();

				char[] charbuffer = new char[(int) clob.length()];

				StringBuffer clobData = new StringBuffer();
				int bytesread = 0;
				while ((bytesread = reader.read(charbuffer)) != -1) {
					clobData.append(charbuffer, 0, bytesread);
				}
				reader.close();
				value = selectString(clobData.toString());

			} else
				value = null;

			break;

		case FieldType.INTEGER:
			value = new Integer(rset.getInt(idx));
			break;

		case FieldType.FLOAT:
			value = new Float(rset.getFloat(idx));
			break;

		case FieldType.DOUBLE:
			value = new Double(rset.getDouble(idx));
			break;

		case FieldType.LONG:
			value = new Long(rset.getLong(idx));
			break;

		case FieldType.BOOLEAN:
			value = new Boolean(rset.getBoolean(idx));
			break;
			
		default:
			value = rset.getObject(idx);

		}

		return value;

	}

	/**
     * db column 명으로 값을 가져온다.
     * 
     * 
     * @param rset
     * @param fieldType
     * @param dbColumnNm
     * @return
     * @throws SQLException
     * @throws IOException
     */
	protected Object getValue(ResultSet rset, int fieldType, String dbColumnNm) throws SQLException, IOException
	{
		Object value = null;
		switch (fieldType) {

		case FieldType.STRING:
			value = selectString(rset.getString(dbColumnNm));
			break;

		case FieldType.BLOB:

			try {
				InputStream input = rset.getAsciiStream(dbColumnNm);

				ByteArrayOutputStream out = new ByteArrayOutputStream();

				while (input.available() > 0) {

					int size = input.available();
					// Logger.debug.println("size = " + size);
					byte[] buf = new byte[size];

					input.read(buf);

					out.write(buf);
				}

				byte[] result = out.toByteArray();

				// value = new String(result, 0, result.length, "KSC5601");
				value = result;
			} catch (IOException ioe) {
			}

			break;

		case FieldType.CLOB:
			java.sql.Clob clob = rset.getClob(dbColumnNm);

			if (clob != null) {

				Reader reader = clob.getCharacterStream();

				char[] charbuffer = new char[(int) clob.length()];

				StringBuffer clobData = new StringBuffer();
				int bytesread = 0;
				while ((bytesread = reader.read(charbuffer)) != -1) {
					clobData.append(charbuffer, 0, bytesread);
				}
				reader.close();
				value = selectString(clobData.toString());

			} else
				value = null;

			break;

		case FieldType.INTEGER:
			value = new Integer(rset.getInt(dbColumnNm));
			break;

		case FieldType.FLOAT:
			value = new Float(rset.getFloat(dbColumnNm));
			break;

		case FieldType.DOUBLE:
			value = new Double(rset.getDouble(dbColumnNm));
			break;

		case FieldType.LONG:
			value = new Long(rset.getLong(dbColumnNm));
			break;

		default:
			value = rset.getObject(dbColumnNm);

		}

		return value;

	}

	/**
     * ResultSet에서 데이터를 가져와 output 변수에 값을 누적하여 채워넣는다. oneRow는 한번의 fetch 결과만을 넣는다.
     * 
     */
	public boolean fetchOneRow(ResultSet rset, DataSet output, DataSet oneRow, int i) throws SQLException
	{
		// System.out.println(">> "+i);

		String fieldName = null;
		boolean existResult = false;

		try {
			// System.out.println(i);
			existResult = rset.next();

			if (!existResult)
				return false;
			
			Field[] outFields = getOutputFieldArray(output.getIOSchema());

			for (int j = 0; j < outFields.length; j++) {
				fieldName = outFields[j].getName();

				if (fieldName == null || fieldName.length() == 0)
					continue;

				int fieldType = outFields[j].getType();

				Object value = null;

				int idx = j + 1;
				
				try{
				value = getValue(rset, fieldType, idx);
				
				
				if(outFields[j].getProperty("encryption") != null && "aria".equals(outFields[j].getProperty("encryption")))
				{					
					
					String plainKey =  output.getProperty("ENCRYPTION_KEY");
					
					if(plainKey == null || "".equals(plainKey))
					{
						Logger.warn.println(LOG_ID+" output ENCRYPTION_KEY is not found...");
					} else
					{
						int plainKeyLen = plainKey.length();					
						byte[] encryptKeyBytes = new byte[32];
						
						KISA_SHA256.SHA256_Encrpyt( plainKey.getBytes(), plainKeyLen, encryptKeyBytes );
						
						value = new String(CipherUtil.decode(encryptKeyBytes, CipherUtil.stringToHex((String)value)) , "utf-8");
						
						if(value != null && !"".equals(value)) value = ((String)value).trim();
						
						Logger.debug.println(LOG_ID+"output decrypt Data:"+value );
					}
				}
				}catch(Exception e){}
				
				output.put(fieldName, value, i);
				if (oneRow != null)
					oneRow.put(fieldName, value);

			}

		} catch (NullPointerException ne) {
			Logger.err.println(LOG_ID + "output schema에 [" + fieldName + "] field가 정의되지 않았습니다.", ne);
		} catch (SQLException sqle) {
			Logger.err.println(LOG_ID + "fetchOneRow " + fieldName + " error");
			throw sqle;
		}

		return existResult;

	}
	
	
	
	/**
     * ResultSet 객체로 DataSet 을 추출한다.
     * 
     * 
     * 
     */
	public void fetchResultSet(ResultSet rset, DataSet input, DataSet output) throws SQLException, Exception
	{
		
		if(input.getProperty(XlsFormat.IS_MAKE_XLS_FILE) != null && "true".equals(input.getProperty(XlsFormat.IS_MAKE_XLS_FILE)))
		{
			fetchResultSetMakeXLS(rset, input, output);
			return;
		}
		
		
		if(input.getProperty(XlsFormat.IS_MAKE_CSV_FILE) != null && "true".equals(input.getProperty(XlsFormat.IS_MAKE_CSV_FILE)))
		{
			fetchResultSetMakeCSV(rset, input, output);
			return;
		}
		
		int rowPerPage = Integer.MAX_VALUE;
		int page = 1;

		// 총 검색건수,fetch된 수는 아니다.
		int total_count = 0;

		boolean isPagingMode = false;

		try {
			rowPerPage = Integer.parseInt(output.getProperty(DataSet.ROWS_PER_PAGE));
			page = Integer.parseInt(output.getProperty(DataSet.PAGE));
			isPagingMode = true;

		} catch (Exception e) {
		}

		// paging 관련 속성이 추가된 경우 이 부분이 실행된다.
		if (isPagingMode) {

			int cursorposition = (page - 1) * rowPerPage ;
			
			rset.setFetchSize(rowPerPage * 10);
			
			/*
			if(page>1){
				rset.absolute(cursorposition);
				rset.previous();
			}
			*/

			
			for (total_count = 0; total_count < cursorposition && rset.next(); total_count++) {

			}
			

			Logger.debug.println(LOG_ID + "page info " + rowPerPage + "/" + page);

		}

		try {
			boolean isNext = true;

			int i = 0;

			while (isNext) {
				isNext = fetchOneRow(rset, output, null, i);
				if( isNext )
					i++;
				if (i >= rowPerPage)
					break;
			}
			total_count = total_count + i;

			if (isPagingMode) {

				// 마지막으로 cursor를 이동
				// ResultSet 이 TYPE_FORWARD_ONLY 로 설정되면 last 와 같은 탐색불가
				// last 인 경우 oracle JDBC 내부적으로 next하면서 data를 cache 
				// 데이터를 읽으므로 더 느린 문제점이 있다.
				// rset.last();
				// total_count = rset.getRow();
				while (rset.next()) {
					total_count++;
				}
				

				output.setProperty(DataSet.RESULT_COUNT, String.valueOf(total_count));
				output.setProperty(DataSet.MAX_PAGE, String.valueOf((int) Math.ceil((double) total_count
						/ (double) rowPerPage)));
			}

			/*
             * if (!existResult) { for (int j = 0; j < getOutputFieldNames().length; j++)
             * output.remove(getOutputFieldNames()[j]); }
             */

		}
		/*
         * catch (NullPointerException ne) { Logger.err.println(LOG_ID + "output schema에 [" + fieldName + "] field가 정의되지
         * 않았습니다.", ne); }
         */

		catch (SQLException e) {
			Logger.err.println(LOG_ID + "getDataSet - SQLException", e);
			// e.printStackTrace();
			throw e;
		}

		// return output;

	}
	
	public void fetchResultSetMakeCSV(ResultSet rset, DataSet input, DataSet output) 
			throws SQLException, Exception
	{
		CSVWriter writer = null;
		
		try
		{
			String filename = System.currentTimeMillis()+"";
			//String dataDir = Configuration.getConfigPath() + File.separator + "csv";
			
			Config conf = Configuration.lookup("/MultipartAttributes");			
			String dataDir = conf.getString("dir")+ File.separator + "csv";
			
	        File dir = new File(dataDir);
	        if(!dir.exists()) dir.mkdirs();
			
			
			writer =  new CSVWriter(new OutputStreamWriter(new FileOutputStream(dir.getPath()+java.io.File.separator+filename+".csv"), "UTF-8"),',', '"');
			writer.writeAll(rset, true);
			
			output.put(XlsFormat.RESULT_FILE_NAME, filename+".csv");
            output.put(XlsFormat.RESULT_FILE_PATH, dir.getPath()+java.io.File.separator);
            output.put(XlsFormat.RESULT_FILE_SIZE, new File(dir.getPath()+java.io.File.separator+filename+".csv").length());
		} catch(Exception ex)
		{
			throw ex;
		} finally
		{	if(writer != null) try{writer.close();}catch(Exception ex){}
		}
	}
	
	public void fetchResultSetMakeXLS(ResultSet rset, DataSet input, DataSet output) 
			throws SQLException, Exception
	{
		SXSSFWorkbook wb = new SXSSFWorkbook(100);
		
		SXSSFSheet sheet = null;
		
		Map<String, CellStyle> styles = null;  
		FileOutputStream out = null;
		
		try
		{
			String[] blockNames = SmartStringArray.split(",", input.getProperty(XlsFormat.BLOKNAMES));
			String sheetTitle = input.getProperty(XlsFormat.XLS_SHEET_TITLE);
			String condition = input.getProperty(XlsFormat.XLS_CONDITION_TITLE);
			
			Block[] blocks = new Block[blockNames.length];
			for(int idx=0; idx<blockNames.length; idx++)
			{
				blocks[idx] = output.getIOSchema().getBlockById(blockNames[idx]);
				if(blocks[idx] == null)
					throw new NullPointerException(blockNames[idx] + " is null");				
			}
			
			//String dataDir = Configuration.getConfigPath() + File.separator + "xls";
			
			Config conf = Configuration.lookup("/MultipartAttributes");			
			String dataDir = conf.getString("dir")+ File.separator + "xls";
			
	        File dir = new File(dataDir);
	        if(!dir.exists()) dir.mkdirs();
	        
	        if(sheetTitle == null || sheetTitle.equals("")) sheetTitle = "Sheet"; 
	        
	        styles = XlsFormat.createStyles(blocks, wb);
	        
	        
	        int l = 0;	        
	        int k = 0;
	        int zdx = 0;
	        
	        int headerRowLen = 0;
	        	
	        SXSSFRow row = null;
            SXSSFCell cell = null;
	        while(rset.next())
            {                    	
	        	if(k % 600000 == 0)
			    {   
	        		Logger.debug.println(LOG_ID+"current fetch "+(k+1));
	        		
					sheet = (SXSSFSheet) wb.createSheet(sheetTitle+"_"+(l));
					sheet.setRandomAccessWindowSize(100);
					
					l++;
					
					for (headerRowLen = 0; headerRowLen < blocks.length; headerRowLen++) 
					{
						row = sheet.createRow(headerRowLen);
												
						Field[] fields = blocks[headerRowLen].getFields();
		                for(int p=0; p<fields.length; p++)
		                {
		                    Field f = fields[p];   
		                    
		                    String headerTitle = f.getLabel();
		            		if (headerTitle == null)
		            			headerTitle = f.getName();
		                    
		            		cell = row.createCell(p);
		            		
							cell.setCellValue(headerTitle);
							cell.setCellStyle(styles.get("header"));		            		
		                    
		                }						
					}					
					zdx = 0;					
			    }
	        	
	        	String value = null;
	        	row = sheet.createRow(headerRowLen+zdx);
	        	for (int i = 0; i < blocks.length; i++) 
                {
    	        	Field[] fields = blocks[i].getFields();
                    for(int p=0; p<fields.length; p++)
                    {
                    	if(fields[i].getFormat() == null || fields[i].getFormat().indexOf("unescapeHtml") <= -1)
                    		value = rset.getString(p+1);
                    	else
                    	{
                    		value = rset.getString(p+1);
                    		
                    		Formatter formatter = null;
                    		
                    		if(fields[i].getFormat().indexOf("unescapeHtml4") > -1)
                			{
                				formatter = new UnEscapeHtmlFormatter("4.0");
                			} else if(fields[i].getFormat().indexOf("unescapeHtml3") > -1)
                			{
                				formatter = new UnEscapeHtmlFormatter("3.0");
                			}
                    		
                    		formatter.format(rset.getString(p+1));
                    		
                    	}
                    	
                    	cell = row.createCell(p);
						cell.setCellValue(value);
						cell.setCellStyle(styles.get(i+"_"+p));
                    	
                    }
                    
                    value = null;
                	cell = null;
                }
            	
            	k++;
            	zdx++;
            }
	        
	        for (int i = 0; i < blocks.length; i++) 
            {
	        	Field[] fields = blocks[i].getFields();
	        	for(int p=0; p<fields.length; p++)
                {
	        		String width = fields[p].getProperty("width"); 
	        		if( width != null && !"".equals(width))
	        		{
	        			try
	        			{
	        				sheet.setColumnWidth((short)p, (short)Integer.parseInt(width));
	        			} catch(Exception ex)
	        			{
	        				Logger.warn.println(LOG_ID+"setColumnWidth error:"+ex.toString());
	        				sheet.autoSizeColumn((short)p);
	        			}
	        		}
                }
            }
	        
	        
	        cell = null;
			row = null;
			
			String filename = System.currentTimeMillis()+"";
			out = new FileOutputStream(dir.getPath()+java.io.File.separator+filename+".xlsx");
	        wb.write(out);
	        out.flush();
	        out.close();
			wb.close();
			wb.dispose();
			
			output.put(XlsFormat.RESULT_FILE_NAME, filename+".xlsx");
            output.put(XlsFormat.RESULT_FILE_PATH, dir.getPath()+java.io.File.separator);
            output.put(XlsFormat.RESULT_FILE_SIZE, new File(dir.getPath()+java.io.File.separator+filename+".xlsx").length());
	        
			
		} catch(Exception ex)
		{
			throw ex;
		} finally
		{	
			if(wb != null) try{wb.dispose();} catch(Exception ex){}
			if(wb != null) try{wb.close();  } catch(Exception ex){}
			if(out != null) try{out.close();} catch(Exception ex){}
		}
	}
	

	/*
     * public ClobWrapper getClobWrapper(String className) throws Exception {
     * 
     * if (className == null) return null;
     * 
     * ClobWrapper wrapper= (ClobWrapper) Class.forName(className).newInstance(); return wrapper; }
     */

	/*
     * public void execute(java.sql.Connection conn, IOSchema schema, DataSet input, DataSet output) throws
     * java.sql.SQLException { String datasourceNm = this.getDataSourceName();
     * 
     * if(datasourceNm!=null) { Logger.info.println("----->"+datasourceNm); java.sql.Connection conn2 =
     * ConnectionManager.getConnection(datasourceNm);
     * 
     * try { executeQuery(conn2,schema,input,output); } catch(SQLException sqle) { throw sqle; } finally {
     * conn2.close(); } } else { Logger.info.println("** ----->"); executeQuery(conn,schema,input,output); } }
     */

	/**
     * BLD 를 수행한다.
     * 
     */
	public void execute(java.sql.Connection conn, IOSchema schema, DataSet input, DataSet output)
			throws java.sql.SQLException, Exception
	{
		boolean isLogPrint = Logger.sql.isPrintMode();

		String QID = "TR:" + schema.getName() + " ID:" + this.getName();

		// 성능측정
		//SessionTracer tracer = SessionTracer.getInstance(this, QID);
		//tracer.start();

		PreparedStatement pstmt = null;
		ResultSet rset = null;

		String p1 = input.getProperty(DataSet.PAGE);
		String p2 = input.getProperty(DataSet.ROWS_PER_PAGE);

		if (p1 != null)
			output.setProperty(DataSet.PAGE, p1);
		if (p2 != null)
			output.setProperty(DataSet.ROWS_PER_PAGE, p2);
		// output.setProperties(input.getProperties());

		try {
			String sqlQuery = null;

			if (isDanymicQuery()) {
				sqlQuery = replaceSQL(getSqlQuery(), input);
				sqlQuery = replaceSQL(sqlQuery, output);

			} else
				sqlQuery = getSqlQuery();

			
			boolean isBindingLog = false;
			
			try
			{
				String logMode = schema.getProcessor().getProperty("sqlLogMode");
				String isPrintSchema = schema.getProcessor().getProperty("isPrint");
				
				if(logMode != null && "bind".equals(logMode))
				{
					isBindingLog = true;
				}
				
				if(isPrintSchema != null && "N".equals(isPrintSchema))
					isLogPrint = false;
				
				
			} catch(Exception ex)
			{			
			}
			
			if (isLogPrint && !isBindingLog)
				Logger.sql.println(QID + "\n" + sqlQuery);

			// paging 기능이 추가되었다면...
			if (p2 != null)
				// pstmt = conn.prepareStatement(inputString(sqlQuery),
				// ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				pstmt = conn.prepareStatement(inputString(sqlQuery), ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
				//pstmt = conn.prepareStatement(inputString(sqlQuery), ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			else
				pstmt = conn.prepareStatement(inputString(sqlQuery));

			
			if(this.getQueryTimeOut() > 0)
				pstmt.setQueryTimeout(this.getQueryTimeOut());
			
			boolean isLoop = true;

			// Logger.info.println(">>>>>>>>>>>>>>>>>>
			// "+pstmt.getMaxFieldSize());

			// 미리 result 에서 값을 다 가져올것인가 ? 관련되 property
			// 이 값이 false 이면
			// ResultSetDataSet객체를 이용하여 처리한다.
			String v = schema.getProcessor().getProperty(ResultSetDataSet.PREFETCH_PROPERTY);
			boolean isPreFetch = true;
			if ("false".equals(v)) {
				isPreFetch = false;

				// 만약 output 객체가
				// ResultSetDataSet 객체가 아니면
				// 무조건 pre fetch 를 해야한다.
				if (!(output instanceof ResultSetDataSet))
					isPreFetch = true;
			}

			for (int i = 0; isLoop; i++) {

				isLoop = setValue(conn, pstmt, input, output, getIntputFieldNames(), i, schema);
				// ? 부분에 값을 대입한다.

				// String[] outputFieldName = getOutputFieldNames();
				// if (outputFieldName == null || outputFieldName.length == 0)

				if (!isSelectQuery()) {
					int updateNum = pstmt.executeUpdate();

					output.setProperty("rowcount.update:" + this.getName(), String.valueOf(updateNum));

					if (isLogPrint)
						Logger.sql.println("TR:" + schema.getName() + " ID:" + this.getName() + " updateNum:"
								+ updateNum);

				} else {
					if (this.getMaxRows() > 0)
						pstmt.setMaxRows(this.getMaxRows());

					if (this.getFetchSize() > 0)
						pstmt.setFetchSize(this.getFetchSize());
					
					rset = pstmt.executeQuery();
					
					if (this.getFetchSize() > 0)
						rset.setFetchSize(this.getFetchSize());
					
					if (output.getIOSchema() == null)
						output.setIOSchema(schema, IOSchema.OUT);
					
					if(!"".equals(input.getText("ENCRYPTION_KEY")))
						output.setProperty("ENCRYPTION_KEY", input.getText("ENCRYPTION_KEY"));
					
					
					// 미리 fetch 하는 경우가 아닌 경우
					if (!isPreFetch) {
						ResultSetDataSet rsDs = (ResultSetDataSet) output;						

						rsDs.setStatement(pstmt);
						rsDs.setResultSet(rset);
						
						// 아래 close 를 하지 못하게 null 로 한다.
						rset = null;
						pstmt = null;
					} else {						
						fetchResultSet(rset, input, output);
					}

				}

				// loop 하는 쿼리가 아니면 바로 빠져나온다.
				if (!this.isLoopQuery())
					break;
			}
		} catch (SQLException sql) {
			throw sql;
		}
		/*
         * catch (Exception e) { e.printStackTrace(); }
         */
		finally {
			try {
				if (rset != null)
					rset.close();
			} catch (SQLException seq) {
			}

			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException sqe) {
			}

			//tracer.end();
		}

	}

	/**
     * Query를 수행하고 ResultSet이 있는 경우는 ResultSet를 반납하고, 그렇지 않을 경우는 null을 반납한다.
     * 
     * 
     * @param conn
     * @param schema
     * @param input
     * @param output
     * @return
     * @throws java.sql.SQLException
     */
	public SQLResult getSQLResult(java.sql.Connection conn, IOSchema schema, DataSet input, DataSet output)
			throws java.sql.SQLException
	{
		PreparedStatement pstmt = null;

		String sql_id = null;

		try {
			String sqlQuery = null;

			if (isDanymicQuery()) {
				sqlQuery = replaceSQL(getSqlQuery(), input);
				sqlQuery = replaceSQL(sqlQuery, output);

			} else
				sqlQuery = getSqlQuery();

			sql_id = "TR:" + schema.getName() + " ID:" + this.getName();

			if (Logger.sql.isPrintMode())
				Logger.sql.println(sql_id + "\n" + sqlQuery);

			pstmt = conn.prepareStatement(inputString(sqlQuery));

			setValue(conn, pstmt, input, output, getIntputFieldNames(), 0, schema);
			// ? 부분에 값을 대입한다.

			// String[] outputFieldName = getOutputFieldNames();
			// if (outputFieldName == null || outputFieldName.length == 0)

			if (!isSelectQuery()) {
				int updateNum = pstmt.executeUpdate();

				output.setProperty("rowcount.update:" + this.getName(), String.valueOf(updateNum));

				if (Logger.sql.isPrintMode())
					Logger.sql.println("TR:" + schema.getName() + " ID:" + this.getName() + " updateNum:" + updateNum);

			} else {
				ResultSet rset = pstmt.executeQuery();
				// fetchResultSet(rset, output, getOutputFieldNames());

				return new SQLResult(pstmt, rset);
			}
		} catch (SQLException sql) {
			Logger.err.println(LOG_ID + " SQL ERROR " + sql_id + " ERR_CODE:" + sql.getErrorCode());

			throw sql;
		} finally {
			/*
             * try { if (pstmt != null) pstmt.close(); } catch (SQLException sqe) { }
             */
		}
		/*
         * catch (Exception e) { e.printStackTrace(); }
         */

		return null;

	}	
}
