/*
 * Created on 2003-11-11
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jdf.framework.logic.spi.process.condition;


import jdf.framework.core.data.DataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author
 * @version 1.0
 * @since 2003-11-11 오후 1:22:00
 * 
 */
public abstract class Operator
{
    private List operatorList = new ArrayList();
    private Operator[] operatorArray;
    
    
    
    /**
     * 자식 Operator를 추가한다.
     * 
     * @param oper
     */
    public void addChildOperator(Operator oper)
    {
        operatorArray = null;
        operatorList.add(oper);
    }
    
    /**
     * 자식 Operator Array를 반환한다.
     * 
     * @return
     */
    public Operator[] getChildOperatorArray()
    {
        if(operatorArray==null) {
            
            operatorArray = (Operator[]) operatorList.toArray(new Operator[] {});
        }
        
        return operatorArray;
    }
    
    
    

    abstract public void execute(java.sql.Connection conn, DataSet intput, DataSet output) throws java.sql.SQLException;


}