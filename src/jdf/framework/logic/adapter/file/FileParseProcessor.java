package jdf.framework.logic.adapter.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

import org.apache.commons.io.input.ReversedLinesFileReader;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.Field;
import jdf.framework.core.data.schema.FieldType;
import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.core.data.schema.Processor;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.core.util.StringFormater;
import jdf.framework.logic.spi.parser.ParseUtil;
import jdf.framework.logic.spi.parser.TranslationException;


/**
 * 
 * @author
 * 
 */
public class FileParseProcessor extends Processor implements ProcessProperties
{
    public FileParseProcessor()
    {
        super();
    }

    private AdapterProperties adapterProperties;

    /*
     * private int startRow = 0;
     * 
     * private int endRow = 1;
     * 
     * private int selectRowNum = 1;
     */

    // 파일 억세스가 정방향이면 1, 역방향이면 -1
    private long directionVal = 1;

    private int lineEndLength = 1;

    private String delimiter;

    private byte[] lineDelim;

    public void setProperty(String key, String value)
    {
        this.processProperties.setProperty(key, value);

        /*
         * if (START_ROW.equals(key)) this.startRow = Integer.parseInt(value);
         * else if (SELECT_ROW_NUM.equals(key)) { this.selectRowNum =
         * Integer.parseInt(value); this.endRow = this.startRow +
         * this.selectRowNum; } else
         */

        if (DIRECTION.equals(key))
        {
            if ("reverse".equals(value))
                this.directionVal = -1;
        } else if (DELIMITER.equals(key))
        {
            this.delimiter = value;
        }

    }

    public void setAdapterProperties(AdapterProperties properties)
    {
        this.adapterProperties = properties;

        this.delimiter = properties.getDelimiter();

        this.lineDelim = this.adapterProperties.getLineDelimiter().getBytes();

    }

    protected void executeAll(DataSet input, DataSet output) throws Exception
    {
        // 쓰기 모드인 경우
        if (this.adapterProperties.isWriteMode())
        {
            executeAllForWrite(input, output);
            return;
        }

        // 길이 로 파싱하는 file 인 경우
        if (this.adapterProperties.isLengthMode())
            executeAllFromLength(input, output);
        else
        {        	
        	if(this.directionVal == 1)
        		executeAllFromDelim(input, output);
        	else
        		executeAllFromDelimReverse(input, output);
        }
            
    }

    /**
     * 
     * 
     * @param input
     * @param key
     * @return
     */
    private String getProperty(DataSet input, String key)
    {
        String val = input.getProperty(key);

        if (val != null)
        {
            return val;
        }
        val = input.getText(key);
        if (val != null && val.length() > 0)
            return val;

        return this.getProperty(key);
    }

