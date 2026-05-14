package jdf.framework.core.util;

import java.util.Hashtable;
import java.util.Map;

/**
 * <p>
 * 객체 참조을 Name(key)으로 구별하여 저장한다.
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class ObjectBinder
{

    private static Map _Ref = new Hashtable();

    /**
     * 객체 검증
     */
    private boolean containKey(Object key)
    {
        if (_Ref.get(key) != null)
            return true;
        else
            return false;
    }

    public Object bind(String key, Object data) throws ObjectBinderException
    {
        if (containKey(key))
            throw new ObjectBinderException("key already exist.");

        return _Ref.put(key, data);
    }

    /**
     * 객체를 가져온다.
     * 
     */
    public Object lookup(Object key)
    {
        Object unit = _Ref.get(key);
        return unit;
    }

    /**
     * 객체를 수정한다.
     * 
     */
    public Object rebind(Object key, Object data) throws ObjectBinderException
    {
        if (!containKey(data))
            throw new ObjectBinderException("key do not exist.");

        Object unit = _Ref.get(key);

        if (unit == null)
        {
            return _Ref.put(key, data);
        } else
        {
            synchronized (_Ref)
            {
                _Ref.remove(key);
                return _Ref.put(key, data);
            }
        }
    }

    /**
     * 객체를 제거한다.
     * 
     */
    public Object unbind(Object key)
    {
        return _Ref.remove(key);
    }

    /**
     * 객체를 모두 제거한다.
     * 
     */
    public void unbindAll()
    {
        _Ref.clear();
    }

    public Object[] list(String name)
    {
        PartialSearcher searcher = new PartialSearcher(_Ref);

        Object[] keys = searcher.match((String) name);

        return keys;
    }

}