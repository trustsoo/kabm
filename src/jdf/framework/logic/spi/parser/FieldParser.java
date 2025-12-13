package jdf.framework.logic.spi.parser;

import java.text.*;

import jdf.framework.core.data.schema.FieldType;


/**
 * <code>FieldTypeDef.java</code>
 * 
 * DataSet의 필드별로 parsing 하기 위한 parser
 * BLD 의 field 정의와 매칭되며, type="string" 도는 type="int" 와 같이
 * 정의된 field 에 따라 파싱을 다르게 하고 만든 class
 * 
 * 왜?
 * 값은 int 타입어어도 ASCII 로 표현한곳이 있고, binary 로 표현한 곳이 있고
 * 상황에 따라 틀린 특성을 가지고 있으므로 그것을 구현하기 위한 class
 * 
 * @version 1.0
 * @author
 */
public class FieldParser implements FieldType
{

    // 다른 Field 데이타 타입과 구별되는 키값
    private String keyName;

    // 내부(JVM) 에서 정의되는 데이타 class type
    private Class typeClass;

    // 화면 출력시 format
    private NumberFormat form;

    

    // 외부 시스템에서 정의된 data type
    private FieldParser remoteType;

    // 소수점이 위치
    private int decimalPoint = 0;

    // 내부 데이타 타입의 int형
    // jdf.framework.core.data.schema.FieldType 참조
    private int type;

    // byte stream으로 변환될때 데이타가 우축정렬인지 좌측정렬인지 결정
    // 기본은 좌측정령
    private int align = ALIGN_LEFT;

    // 자리수를 맞추어 주기 위해 들어가는 byte 데이타
    // 기본은 0x00 즉 null 이다.
    byte fillData = 0x00;

    /**
     * 기본 생성자
     *
     */
    public FieldParser()
    {
    }

    /**
     * 생성
     * 
     */
    public FieldParser setInfo(String keyName, String typeClassName, String format, int align, byte fillData,
            int decimalPoint) throws ClassNotFoundException
    {
        this.keyName = keyName.toLowerCase();
        this.typeClass = Class.forName(typeClassName);
        
        this.align = align;
        this.fillData = fillData;
        this.decimalPoint = decimalPoint;

        DecimalFormat tmp = new DecimalFormat();
        tmp.applyPattern(format);

        this.form = tmp;

        setType(typeClass);

        return this;
    }

    void setRemoteType(FieldParser remoteType)
    {
        this.remoteType = remoteType;
    }

    private void setType(Class typeClass)
    {
        if (typeClass == String.class)
            type = STRING;

        else if (typeClass == Integer.class)
            type = INTEGER;

        else if (typeClass == Float.class)
            type = FLOAT;

        else if (typeClass == Long.class)
            type = LONG;

        else if (typeClass == Double.class)
            type = DOUBLE;

        else
            type = UNDEFINED;
    }

    /**
     * jdf.framework.core.data.schema.FieldType 에 정의된 type 코드를 return 한다.
     * 
     * @return
     */
    public int getType()
    {
        return this.type;
    }

    /**
     * 필드명을 가져온다.
     * 
     * @return
     */
    public String getKeyName()
    {
        return this.keyName;
    }

    public Class getTypeClass()
    {
        return this.typeClass;
    }

    public NumberFormat getFormat()
    {
        return this.form;
    }

    public FieldParser getRemoteFieldParser()
    {
        return this.remoteType;
    }

    protected Object transRemoteType(Object data)
    {
        if (this.remoteType == null || data == null)
            return data;

        String str = data.toString();

        switch (remoteType.getType()) {

        case STRING:
            return str;

        case INTEGER:

            if (this.decimalPoint > 0)
            {
                Float fVal = new Float(str);

                int i = (int) (fVal.floatValue() * Math.pow(10, decimalPoint));

                return new Integer(i);

            } else
                return new Integer(str);

        case LONG:

            if (this.decimalPoint > 0)
            {
                Double dVal = new Double(str);

                long i = (long) (dVal.doubleValue() * Math.pow(10, decimalPoint));

                return new Long(i);

            } else
                return new Long(str);

        default:
            return data;
        }
    }

    protected Object transInnerType(Object data)
    {
        if (this.remoteType == null || data == null)
            return data;

        String str = data.toString();

        switch (this.type) {
        case STRING:
            return str;

        case INTEGER:
            return new Integer(str);

        case LONG:
            return new Long(str);

        case FLOAT:
            Float fVal = new Float(str);
            float f = fVal.floatValue() / (float) Math.pow(10, decimalPoint);
            return new Float(f);

        case DOUBLE:
            Double dVal = new Double(str);
            double d = dVal.doubleValue() / Math.pow(10, decimalPoint);
            return new Double(d);

        default:
            return data;
        }
    }