    /**
     * 파일 기록모드
     * 
     * @param input
     * @param output
     * @throws Exception
     */
    private void executeAllForWrite(DataSet input, DataSet output) throws Exception
    {
        String filename = getProperty(input, "fileName");

        String selectRownum = getProperty(input, "select-rownum");
        String direction = getProperty(input, "direction");
        String startRowStr = getProperty(input, "start-row");

        int startRow = 0;
        int selectRowNum = -1;
        int endRow = 0;

        try
        {
            startRow = Integer.parseInt(startRowStr);
        } catch (Exception e)
        {

        }

        try
        {
            selectRowNum = Integer.parseInt(selectRownum);
            endRow = startRow + selectRowNum;
        } catch (Exception e)
        {

        }

        if ("reverse".equals(direction))
            this.directionVal = -1;
        else
            this.directionVal = 1;

        String fileFullPath = filename;
        if (filename.indexOf("/") != 0)
            fileFullPath = this.adapterProperties.getRootDirectory() + filename;

        File f = new File(fileFullPath);

        if ("true".equals(getProperty(input, "deleteBeforeWrite")))
            f.delete();

        if (!f.exists())
            f.createNewFile();

        IOSchema bodySchema = input.getIOSchema();
        Block[] blocks = bodySchema.getInputBlocks();

        Block b = blocks[0];
        Field[] fields = b.getFields();

        // input에 들어있는 최대 갯수
        int maxCount = input.getCount(fields[0].getName());

        // r ,rw, rws, rwd
        RandomAccessFile raf = null;

        try
        {
            raf = new RandomAccessFile(f, "rw");
            raf.seek(raf.length()); // 끝으로 이동

            FileOutputStream fos = new FileOutputStream(raf.getFD());
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            // ByteArrayOutputStream bout = new ByteArrayOutputStream();

            for (int seq = 0; seq < maxCount; seq++)
            {
                for (int j = 0; j < fields.length; j++)
                {
                    Field fld = fields[j];
                    int type = fld.getType();
                    String keyName = fld.getName();

                    int fieldSize = fld.getSize();

                    try
                    {
                        switch (type) {

                        case FieldType.INTEGER:
                        case FieldType.FLOAT:
                            if (this.adapterProperties.isTextNumber())
                            {
                                int v = input.getInt(keyName, seq);
                                String d = StringFormater.fillZero(String.valueOf(v), fieldSize);

                                bos.write(d.getBytes());
                            } else
                            {
                                byte[] data = ParseUtil.transIntToBytes(input.getInt(keyName, seq), fieldSize);
                                bos.write(data);
                            }
                            break;

                        case FieldType.LONG:
                        case FieldType.DOUBLE:
                            if (this.adapterProperties.isTextNumber())
                            {
                                long v = input.getLong(keyName, seq);
                                String d = StringFormater.fillZero(String.valueOf(v), fieldSize);

                                bos.write(d.getBytes());
                            } else
                            {
                                byte[] data = ParseUtil.transLongToBytes(input.getLong(keyName, seq), fieldSize);
                                bos.write(data);
                            }
                            break;

                        case FieldType.STRING:
                        default:
                            byte[] data = ParseUtil.transStringToBytes(input.get(keyName, seq).toString(), fieldSize,
                                    ParseUtil.ALIGN_LEFT, (byte) 0x20);

                            bos.write(data);
                            break;

                        }
                    } catch (Exception e)
                    {
                        throw new TranslationException(e.getMessage());
                    }

                }
                bos.write(lineDelim);
            }

            bos.flush();
            bos.close();

            // raf.write(bout.toByteArray());

        } catch (Exception e)
        {
            throw e;
        } finally
        {
            if (raf != null)
                raf.close();
        }

    }

    /**
     * 
     * @param input
     * @param output
     * @throws Exception
     */
    protected void executeAllFromDelim(DataSet input, DataSet output) throws Exception
    {     
        IOSchema bodySchema = input.getIOSchema();
        Block[] blocks = bodySchema.getOutputBlocks();

        /*
         * if (blocks.length != 1) throw new Exception("단 1개의 block 만을 지원합니다.");
         */
        Block b = blocks[0];

        String filename = getProperty(input, "fileName");

        /*
         * String filename = input.getText("fileName"); if (filename.length() ==
         * 0) filename = this.getProperty(FILE_NAME);
         */

        String fileFullPath = filename;
        if (filename.indexOf("/") != 0)
            fileFullPath = this.adapterProperties.getRootDirectory() + filename;

        File f = new File(fileFullPath);

        int startRow = 0;
        int selectRowNum = -1;
        int endRow = 0;

        try
        {
            startRow = Integer.parseInt(getProperty(input, "start-row"));
        } catch (Exception e)
        {
        }

        try
        {
            selectRowNum = Integer.parseInt(getProperty(input, "select-rownum"));
            endRow = startRow + selectRowNum;
        } catch (Exception e)
        {

        }
        
        BufferedReader reader = null;
        try
        {
	        reader = new BufferedReader(new FileReader(f));
	
	        
	
	        int seq = 0;
	        int idx = 0;
	        // while (reader.ready())
	        for (int p = 0; reader.ready(); p++)
	        {
	            try
	            {
	                String line = reader.readLine();
	
	                if (seq >= startRow && (seq < endRow || selectRowNum < 0))
	                {
	                    String[] data = SmartStringArray.split(this.delimiter, line);
	
	                    for (int j = 0; j < data.length; j++)
	                    {
	                        if (data[j].length() >= 0)
	                            output.put(b.getFields()[j].getName(), data[j], idx);
	                    }
	
	                    idx++;
	                }
	            } catch (Exception e)
	            {
	                throw new Exception((p + 1) + "번째  라인 파싱에러 발생 " + e.toString());
	            }
	
	            seq++;
	        }
        }catch(Exception ex)        
        {
        	throw ex;
        } finally
        {
        	if(reader != null) try{reader.close();}catch(IOException iex){}
        }
        

    }

