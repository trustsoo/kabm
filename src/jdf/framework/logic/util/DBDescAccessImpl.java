package jdf.framework.logic.util;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.*;
import java.util.*;

import jdf.framework.core.db.ConnectionManager;
import jdf.framework.core.util.SmartStringArray;

/**
 * MySQL�� ���Ͽ� URI�� �ش��ϴ� �н��� ����Ʈ�� �����ϰų� ��ȯ �޴´�.
 * �� Ŭ������ ����ϱ��� �켱 �����ͺ��̽����� �Ʒ��� ��: ���̺��� x���Ͽ��� �Ѵ�.
 * �Ʒ��� �� ���̺�; �� �����ִ� ���̴�.
 * Mysql�� example)
 * CREATE TABLE `ioschema` (
 * `uri` varchar(100) NOT NULL default '',
 * `idx` int(11) NOT NULL default '0', 
 * `value` longblob,
 * PRIMARY KEY  (`uri`, `idx`),
 * UNIQUE KEY `descriptores_index` (`uri`, `idx`)
 * );
 *
 */
public class DBDescAccessImpl implements DescAccessible
{
	// �⺻���� ���̺��
	private static String DEFAULT_TABLE_NAME = "descriptores";
	
	// �����ͺ��̽� Ǯ ��
	private String dbPoolname;
	
	// ���̺� ��
	private String tablename;
	
	// descriptor�� ���丮��8�� ������ ����(�ʱⰪ true)
	private boolean isHistory = true;

    /**
	 * ����Ʈ�� ��d�� �����ͺ��̽� POOL; ����Ѵ�.
	 */
	public DBDescAccessImpl()
    {
        dbPoolname = null;
    }
    
	/**
	 * ����Ʈ�� ��d�� �����ͺ��̽� POOL; ����Ѵ�.
	 */
	public DBDescAccessImpl(String dbPoolname)
	{
		this(dbPoolname, DEFAULT_TABLE_NAME, true);
	}

    /**
	 * �ش� �����ͺ��̽� POOL; ����Ѵ�.
	 * @param dbPoolname
	 */
	public DBDescAccessImpl(String dbPoolname, String tablename, boolean isHistory)
    {
        this.dbPoolname = null;
        this.dbPoolname = dbPoolname;
		this.tablename = tablename;
		this.isHistory = isHistory;
    }

    private Connection getConnection() throws Exception
    {
        if(dbPoolname != null && dbPoolname.length() > 0)
            return ConnectionManager.getConnection(dbPoolname);
        else
            return ConnectionManager.getConnection();
    }
	
	/**
	 * URI�� ���� ����� ����Ʈ�� ��ȯ�Ѵ�.
	 * @param uri
	 * @return byte[]
	 */
    public byte[] read(String uri) throws Exception
    {
        String tokens[] = SmartStringArray.split("/", uri);
        
		if(tokens.length == 1)
            tokens = SmartStringArray.split("\\", uri);
        
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < tokens.length - 1; i++)
		{
            if(tokens[i] != null && tokens[i].length() > 0)
                sb.append(tokens[i]).append("/");
		}
        sb.append(tokens[tokens.length - 1]);

        String normalUri = sb.toString();

		Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        byte[] value = null;

