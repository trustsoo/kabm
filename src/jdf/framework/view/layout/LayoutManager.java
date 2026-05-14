package jdf.framework.view.layout;

import jdf.framework.core.log.Logger;
import jdf.framework.view.layout.dao.LayoutDao;
import jdf.framework.view.layout.dao.XmlFileDao;
import jdf.framework.view.layout.entity.Layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * Templete들을 관리하는 Manager
 * 
 * 
 * @author
 * @version 1.0
 * @since 2003-3-05 오전 9:40:58
 *
 */
public class LayoutManager
{
    private final static String LOG_ID="<t:LayoutManager> ";

    // layout 저장소
    private Map<Object, Object> layoutStore= new HashMap<Object, Object>();

    private List<?> layoutStoreList= new ArrayList<>();

    // LayoutManager 객체
    private static LayoutManager instance;

    // 기본 layout
    private Layout defaultLayout= null;
    
    // 기본 DAO로 xml file 기반의 DAO를 사용한다.
    private LayoutDao layoutDao;
    
    private static String layoutDaoClassNm = "jdf.framework.view.layout.dao.XmlFileDao";
    

    private LayoutManager()
    {
        load();
    }
    
    
    
    private static LayoutDao getLayoutDao() {
        
        try {
            return (LayoutDao) Class.forName(layoutDaoClassNm).newInstance();
            
        } catch(Exception e) {
            Logger.warn.println(LOG_ID+"getLayoutDao error. "+e.toString());
            return new XmlFileDao();
        }
    }
    
    public static void setLayoutDaoClassName(String nm)
    {
        layoutDaoClassNm = nm;
    }
    

    public synchronized static LayoutManager getInstance()
    {
        if (instance == null)
            instance= new LayoutManager();

        return instance;
    }

    public Layout getLayout(String name)
    {
        return (Layout) this.layoutStore.get(name);
    }

    public Layout[] getLayoutArray()
    {
        //return (Layout[]) this.layoutStore.keySet().toArray(new Layout[] {});
        try
        {
            return (Layout[]) this.layoutStoreList.toArray(new Layout[0]);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }

    public Layout getDefaultLayout()
    {
        return defaultLayout;
    }

    public void load()
    {
        this.layoutDao = getLayoutDao();

        try
        {
            layoutDao.load(layoutStore, layoutStoreList);
            this.defaultLayout = layoutDao.getDefaultLayout();
        }
        catch (Exception e)
        {
            Logger.err.println(LOG_ID+"load layout config file fail.",e);
        }

    }

}