    protected void executeAllFromDelimReverse(DataSet input, DataSet output)  throws Exception
    {
    	IOSchema bodySchema = input.getIOSchema();
        Block[] blocks = bodySchema.getOutputBlocks();

        /*
         * if (blocks.length != 1) throw new Exception("단 1개의 block 만을 지원합니다.");
         */
        Block b = blocks[0];

        String filename = getProperty(input, "fileName");

        /*
         * String filename = input.getText("fileName"); if (filename.length() ==
         * 0) filename = this.getProperty(FILE_NAME);
         */

        String fileFullPath = filename;
        if (filename.indexOf("/") != 0)
            fileFullPath = this.adapterProperties.getRootDirectory() + filename;

        File f = new File(fileFullPath);
        
        int startRow = 0;
        int selectRowNum = -1;
        int endRow = 0;

        try
        {
            startRow = Integer.parseInt(getProperty(input, "start-row"));
        } catch (Exception e)
        {
        }

        try
        {
            selectRowNum = Integer.parseInt(getProperty(input, "select-rownum"));
            endRow = startRow + selectRowNum;
        } catch (Exception e)
        {

        }
        
        ReversedLinesFileReader fr = null;
		try 
		{
			fr = new ReversedLinesFileReader(f,4096,Charset.forName("UTF-8"));
			String line;
			int seq = 0;
	        int idx = 0;
			while( (line = fr.readLine()) !=  null)
			{
				try
	            {
					if (seq >= startRow && (seq < endRow || selectRowNum < 0))
	                {
						
	                    String[] data = SmartStringArray.split(this.delimiter, line);
	
	                    for (int j = 0; j < data.length; j++)
	                    {
	                        if (data[j].length() >= 0)
	                            output.put(b.getFields()[j].getName(), data[j], idx);
	                    }
	
	                    idx++;
	                }
	            } catch(Exception e)
				{
	            	throw new Exception((seq + 1) + "번째  라인 파싱에러 발생 " + e.toString());
				}
				
				
				seq++;
			}
			
		} catch (IOException e) {
			throw e;
		} finally
		{
			if(fr != null) try{fr.close();}catch(IOException ex){}
		}
    }
    
