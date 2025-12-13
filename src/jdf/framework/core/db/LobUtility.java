package jdf.framework.core.db;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;

import oracle.sql.CLOB;

public class LobUtility
{
	public static byte[] getBytes(Clob content) throws SQLException, IOException
	{
		BufferedInputStream bis = new BufferedInputStream(content.getAsciiStream());
		byte[] buf = new byte[(int) content.length()];
		bis.read(buf, 0, buf.length);
		bis.close();

		return buf;

	}
	
	/**
	 * Method getString.
	 * @param content
	 * @return String
	 */
	public static String getString(Clob content) throws SQLException, IOException
	{
		return new String(getBytes(content));
	}
	
	
	
	public static char[] getBytes(CLOB content) throws SQLException, IOException
	{
		BufferedReader br = new BufferedReader(content.getCharacterStream());
		char[] charArrary = new char[(int)content.length()];
		br.read( charArrary, 0,  (int)content.length() );

		return charArrary;
		
		/*
		content.getChars( content.length(), 0, charArrary );
		*/

	}
	
	/**
	 * Method getString.
	 * @param formXml
	 * @return String
	 */
	public static String getString(CLOB content) throws SQLException, IOException
	{
		return new String(getBytes(content));
	}
}