        try
        {
            String sql = "SELECT value FROM " + tablename + " WHERE uri=? order by idx desc";
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, normalUri);
            rs = statement.executeQuery();
            if(rs != null && rs.next())
                value = rs.getBytes(1);
        }
        catch(SQLException sqlExc)
        {
            throw sqlExc;
        }
		finally
        {
            if (rs != null)
            {
                try{ rs.close(); } catch (SQLException se) {}
            }
            if (statement != null)
            {
                try{ statement.close(); } catch (SQLException se) {}
            }
            if (connection != null)
            {
                try{ connection.close(); } catch (SQLException se) {}
            }
        }

        return value;
    }

	/**
	 * URI�� ���� ����� ����Ʈ�� ��Ʈ�����·� ��ȯ�Ѵ�.
	 * @param uri
	 * @return InputStream
	 */
	public InputStream readStream(String uri) throws Exception
	{
		return new ByteArrayInputStream(read(uri));
	}
	
	/**
	 * URI�� ����Ʈ�� �����Ѵ�.
	 * @param uri
	 * @param b
	 */
    public void write(String uri, byte[] b) throws Exception
    {
		String tokens[] = SmartStringArray.split("/", uri);
        
		if(tokens.length == 1)
            tokens = SmartStringArray.split("\\", uri);
        
		StringBuffer sb = new StringBuffer();

        for(int i = 0; i < tokens.length - 1; i++)
		{
            if(tokens[i] != null && tokens[i].length() > 0)
                sb.append(tokens[i]).append("/");
		}
        sb.append(tokens[tokens.length - 1]);

        String normalUri = sb.toString();

        Connection connection = null;
        PreparedStatement statement = null;
		ResultSet rs = null;
		int idx = -1;
        try
        {
			String sql = null;
        	if (isHistory)
        	{
	        	sql = "SELECT idx FROM " + tablename + " WHERE uri=? order by idx desc";
				connection = getConnection();
				statement = connection.prepareStatement(sql);
				statement.setString(1, normalUri);
				rs = statement.executeQuery();
				if(rs != null && rs.next())
					idx = rs.getInt(1);
        	}
        	else
        	{
				sql = "DELETE FROM " + tablename + " WHERE uri=?";
				connection = getConnection();
				statement = connection.prepareStatement(sql);
				statement.setString(1, normalUri);
				statement.execute();
        	}
			
            sql = "INSERT INTO " + tablename + " (uri, value, idx) VALUES (?, ?, ?)";
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, normalUri);
            statement.setBytes(2, b);
			statement.setInt(3, ++idx);
            statement.execute();
        }
        catch(Exception sqlExc)
        {
            throw sqlExc;
        }
        finally
        {
            if (statement != null)
            {
                try{ statement.close(); } catch (SQLException se) {}
            }
            if (connection != null)
            {
                try{ connection.close(); } catch (SQLException se) {}
            }
        }
    }
	
	/**
	 * URI�� ��Ʈ��; �����Ѵ�.
	 * @param uri
	 * @param b
	 */
    public void write(String uri, String s) throws Exception
    {
        write(uri, s.getBytes());
    }

	/**
	 * URI�� ��Ʈ��; �����Ѵ�.
	 * @param uri
	 * @param b
	 */
	public void write(String uri, InputStream in) throws Exception
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);

		byte[] buf = new byte[1024];

		int j;
		while ((j = in.read(buf)) > -1) 
			bos.write(buf, 0, j);

		buf = bos.toByteArray();

		write(uri, buf);
	}
	
	/**
	 * URI�� �ش��ϴ� Descriptor�� x���ϴ��� ���θ� Ȯ���Ѵ�.
	 * @param uri
	 * @return boolean x�� ����
	 */
	public boolean exists(String uri) throws Exception
	{
		// ���� ���丮 ������ ���̶�� ��v�� ����; ���Ѵ�.
		if (isHistory)
			return false;
			
		String tokens[] = SmartStringArray.split("/", uri);
        
		if(tokens.length == 1)
			tokens = SmartStringArray.split("\\", uri);

		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < tokens.length - 1; i++)
		{
			if(tokens[i] != null && tokens[i].length() > 0)
				sb.append(tokens[i]).append("/");
		}
		sb.append(tokens[tokens.length - 1]);

		String normalUri = sb.toString();

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		byte[] value = null;

		try
		{
			String sql = "SELECT value FROM " + tablename + " WHERE uri=? order by idx desc";
			connection = getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, normalUri);
			rs = statement.executeQuery();
			if(rs != null)
			{
				if (rs.next())
					return true;
				else
					return false;
			}
			else
				return false;	
		}
		catch(SQLException sqlExc)
		{
			throw sqlExc;
		}
		finally
		{
			if (rs != null)
			{
				try{ rs.close(); } catch (SQLException se) {}
			}
			if (statement != null)
			{
				try{ statement.close(); } catch (SQLException se) {}
			}
			if (connection != null)
			{
				try{ connection.close(); } catch (SQLException se) {}
			}
		}
	}
	
	/**
	 * ��ü URI�� ����Ʈ�� ���´�.
	 * @return String[] URI�� ����Ʈ
	 */
	public String[] list() throws Exception
	{
		SmartStringArray array = new SmartStringArray();
	
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		byte[] value = null;

		try
		{
			String sql = "SELECT DISTINCT uri FROM " + tablename;
			connection = getConnection();
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			
			if (rs != null)
			{
				while (rs.next())
				{
					String uri = rs.getString(1);
					array.add(uri);
				}
			}
		}
		catch(SQLException sqlExc)
		{
			throw sqlExc;
		}
		finally
		{
			if (rs != null)
			{
				try{ rs.close(); } catch (SQLException se) {}
			}
			if (statement != null)
			{
				try{ statement.close(); } catch (SQLException se) {}
			}
			if (connection != null)
			{
				try{ connection.close(); } catch (SQLException se) {}
			}
		}

		return array.toArray();
	}
	
	/**
	 * �ش� URI ���丮���� ����Ʈ�� ���´�.
	 * @return String[] URI�� ����Ʈ
	 */
	public String[] list(String uri) throws Exception
	{
		String tokens[] = SmartStringArray.split("/", uri);

		if(tokens.length == 1)
			tokens = SmartStringArray.split("\\", uri);

		StringBuffer sb = new StringBuffer();
		sb.append("/");
		for(int i = 0; i < tokens.length; i++)
		{
			if(tokens[i] != null && tokens[i].length() > 0)
				sb.append(tokens[i]).append("/");
		}
		
		String normalUri = sb.toString();
		
		Map map = new HashMap();

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		byte[] value = null;

		try
		{
			String sql = "SELECT DISTINCT uri FROM " + tablename +
						" WHERE substring(concat('/', uri), 1,"+normalUri.length()+")= ?";
			connection = getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, normalUri);
			rs = statement.executeQuery();
			System.out.println("normalUri["+normalUri+"]");
			System.out.println("sql      ["+sql+"]");
			if (rs != null)
			{
				while (rs.next())
				{
					String strUri = rs.getString(1).substring(normalUri.length() - 1);
					System.out.println("["+strUri+"]");
					int p = strUri.indexOf("/");
					if (p == -1)
					{
						//����
						map.put("F " + strUri, null);
					}
					else
					{
						//���丮
						String dir = strUri.substring(0, p);
						map.put("D " + dir, null);
					}
				}
			}
		}
		catch(SQLException sqlExc)
		{
			throw sqlExc;
		}
		finally
		{
			if (rs != null)
			{
				try{ rs.close(); } catch (SQLException se) {}
			}
			if (statement != null)
			{
				try{ statement.close(); } catch (SQLException se) {}
			}
			if (connection != null)
			{
				try{ connection.close(); } catch (SQLException se) {}
			}
		}

		return (String[]) map.keySet().toArray(new String[]{});
	}

    public static void main(String args[]) throws Exception
    {
        DBDescAccessImpl access = new DBDescAccessImpl();
        
		byte b[] = {49, 50, 51, 52, 53, 54, 55, 56, 57, 48};
		
		// /a/b/c
        String uri = args[0];
        access.write(uri, b);
        b = access.read(uri);
        System.out.println(new String(b, 0, b.length));
    }
}
