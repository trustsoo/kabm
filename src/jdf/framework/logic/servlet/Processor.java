package jdf.framework.logic.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.ConnectionFactory;
import jdf.framework.core.data.cci.Interaction;
import jdf.framework.core.log.Logger;
import jdf.framework.logic.adapter.BaseConnectionFactory;
import jdf.framework.logic.transform.Transformer;
import jdf.framework.logic.transform.TransformerFactory;


/**
 * Anylogic을 실행시키는 서블릿 페이지
 * 
* @author Eun Jeong-Ho
 */
public class Processor extends HttpServlet implements AnyLogicControl
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private final static String LOG_ID = "<al-svlt:Processor> ";
	
	
    private static final String RESPONSE_TYPE_SYMBOL= "resType";
    private static final String TR_CODE_SYMBOL= "trCode";

    private static final byte[] XML_DECLARE= "<?xml version=\"1.0\" encoding=\"euc-kr\"?>".getBytes();

    public void init(ServletConfig config) throws ServletException
    {
        /*
        DeploymentManager dm= DeploymentManager.getInstance();

        dm.init();
        dm.load();
        */

        super.init(config);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException
    {
        try
        {
            // 필수 attribute
            String resType= req.getParameter(RESPONSE_TYPE_SYMBOL);
            String trCode= req.getParameter(TR_CODE_SYMBOL);

            DataSet input= new DataSet();

            setDataSet(req, input);

            long before= System.currentTimeMillis();

            ConnectionFactory factory= new BaseConnectionFactory();
            Connection connection= null;
            Interaction inter= null;
            DataSet output= null;
            try
            {

                connection= factory.getConnection();
                inter= connection.createInteraction();
                output= inter.execute(trCode, input);
            }
            catch (Exception e)
            {
                throw e;
            }
            finally
            {
                if (connection != null)
                    connection.close();
            }

            long after= System.currentTimeMillis();
            long t= after - before;
            Logger.debug.println("[" + trCode + "] 처리시간: " + t);

            TransformerFactory tFactory= TransformerFactory.getInstance();

            if (resType == null || resType.length() <= 0)
                resType= "comma";
            Transformer transformer= tFactory.getTransformer(resType);

            java.io.OutputStream osteam= res.getOutputStream();

            if ("xml".equals(resType))
            {
                res.setContentType("text/xml; charset=euc-kr");

                osteam.write(XML_DECLARE);
                
                osteam.write("<dataset>".getBytes());

                osteam.write(output.getIOSchema().toString().getBytes());
                
                transformer.transform(output, osteam);
                
                osteam.write("</dataset>".getBytes());
            }
            else
	            transformer.transform(output, osteam);
            
            	
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ServletException(e.getMessage());
        }
    }

    private void setDataSet(HttpServletRequest req, DataSet input) throws ServletException
    {
        Enumeration fieldNames= req.getParameterNames();

        while (fieldNames.hasMoreElements())
        {
            String paramName= (String) fieldNames.nextElement();

            if (paramName.equals(RESPONSE_TYPE_SYMBOL) || paramName.equals(TR_CODE_SYMBOL))
                continue;

            try
            {
                for (int i= 0; i < req.getParameterValues(paramName).length; i++)
                {
                    Object paramValue= req.getParameterValues(paramName)[i];

                    input.put(paramName, paramValue, i);
                    //Logger.debug.println("anylogic.servlet.Processor set "+paramName+"["+i+"]="+paramValue);
                }

            }
            catch (Exception ex)
            {
                throw new ServletException(ex);
            }

        }
        
        Logger.debug.println(LOG_ID+"input:"+input.toString());
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        doGet(req, res);
    }

}