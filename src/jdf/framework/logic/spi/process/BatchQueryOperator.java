package jdf.framework.logic.spi.process;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.core.log.Logger;


/**
 * 
 * 일괄 batch 처리용 query Operator
 * 
 * 내부적으로 java.sql.Statement 의 executeBatch() 메쏘드를 사용한다.
 * 
 * @author
 * 
 */
public class BatchQueryOperator extends QueryOperator {
	private final static String LOG_ID = "<l:BatchQueryOperator> ";

	/**
	 * 기본생성자
	 * 
	 * @param encoding
	 */
	public BatchQueryOperator(String encoding) {
		super(encoding);
	}

	/**
	 * BLD 를 실행한다.
	 * 
	 */
	public void execute(java.sql.Connection conn, IOSchema schema,
			DataSet input, DataSet output) throws java.sql.SQLException {
		PreparedStatement pstmt = null;

		boolean isLogPrint = Logger.sql.isPrintMode();

		try {
			String sqlQuery = null;

			if (isDanymicQuery()) {
				sqlQuery = replaceSQL(getSqlQuery(), input);
				sqlQuery = replaceSQL(sqlQuery, output);

			} else {
				sqlQuery = getSqlQuery();
			}

			if (isLogPrint) {
				Logger.sql.println("TR:" + schema.getName() + " ID:"
						+ this.getName() + " [BATCH]\n" + sqlQuery);
			}

			pstmt = conn.prepareStatement(inputString(sqlQuery));

			boolean isLoop = true;

			// Logger.info.println(">>>>>>>>>>>>>>>>>>
			// "+pstmt.getMaxFieldSize());

			int executeMaxRows = Integer.MAX_VALUE;
			try {
				executeMaxRows = Integer.parseInt(input
						.getProperty("batch-execute-maxrows"));
			} catch (Exception e) {
			}

			if (isSelectQuery())
				throw new java.sql.SQLException(
						"batch 모드에서는 select 쿼리를 사용할 수 없습니다.");

			for (int i = 0; isLoop; i++) {
				// if (Logger.sql.isPrintMode()) Logger.sql.println("row
				// sequence:"+i);

				isLoop = setValue(conn, pstmt, input, output,
						getIntputFieldNames(), i, schema);
				// ? 부분에 값을 대입한다.

				// String[] outputFieldName = getOutputFieldNames();
				// if (outputFieldName == null || outputFieldName.length == 0)

				pstmt.addBatch();

				if (!isLoop || (i != 0 && i % executeMaxRows == 0)) {

					Logger.debug.println(LOG_ID + "executeBatch " + i
							+ " batch-maxrow:" + executeMaxRows);
					int[] updateNum = pstmt.executeBatch();

					output.add(this.getName() + ":updateNum", updateNum);
				}

			}

			// int[] resultUpCounts = pstmt.executeBatch();

		} catch (SQLException sql) {
			throw sql;
		}
		/*
		 * catch (Exception e) { e.printStackTrace(); }
		 */
		finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException sqe) {
			}
		}

	}

}
