/*
 * Created on 2003-11-06
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jdf.framework.logic.spi.process.condition;

import jdf.framework.core.data.*;

/**
 * 
 * $input.code <= 234
 * $input.code = 'test'
 * $input.code = $input.jcode
 * 
 * 와 같이 조건 문자열 값이 들어오면 parsing하여 
 * 비교연산 할수 있는 내부정보를 만들고 가지고 있는 Entity
 * 
 * 비교 연산자를 기준으로 좌측에 있는 값을 val1
 * 비교 연산자를 기준으로 우측에 있는 값을 val2
 * 그리고 비교연산자를 sign으로 정의한다.
 * 
 * val2는 경우에 따라
 * 문자값
 * 숫자값
 * 변수
 * 이렇게 3가지 경우가 올수 있다.
 * 
 * 
 * @author
 * @version 1.0
 * @since 2003-11-06 오후 4:09:46
 * 
 */
public class ConditionEntity implements ConditionChecker
{

    private final static int INPUT= 0;

    private final static int OUTPUT= 1;

    private final static int TEXT= 0;
    private final static int NUMBER= 1;
    private final static int PARAM= 2;

    private String condition;

    private ValueInfo value1;

    private ValueInfo value2;

    private int csign;
    private String sign_text;

    public ConditionEntity(String condition) throws IllegalArgumentException
    {
        //System.out.println("INPUT = " + condition);
        analysize(condition);

        this.condition= condition;
    }

    private void analysize(String condition) throws IllegalArgumentException
    {
        if (condition == null)
            throw new IllegalArgumentException("condition null");

        int seperatePoint= 0;

        int p1= condition.indexOf("=");
        int p2= condition.indexOf("!");
        int p3= condition.indexOf("<");
        int p4= condition.indexOf(">");

        if (p1 > 0)
            seperatePoint= p1;

        if (p2 > 0)
        {
            if (p2 < seperatePoint || seperatePoint == 0)
                seperatePoint= p2;
        }

        if (p3 > 0)
        {
            if (p3 < seperatePoint || seperatePoint == 0)
                seperatePoint= p3;
        }

        if (p4 > 0)
        {
            if (p4 < seperatePoint || seperatePoint == 0)
                seperatePoint= p4;
        }

        String val1= condition.substring(0, seperatePoint);

        int chkp= seperatePoint + 1;

        p1= condition.indexOf("=", chkp);
        p2= condition.indexOf("!", chkp);
        p3= condition.indexOf("<", chkp);
        p4= condition.indexOf(">", chkp);

        //System.out.println(seperatePoint+"=="+p1+":"+p2+":"+p3+":"+p4+":");

        int chk= p1 + p2 + p3 + p4;
        String sign= null;
        String val2= null;

        if (chk > -4)
        {
            chk= chk + 4;
            //System.out.println(chk);
            sign= condition.substring(seperatePoint, chkp + 1);
            val2= condition.substring(chkp + 1);
        }
        else
        {
            //System.out.println("cc");
            sign= condition.substring(seperatePoint, chkp);
            val2= condition.substring(chkp);
        }

        /*
        System.out.println("val1="+val1+"/");
        System.out.println("sign="+sign+"/");
        System.out.println("val2="+val2+"/");
        */

        analysize(val1, sign, val2);
    }

    private void analysize(String val1, String sign, String val2) throws IllegalArgumentException
    {
        val1= val1.trim();
        sign= sign.trim();
        val2= val2.trim();

        //System.out.println(val1+"::"+sign+"::"+val2);

        value1= getValueInfo(val1);
        value2= getValueInfo(val2);
        sign_text= sign;

        //////////////////////////////////////////
        // sign 파싱단계   

        if (sign.length() == 1)
        {

            if ("=".equals(sign))
                csign= EQ;
            else if ("<".equals(sign))
                csign= LT;
            else if (">".equals(sign))
                csign= GT;
            else
                throw new IllegalArgumentException("올바른 부호(!,=,<,>)가 아닙니다.");
        }
        else
        {

            if ("==".equals(sign))
                csign= EQ;
            else if ("!=".equals(sign))
                csign= NEQ;
            // 에러
            else if ("<=".equals(sign))
                csign= LTE;
            else if ("=<".equals(sign))
                csign= LTE;
            else if (">=".equals(sign))
                csign= GTE;
            else if ("=>".equals(sign))
                csign= GTE;
            else
                throw new IllegalArgumentException("올바른 부호가 아닙니다. " + sign);
        }

        //System.out.println(value1.toString());
        //System.out.println(value2.toString());

        if (csign != EQ && csign != NEQ)
            if (value1.getMode() == TEXT || value2.getMode() == TEXT)
                throw new IllegalArgumentException("문자와는 대소비교를 할수없습니다.");

    }

