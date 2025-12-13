/*
 * Created on 2003-11-11
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jdf.framework.logic.spi.process;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.schema.Field;
import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.core.data.schema.ProcedureMapper;
import jdf.framework.core.log.Logger;


/**
 * stored procedure 를 실행하기 위한 class
 * 
 * 
 * @author
 * @version 1.0
 * @since 2003-11-11 오후 4:53:08
 *  
 */
public class ProcedureQueryOperator extends QueryOperator
{
    private final static String LOG_ID = "<l:ProcedureQueryOperator> ";

    /**
     * 기본생성자
     *
     */
    public ProcedureQueryOperator()
    {
        super();
    }
    
    /**
     * dbEncoding 을 이용한 생성자
     * 
     * @param dbEncoding
     */
    public ProcedureQueryOperator(String dbEncoding)
    {
        super(dbEncoding);
    }

    /**
     * BLD를 샐행한다.
     * 
     */
    public void execute(java.sql.Connection conn, IOSchema schema, DataSet input, DataSet output)
            throws java.sql.SQLException
    {
        CallableStatement pstmt = null;
        ResultSet rset = null;
        
        String sql_id = null;

        try
        {

            String sqlQuery = replaceSQL(getSqlQuery(), input);
            sqlQuery = replaceSQL(sqlQuery, output);
            
            sql_id = "TR:" + schema.getName() + " ID:" + this.getName();

            if (Logger.sql.isPrintMode())
                Logger.sql.println(sql_id + "\n" + sqlQuery);

            // sp 는 encoding 하면 문제가 생긴가?
            //pstmt = conn.prepareCall(inputString(sqlQuery));
            pstmt = conn.prepareCall(inputString(sqlQuery));
            
            if(this.getQueryTimeOut() > 0)
				pstmt.setQueryTimeout(this.getQueryTimeOut());
            
            boolean isLoop = true;

            int procedureMapNum = this.getProcedureMapperNum();

            String[] inputFieldNames = getIntputFieldNames();

            if (procedureMapNum > 0)
            {
                inputFieldNames = this.getInputProcedureMappingFields();

                for (int i = 0; i < procedureMapNum; i++)
                {
                    ProcedureMapper mapper = this.getProcedureMapper(i);

                    if (mapper.getMode() == ProcedureMapper.OUT)
                    {
                        //Logger.debug.println(" * regist param
                        // "+mapper.getSequence()+" "+mapper.getDBType());
                        //Logger.debug.println(" * "+Types.REF+"
                        // "+OracleTypes.CURSOR);

                        if (mapper.getDBOutSize() > 0)
                            pstmt.registerOutParameter(mapper.getSequence(), mapper.getDBType(), mapper.getDBOutSize());
                        else
                            pstmt.registerOutParameter(mapper.getSequence(), mapper.getDBType());
                    }
                }
            }
            
            
            //Logger.info.println("--->>>>>>>>>>>>>>>>>>1 "+pstmt.getMaxFieldSize());
            //pstmt.setMaxFieldSize(3000000);
            //Logger.info.println("--->>>>>>>>>>>>>>>>>>2 "+pstmt.getMaxFieldSize());
            

            for (int i = 0; isLoop; i++)
            {
                isLoop = setValue(conn, pstmt, input, output, inputFieldNames, i, schema);
                // ? 부분에 값을 대입한다.

                String[] outputFieldName = getOutputFieldNames();

                if (outputFieldName == null || outputFieldName.length == 0)
                {
                    //Logger.debug.println("before execure");
                    pstmt.execute();
                    //Logger.debug.println("end execure");

                    if (procedureMapNum > 0)
                    {
                        for (int q = 0; q < procedureMapNum; q++)
                        {
                            ProcedureMapper mapper = this.getProcedureMapper(q);

                            if (mapper.getMode() == ProcedureMapper.OUT)
                            {
                                if (mapper.isResultSetMode())
                                {
                                    // ResultSet으로 가져오기
                                    ResultSet rs = (ResultSet) pstmt.getObject(mapper.getSequence());
                                    // 구현 필요

                                    int maxrowNum = this.getMaxRows();
                                    if (maxrowNum < 1)
                                        maxrowNum = Integer.MAX_VALUE;

                                    // test
                                    //System.out.println( "
                                    // >>>>>>>>>>>>>>>>>>"+maxrowNum);
                                    //maxrowNum = 10;

                                    for (int p = 0; rs.next() && p < maxrowNum; p++)
                                    {
                                        for (int k = 0; k < mapper.getFieldNum(); k++)
                                        {
                                            String io_fieldNm = mapper.getFieldName(k);

                                            if (io_fieldNm == null || io_fieldNm.length() == 0)
                                                continue;

                                            Field f = output.getField(io_fieldNm);

                                            if (f == null)
                                                throw new Exception(io_fieldNm + " is not defined in BLD");

                                            int type = f.getType();

                                            String dbColumnNm = mapper.getDbMappingName(k);

                                            Object val = null;
                                            if (dbColumnNm == null)
                                            {
                                                val = this.getValue(rs, type, k + 1);
                                            }

                                            else
                                                val = this.getValue(rs, type, dbColumnNm);

                                            output.put(io_fieldNm, val, p);
                                        }

                                    }

                                    rs.close();

                                } //if( mapper.isResultSetMode() )
                                else
                                    output.put(mapper.getFieldName(0), pstmt.getObject(mapper.getSequence()));
                            }
                        }

                    }

                } else
                {
                    if (this.getMaxRows() > 0)
                        pstmt.setMaxRows(this.getMaxRows());
                    
                    if (this.getFetchSize() > 0)
                        pstmt.setFetchSize(this.getFetchSize());
                    
                    
                    
                    rset = pstmt.executeQuery();
                    fetchResultSet(rset, input, output);
                }

                // loop 하는 쿼리가 아니면 바로 빠져나온다.
                if (!this.isLoopQuery())
                    break;
            }

        } catch (SQLException sql)
        {
            //sql.printStackTrace();
            Logger.err.println(LOG_ID+" SQL ERROR "+sql_id+" ERR_CODE:"+sql.getErrorCode());
            
            throw sql;
        } catch (Exception e)
        {
            Logger.err.println(LOG_ID+" SQL ERROR "+sql_id, e);
            
        } finally
        {
			try {
				if (rset != null)
					rset.close();
			} catch (SQLException seq) {
			}

            try
            {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException sqe)
            {
            }
        }

    }

}