    /**
     * 
     * 
     * @param input
     * @param output
     * @throws Exception
     */
    protected void executeAllFromLength(DataSet input, DataSet output) throws Exception
    {
        IOSchema bodySchema = input.getIOSchema();

        // 건너띠는 바이트 수
        int skipByteLength = 0;
        try
        {
            skipByteLength = Integer.parseInt(getProperty(input, "skipByteLength"));
        } catch (Exception e)
        {
        }

        // 파싱할 파일명
        String filename = getProperty(input, "fileName");
        String fileFullPath = filename;
        if (filename.indexOf("/") != 0)
            fileFullPath = this.adapterProperties.getRootDirectory() + filename;

        File f = new File(fileFullPath);
        
        // 파일 총길이
        long fileTotalLength = f.length();

        // raf
        /*
         * RandomAccessFile raf = new RandomAccessFile(f, "r"); if
         * (skipByteLength > 0) { // this.startRow =
         * this.startRow+skipByteLength; raf.skipBytes(skipByteLength); }
         */

        BufferedInputStream bis =null;
        
        try {
        bis = new BufferedInputStream(new FileInputStream(f), 2048);
        bis.skip(skipByteLength);

        Block[] blocks = bodySchema.getOutputBlocks();

        /*
         * if (blocks.length <1) throw new Exception(" 1개 이상의 block 을 지원합니다.");
         */

        Block b = blocks[0];
        int delimLength = this.adapterProperties.getLineDelimiter().length();
        int blockLength = b.getTotalFieldSize() + delimLength;

        // 총건수
        int totalRows = (int) ((fileTotalLength - skipByteLength) / blockLength);
        output.setProperty("DS:TOTAL_COUNT", String.valueOf(totalRows));

        Field[] fields = b.getFields();

        int startRow = 0;
        int selectRowNum = -1;
        int endRow = 0;

        try
        {
            startRow = Integer.parseInt(getProperty(input, "start-row"));
        } catch (Exception e)
        {
        }

        try
        {
            selectRowNum = Integer.parseInt(getProperty(input, "select-rownum"));
            endRow = startRow + selectRowNum;
        } catch (Exception e)
        {

        }

        long startPoint = 0;
        long endPoint = 0;
        // 정방향인 경우
        if (this.directionVal == 1)
        {

            startPoint = startRow * blockLength;

            if (selectRowNum < 0)
                endPoint = fileTotalLength;
            else
                endPoint = (startRow + selectRowNum) * blockLength;

        }
        // 역방향인 경우
        else
        {
            endPoint = fileTotalLength - (startRow * blockLength);

            if (selectRowNum < 0)
                startPoint = 0;
            else
            {
                startPoint = fileTotalLength - ((startRow + selectRowNum) * blockLength);
                if (startPoint < 0)
                {
                    startPoint = 0;
                }
            }

        }
        // 구간내의 총 row의 갯수
        selectRowNum = (int) ((endPoint - startPoint+delimLength) / blockLength);
       
        /*
        System.out.println(" 파일 총길이     ==========> " + fileTotalLength);
        System.out.println(" block 총길이 ==========> " + blockLength);
        System.out.println(" 시작 포인트     ==========> " + startPoint);
        System.out.println(" 종료 포인트     ==========> " + endPoint);
        System.out.println(" 선택갯수          ==========> " + selectRowNum);
       */
        
        byte[] bytes = new byte[blockLength];

        bis.skip(startPoint);
        for (int i = 0; i < selectRowNum; i++)
        {
            bis.read(bytes);

            int stPoint = 0;

            for (int j = 0; j < fields.length; j++)
            {
                Field fld = fields[j];
                int type = fld.getType();

                int fieldSize = fld.getSize();

                Object result = null;

                try
                {
                    switch (type) {

                    case FieldType.INTEGER:
                    case FieldType.FLOAT:
                        if (this.adapterProperties.isTextNumber())
                        {
                            String str = ParseUtil.transBytesToString(bytes, stPoint, fieldSize);
                            result = new Integer(Integer.parseInt(str));
                        } else
                        {
                            int crntInt = 0;
                            crntInt = ParseUtil.transBytesToInt(bytes, stPoint, fieldSize);
                            result = new Integer(crntInt);
                        }
                        break;

                    case FieldType.LONG:
                    case FieldType.DOUBLE:
                        if (this.adapterProperties.isTextNumber())
                        {
                            String str = ParseUtil.transBytesToString(bytes, stPoint, fieldSize);
                            result = new Long(Long.parseLong(str));
                        } else
                        {
                            long crntLong = ParseUtil.transBytesToLong(bytes, stPoint, fieldSize);
                            result = new Long(crntLong);
                        }
                        break;

                    case FieldType.STRING:
                    default:
                        String str = ParseUtil.transBytesToString(bytes, stPoint, fieldSize);
                        result = str;
                        break;

                    }
                } catch (Exception e)
                {
                    throw new TranslationException(e.getMessage());
                }

                stPoint = stPoint + fieldSize;

                // 정방향이면
                if (this.directionVal == 1)
                    output.put(fld.getName(), result, i);
                else
                {
                    output.put(fld.getName(), result, (selectRowNum - i - 1));
                }
            }

        }
        
        }catch(Exception ee) {
            throw ee;
        }
        finally {
            if(bis!=null)
                bis.close();
        }

    }

    private long getSeekPosition(long fileLength, int lineLength, int startRow)
    {
        long seekPoint = 0;
        // 정방향인 경우
        if (this.directionVal == 1)
        {
            seekPoint = lineLength * startRow;

        }
        // 역방향인 경우
        else
        {
            seekPoint = fileLength - lineLength * (startRow + 1);
        }
        if (seekPoint < 0 || seekPoint > fileLength)
            return -1;

        return seekPoint;

    }

}