    /**
     * 
     * @param data
     * @param fieldSize
     * @return
     * @throws TranslationException
     */
    public byte[] trans(Object data, int fieldSize) throws TranslationException
    {
        return trans(data, fieldSize, false);
    }

    /**
     * 
     * 
     * @param data
     * @param fieldSize
     * @param isLittleEndian
     * @return
     * @throws TranslationException
     */
    public byte[] trans(Object data, int fieldSize, boolean isLittleEndian) throws TranslationException
    {

        byte[] result = null;

        data = transRemoteType(data);

        int type = this.type;
        if (this.remoteType != null)
            this.type = this.remoteType.getType();

        try
        {
            switch (type) {

            case STRING:
                result = ParseUtil.transStringToBytes(data.toString(), fieldSize, this.align, this.fillData);
                break;

            case INTEGER:
                int intVal = 0;
                try
                {
                    intVal = ((Integer) data).intValue();
                } catch (ClassCastException cce)
                {
                    try
                    {
                        intVal = Integer.parseInt(data.toString());
                    } catch (NumberFormatException nfe)
                    {
                        throw new NumberFormatException(keyName + " field 값을 [int]형으로 처리불가");
                    }
                }
                if (isLittleEndian)
                    result = ParseUtil.transIntToLittleEndianBytes(intVal, fieldSize);
                else
                    result = ParseUtil.transIntToBytes(intVal, fieldSize);
                break;

            case LONG:
                long longVal = 0;
                try
                {
                    longVal = ((Long) data).longValue();
                } catch (ClassCastException cce)
                {
                    try
                    {
                        longVal = Long.parseLong(data.toString());
                    } catch (NumberFormatException nfe)
                    {
                        throw new NumberFormatException(keyName + " field 값을 [long]형으로 처리불가");
                    }
                }
                result = ParseUtil.transLongToBytes(longVal, fieldSize);
                break;

            case DOUBLE:
                double doubleVal = 0;
                try
                {
                    if (data == null)
                        doubleVal = 0;
                    else
                        doubleVal = ((Double) data).doubleValue();
                } catch (ClassCastException cce)
                {
                    try
                    {
                        doubleVal = Double.parseDouble(data.toString());
                    } catch (NumberFormatException nfe)
                    {
                        throw new NumberFormatException(this.getKeyName() + " field 값을 [double]형으로 처리불가");
                    }
                }
                result = ParseUtil.transDoubleToBytes(doubleVal, fieldSize);
                break;

            default:
                result = ParseUtil.transStringToBytes("", fieldSize, this.align, this.fillData);

            }
        } catch (Exception e)
        {
            throw new TranslationException(e.getMessage());
        }

        return result;
    }

    /**
     * 
     * @param bytes
     * @param stPoint
     * @param fieldSize
     * @return
     * @throws TranslationException
     */
    public Object trans(byte[] bytes, int stPoint, int fieldSize) throws TranslationException
    {
        return trans(bytes, stPoint, fieldSize, false);
    }

    public Object trans(byte[] bytes, int stPoint, int fieldSize, boolean isLittleEndian) throws TranslationException
    {
        Object result;

        int type = this.type;
        if (this.remoteType != null)
            this.type = this.remoteType.getType();

        try
        {
            switch (type) {

            case FieldType.INTEGER:
            case FieldType.FLOAT:
                int crntInt = 0;
                if (isLittleEndian)
                    crntInt = ParseUtil.transLittleEndianBytesToInt(bytes, stPoint, fieldSize);
                else
                    crntInt = ParseUtil.transBytesToInt(bytes, stPoint, fieldSize);

                result = new Integer(crntInt);
                break;

            case FieldType.LONG:
                long crntLong = ParseUtil.transBytesToLong(bytes, stPoint, fieldSize);
                result = new Long(crntLong);
                break;
                
            case FieldType.DOUBLE:
                double crntDouble = ParseUtil.transBytesToDouble(bytes, stPoint, fieldSize);
                result = new Double(crntDouble);
                break;

            case STRING:
            default:

                String str = ParseUtil.transBytesToString(bytes, stPoint, fieldSize);
                // if (str != null)
                // str = str.trim();
                result = str;
                break;

            }
        } catch (Exception e)
        {
            throw new TranslationException(e.getMessage());
        }

        result = this.transInnerType(result);

        return result;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return this.keyName.hashCode();
    }

    /**
     * Returns the align.
     * 
     * @return int
     */
    public int getAlign()
    {
        return align;
    }

    /**
     * Returns the decimalPoint.
     * 
     * @return int
     */
    public int getDecimalPoint()
    {
        return decimalPoint;
    }

    /**
     * Returns the fillData.
     * 
     * @return byte
     */
    public byte getFillData()
    {
        return fillData;
    }

    /**
     * Returns the form.
     * 
     * @return NumberFormat
     */
    public NumberFormat getForm()
    {
        return form;
    }

}