    private ValueInfo getValueInfo(String val) throws IllegalArgumentException
    {
        ValueInfo value= null;

        // param 형태인 경우
        if (val.indexOf("$") == 0)
        {
            value= new ValueInfo(PARAM);

            int sp= val.indexOf(".");

            String type= val.substring(1, sp);
            String name= val.substring(sp + 1);

            if ("input".equals(type))
                value.setParamValue(INPUT, name);

            else if ("output".equals(type))
                value.setParamValue(OUTPUT, name);
            else
                throw new IllegalArgumentException("잘못된 type이름.  input 또는 output이어야 합니다. " + val);

        }

        else if (val.indexOf("'") == 0)
        {
            value= new ValueInfo(TEXT);

            int lastPoint= val.length() - 1;

            if (val.indexOf("'", 1) == lastPoint)
            {
                value.setTextValue(val.substring(1, lastPoint));
            }
            else
                throw new IllegalArgumentException("값 문자열이 ' 로 종결되지 않았습니다." + val);

        }
        else
        {

            value= new ValueInfo(NUMBER);
            value.setNumverValue(val);
        }

        return value;
    }

    public boolean check(DataSet input, DataSet output)
    {
        Object val1= null;
        Object val2= null;

        if (value1.getMode() == PARAM)
        {
            if (value1.getParamType() == INPUT)
                val1= input.get(value1.getParamName());
            else if (value1.getParamType() == OUTPUT)
                val1= output.get(value1.getParamName());
        }
        else
            val1= value1.toStringValue();

        if (value2.getMode() == PARAM)
        {
            if (value2.getParamType() == INPUT)
                val2= input.get(value2.getParamName());
            else if (value2.getParamType() == OUTPUT)
                val2= output.get(value2.getParamName());
        }
        else
            val2= value2.toStringValue();

        if (csign == EQ)
        {
            if (val1.equals(val2))
                return true;
            return false;
        }
        else if (csign == NEQ)
        {
            if (val1.equals(val2))
                return false;

            return true;
        }

        else
        {
            double valNum1= Double.parseDouble(val1.toString());
            double valNum2= Double.parseDouble(val2.toString());

            switch (csign)
            {

                case LT :
                    return (valNum1 < valNum2);

                case GT :
                    return (valNum1 > valNum2);

                case LTE :
                    return (valNum1 <= valNum2);

                case GTE :
                    return (valNum1 >= valNum2);
            }

        }

        return false;

    }

    public String toString()
    {
        StringBuffer buf= new StringBuffer();

        buf.append(this.value1.toString()).append(this.sign_text).append(this.value2.toString());

        return buf.toString();
    }

    public static void main(String[] args)
    {

        ConditionEntity condition= new ConditionEntity("$input.x= 'test' ");
        //ConditionEntity condition= new ConditionEntity("$input.p1 < 200");

        DataSet input= new DataSet();
        DataSet output= new DataSet();
        input.put("p1", "10");
        input.put("p2", "text");

        output.put("o1", "20");
        output.put("o2", "text");

        boolean chk= condition.check(input, output);

        System.out.println("condition = " + condition.toString());

        if (chk)
            System.out.println("true");
        else
            System.out.println("false");

    }

    class ValueInfo
    {

        private int mode;

        private int type;
        private String name;

        private String str_value;
        private String num_value;
        private double number_value;

        ValueInfo(int mode)
        {
            this.mode= mode;
        }

        void setParamValue(int type, String name)
        {
            this.type= type;
            this.name= name;
        }

        void setTextValue(String value)
        {
            this.str_value= value;
        }

        void setNumverValue(String value) throws NumberFormatException
        {
            this.str_value= value;
            this.number_value= Double.parseDouble(value);
        }

        int getMode()
        {
            return this.mode;
        }

        int getParamType()
        {
            return this.type;
        }

        String getParamName()
        {
            return this.name;
        }

        String getTextValue()
        {
            return this.str_value;
        }

        double getNumberValue()
        {
            return this.number_value;
        }

        String toStringValue()
        {
            return str_value;
        }

        public String toString()
        {
            switch (this.mode)
            {
                case TEXT :
                    return toStringValue() + "[text]";

                case NUMBER :
                    return toStringValue() + "[num]";

                case PARAM :
                    if (type == INPUT)
                        return "$input:" + name;
                    else if (type == OUTPUT)
                        return "$output:" + name;
            }

            return null;
        }

    }

}