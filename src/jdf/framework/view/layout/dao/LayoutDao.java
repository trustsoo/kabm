/*
 * Created on 2004. 11. 23.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jdf.framework.view.layout.dao;

import jdf.framework.view.layout.entity.Layout;

import java.util.List;
import java.util.Map;

/**
 * @author
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public interface LayoutDao
{

    public void load(Map layoutStore, List layoutStoreList) throws Exception;

    public Layout getDefaultLayout();

}