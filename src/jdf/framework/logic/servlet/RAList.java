package jdf.framework.logic.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdf.framework.logic.spi.management.DeploymentManager;
import jdf.framework.logic.spi.management.RADeployDescriptor;




/**
 * 
 * Resource Adapter list 를 출력한다.
 * 
 * @author
 */
public class RAList extends HttpServlet implements AnyLogicControl
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

    private static final String XML_DECLARE = "<?xml version=\"1.0\" encoding=\"euc-kr\"?>";

    private DeploymentManager dm;

    public void init(ServletConfig config) throws ServletException
    {
        dm = DeploymentManager.getInstance();

        if (!dm.isLoad())
            dm.load();

        super.init(config);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException,
            IOException
    {
        String mode = req.getParameter("type");
        if ("xml".equals(mode))
            this.processXml(req, res);
        else
            this.processGet(req, res);

    }

    /**
     * anyBUILDER나 일반적인 text 형태인 경우
     * 
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void processGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        res.setHeader("cache-control", "no-cache");
        res.setHeader("expires", "0");
        res.setHeader("progma", "no-cache");

        PrintWriter out = new PrintWriter(new OutputStreamWriter(res.getOutputStream(), "KSC5601"));
        try
        {
            RADeployDescriptor[] ras = dm.getRADescriptors();

            for (int i = 0; i < ras.length; i++)
            {
                out.println(ras[i].getName());
            }
        } catch (Exception e)
        {
            //Logger.err.println(LOG_ID+"error",e);
            throw new ServletException(e.getMessage());
        }

        out.flush();
        out.close();
    }

    /**
     * XML 형태로 return
     * 
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    private void processXml(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        res.setContentType("text/xml; charset=euc-kr");
        PrintWriter out = new PrintWriter(new OutputStreamWriter(res.getOutputStream(), "KSC5601"));

        out.println(XML_DECLARE);
        out.println("<anylogic-connector>");
        try
        {
            RADeployDescriptor[] ras = dm.getRADescriptors();

            for (int i = 0; i < ras.length; i++)
            {
                out.println("<adapter>");

                out.print("<name>");
                out.print(ras[i].getName());
                out.print("</name>");

                out.print("<adapter-class>");
                out.print(ras[i].getResourceAdapterClassName());
                out.print("</adapter-class>");

                out.print("<processor-class>");
                out.print(ras[i].getProcessorClassName());
                out.print("</processor-class>");

                out.println("</adapter>");
            }
        } catch (Exception e)
        {
            //Logger.err.println(LOG_ID+"error",e);
            throw new ServletException(e.getMessage());
        }
        out.println("</anylogic-connector>");

        out.flush();
        out.close();

    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException,
            IOException
    {
        doGet(req, res);
    }
}