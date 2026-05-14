package jdf.framework.logic.util;

import jdf.framework.core.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * URI에 따른 저장소를 접근할 수 있는 DescAccessible 구현 클래스를 반환한다. example) File:
 * file:///c:/defaultDir DBMS: dbms:///defaultPool/defaultTableName
 * 
 * @author Eun Jeong-Ho
 */
public class DescAccessFactory
{
    private static Map map = new HashMap();

    /**
     * 해당 URI에 따른 저장소를 반환한다.
     * 
     * @param uri
     *            URI
     * @return DescAccessible 저장소를 접근할수 있는 구현 클래스
     */
    public static synchronized DescAccessible getInstance(String uri)
    {
        return getInstance(uri, false);
    }

    /**
     * 해당 URI에 따른 저장소를 반환한다.
     * 
     * @param uri
     *            URI
     * @param isHistory
     *            저장시 히스토리를 남길지 여부(현재, DBMS만 적용)
     * @return DescAccessible 저장소를 접근할수 있는 구현 클래스
     */
    public static synchronized DescAccessible getInstance(String uri, boolean isHistory)
    {
        DescAccessible accessible = (DescAccessible) map.get(uri);
        if (accessible != null)
            return accessible;

        if (uri == null || uri.length() == 0)
        {
            String dir = Configuration.getConfigPath() + "/history";
            //accessible = new FileDescAccessImpl(dir);
            accessible = new MultiFileDescAccessImpl(dir);
        } else
        {
            int p = uri.indexOf(":");
            
            String prefix = "file";
            if(p<0) {
                if( uri.indexOf(".") >-1 )
                    uri = "file:////"+Configuration.getConfigPath() + uri.substring(1);
                else
                    prefix="";
            }
            else
                prefix = uri.substring(0, p).toLowerCase();
            // 윈도우인 경우 C가 return

            if (prefix.equals("file"))
            {
                p = uri.indexOf("///");
                String dir = uri.substring(p + 3);

                //accessible = new FileDescAccessImpl(dir);
                accessible = new MultiFileDescAccessImpl(dir);
            } else if (prefix.equals("dbms"))
            {
                p = uri.indexOf("///");
                String tmp = uri.substring(p + 3);
                p = tmp.indexOf("/");
                String poolname = tmp.substring(0, p);
                String tablename = tmp.substring(p + 1);

                accessible = new DBDescAccessImpl(poolname, tablename, isHistory);
            }
            else {
                accessible = new MultiFileDescAccessImpl(uri);
            }
        }
        map.put(uri, accessible);

        return accessible;
    }
}