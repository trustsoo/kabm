/*
 * Created on 2004-03-03
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jdf.framework.view.menu.dao;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.DefaultConnectionFactory;
import jdf.framework.core.data.cci.Interaction;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.core.util.Utility;
import jdf.framework.view.layout.entity.Layout;
import jdf.framework.view.menu.entity.MenuItem;
import jdf.framework.view.menu.entity.WebSiteMenu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 
 * DbmsMenuDao
 * 
 * @author 
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DbmsMenuDao extends MenuDaoBase implements MenuDao
{
    private final static String LOG_ID="<t:DbmsMenuDao> ";
    
    

    private static String DEFAULT_SCHEMA= "/common/MenuMgr";

    private static String CHILD_SCHEMA= "/common/MenuMgr";


    private List<MenuItem> contentDirList= new ArrayList<MenuItem>();

    public DbmsMenuDao()
    {
        super();
    }

    /**
     * 
     * @see seemile.framework.view.menu.dao.MenuDao#setSourceInfo(String)
     */
    public void setSourceInfo(String source)
    {
        return;
    }

    public List<MenuItem> getPublishMenuList()
    {
        return publishMenuList;
    }

    private List<MenuItem> getMenuList(MenuItem parent, String parentMenuId)
    {
    	//MenuItem top= new MenuItem("", "", -1, new String[] {});
        List<MenuItem> menuList= new ArrayList<MenuItem>();

        String trcode= DEFAULT_SCHEMA;
        DataSet input= new DataSet();
        if (parentMenuId != null && parentMenuId.length() > 0)
        {
            input.put("menu_id", parentMenuId);
            input.put("depth", String.valueOf(parent.getDepth()));

            trcode= CHILD_SCHEMA;
        }
        else
        {
            input.put("depth", "0");
            input.put("menu_id", "top");
        }
        input.put("cmd", "GET_LIST");
        DataSet output= execute(trcode, input);
        

        for (int i= 0; i < output.getCount("menu_nm"); i++)
        {
            try
            {
                String name= output.getText("menu_nm", i);
                String url= output.getText("url", i);
                String is_view = output.getText("view_yn", i);
                String log_yn = output.getText("log_yn", i);
                int depth= output.getInt("level", i);
                //Logger.debug.println(name+":"+depth);
                String[] authLevels= SmartStringArray.split(",", output.getText("auth_type", i));
                /*for(int idx=0; idx<authLevels.length; idx++)
                {
                	if(authLevels[idx] != null && authLevels[idx].equals("S"))
                	{
                		url = "https://euro.innopot.com"+url;
                		break;
                	}
                }*/
                
                
                
                MenuItem menu= new MenuItem(name, url, depth, authLevels);
                
                String idx = output.getText("menu_id", i);                                
                
                setMenuMap(url, menu);

                menu.setId(idx);
                
                
                
                if(parent != null && parent.getIndexKey() != null)
                {                	
                	
                	menu.setIndexKey(parent.getIndexKey()+"_"+idx);
                } else
                {
                	menu.setIndexKey(idx);
                }
                
                
                
                menu.setMenuAlias(output.getText("menu_alias", i));
                menu.setPopupYN(output.getText("popup_yn", i));
                
                
                if("N".equals(is_view))
                	menu.setVisiable(false);
                else
                	menuList.add(menu);
                

                String layoutName= output.getText("layout", i);

                //layout 틀을 정의한다.
                Layout layout= layoutMgr.getLayout(layoutName);

                // 정의된 layout이 없으면
                // 기본 layout을 정의
                if (layout == null)
                    layout= layoutMgr.getDefaultLayout();

                menu.setLayout(layout);

                menu.setParent(parent);
                
                menu.setLogYn(log_yn);
               
                Logger.debug.println(LOG_ID+"menu=" + menu.toString());
                
                
                //List<MenuItem> childList = getMenuList(menu, menu.getIndexKey());
                List<MenuItem> childList = getMenuList(menu, menu.getId());
                
                menu.setChildMenuItems(childList);
                //menuMapForIndex.put(idx, menu);
                menuMapForIndex.put(menu.getIndexKey(), menu);
                setExtendsMenuParam(menu, output.getText("params", i));
                
                

            }
            catch (Exception e)
            {
            	Logger.err.println(LOG_ID+Utility.getStackTrace(e));
                e.printStackTrace();
            }

        }
        
        
        
        return menuList;

    }

    public WebSiteMenu getWebSiteMenu()
    {
        List<MenuItem> menuList= null;

        String siteName= null;
        String domain= null;


        //Logger.debug.println(LOG_ID+"getWebSiteMenu 1 "+output);

        MenuItem top= new MenuItem("", "", -1, new String[] {});

        menuList= this.getMenuList(top, "");

       
        
        Iterator<String> ite = menuMapForIndex.keySet().iterator();
		while(ite.hasNext())
		{				
			
			String _key = ite.next();			
			MenuItem item = (MenuItem)menuMapForIndex.get(_key);
			System.out.println(_key+">>"+item);
			
			
			
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
    private void setExtendsMenuParam(MenuItem menu, String ext)
    {
        if (ext == null || ext.length() == 0)
            return;

        String[] keyVals= SmartStringArray.split(" ", ext);

        for (int i= 0; i < keyVals.length; i++)
        {
            String[] tmp= SmartStringArray.split(":", keyVals[i]);

            String key= tmp[0];
            String val= tmp[1];

            Logger.debug.println(LOG_ID+"setExtendsMenuParam[" + key + "=" + val + "]");
            menu.setParam(key, val);

            // another-url 인 경우
            if ("another-url".equals(key))
            {
                tmp= SmartStringArray.split(",", val);

                for (int j= 0; j < tmp.length; j++)
                {
                    String another_url= tmp[j];
                    setMenuMap(another_url, menu);
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
    
    public static void main(String[] args) throws Exception
    {
    	new DbmsMenuDao().getWebSiteMenu(); 
    }

}
