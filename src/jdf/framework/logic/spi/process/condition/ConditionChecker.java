/*
 * Created on 2003-11-05
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jdf.framework.logic.spi.process.condition;

import jdf.framework.core.data.DataSet;

/**
 * 
 * @author
 * @version 1.0
 * @since 2003-11-05 오후 2:58:19
 * 
 */
public interface ConditionChecker
{

    /**
     * equalas ==
     * 
     */
    public static final int EQ= 0;

    /**
     * not equals !=
     * 
     */
    public static final int NEQ= 1;

    /**
     * less than <
     * 
     */
    public static final int LT= 2;


    /**
     * greater than >
     * 
     */
    public static final int GT= 3;


    /**
     * less than or equal <=
     * 
     */
    public static final int LTE= 4;

    /**
     * greater than or equal to >=
     * 
     */
    public static final int GTE= 5;
    


    
    public boolean check(DataSet input, DataSet output);


}
