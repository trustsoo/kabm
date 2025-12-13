/*
 * Created on 2004. 11. 15.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jdf.framework.view.menu.dao;

import java.io.ByteArrayInputStream;
import java.util.List;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.DefaultConnectionFactory;
import jdf.framework.core.data.cci.Interaction;
import jdf.framework.core.log.Logger;
import jdf.framework.core.xml.DocBuilder;
import jdf.framework.core.xml.XMLReferer;
import jdf.framework.view.menu.entity.MenuItem;
import jdf.framework.view.menu.entity.WebSiteMenu;

import org.w3c.dom.Document;


/**
 * @author
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class XmlDbMenuDao extends XmlMenuDao
{
    private final static String LOG_ID="<t:XmlDbMenuDao> ";

    public XmlDbMenuDao()
    {
        super();
    }

    public WebSiteMenu getWebSiteMenu(String dir, String filename)
    {

        List menuList = null;

        String siteName = null;
        String domain = null;

        try
        {
            Connection conn = null;
            DataSet output = new DataSet();
            
            try {
                conn = DefaultConnectionFactory.getConnection();
                
                Interaction interact = conn.createInteraction();
                
                output = interact.execute("/anytemplet/getSitemap", new DataSet());
                
            }catch (Exception e) {
                
            }
            finally {
                if(conn!=null)
                    conn.close();
            }
            
            String menuXmlStr = output.getText("sitemap");

            ByteArrayInputStream bis = new ByteArrayInputStream(menuXmlStr.getBytes());
            Document doc = DocBuilder.getDocument(bis);

            XMLReferer xmlDoc = new XMLReferer(doc);

            try
            {
                if (siteName == null)
                    siteName = xmlDoc.lookup("/site-menu").getString("name");

                if (domain == null)
                    domain = xmlDoc.lookup("/site-menu").getString("domain");

                xmlDoc.lookup("/site-menu/start-page");

                String url = xmlDoc.find("url").getText();
                MenuItem menu = new MenuItem("startPage", url);

                menu.setOriginPage(xmlDoc.find("publish/source-url").getText());
                menu.setGenTimeGap(getInt(xmlDoc.find("publish/time-gap").getText()));
                menu.setPageSize(getInt(xmlDoc.find("publish/min-page-size").getText()));

                if (menu.getPublishTimeGap() > 0)
                    publishMenuList.add(menu);
            } catch (Exception eee)
            {
            }

            xmlDoc.lookup("/site-menu/topmenu");

            // 각 자식 메뉴 load
            menuList = getMenuList(null, xmlDoc, null);

        } catch (Exception e)
        {
            e.printStackTrace();
            Logger.err.println(LOG_ID+"load menu DBMS fail. " + e.toString());
        }

        Logger.info.println(LOG_ID+"load menu info sucess.");

        WebSiteMenu site_menu = new WebSiteMenu(siteName, menuList, menuMap, menuMapForIndex, contentDirList);
        site_menu.setParam("name", siteName);
        site_menu.setParam("domain", domain);

        return site_menu;
    }

}