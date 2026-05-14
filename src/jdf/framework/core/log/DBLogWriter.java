package jdf.framework.core.log;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DBLogWriter
{
	private static final String LOG_ID = "<at:DBLogWriter> ";
	public static void write(String thread_id, String serverId, String log_mode, String content)
	{			
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO TB_FRAMEWORK_LOG")
		   .append("(thread_id, server_alias, log_mode, logs)").append("\n")
		   .append("VALUES").append("\n")
		   .append("(?, ?, ?, ?)");
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try
		{
			Config conf = Configuration.lookup("/logger");
			String poolName = conf.getString("logDBPool");
			
			
			conn = ConnectionManager.getConnection(poolName);
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, thread_id);
			pstmt.setString(2, serverId);
			pstmt.setString(3, log_mode);
			pstmt.setString(4, content);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch(Exception ex)
		{
			System.out.println(LOG_ID+ex.toString());
		} finally
		{
			if(pstmt != null) try{pstmt.close();pstmt=null;}catch(Exception ex){}
			if(conn != null) try{conn.close();conn=null;}catch(Exception ex){}
		}
	}
}