package jdf.framework.view.menu.dao;

import jdf.framework.core.log.Logger;
import jdf.framework.view.layout.LayoutManager;
import jdf.framework.view.menu.entity.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MenuDaoBase
{
    private final static String LOG_ID = "<t:MenuDao> ";

    protected Map<String, MenuItem> menuMap = new HashMap<String, MenuItem>();

    protected Map<String, MenuItem> menuMapForIndex = new HashMap<String, MenuItem>();

    protected List<MenuItem> publishMenuList = new ArrayList<MenuItem>();

    // layout manager
    protected LayoutManager layoutMgr = LayoutManager.getInstance();

    protected String contextPath = "";

    protected MenuDaoBase()
    {
    }
    
    /**
     * 
     * @see MenuDao#setContextPath(String)
     */
    public void setContextPath(String contextPath)
    {
        if(contextPath!=null && contextPath.length()>1)
            this.contextPath=contextPath;
    }

    /**
     * url이 key 가 되므로 만약 같은 url이 있으면 url 뒤에 실제 index key 정보를 붙여 메뉴를 표시할 수 있게 한다.
     * 
     */
    protected void setMenuMap(String url, MenuItem menu)
    {
        if (url == null || menu == null)
        {
            Logger.debug.println(LOG_ID + "setMenuMap NULL");
            return;
        }
        
        MenuItem premenu = (MenuItem) menuMap.get(url);

        if (premenu != null)
        {
            if (premenu.isHoldUrl())
                return;
        }

        // String preUrl = url;

        /*
         * if (premenu != null) { if (url.indexOf("?") > 0) url += "&"; else url +=
         * "?";
         * 
         * url += MENU_KEY + "=" + menu.getIndexKey();
         * 
         * menu.setUrl(url); }
         */

        menuMap.put(url, menu);

        String orgPageUrl = menu.getOriginPage();
        if (orgPageUrl != null && orgPageUrl.length() > 0)
        {
            menuMap.put(orgPageUrl, menu);

            if (menu.getPublishTimeGap() > 0)
            {
                publishMenuList.add(menu);
            }
            Logger.debug.println(LOG_ID + "PUBLISH SET " + menu.getName() + " : " + url);
        }

        menuMapForIndex.put(menu.getIndexKey(), menu);

        Logger.info.print(".", false);

        Logger.debug.println(LOG_ID+"SET " + menu.getName() + " : " + url, false);
    }

    protected static String getValue(String val1, String val2)
    {
        if (val1 == null)
            return val2;

        return val1;
    }

    protected static boolean getBoolean(String val1, boolean val2)
    {
        if (val1 == null)
            return val2;

        return getBoolean(val1);
    }

    protected static int getInt(String val1)
    {
        return getInt(val1, 0);
    }

    protected static int getInt(String val1, int val2)
    {
        try
        {
            if (val1 == null)
                return val2;
            else
                return Integer.parseInt(val1);
        } catch (Exception e)
        {
            return 0;
        }

    }

    protected static boolean getBoolean(String val)
    {
        if ("true".equals(val))
            return true;
        else
            return false;
    }

    protected static String getMenuPathMark(String preIdx, int idx)
    {
        if (preIdx == null)
            preIdx = "";

        if (idx < 10)
            preIdx = preIdx + "0" + idx;
        else
            preIdx = preIdx + idx;

        return preIdx;
    }
}