package com.kabm.filter.menu;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.DefaultConnectionFactory;
import jdf.framework.core.data.cci.Interaction;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.view.layout.entity.Layout;
import jdf.framework.view.menu.dao.DbmsMenuDao;
import jdf.framework.view.menu.entity.MenuItem;
import jdf.framework.view.menu.entity.WebSiteMenu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultiTenantMenuDao extends DbmsMenuDao
{	
	private final static String LOG_ID="<t:MultiTenantMenuDao> ";

    private static String DEFAULT_SCHEMA= "/common/MenuMgr";

    private static String CHILD_SCHEMA= "/common/MenuMgr";
	
    private List<MenuItem> contentDirList= new ArrayList<MenuItem>();    
        
    
	private List<MenuItem> getMenuList(MenuItem parent, String parentMenuId)
	{
		List<MenuItem> menuList= new ArrayList<MenuItem>();
		String trcode= DEFAULT_SCHEMA;
		DataSet input= new DataSet();		
		
		input.put("cmd", "GET_LIST_ALL");
		
		
		
		
		//input.put("menu_id", "top");
		DataSet menuData = execute(trcode, input);
		
        for (int i= 0; i < menuData.getCount("menu_nm"); i++)
        {
        	String name= menuData.getText("menu_nm", i);
            String url= menuData.getText("url", i);
            String is_view = menuData.getText("view_yn", i);
            String tenant_id = menuData.getText("cmpny_id", i).toLowerCase();
            int depth= menuData.getInt("level", i);
            String[] authLevels= SmartStringArray.split(",", menuData.getText("auth_type", i));
            String menu_icon = menuData.getText("menu_icon", i);
            
            MenuItem menu= new MenuItem(name, url, depth, authLevels);
            
            String idx = menuData.getText("menu_id", i);                                
            
            setMenuMap(tenant_id+url, menu);

            menu.setId(idx);
            menu.setParentMenuId(menuData.getText("p_menu_id", i));
            
            
            menu.setIndexKey(menuData.getText("menu_path", i).replaceAll(">", "_"));
            
            menu.setMenuAlias(menuData.getText("menu_alias", i));
            menu.setPopupYN(menuData.getText("popup_yn", i));
            
            menu.setTenantId(tenant_id);
            menu.setParam("menu_icon", menu_icon);
            
            
            if("N".equals(is_view))
            	menu.setVisiable(false);
            else
            	menuList.add(menu);
            

            String layoutName= menuData.getText("layout", i);

            //layout 틀을 정의한다.
            Layout layout= layoutMgr.getLayout(layoutName);

            // 정의된 layout이 없으면
            // 기본 layout을 정의
            if (layout == null)
                layout= layoutMgr.getDefaultLayout();

            menu.setLayout(layout);
           
            Logger.debug.println(LOG_ID+"menu=" + menu.toString());
            menuMapForIndex.put(menu.getIndexKey(), menu);            
            setExtendsMenuParam(menu, menuData.getText("params", i), tenant_id);
            
        }
        
		return menuList;
	}
	
	
	private void setChildMenuList(List<MenuItem> menuList)
	{
		List<MenuItem> childList = null;
		for(int idx=0; idx<menuList.size(); idx++)
		{
			MenuItem parentItem = menuList.get(idx);
			childList = new ArrayList<MenuItem>();
			for(int kdx=0; kdx<menuList.size(); kdx++)
			{
				MenuItem childItem = menuList.get(kdx);
				
				if(parentItem.getId().equals(childItem.getParentMenuId()))
				{
					childItem.setParent(parentItem);
					childList.add(childItem);
				}
				
			}
			
			parentItem.setChildMenuItems(childList);
			
			
		}
	}
	
	
	public WebSiteMenu getWebSiteMenu()
    {
        List<MenuItem> menuList= null;

        String siteName= null;
        String domain= null;

        MenuItem top= new MenuItem("", "", -1, new String[] {});

        menuList= this.getMenuList(top, "");
        setChildMenuList(menuList);	
        
        Iterator<String> ite = menuMapForIndex.keySet().iterator();
		while(ite.hasNext())
		{							
			String _key = ite.next();			
			MenuItem item = (MenuItem)menuMapForIndex.get(_key);			
		}
        
        

        WebSiteMenu site_menu= new WebSiteMenu(siteName, menuList, menuMap, menuMapForIndex, contentDirList);
        site_menu.setParam("name", siteName);
        site_menu.setParam("domain", domain);

        return site_menu;
    }
	
	
	
	
	private DataSet execute(String ioschema, DataSet input)
    {
        Connection conn= null;
        DataSet output= new DataSet();
        try
        {
            conn= DefaultConnectionFactory.getConnection();
            Interaction interact= conn.createInteraction();

            output= interact.execute(ioschema, input);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close();
                }
                catch (Exception ee)
                {}
            }
        }

        return output;
    }
	
	
	/**
     * layout=blog iframe=test.jsp?q=23 another-url=erwer,
     * ?a=1&b=2&c=3(a:1 b:2 c:3)
     * 
     * 
     * @param menu
     * @param ext
     */
    private void setExtendsMenuParam(MenuItem menu, String ext, String tenant_id)
    {
        if (ext == null || ext.length() == 0)
            return;

        String[] keyVals= SmartStringArray.split(" ", ext);

        for (int i= 0; i < keyVals.length; i++)
        {
            String[] tmp= SmartStringArray.split(":", keyVals[i]);

            String key= tmp[0];
            String val= tmp[1];

            //Logger.debug.println(LOG_ID+"setExtendsMenuParam[" + key + "=" + val + "]");
            menu.setParam(key, val);

            // another-url 인 경우
            if ("another-url".equals(key))
            {
                tmp= SmartStringArray.split(",", val);

                for (int j= 0; j < tmp.length; j++)
                {
                    String another_url= tmp[j];
                    setMenuMap(tenant_id+another_url, menu);
                }
            }

            // layout인 경우
            if ("layout".equals(key))
            {
                Layout layout= layoutMgr.getLayout(val);

                // 정의된 layout이 없으면
                // 기본 layout을 정의
                if (layout == null)
                    layout= layoutMgr.getDefaultLayout();

                menu.setLayout(layout);

            }

        }
    }


    public static void main(String[] args)
    {
    	new MultiTenantMenuDao().getWebSiteMenu();
    	
    	
    	
    }
}