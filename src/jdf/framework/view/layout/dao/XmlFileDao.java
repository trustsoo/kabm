/*
 * Created on 2004. 11. 23.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jdf.framework.view.layout.dao;

import jdf.framework.core.Configuration;
import jdf.framework.core.log.Logger;
import jdf.framework.core.xml.XMLReferer;
import jdf.framework.view.layout.entity.Layout;

import java.util.List;
import java.util.Map;


/**
 * @author
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class XmlFileDao implements LayoutDao
{
    private final static String LOG_ID = "<t:XmlFileDao> ";

    private Layout defaultLayout;

    public XmlFileDao()
    {

    }

    /*
     * (non-Javadoc)
     * 
     * @see anytemplet.layout.dao.LayoutDao#getDefaultLayout()
     */
    public Layout getDefaultLayout()
    {
        return this.defaultLayout;
    }

    /*
     * (non-Javadoc)
     * 
     * @see anytemplet.layout.dao.LayoutDao#load(java.util.Map, java.util.List)
     */
    public void load(Map layoutStore, List layoutStoreList) throws Exception
    {
        layoutStore.clear();
        layoutStoreList.clear();

        String dir = Configuration.getConfigPath();
        String filename = "layout.xml";

        XMLReferer doc = new XMLReferer(dir, filename);

        doc.lookup("/layout-profile/portal/layouts");

        String defaultLayoutName = doc.getString("default");

        doc.lookup("layout");

        while (doc.next())
        {
            String layoutName = null;

            try
            {
                layoutName = doc.getString("name");

                Layout layout = new Layout(layoutName);

                // template 화일 set
                layout.setTemplate(doc.find("template-page").getText());

                layoutStore.put(layoutName, layout);
                layoutStoreList.add(layout);

                Logger.info.println(LOG_ID+"regist [" + layoutName + "] " + layout.getTemplate());

                // templet을 적용하지 못하는 경우 include를 하여야 하는데
                // 그때 사용한다.
                layout.setHeaderFileName(doc.find("header-filename").getText());
                layout.setFooterFileName(doc.find("footer-filename").getText());

                String header = doc.find("header/exists").getText();
                String footer = doc.find("footer/exists").getText();
                String columns = doc.find("columns/number").getText();

                if ("false".equals(header))
                    layout.setHeaderExist(false);
                else
                    layout.setHeaderExist(true);

                if ("false".equals(footer))
                    layout.setFooterExist(false);
                else
                    layout.setFooterExist(true);

                if (columns != null && columns.length() > 0)
                    layout.setColumns(Integer.parseInt(columns));

            } catch (Exception ee)
            {
                Logger.warn.println(LOG_ID + "read layout err. " + layoutName, ee);
                //Logger.warn.println(ee);
            }

            this.defaultLayout = (Layout) layoutStore.get(defaultLayoutName);
        }

    }
}