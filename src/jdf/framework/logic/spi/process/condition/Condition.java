/*
 * Created on 2003-11-06
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jdf.framework.logic.spi.process.condition;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.core.util.StringFormater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * $input.x='test' or 't'='test' and 'x'='x'
 * 
 * 와 같이 조건 문자열 값이 들어오면 parsing하여 
 * 비교연산 할수 있는 내부정보를 만들고 가지고 있는 Entity
 * 
 * 
 * 1) 괄호()를 먼저 분리 재귀적으로 Condtion 객체 생성
 * 2) or 를 구분자로 하여 분리한다음 Condition 객체 생성
 * 3) and 를 구분자로 하여 ConditionEntity 객체 생성
 * 4) CondtionChecker
 * 
 * 
 * @author
 * @version 1.0
 * @since 2003-11-06 오후 4:09:46
 * 
 */
public class Condition implements ConditionChecker
{

    private final static int OR= 0;
    private final static int AND= 1;

    private int mode= OR;

    private ConditionChecker[] checkerArray;
    private List checkerList= new ArrayList();
    private String condition;

    public Condition(String condition) throws IllegalArgumentException
    {
        //System.out.println("INPUT : " + condition);

        analysize(condition);

        this.condition= condition;
    }

    private void analysize(String val) throws IllegalArgumentException
    {
        Map conditionMap= new HashMap();

        //  괄호 우선 처리
        int idx= 0;
        while (val.indexOf("(") >= 0)
        {

            String r= getBlockCondition(val);
            String key= "##" + idx;

            val= StringFormater.replaceStr(val, "(" + r + ")", key);

            Condition condition= new Condition(r);

            conditionMap.put(key, condition);

            //System.out.println(idx + " : " + r + " : " + val);

            idx++;
        }

        String[] orArray= SmartStringArray.split("or", val);

        if (orArray.length > 1)
        {
            mode= OR;
            //System.out.println("OR 모드");
        }

        else
        {
            mode= AND;
            //System.out.println("AND 모드");
        }

        for (int i= 0; i < orArray.length; i++)
        {
            String conditionText= orArray[i].trim();

            Object condition= conditionMap.get(conditionText);

            if (condition == null)
            {
                // or모드인 경우
                if (mode == OR)
                {

                    if (conditionText.indexOf("and") < 0)
                        condition= new ConditionEntity(conditionText);
                    else
                        condition= new Condition(conditionText);

                    checkerList.add(condition);
                }

                else
                {
                    String[] andArray= SmartStringArray.split("and", conditionText);

                    for (int j= 0; j < andArray.length; j++)
                    {
                        conditionText= andArray[j].trim();

                        condition= conditionMap.get(conditionText);

                        if (condition == null)
                        {
                            condition= new ConditionEntity(conditionText);
                        }

                        checkerList.add(condition);

                    }

                }
            }
            else
                checkerList.add(condition);

        }

        /*
        for (int i= 0; i < checkerList.size(); i++)
        {
            System.out.println(checkerList.size() + "[" + i + "] -->" + checkerList.get(i));
        }
        */

        checkerArray= (ConditionChecker[]) checkerList.toArray(new ConditionChecker[] {});

        //System.out.println("+++" + checkerArray.length);

    }

    public String toString()
    {
        return condition;
    }

    public static void main(String[] args) throws Exception
    {
        //String val2= "$input.x='test' or ((3< 43 or 43>=34) or 'test'='test') or 't'='test'";
        String val2= "($input.x='test' and ('t'='test' and 'x'='x')) and 'x'='x'";
        //String val2= "$input.x='test' or 't'='test' and 'x'='x'";

        System.out.println("=" + val2);
        Condition c= new Condition(val2);

        DataSet input= new DataSet();
        DataSet output= new DataSet();
        input.put("x", "test");
        
        
        Thread.sleep(2000);        

        long s = System.currentTimeMillis();
        
        
        if (c.check(input, output))
            System.out.println("true");
        else
            System.out.println("false");
            
        
        
        long e = System.currentTimeMillis();
        
        System.out.println(e-s);

        //if ('test'== 't' and 't' == 't' or 'test'!= 'te')

    }

    public boolean check(DataSet input, DataSet output)
    {
        if (this.mode == OR)
        {
            for (int i= 0; i < checkerArray.length; i++)
            {
                ConditionChecker checker= checkerArray[i];

                if (checker.check(input, output))
                    return true;
            }

            return false;
        }
        else if (this.mode == AND)
        {
            for (int i= 0; i < checkerArray.length; i++)
            {
                ConditionChecker checker= checkerArray[i];

                if (!checker.check(input, output))
                    return false;

            }
            return true;

        }
        
        return false;
    }

    private static String getBlockCondition(String val)
    {
        if (val.indexOf("(") < 0)
            return null;

        // ( 가 있는 경우 체크
        char[] x= val.toCharArray();

        int stx= 0;
        int sp= 0;
        int ep= 0;
        boolean isFirstFind= true;

        for (int i= 0; i < x.length; i++)
        {
            if (x[i] == '(')
            {
                stx++;

                if (isFirstFind)
                {
                    sp= i;
                    isFirstFind= false;
                }
            }

            else if (x[i] == ')')
            {
                stx--;
                ep= i;

                if (stx == 0)
                    break;
            }
        }

        //System.out.println("sp=" + sp);
        //System.out.println("ep=" + ep);

        String result= val.substring(sp + 1, ep);
        return result;
    }

}